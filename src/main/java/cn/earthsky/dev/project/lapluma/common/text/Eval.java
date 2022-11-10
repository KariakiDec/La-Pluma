package cn.earthsky.dev.project.lapluma.common.text;

import cn.earthsky.dev.project.lapluma.LaPluma;
import lombok.AllArgsConstructor;
import lombok.Singular;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Eval {

    public interface EvalResult{
        String getType();
    }
    @Value @AllArgsConstructor
    public static class Prompt implements EvalResult{
        String speaker;
        @Singular List<String> text;

        @Override
        public String getType() {
            return "Prompt";
        }
    }

    @Value @AllArgsConstructor
    public static class Function implements EvalResult{
        String functionText;

        @Override
        public String getType() {
            return "Function";
        }
    }

    @Value @AllArgsConstructor
    public static class Selection implements EvalResult{
        String selectText;
        String[] functionText;

        @Override
        public String getType() {
            return "Selection";
        }
    }

    private static final Pattern FUNCTION_PATTERN = Pattern.compile("(^<([\\s\\S]+)>)"); // 2 - Function
    private static final Pattern SELECTION_PATTERN = Pattern.compile("((^\\[([\\s\\S]+)\\])(<[\\s\\S]+>)+)"); // 3 - Selection 4 - Functuons
    private static final Pattern MULTI_FUNCTION = Pattern.compile("(<([\\S\\s]+)>+?)"); // 2 Context

    public static List<EvalResult> eval(List<String> lines) {
        List<EvalResult> results = new ArrayList<>();
        for (int i = 0; i < lines.size(); i++) {
            String t = lines.get(i).replaceAll("&","§");
            if (t.startsWith("#")) {
                continue;
            }
            if(t.isEmpty()){
                continue;
            }
            if(t.replaceAll(" ","").isEmpty()) {
                continue;
            }
            Matcher selM = SELECTION_PATTERN.matcher(t);
            if(selM.matches()){ // Selection
                try {
                    String selection = selM.group(3);
                    String funcs = selM.group(4);
                    Matcher res = MULTI_FUNCTION.matcher(funcs);
                    if(res.matches()) {
                        String ctx = res.group(2);
                        String[] functions = ctx.split("><");
                        results.add(new Selection(selection, functions));
                    }else{
                        Matcher fRes = FUNCTION_PATTERN.matcher(funcs);
                        if(fRes.matches()){
                            results.add(new Selection(selection, new String[]{fRes.group(2)}));
                        }else{
                            LaPluma.getLogger().log(Level.WARNING,"Illegal syntax of selection prompt: " + t);
                        }
                    }
                }catch (Exception throwable){
                    LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse selection in conversation", throwable);
                }
            }else{
                Matcher funM = FUNCTION_PATTERN.matcher(t);
                if(funM.matches()){ // Function
                    try {
                        String func = funM.group(2);
                        results.add(new Function(func));
                    }catch (Throwable throwable){
                        LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse function in conversation", throwable);
                    }
                }else if (t.contains(":") && !t.startsWith(":")) { // Prompt
                    try {
                        String[] array = t.split(":");
                        String speaker = array[0];
                        String text = array[1];
                        if (array.length > 2) {
                            for (int j = 2; j < array.length; j++) {
                                text = text.concat(":").concat(array[j]);
                            }
                        }
                        results.add(new Prompt(speaker, Arrays.asList(text.split("\n"))));
                    }catch (Throwable throwable){
                        LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse prompt in conversation", throwable);
                    }
                }  else if (!t.replaceAll(" ","").startsWith("#")) { // Note
                    LaPluma.getLogger().log(Level.INFO,"Invalid Conversation Structure: " + t);
                }
            }

            /*  Old Code
            if(t.startsWith("[")){ // Selection
                try {
                    int c = t.lastIndexOf("]");
                    int d = t.lastIndexOf("<");
                    int f = t.lastIndexOf(">");
                    if (c < d && d < f && c > 0) {
                        String selectionText = t.substring(1, c);
                        String functionText = t.substring(d + 1, f);
                        results.add(new Selection(selectionText, new String[]{functionText}));
                    }else{
                        LaPluma.getLogger().log(Level.INFO,"Illegal syntax of selection prompt: " + t);
                    }
                }catch (Throwable throwable){
                    LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse selection in conversation", throwable);
                }
            }else if (t.startsWith("<") && t.endsWith(">")) { // Function
                try {
                    String functionText = t.substring(1, t.length() - 1);
                    results.add(new Function(functionText));
//                    SkyHUD.getLogger().log(Level.INFO,"[Journals] Parse Function" + t);
                }catch (Throwable throwable){
                    LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse function in conversation", throwable);
                }
            } else if (t.contains(":") && !t.startsWith(":")) { // Prompt
                try {
//                    SkyHUD.getLogger().log(Level.INFO,"[Journals] Parse Words" + t);
                    String[] array = t.split(":");
                    String speaker = array[0];
                    String text = array[1];
                    if (array.length > 2) {
                        for (int j = 2; j < array.length; j++) {
                            text = text.concat(":").concat(array[j]);
                        }
                    }
                    results.add(new Prompt(speaker, Arrays.asList(text.split("\n"))));
                }catch (Throwable throwable){
                    LaPluma.getLogger().log(Level.WARNING, "[Journals] failed to parse prompt in conversation", throwable);
                }
            }  else if (!t.replaceAll(" ","").startsWith("#")) { // Note
                LaPluma.getLogger().log(Level.INFO,"Invalid Conversation Structure: " + t);
            }

             */


        }
        return results;
    }
}
