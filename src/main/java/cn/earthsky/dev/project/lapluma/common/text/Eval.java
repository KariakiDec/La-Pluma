package cn.earthsky.dev.project.lapluma.common.text;

import cn.earthsky.dev.project.lapluma.LaPluma;
import lombok.AllArgsConstructor;
import lombok.Singular;
import lombok.Value;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

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
        String functionText;

        @Override
        public String getType() {
            return "Selection";
        }
    }


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
            if(t.replaceAll(" ","").isEmpty()){
                continue;
            }
            if(t.startsWith("[")){ // Selection
                try {
                    int c = t.lastIndexOf("]");
                    int d = t.lastIndexOf("<");
                    int f = t.lastIndexOf(">");
                    if (c < d && d < f && c > 0) {
                        String selectionText = t.substring(1, c);
                        String functionText = t.substring(d + 1, f);
                        results.add(new Selection(selectionText, functionText));
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
        }
        return results;
    }
}
