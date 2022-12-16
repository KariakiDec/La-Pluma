package cn.earthsky.dev.project.lapluma.common.network;

import cn.earthsky.dev.project.lapluma.LaPluma;
import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.Functions;
import cn.earthsky.dev.project.lapluma.common.JournalNamespace;
import cn.earthsky.dev.project.lapluma.common.Parsing;
import com.google.common.base.Charsets;
import cn.earthsky.dev.project.lapluma.common.text.ConversationLoader;
import cn.earthsky.dev.project.lapluma.common.text.ConversationStructure;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.FMLEventChannel;
import net.minecraftforge.fml.common.network.FMLNetworkEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

import java.util.Objects;
import java.util.logging.Level;

public class ProxyPacketHandler {

    static FMLEventChannel channel;
    public static final String MSG_CHANNEL = "SkyHUDMessage";

    public void init(){
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
        ProxyPacketHandler.channel = NetworkRegistry.INSTANCE.newEventDrivenChannel(MSG_CHANNEL);
        channel.register(this);
    }

    private String lastWorld;

    @SubscribeEvent
    public void onQuitServer(FMLNetworkEvent.ClientDisconnectionFromServerEvent evt){
        lastWorld = null;
    }

    @SubscribeEvent
    public void onJoinServer(EntityJoinWorldEvent evt){
        if(Objects.equals(lastWorld, evt.getWorld().getWorldInfo().getWorldName())){
            return;
        }
        if(evt.getEntity() instanceof EntityPlayerSP && Minecraft.getMinecraft().player == evt.getEntity()) {
            lastWorld = evt.getWorld().getWorldInfo().getWorldName();
            Minecraft.getMinecraft().addScheduledTask(() -> {
                ByteBuf pool = Unpooled.buffer();
                pool.writeInt(-1);
                pool.writeInt(0);
                pool.writeBytes((LaPluma.MD5HASH).getBytes(Charsets.UTF_8));
                FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(pool), MSG_CHANNEL);
                channel.sendToServer(packet);
            });
        }
    }

    public static void sendPacket(int act, int data, String ctx){
        ByteBuf pool = Unpooled.buffer();
        pool.writeInt(act);
        pool.writeInt(data);
        pool.writeBytes(ctx.getBytes(Charsets.UTF_8));
        FMLProxyPacket packet = new FMLProxyPacket(new PacketBuffer(pool), MSG_CHANNEL);
        channel.sendToServer(packet);
    }
    @SubscribeEvent
    public void onClientPacket(FMLNetworkEvent.ClientCustomPacketEvent evt) {
        FMLProxyPacket packet = evt.getPacket();
        if(packet.channel().equals(MSG_CHANNEL)){
            ByteBuf byteBuf = evt.getPacket().payload();
            // 0
            int a = byteBuf.readInt();
            int b = byteBuf.readInt();
            final String c = byteBuf.toString(Charsets.UTF_8).replaceAll("\0","");
            // ACT
            if(a == 0) { // Open Packet
                if(c.length() > 0){
                    Minecraft.getMinecraft().addScheduledTask(() -> {
                        ConversationStructure str = JournalNamespace.get(c);
                        if(str != null){
                            Minecraft.getMinecraft().displayGuiScreen(new GuiDialog(str));
                            ProxyPacketHandler.sendPacket(1,0, str.getName());
                        }
                    });
                }
            }else if(a == 2){
                GuiScreen screen = Minecraft.getMinecraft().currentScreen;
                if(screen instanceof GuiDialog){
                    try{
                        Functions.doFunction(new Parsing(c), (GuiDialog) screen);
                    }catch (Throwable throwable){
                        LaPluma.getLogger().log(Level.WARNING, "cannot parse function '" + c + " from server", throwable);
                    }
                }
            }else if(a == 9) {
                System.out.println("Received Handled Message: " + c);
                PlaceholderConnect.handleRequest(b, c);
            }
        }
    }

    /*
    0 int - 命令类型
            0 - 打开对话
            1 - hash合法验证
            2 - 运行function
            9 - papi
    1 int - 附加内容
               0 = 0 空
               0 = 1 是否合法 true 1 / false 0
               0 = 9 PAPI parse编码 index
    2 string - 对话名
     */
}
