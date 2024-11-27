package ru.ir.visualiser.parser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    private static int getLineNumber(String text, int index) {
        int line = 1;
        for (int i = 0; i < index; i++) {
            if (text.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    public static ModuleIR parseModule(Path filePath) throws IOException {
        String content = Files.readString(filePath);
        return parseModule(content);
    }

    public static ModuleIR parseModule(String input) { 
        String moduleID = "";
        String regexModuleName = "; ModuleID = (.*)";
        Pattern patternName = Pattern.compile(regexModuleName);
        Matcher matcher = patternName.matcher(input);
        if(matcher.find()) {
            moduleID = matcher.group(1);
        }
        String regexFunc = "(; Function Attrs: .*\\n)?(define[\\s\\S]*?\\}|declare.*\\n)";
        Pattern patternFunctions = Pattern.compile(regexFunc);
        matcher = patternFunctions.matcher(input);

        ModuleIR moduleIR = new ModuleIR(moduleID, input);
        ArrayList<FunctionIR> functions = new ArrayList<>();

        while (matcher.find()) {
            functions.add(parseFunction(matcher.group(), getLineNumber(input, matcher.start()), getLineNumber(input, matcher.end())));
        }

        for (FunctionIR function : functions) {
            moduleIR.addNameToFunction(function.getFunctionName(), function);
            moduleIR.addFunctionToName(function.getFunctionName(), function);
        }
        return moduleIR;
    }

    private static FunctionIR parseFunction(String input, int startLine, int endLine) {
        String regexFuncName = "(define|declare)[^@]* @(\\w*)";
        Pattern patternName = Pattern.compile(regexFuncName);

        Matcher matcher = patternName.matcher(input);
        String functionID = "";
        if(matcher.find()) {
            functionID = matcher.group(2);
        }

        FunctionIR functionIR = new FunctionIR(functionID, input, startLine, endLine);

        String regexBlock = "(\\{[\\s\\S]*?(?:\\n\\n|}))|(\\d+:[\\s\\S]*?(?:\\n\\n|\\n\\}))";
        Pattern patternBlock = Pattern.compile(regexBlock);
        matcher = patternBlock.matcher(input);
        while (matcher.find()) {
            if(matcher.group(1) != null) {
                functionIR.addBlock(parseBlock(matcher.group(1), true,
                           startLine + getLineNumber(input, matcher.start()),
                            startLine + getLineNumber(input, matcher.end())));
                continue;
            }
            functionIR.addBlock(parseBlock(matcher.group(2), false,
                       startLine + matcher.start(), startLine + matcher.end()));

        }
        return functionIR;
    }

    private static BlockIR parseBlock(String input, boolean initial, int startLine, int endLine) {
        if (initial) {
            return new BlockIR(initial, input, startLine, endLine);
        }
        BlockIR blockIR = new BlockIR(false, input, startLine, endLine);
        String regexId = "(\\d+):";
        Pattern patternId = Pattern.compile(regexId);
        Matcher matcher = patternId.matcher(input);
        if(matcher.find()) {
            blockIR.setLabelIR(matcher.group(1));
        }
        return blockIR;
    }
}
