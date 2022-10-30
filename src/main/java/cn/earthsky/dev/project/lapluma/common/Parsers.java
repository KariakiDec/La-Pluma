package cn.earthsky.dev.project.lapluma.common;

import cn.earthsky.dev.project.lapluma.LaPluma;

import java.math.BigInteger;
import java.util.*;
import java.util.logging.Level;

public class Parsers {
    public static List<Parsing> parseFunctions(Collection<String> functions){
        ArrayList<Parsing> list = new ArrayList<>();
        functions.forEach(str -> list.add(new Parsing(str)));
        return list;
    }
    public static List<Parsing> parseFunctions(String... functions){
        if(functions == null){
            throw new IllegalArgumentException("functions cannot be null");
        }
        return parseFunctions(Arrays.asList(functions));
    }

    public static List<String> parseList(String originalArgument){
        if(originalArgument == null){
            return Collections.emptyList();
        }
        if(!originalArgument.contains(";")){
            return Collections.singletonList(originalArgument);
        }
        return new ArrayList<>(Arrays.asList(originalArgument.split(";")));
    }

    public static double parseDouble(String val, double def){
        if(val == null){
            return def;
        }

        try{
            return Double.parseDouble(val);
        }catch (NumberFormatException exc){
            LaPluma.getLogger().log(Level.WARNING,"Cannot parse " + val + " as a double");
            return def;
        }
    }

    public static float parseFloat(String val, float def){
        if(val == null){
            return def;
        }

        try{
            return Float.parseFloat(val);
        }catch (NumberFormatException exc){
            LaPluma.getLogger().log(Level.WARNING,"Cannot parse " + val + " as a float");
            return def;
        }
    }

    public static int parseInteger(String val, int def){
        if(val == null){
            return def;
        }

        try{
            if(val.startsWith("0x")){
                return new BigInteger(val.substring(2),16).intValue();
            }
            return Integer.parseInt(val);
        }catch (NumberFormatException exc){
            LaPluma.getLogger().log(Level.WARNING,"cannot handle " + val + " as integer, return a def val", exc);
            return def;
        }
    }


    public static boolean parseBoolean(String val){
        if(val == null){
            return false;
        }
        String var = val.toLowerCase();
        if(var.equals("yes") ||
                var.equals("true") ||
                var.equals("right") ||
                var.equals("correct") ||
                var.equals("allowed") ||
                var.equals("allow") ||
                var.equals("permitted") ||
                var.equals("on")){
            return true;
        }
        return false;
    }

    public static byte parseByte(String val, byte def){
        if(val == null){
            return def;
        }
        try{
            return Byte.parseByte(val);
        }catch (NumberFormatException exc){
            LaPluma.getLogger().log(Level.WARNING,"Cannot parse " + val + " as a byte");
            return def;
        }
    }
}
