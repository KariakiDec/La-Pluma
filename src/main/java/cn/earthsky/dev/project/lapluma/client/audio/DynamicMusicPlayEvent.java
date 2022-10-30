package cn.earthsky.dev.project.lapluma.client.audio;


import cn.earthsky.dev.project.lapluma.LaPluma;
import net.minecraft.client.audio.ISound;
import net.minecraftforge.client.event.sound.PlaySoundSourceEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.logging.Level;

@Mod.EventBusSubscriber
public class DynamicMusicPlayEvent {


    @SubscribeEvent
    public static void onSoundPlay(PlaySoundSourceEvent evt) {
        ISound sound = evt.getSound();
        if(sound instanceof DynamicMusicSound){
            String songUrl = ((DynamicMusicSound) sound).getSongUrl();
            System.out.println("Will Play: " + songUrl);
            try {
                evt.getManager().sndSystem.newStreamingSource(false, evt.getUuid(), songUrl,  true,
                        sound.getXPosF(), sound.getYPosF(), sound.getZPosF(), sound.getAttenuationType().getTypeInt(), 16);
            }catch (Exception e){
                LaPluma.getLogger().log(Level.WARNING, e.getMessage());
            }
        }
    }
}
