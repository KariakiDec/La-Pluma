package cn.earthsky.dev.project.lapluma.client.event;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.network.ProxyPacketHandler;
import cn.earthsky.dev.project.lapluma.common.text.ConversationStructure;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class PlayJournalCommandEvent {

    private static int tickTime = 0;
    public static ConversationStructure toOpen = null;

    @SubscribeEvent
    public static void onServerTick(TickEvent.ClientTickEvent evt){
        if(toOpen != null){
            tickTime++;
            if(tickTime >= 20){
                Minecraft.getMinecraft().displayGuiScreen(new GuiDialog(toOpen));
                ProxyPacketHandler.sendPacket(1,0, toOpen.getName());
                tickTime = 0;
                toOpen = null;
            }
        }
    }
}
