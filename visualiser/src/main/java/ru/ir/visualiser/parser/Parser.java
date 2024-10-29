package ru.ir.visualiser.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {

    public ModuleIR parseModule(String input) {
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

        while (matcher.find()) {
            moduleIR.addFunction(parseFunction(matcher.group()));
        }
        //ДОБАВИТЬ ДЛЯ ФУНКЦИИ в лист и в билдер
        return moduleIR;
    }

    public FunctionIR parseFunction(String input) {
        String regexFuncName = "(define|declare)[^@]* @(\\w*)";
        Pattern patternName = Pattern.compile(regexFuncName);

        Matcher matcher = patternName.matcher(input);
        String functionID = "";
        if(matcher.find()) {
            functionID = matcher.group(2);
        }

        FunctionIR functionIR = new FunctionIR(functionID, input);
        String regexBlock = "(\\{[\\s\\S]*?(?:\\n\\n|}))|(\\d+:[\\s\\S]*?(?:\\n\\n|\\n\\}))";
        Pattern patternBlock = Pattern.compile(regexBlock);
        matcher = patternBlock.matcher(input);
        while (matcher.find()) {
            if(matcher.group(1) != null) {
                functionIR.addBlock(parseBlock(matcher.group(1), true));
                continue;
            }
            functionIR.addBlock(parseBlock(matcher.group(2), false));

        }
        return functionIR;
    }

    public BlockIR parseBlock(String input, boolean initial) {
        if (initial) {
            return new BlockIR(initial, input);
        }
        BlockIR blockIR = new BlockIR(false, input);
        String regexId = "(\\d+):";
        Pattern patternId = Pattern.compile(regexId);
        Matcher matcher = patternId.matcher(input);
        if(matcher.find()) {
            blockIR.setLabelIR(matcher.group(1));
        }
        return blockIR;
    }
}