package cn.earthsky.dev.project.lapluma.common;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parsing {
    @Getter private String originalExpression;


    private final static Pattern pattern = Pattern.compile("((\\w+)\\(([\\u4E00-\\u9FA5\\u4e00-\\u9fa5\\x00-\\xffA-Za-z0-9_\\u201c\\u300e\\u300f\\u201d\\u003a\\u002c\\uff0c\\u00a7\\u30fb\\u00b7\\u300a\\u300b\\u3002\\uff1f\\uff01\\u2014\\u0040\\u0023\\uffe5\\u20ac\\u0025\\uff05 !?/|+=-@$^&*().{}<>]*)\\))");
    private final static String GENERAL_PATTERN = "[\\u4e00-\\u9fa5\\x00-\\xffA-Za-z0-9_:, !?/|+=-@#$%^&*().{}<>]";
    private final static Pattern string_pattern = Pattern.compile("\"(" + GENERAL_PATTERN + "*)\"");
    private final static Pattern multiple = Pattern.compile("\\s*&&(?=([^\"]*\"[^\"]*\")*[^\"]*$)\\s*");



    @Getter private String functionName;
    private Map<String,String> arguments;

    public Map<String,String> getArguments(){
        return new HashMap<>(arguments);
    }

    public Parsing(String expression){
        this.originalExpression = expression;
        arguments = new HashMap<>();
        Matcher matcher = pattern.matcher(expression);
        if(!matcher.find()){
            throw new IllegalArgumentException("Cannot parse '" + expression + "'");
        }
        this.functionName = matcher.group(2);
        try {
            if(!matcher.group(3).isEmpty()) {
                String argumentAll = matcher.group(3);
//                System.out.println(argumentAll);
//                String[] argumentEntries = argumentAll.split("(,\\s*)");
                String[] argumentEntries = argumentAll.split(",(?=([^\"]*\"[^\"]*\")*[^\"]*$)");
                for (String entry : argumentEntries) {
//                    String[] pair = entry.replaceAll("\\$comma\\$", ",").split("(:\\s*)");

                    String s = entry.replaceAll("\\$comma\\$", ",");
                    int firstCut = s.replaceAll("\\$comma\\$", ",").indexOf(':');
                    String key = s.substring(0, firstCut).replaceAll("\\s*", "");
                    String val = "";
                    if(firstCut +1 < s.length()){
                        val = s.substring(firstCut + 1);
                    }
                    if(val.startsWith(" ")){
                        val = val.substring(1);
                    }
                    Matcher mac = string_pattern.matcher(val);
                    if(mac.find()){
                        val = mac.group(1);
                    }
                    arguments.put(key, val);
                }
            }
        }catch (Exception exc){
            System.out.println("[Parsing Function] Cannot parse expression " + expression);
            exc.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return originalExpression;
    }

    public void printParsingResult(){
        System.out.println("###Parsing Result: " + this.hashCode());
        System.out.println("Expression: " + getOriginalExpression());
        System.out.println("Function Name: " + functionName);
        System.out.println("Function Arguments:");
        arguments.forEach((key,val) -> {
            System.out.println("  - " + key + ": " + val);
        });
        System.out.println("######## End");
    }
}
