package cn.earthsky.dev.project.lapluma.common.text;


import cn.earthsky.dev.project.lapluma.common.text.prompts.FunctionPrompt;
import cn.earthsky.dev.project.lapluma.common.text.prompts.SelectionPrompt;
import cn.earthsky.dev.project.lapluma.common.text.prompts.WordPrompt;

import java.util.ArrayList;
import java.util.List;

public class ConversationStructure {
    private List<ConversationPrompt> prompts = new ArrayList<>();
    private String name;

    public ConversationStructure(String name){
        this.name = name;
    }


    public ConversationStructure with(ConversationPrompt prompt){
        addPrompt(prompt);
        return this;
    }

    public boolean isValid(){
        return !prompts.isEmpty();
    }

    public ConversationPrompt get(int order){
        if(order >= prompts.size()){
            return null;
        }
        return prompts.get(order);
    }

    public int length(){
        return prompts.size();
    }

    public ConversationPrompt at(int order){
        return get((order));
    }

    public List<ConversationPrompt> getPrompts() {
        return prompts;
    }

    public void addPrompt(ConversationPrompt prompt){
        this.prompts.add(prompt);
    }



    public void loadPrompts(List<String> rawText){
        SelectionPrompt constructing = null;
        for(Eval.EvalResult result : Eval.eval(rawText)){
            if(constructing == null) {
                if (result instanceof Eval.Prompt) {
                    List<String> texts = ((Eval.Prompt) result).getText();
                    for(String t : texts) {
                        addPrompt(new WordPrompt(((Eval.Prompt) result).getSpeaker(), t));
                    }
                } else if (result instanceof Eval.Function) {
                    addPrompt(new FunctionPrompt(((Eval.Function) result).getFunctionText()));
                } else if (result instanceof Eval.Selection) {
                    constructing = new SelectionPrompt();
                    constructing.addSelection(((Eval.Selection) result).getSelectText(), ((Eval.Selection) result).getFunctionText());
                }
            }else{
                if(result instanceof Eval.Selection){
                    constructing.addSelection(((Eval.Selection) result).getSelectText(), ((Eval.Selection) result).getFunctionText());
                }else{
                    addPrompt(constructing);
                    constructing = null;
                    if (result instanceof Eval.Prompt) {
                        List<String> texts = ((Eval.Prompt) result).getText();
                        for(String t : texts) {
                            addPrompt(new WordPrompt(((Eval.Prompt) result).getSpeaker(), t));
                        }
                    } else if (result instanceof Eval.Function) {
                        addPrompt(new FunctionPrompt(((Eval.Function) result).getFunctionText()));
                    }
                }
            }
        }
        if(constructing != null){
            addPrompt(constructing);
        }
    }

    public String getName() {
        return name;
    }
}
