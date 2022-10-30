package cn.earthsky.dev.project.lapluma.client.gui.fx;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Getter;

public class FXFadeIn extends FX implements FXFade{

    @Getter private float gAlpha = 0;

    public FXFadeIn(GuiDialog gui) {
        super(gui);
    }

    @Override
    public String getName() {
        return "FadeIn";
    }

    @Override
    protected void onUpdate() {
        gAlpha += 1f/40;
        if(gAlpha >= 1){
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
