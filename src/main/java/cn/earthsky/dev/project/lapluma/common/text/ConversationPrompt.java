package cn.earthsky.dev.project.lapluma.common.text;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Builder;
import lombok.Value;

public interface ConversationPrompt {
    @Value
    @Builder
    class PromptResult{
        @Override
        public String toString() {
            return "PromptResult{" +
                    "delayTicks=" + delayTicks +
                    ", endConversation=" + endConversation +
                    ", newPage=" + newPage +
                    ", lock=" + lock +
                    '}';
        }

        @Builder.Default
        int delayTicks = 100;
        @Builder.Default
        boolean endConversation = false;
        @Builder.Default
        boolean newPage = false;
        @Builder.Default
        boolean lock = false;
    }

    void sendPrompt(GuiDialog screen);
    PromptResult getResult(GuiDialog screen);
}
