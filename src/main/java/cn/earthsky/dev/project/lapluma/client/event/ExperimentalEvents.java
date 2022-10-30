package cn.earthsky.dev.project.lapluma.client.event;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

@Mod.EventBusSubscriber
public class ExperimentalEvents {
    private static long lastCritical = System.currentTimeMillis() - 10000;
    private static int before = Minecraft.getMinecraft().gameSettings.limitFramerate;
    @SubscribeEvent
    public static void onCritical(CriticalHitEvent evt){
        if(evt.isVanillaCritical()) {
            lastCritical = System.currentTimeMillis();
            before = Minecraft.getMinecraft().gameSettings.limitFramerate;
            step = 0;
            System.out.println("Updated Critical Time");
        }
    }

    static int delayTime = 10000;
    static int beginFrame = 20;
    static int durationFrame = 10;
    static int step = -1;

    @SubscribeEvent
    public static void onRender(TickEvent.RenderTickEvent evt){
        if(evt.phase == TickEvent.Phase.END){
            if(step < 15 && step >= 0){
                step++;
                Minecraft mc = Minecraft.getMinecraft();
                if(mc.gameSettings.limitFramerate <= before){
                    mc.gameSettings.limitFramerate +=  (step < 8 ? -1 : 1)*Math.abs((before-beginFrame))/7;
                    if(mc.gameSettings.limitFramerate < 20){
                        mc.gameSettings.limitFramerate = 20;
                    }
                    if(mc.gameSettings.limitFramerate > before){
                        mc.gameSettings.limitFramerate = before;
                    }
                    System.out.println("Frame to " + mc.gameSettings.limitFramerate);
                }else{
                    mc.gameSettings.limitFramerate = before;
                }
                if(step >= 15){
                    step = -1;
                }
            }else{
                before = Minecraft.getMinecraft().gameSettings.limitFramerate;
            }
        }
    }
}
