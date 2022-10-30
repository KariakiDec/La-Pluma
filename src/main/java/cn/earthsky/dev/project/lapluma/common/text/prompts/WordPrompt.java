package cn.earthsky.dev.project.lapluma.common.text.prompts;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.text.ConversationPrompt;

public class WordPrompt implements ConversationPrompt {

    private String speaker;
    private String sentences;

    public String getSpeaker() {
        return speaker;
    }

    public String getSentences() {
        return sentences;
    }

    public WordPrompt(String speaker, String sentences){
        this.speaker  = speaker;
        this.sentences = sentences;
    }


    @Override
    public void sendPrompt(GuiDialog screen) {
        screen.showText(speaker, sentences);
    }

    @Override
    public PromptResult getResult(GuiDialog screen) {
        return PromptResult.builder().build();
    }
}
