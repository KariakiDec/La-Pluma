package cn.earthsky.dev.project.lapluma.common.network;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import net.minecraft.client.Minecraft;

public class PlaceholderConnect {
    static String[] arrayHolder = new String[64];
    static int indexCursor = 0;

    static int nextIndexCursor(){
        indexCursor++;
        if(indexCursor >= 64){
            indexCursor = 1;
        }
        return indexCursor -1;
    }

    public static void submitRequest(String original){
        int i = nextIndexCursor();
        arrayHolder[i] = original;
        ProxyPacketHandler.sendPacket(9,i,original);
    }

    public static void handleRequest(int index, String output){
        if(index < 64 && index >= 0){
            String original = arrayHolder[index];
            if(original != null){
                Minecraft mc = Minecraft.getMinecraft();
                if(mc.currentScreen != null){
                    if(mc.currentScreen instanceof GuiDialog){
                        GuiDialog dialog = (GuiDialog) mc.currentScreen;
                        dialog.tryRAUFullText(original, output);
                        arrayHolder[index] = null;
                    }
                }
            }
        }
    }
}
