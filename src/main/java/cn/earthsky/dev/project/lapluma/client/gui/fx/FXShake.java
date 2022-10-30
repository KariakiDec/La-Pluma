package cn.earthsky.dev.project.lapluma.client.gui.fx;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Getter;

public class FXShake extends FX{
    @Getter
    int x = 0;
    @Getter int y = 0;

    private double amplitude;
    private int cycleTick;

    private final double TWO_PI = Math.PI * 2;

    public FXShake(GuiDialog gui, double amplitude, int cycleTick) {
        super(gui);
        this.amplitude = amplitude;
        this.cycleTick = cycleTick;
    }

    @Override
    public String getName() {
        return "Shake";
    }

    @Override
    protected void onUpdate() {
//        System.out.println(amplitude*Math.sin((getDuration()*1f)/cycleTick*TWO_PI));
        x += amplitude*Math.sin((getDuration()*1f)/cycleTick*TWO_PI);
        y += amplitude*Math.cos((getDuration()*1f)/cycleTick*TWO_PI);
    }

    @Override
    protected void onRender() {

    }

    @Override
    protected void onDone() {

    }
}
