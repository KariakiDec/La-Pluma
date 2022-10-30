package cn.earthsky.dev.project.lapluma.common.text.prompts;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.text.ConversationPrompt;

import java.util.LinkedHashMap;
import java.util.Map;

public class SelectionPrompt implements ConversationPrompt {

    private Map<String, String> selectionFunctions;
    public SelectionPrompt(){
        selectionFunctions = new LinkedHashMap<>();
    }

    public void addSelection(String selectionText, String functionText) {
        selectionFunctions.put(selectionText, functionText);
    }

    @Override
    public void sendPrompt(GuiDialog screen) {
        for(Map.Entry<String,String> funcs : selectionFunctions.entrySet()) {
            screen.addSelection((s) -> {

            }, funcs.getKey());
        }
    }

    @Override
    public PromptResult getResult(GuiDialog screen) {
        return null;
    }
}
