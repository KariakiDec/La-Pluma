package cn.earthsky.dev.project.lapluma.common;

import cn.earthsky.dev.project.lapluma.client.event.PlayJournalCommandEvent;
import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.client.gui.fx.FXFadeIn;
import cn.earthsky.dev.project.lapluma.client.gui.fx.FXFadeOut;
import cn.earthsky.dev.project.lapluma.client.gui.fx.FXShake;
import cn.earthsky.dev.project.lapluma.common.text.AVGCharacter;
import cn.earthsky.dev.project.lapluma.common.text.ConversationLoader;
import cn.earthsky.dev.project.lapluma.common.text.ConversationStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.text.TextComponentString;

public class Functions {
    public final static void doFunction(Parsing parsing, GuiDialog screen){
        if(parsing == null || screen == null){
            return;
        }
        if(parsing.getFunctionName().equalsIgnoreCase("clean")){
            screen.clearCharacter();
        }
        else if(parsing.getFunctionName().equalsIgnoreCase("show")){
            String id = Selector.searchNonNull(parsing.getArguments(),"avg","a","actor","act","id","path","val");
            boolean dimmed = Parsers.parseBoolean(Selector.searchNonNull(parsing.getArguments(),"dimmed","dim","dark","d"));
            int pos = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"pos","p","loc","location","position"), 50);
            screen.addCharacter(new AVGCharacter(id,pos, dimmed));
        }else if(parsing.getFunctionName().equalsIgnoreCase("solo")){
            screen.clearCharacter();
            String id = Selector.searchNonNull(parsing.getArguments(),"avg","a","actor","act","id","path","val");
            int pos = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"pos","p","loc","location","position"), 50);
            screen.addCharacter(new AVGCharacter(id,pos, false));
        }else if(parsing.getFunctionName().equalsIgnoreCase("bg")){
            String bg = Selector.searchNonNull(parsing.getArguments(),"bg","b","background","val","v");
            screen.setBackground(bg);
        }else if(parsing.getFunctionName().equalsIgnoreCase("resetBg")){
            screen.setBackground("bg");
        }else if(parsing.getFunctionName().equalsIgnoreCase("color")){
            int c = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"color","colour","c","val","v"), 0xffff5733);
            screen.setSplitLineColor(c);
        }else if(parsing.getFunctionName().equalsIgnoreCase("center")){
            screen.setCenterText(true);
        }else if(parsing.getFunctionName().equalsIgnoreCase("stopCenter")){
            screen.setCenterText(false);
        }else if(parsing.getFunctionName().equalsIgnoreCase("fx_fadeIn")){
            screen.setFX(new FXFadeIn(screen));
        }else if(parsing.getFunctionName().equalsIgnoreCase("fx_fadeOut")){
            screen.setFX(new FXFadeOut(screen));
        }else if(parsing.getFunctionName().equalsIgnoreCase("fx_shakeShort")){
            FXShake shake = new FXShake(screen,2.5,4);
            shake.setAutoDismiss(true,22);
            screen.setFX(shake);
        }else if(parsing.getFunctionName().equalsIgnoreCase("fx_shakeFor")){
            int d = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"t","time","d","duration"), 22);
            FXShake shake = new FXShake(screen,2.5,4);
            shake.setAutoDismiss(true,d);
            screen.setFX(shake);
        }else if(parsing.getFunctionName().equalsIgnoreCase("fx_shakeCustom")){
            int d = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"t","time","d","duration"), 22);
            double a = Parsers.parseDouble(Selector.searchNonNull(parsing.getArguments(),"amp","amplitude","a"), 2.5);
            int t = Parsers.parseInteger(Selector.searchNonNull(parsing.getArguments(),"cycle","c","period","p"), 4);
            FXShake shake = new FXShake(screen,a,t);
            shake.setAutoDismiss(true,d);
            screen.setFX(shake);
        }else if(parsing.getFunctionName().equalsIgnoreCase("music")){
            String n = Selector.searchNonNull(parsing.getArguments(),"m","music","audio","a","sound","s","name","n","val","value","v");
            screen.setPlayingMusic(n);
        }else if(parsing.getFunctionName().equalsIgnoreCase("sound")){
            String n = Selector.searchNonNull(parsing.getArguments(),"s","n","sound","name");
            float volume = Parsers.parseFloat(Selector.searchNonNull(parsing.getArguments(),"volume","v"), 1.0f);
            float pitch = Parsers.parseFloat(Selector.searchNonNull(parsing.getArguments(),"pitch","p"), 1.0f);
            SoundEvent event = SoundEvent.REGISTRY.getObject(new ResourceLocation(n));
            if(event != null){
                Minecraft.getMinecraft().world.playSound(Minecraft.getMinecraft().player.getPosition(),event, SoundCategory.MASTER,volume, pitch, false);
            }
        }else if(parsing.getFunctionName().equalsIgnoreCase("isound")){

        }else if(parsing.getFunctionName().equalsIgnoreCase("continue")) {
            String journal = Selector.searchNonNull(parsing.getArguments(), "next", "n", "v", "value", "val", "journal", "j", "c", "d", "destination");
            if (Minecraft.getMinecraft().currentScreen != null && Minecraft.getMinecraft().currentScreen instanceof GuiDialog) {
                ConversationStructure str = ConversationLoader.loadStructureFromResource(journal);
                if(str != null) {
                    ((GuiDialog) Minecraft.getMinecraft().currentScreen).continueStructure(str);
                }
            }
        }
    }
}
