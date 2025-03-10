package ru.ir.visualiser.core.parser;

import ru.ir.visualiser.model.analysis.Scev;
import ru.ir.visualiser.model.ir.BlockIR;
import ru.ir.visualiser.model.ir.Dot;
import ru.ir.visualiser.model.ir.FunctionIR;
import ru.ir.visualiser.model.ir.ModuleIR;
import ru.ir.visualiser.core.parser.loops.LoopBlock;
import ru.ir.visualiser.core.parser.loops.LoopIR;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class that performs parsing of llvm ir files.
 */
public class Parser {

    /**
     * Method to determine at which line char with index is located
     *
     * @param text - text to check in
     *
     * @param index - index of a char
     *
     * @return number of line
     */
    public static int getLineNumber(String text, int index) {
        int line = 1;
        for (int i = 0; i < index; i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    /**
     * Method to parse whole IR module. Finds name of a module. Finds every
     * function, cuts their bodies, Determines range of lines for each function,
     * and calls parseFunction for each.
     *
     * @param input - Text of IR.
     *
     * @return ModuleIR
     */
    public static ModuleIR parseModule(String input, Iterable<String> dotFiles) {
        String moduleID = "";
        String regexModuleName = "; ModuleID = (.*)";
        Pattern patternName = Pattern.compile(regexModuleName);
        Matcher matcher = patternName.matcher(input);
        if (matcher.find()) {
            moduleID = matcher.group(1);
        }
        String regexFunc = "(; Function Attrs: .*\\n)?(define[\\s\\S]*?}|declare.*\\n)";
        Pattern patternFunctions = Pattern.compile(regexFunc);
        matcher = patternFunctions.matcher(input);

        String[] mt = input.split("\n");
        for (int i = 0; i < mt.length; i++) {
            mt[i] = mt[i].strip();
        }

        List<String> moduleText = List.of(mt);

        ModuleIR moduleIR = new ModuleIR(moduleID, moduleText);
        ArrayList<FunctionIR> functions = new ArrayList<>();

        while (matcher.find()) {
            functions.add(parseFunction(matcher.group(), getLineNumber(input, matcher.start()), getLineNumber(input, matcher.end())));
        }

        for (FunctionIR function : functions) {
            moduleIR.addFunction(function);
        }

        for (String dotFile : dotFiles) {
            Dot dot = parseDot(dotFile);
            moduleIR.addNameToDot(dot.getFunctionName(), dot);
        }

        return moduleIR;
    }

    /**
     * Method to parse whole dot file. Finds svg id to label mapping.
     *
     * @param input - Text of dot.
     *
     * @return Dot
     */
    public static Dot parseDot(String input) {
        Dot dot;

        String regexName = "digraph \"CFG for '(.*)' function\"";
        Pattern patternName = Pattern.compile(regexName);
        Matcher matcherName = patternName.matcher(input);
        if (matcherName.find()) {
            dot = new Dot(matcherName.group(1));
        } else {
            throw new IllegalArgumentException("Can't find name of the function in dot file");
        }

        String regexId = "(Node0x[0-9a-f]+)\\s*\\[[^]]*label=\"\\{([^:]+)";
        Pattern patternId = Pattern.compile(regexId);
        Matcher matcher = patternId.matcher(input);
        while (matcher.find()) {
            dot.addSvgIdToLabel(matcher.group(1), matcher.group(2));
        }
        return dot;
    }

    /**
     * Method to parse function. Finds function name, cuts every block from
     * function body and calls parseBlock for each.
     *
     * @param input - Function body
     *
     * @param startLine - number of first line of that function(counting from
     * start of module).
     *
     * @param endLine - number of last line of that function(counting from start
     * of module).
     *
     * @return FunctionIR
     */
    private static FunctionIR parseFunction(String input, int startLine, int endLine) {
        String regexFuncName = "(define|declare)[^@]* @(\\w*)";
        Pattern patternName = Pattern.compile(regexFuncName);

        Matcher matcher = patternName.matcher(input);
        String functionID = "";
        if (matcher.find()) {
            functionID = matcher.group(2);
        }

        FunctionIR functionIR = new FunctionIR(functionID, input, startLine, endLine);

        String regexBlock = "(\\{[\\s\\S]*?(?:\\n\\n|}))|(\\d+:[\\s\\S]*?(?:\\n\\n|\\n}))";
        Pattern patternBlock = Pattern.compile(regexBlock);
        matcher = patternBlock.matcher(input);
        while (matcher.find()) {
            if (matcher.group(1) != null) {
                BlockIR blockIr = parseBlock(matcher.group(1),
                        startLine + getLineNumber(input, matcher.start()),
                        startLine + getLineNumber(input, matcher.end()));
                functionIR.addBlock(blockIr);
                continue;
            }
            BlockIR blockIr = parseBlock(matcher.group(2),
                    startLine + getLineNumber(input, matcher.start()),
                    startLine + getLineNumber(input, matcher.end()));
            functionIR.addBlock(blockIr);
        }
        return functionIR;
    }

    /**
     * Method for parsing block
     *
     * @param input - body of a block
     *
     * @param startLine - number of first line of that block(counting from start
     * of module).
     *
     * @param endLine - number of last line of that block(counting from start of
     * module).
     *
     * @return - BlockIR
     */
    private static BlockIR parseBlock(String input, int startLine, int endLine) {
        String regexId = "(\\d+):";
        Pattern patternId = Pattern.compile(regexId);
        Matcher matcher = patternId.matcher(input);
        String label = "";
        if (matcher.find()) {
            label = matcher.group(1);
        }
        return new BlockIR(label, startLine, endLine);
    }

    /**
     * Function that extracts Loop info for certain function from Opt output.
     *
     * @param function - name of a function
     *
     * @param text - opt output
     *
     * @return - Loop Structure
     */
    public static List<LoopIR> findLoopInfofromOpt(FunctionIR function, String text) {
        String regex = "Loop info for function '" + function.getFunctionName() + "':\\n([\\s\\S]*?)(?=Loop info for function |\\Z)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        List<LoopIR> res;
        if (matcher.find()) {
            res = parseLoops(function, matcher.group(1));
        } else {
            throw new IllegalArgumentException("Can't find name of the function");
        }
        return res;
    }

    /**
     * Extracts every loop from preparsed opt output for single function.
     *
     * @param function - function for which loop info is analyzed
     *
     * @param text - preparsed by findLoopInfofromOpt
     *
     * @return parsed loops
     */
    private static List<LoopIR> parseLoops(FunctionIR function, String text) {
        String regex = "Loop at depth (\\d+) containing: ([\\s\\S]*?\\n|[\\s\\S]*)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        List<LoopIR> res = new ArrayList<>();
        while (matcher.find()) {
            res.add(parseLoop(function, matcher.group(2), Integer.parseInt(matcher.group(1))));
        }
        return res;
    }

    /**
     * Parses single Loop and creates LoopIR and LoopBlock elements.
     *
     * @param function - function for which loop info is analyzed
     *
     * @param text - single loop info without "Loop at depth ... containing"
     *
     * @param depth - depth of a loop
     *
     * @return parsed Loop
     */
    private static LoopIR parseLoop(FunctionIR function, String text, int depth) {
        ArrayList<LoopBlock> blocks = new ArrayList<>();
        for (String now : text.split(",")) {
            String regex = "%(\\w+)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(now);
            if (matcher.find()) {
                String check = matcher.group(1);
                Collection<BlockIR> blocksIr = function.getBlocks();
                BlockIR blockIR = null;
                for (BlockIR block : blocksIr) {
                    if (block.getLabel().equals(check)) {
                        blockIR = block;
                    }
                }
                if (blockIR == null) {
                    throw new IllegalArgumentException("Can't find block");
                }
                LoopBlock block = new LoopBlock(blockIR);
                if (now.contains("<exiting>")) {
                    block.setExit(true);
                }
                if (now.contains("<latch>")) {
                    block.setLatch(true);
                }
                if (now.contains("<header>")) {
                    block.setHeader(true);
                }
                blocks.add(block);
            } else {
                throw new IllegalArgumentException("No block label is found");
            }

        }
        return new LoopIR(blocks, depth);
    }

    /**
     * Method that parses scev analysis.
     *
     * @param input - text of scev file
     *
     * @param module - module for which scev is parsed
     *
     * @return - parsed scev
     */
    public static Scev parseScev(String input, ModuleIR module) {
        List<String> moduleLines = module.getModuleText();
        String[] scevLines = input.split("\n");

        Map<Integer, String> scev = new java.util.HashMap<>();

        int moduleLine = 0;
        int scevLine = 0;

        boolean collecting = false;

        while (scevLine < scevLines.length) {
            if (collecting) {
                if (scevLines[scevLine].startsWith("Determining loop execution counts for:")) {
                    collecting = false;
                    continue;
                }

                String scevExpr = scevLines[scevLine].strip().split(" ")[0];
                
                while (!moduleLines.get(moduleLine).startsWith(scevExpr)) {
                    System.out.println("Module line: " + moduleLines.get(moduleLine) + '\n');
                    System.out.println("Scev line:   " + scevExpr + '\n');
                    moduleLine += 1;
                }

                scevLine += 1;
                String scevString = scevLines[scevLine].strip().substring(3).strip();
                scev.put(moduleLine, scevString);
                scevLine += 1;
                moduleLine += 1;
            } else {
                if (scevLines[scevLine].startsWith("Classifying expressions for:")) {
                    collecting = true;
                }
                scevLine += 1;
            }
        }

        return new Scev(scev);
    }
}
