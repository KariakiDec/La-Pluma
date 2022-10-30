package cn.earthsky.dev.project.lapluma.client;

import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import org.lwjgl.input.Keyboard;

@Mod.EventBusSubscriber
public class KeyLoader {
    private static final KeyBinding open = new KeyBinding("Dialog Example", KeyConflictContext.IN_GAME, KeyModifier.NONE, Keyboard.KEY_K, "key.category.lapluma");

    public static void init(){
        ClientRegistry.registerKeyBinding(open);
    }

    @SubscribeEvent
    public static void onKeyType(InputEvent.KeyInputEvent evt){
        if(open.isPressed()){
            Minecraft.getMinecraft().displayGuiScreen(new GuiDialog(GuiDialog.EXAMPLE_STRUCTURE));
        }
    }
}
