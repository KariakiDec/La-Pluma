package cn.earthsky.dev.project.lapluma.client.gui.fx;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Getter;

public abstract class FX {
    public abstract String getName();
    protected abstract void onUpdate();
    protected abstract void onRender();

    protected abstract void onDone();

    public void done(){
        onDone();
    }

    boolean autoDismiss = false;
    int maxDuration = -1;

    public void setAutoDismiss(boolean enable, int maxDuration){
        autoDismiss = enable;
        this.maxDuration = maxDuration;
    }

    @Getter protected GuiDialog gui;
    @Getter private int duration;

    public FX(GuiDialog gui){
        this.gui = gui;
    }

    public void render(){
        onRender();
    }

    public void updateFx(){
        duration++;
        onUpdate();
        if(autoDismiss){
            if(duration >= maxDuration){
                gui.stopCurrentFX();
            }
        }
    }
}
