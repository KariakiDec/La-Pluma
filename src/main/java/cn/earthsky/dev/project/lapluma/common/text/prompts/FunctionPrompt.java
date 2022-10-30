package cn.earthsky.dev.project.lapluma.common.text.prompts;

import cn.earthsky.dev.project.lapluma.LaPluma;
import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.Functions;
import cn.earthsky.dev.project.lapluma.common.Selector;
import cn.earthsky.dev.project.lapluma.common.Parsers;
import cn.earthsky.dev.project.lapluma.common.Parsing;
import cn.earthsky.dev.project.lapluma.common.text.ConversationPrompt;

import java.util.logging.Level;

public class FunctionPrompt implements ConversationPrompt{
    private int customDelay = 100;
    private Parsing parsing;

    public FunctionPrompt(String functionText){
        try{
            customDelay = Integer.parseInt(functionText);
        }catch (NumberFormatException ignored){
            try {
                parsing = new Parsing(functionText);
            }catch (Throwable throwable) {
                LaPluma.getLogger().log(Level.WARNING, "[Journal] Failed to parse function text of " + parsing, throwable);
            }
        }

        if(parsing != null){
            if(parsing.getFunctionName().equalsIgnoreCase("delay")){
                try{
                    customDelay = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"val","value","v","t","tick"),customDelay);
                }catch (NumberFormatException ignored){}
            }
        }
    }

    @Override
    public void sendPrompt(GuiDialog screen) {
        Functions.doFunction(parsing,screen);
    }

    @Override
    public ConversationPrompt.PromptResult getResult(GuiDialog screen) {
        return ConversationPrompt.PromptResult.builder().delayTicks(customDelay).build();
    }
}
