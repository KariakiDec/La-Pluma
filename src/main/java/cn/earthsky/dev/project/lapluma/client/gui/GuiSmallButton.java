package cn.earthsky.dev.project.lapluma.client.gui;

import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class GuiSmallButton extends GuiButton {
    @Getter private Runnable callback;
    @Getter private String iconName;

    @Getter private String descriptionText;

    @Getter private int xPos;
    @Getter private int yPos;

    public GuiSmallButton(int buttonId, int x, int y, Runnable runnable, String iconName, String descriptionText) {
        super(buttonId, 0, 0, 16, 16, "");
        this.callback = runnable;
        this.iconName = iconName;
        this.descriptionText = descriptionText;
        this.xPos = x;
        this.yPos = y;
    }

    public void updatePosition(GuiScreen screen){
        this.x = (int) (screen.width*(xPos/100f));
        this.y = (int) (screen.height*(yPos/100f));
    }

    @Override
    public void drawButton(Minecraft mc, int mouseX, int mouseY, float partialTicks) {
        if (this.visible) {
            this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
            int i = this.getHoverState(this.hovered);
            this.mouseDragged(mc, mouseX, mouseY);
            mc.getTextureManager().bindTexture(new ResourceLocation("lapluma:icon/" + iconName + ".png"));
            Gui.drawModalRectWithCustomSizedTexture(this.x, this.y, 0,0,16,16,16,16);
            if(i == 2){
                mc.fontRenderer.drawStringWithShadow(descriptionText, mouseX + 2, mouseY + 2, 0xFFFFFF55);
            }
        }
    }
}
