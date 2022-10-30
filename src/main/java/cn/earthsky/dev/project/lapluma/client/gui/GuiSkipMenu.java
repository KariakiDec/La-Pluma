package cn.earthsky.dev.project.lapluma.client.gui;

import net.minecraft.client.Minecraft;

public class GuiSkipMenu {

    private int noButtonWidth;
    private int yesButtonWidth;
    private int buttonHeight;

    public GuiSkipMenu(GuiDialog parent){
        Minecraft mc = Minecraft.getMinecraft();
        noButtonWidth = mc.fontRenderer.getStringWidth("取消");
        yesButtonWidth = mc.fontRenderer.getStringWidth("确认");
        buttonHeight = mc.fontRenderer.FONT_HEIGHT + 4;

    }


    private int menuLeft;
    private int menuTop;
    private int menuRight;
    private int menuDown;

    private int yesButtonX;
    private int yesButtonY;

    private int noButtonX;
    private int noButtonY;

    private String title = "剧情标题";
    private String description = "剧情概要，对剧情简短的描述使选择跳过的玩家并不丢失太多情节信息，在Journal文件中通过info(title,abstract)来指定。";

    private void updatePos(int width, int height){
        this.menuLeft = width/2 - 120;
        this.menuRight = width/2 + 120;
        this.menuTop = height/2 - 60;
        this.menuDown = height/2 + 60;

        this.yesButtonX = this.menuRight - 21 - yesButtonWidth;
        this.yesButtonY = this.menuDown - 20;

        this.noButtonX = this.menuLeft + 21;
        this.noButtonY = this.menuDown - 20;

    }

    protected void onResize(Minecraft mcIn, int w, int h) {
        updatePos(w,h);
    }

    private boolean isInYesButton(int mouseX, int mouseY){
        return mouseX > this.yesButtonX-4 && mouseX < this.yesButtonX+this.yesButtonWidth+2 && mouseY > this.yesButtonY -2 && mouseY < this.yesButtonY+buttonHeight+2;
    }

    private boolean isInNoButton(int mouseX, int mouseY){
        return mouseX > this.noButtonX-4 && mouseX < this.noButtonX+this.noButtonWidth+2 && mouseY > this.noButtonY -2 && mouseY < this.noButtonY+buttonHeight+2;
    }

    public void drawSkipMenu(Minecraft mc, GuiDialog screen, int mouseX, int mouseY, float partialTicks) {

        screen.drawGradientRect(menuLeft, menuTop, menuRight, menuDown, 0xCC424242, 0xCC212121);

        screen.drawCenteredString(screen.mc.fontRenderer, "确认要跳过剧情吗?",screen.width/2, menuTop + 10, 0xFFFFFFFF);

        mc.fontRenderer.drawStringWithShadow("确认", yesButtonX, yesButtonY, (isInYesButton(mouseX,mouseY) ? 0xFFFFFF55 : 0xFFFFFFFF));
        mc.fontRenderer.drawStringWithShadow("取消", noButtonX, noButtonY, (isInNoButton(mouseX,mouseY) ? 0xFFFFFF55 : 0xFFFFFFFF));

        screen.drawCenteredString(screen.mc.fontRenderer,"§l" + title, screen.width/2, menuTop + 27, 0xFFFFFFFF);

        int trimWidth = 222;
        mc.fontRenderer.drawSplitString(description,menuLeft + 10, menuTop + 43, trimWidth, 0xFFFFFFFF);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton, GuiDialog parent) {
        if(mouseButton == 0){
            if(isInNoButton(mouseX, mouseY)){
                parent.setShowSkipMenu(false);
                parent.playPressedSound();
            }else if(isInYesButton(mouseX, mouseY)){
                parent.trySkipped = true;
                Minecraft.getMinecraft().displayGuiScreen(null);
                parent.playPressedSound();
            }
        }
    }
}
