package cn.earthsky.dev.project.lapluma.common;

import java.util.Map;

public final class Selector {
    public static String searchNonNull(Map<String,String> arguments, String... alias){
        assert arguments != null;
        String result = null;
        for(String s : alias){
            result = arguments.get(s);
            if(result != null){
                break;
            }
        }
        return result;
    }

    public static String searchWithDefault(Map<String,String> arguments, String def, String... alias){
        assert arguments != null;
        String result = def;
        for(String s : alias){
            if(arguments.containsKey(s)) {
                result = arguments.get(s);
                break;
            }
        }
        return result;
    }

    public static void fill(Map<String,String> arguments, String fillWith, String... alias){
        assert alias != null && arguments != null;
        for(String s : alias){
            arguments.putIfAbsent(s, fillWith);
        }
    }
}
