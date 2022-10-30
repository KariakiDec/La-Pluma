package cn.earthsky.dev.project.lapluma.client.gui.fx;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Getter;

public class FXFadeOut extends FX implements FXFade{

    @Getter private float gAlpha = 1;

    public FXFadeOut(GuiDialog gui) {
        super(gui);
    }

    @Override
    public String getName() {
        return "FadeOut";
    }

    @Override
    protected void onUpdate() {
        gAlpha -= 1f/40;
        if(gAlpha <= 0){
            gui.stopCurrentFX();
        }
    }

    @Override
    protected void onRender() {

    }

    @Override
    protected void onDone() {

    }

    @Override
    public float gAlpha() {
        return gAlpha;
    }
}
