package cn.earthsky.dev.project.lapluma.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.*;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class GuiLogSlider {
    private GuiDialog parent;

    private List<String> log = new ArrayList<>();
    private int scrollCursor = 0;

    public void addLog(String text){
        log.add(0,text);
    }

    private boolean isShow = false;
    public GuiLogSlider(GuiDialog parent){
        this.parent = parent;

    }

    public void updateShowState(boolean isShow){
        if(isShow != this.isShow) scrollCursor = 0;
        this.isShow = isShow;
    }

    public void handleMouseInput() throws IOException
    {

        int i = Mouse.getEventDWheel();

        if (i != 0)
        {
            if (i > 1)
            {
                i = 1;
            }

            if (i < -1)
            {
                i = -1;
            }

            this.scrollCursor += i;
            if(scrollCursor < 0){
                scrollCursor = 0;
            }else if(scrollCursor != 0 && scrollCursor > log.size()-1){
                scrollCursor = log.size()-1;
            }
        }
    }

    public void onDrawLog(int mouseX, int mouseY, float partialTicks){
        int blanketSize = (int) (20 * parent.getWidthScale());
        int trimWidth = (int) ((parent.width - blanketSize * 4) / (parent.getWidthScale() * 1.3));


        parent.drawGradientRect(0,0,parent.width, parent.height, 0x80424242,0x80212121);
        FontRenderer font = Minecraft.getMinecraft().fontRenderer;
//        font.drawString("Cursor " + scrollCursor + " Log " + log.size(), 0,0,0xffffffff);

        int lower = parent.height;

        double textScale = parent.getHeightScale() * 1.3d;
        GL11.glPushMatrix();
        GL11.glScaled(textScale, textScale, 1);
        for(int i = Math.max(scrollCursor, 0);i < log.size();i++){
            if(lower-12*textScale <= 10){
                break;
            }
            String t = log.get(i);
            List<String> trimed = font.listFormattedStringToWidth(t, trimWidth);
            lower -= 12*textScale * trimed.size();
            font.drawSplitString(t, (int) (20/textScale), (int) ((lower-9)/textScale), trimWidth, 0xffffffff);
        }
        GL11.glPopMatrix();

    }
}
