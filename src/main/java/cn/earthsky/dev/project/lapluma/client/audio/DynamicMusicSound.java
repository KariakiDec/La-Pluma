package cn.earthsky.dev.project.lapluma.client.audio;

import cn.earthsky.dev.project.lapluma.LaPluma;
import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.MovingSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;

import java.util.Objects;

public class DynamicMusicSound extends MovingSound {

    @Getter private final String songUrl;
    private final BlockPos pos;


    @Override
    public boolean canRepeat(){
        return true;
    }

    public DynamicMusicSound(BlockPos pos, String songUrl) {
        super(LaPluma.Sounds.MUSIC, SoundCategory.RECORDS);
        this.songUrl = songUrl;
        this.xPosF = pos.getX() + 0.5f;
        this.yPosF = pos.getY() + 0.5f;
        this.zPosF = pos.getZ() + 0.5f;
        this.volume = 4.0f;
        this.pos = pos;
        this.repeatDelay = -1;


    }

    @Override
    public void update() {
        if(Minecraft.getMinecraft().currentScreen == null){
            donePlaying = true;
            return;
        }
        if(!(Minecraft.getMinecraft().currentScreen instanceof GuiDialog)){
            donePlaying = true;
        }else{
            if(!Objects.equals(((GuiDialog) Minecraft.getMinecraft().currentScreen).getPlayingMusic(), songUrl)){
                donePlaying = true;
            }
        }
    }
}
