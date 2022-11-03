package cn.earthsky.dev.project.lapluma;

import cn.earthsky.dev.project.lapluma.common.commands.PlayJournalCommand;
import de.cuina.fireandfuel.CodecJLayerMP3;
import cn.earthsky.dev.project.lapluma.client.KeyLoader;
import cn.earthsky.dev.project.lapluma.client.gui.GuiDialog;
import cn.earthsky.dev.project.lapluma.common.network.ProxyPacketHandler;
import cn.earthsky.dev.project.lapluma.common.text.ConversationLoader;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.*;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.event.sound.SoundSetupEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;
import paulscode.sound.SoundSystemConfig;
import paulscode.sound.SoundSystemException;

import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@Mod(
        modid = LaPluma.MOD_ID,
        name = LaPluma.MOD_NAME,
        version = LaPluma.VERSION
)
public class LaPluma {

    public static final Logger getLogger(){
        return Logger.getLogger("La Pluma");
    }

    public static final String MOD_ID = "lapluma";
    public static final String MOD_NAME = "La Pluma";
    public static final String VERSION = "1.0-SNAPSHOT";

    public static String MD5HASH;



    /**
     * This is the instance of your mod as created by Forge. It will never be null.
     */
    @Mod.Instance(MOD_ID)
    public static LaPluma INSTANCE;

    /**
     * This is the first initialization event. Register tile entities here.
     * The registry events below will have fired prior to entry to this method.
     */
    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) {
        new ProxyPacketHandler().init();
    }

    /**
     * This is the second initialization event. Register custom recipes
     */
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new PlayJournalCommand());
//        KeyLoader.init();
    }

    /**
     * This is the final initialization event. Register actions from other mods here
     */
    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {
        GuiDialog.EXAMPLE_STRUCTURE = ConversationLoader.loadStructureFromResource("example");

        ((SimpleReloadableResourceManager) Minecraft.getMinecraft().getResourceManager()).registerReloadListener(resourceManager -> invokeMD5Hash());
    }

    public static boolean hasDialogBubbleProvided = false;

    public static void invokeMD5Hash(){
        String md5Key = "OUTPUT FAILED";
        // Generate MD5
        try {
            ResourcePackRepository rpr = Minecraft.getMinecraft().getResourcePackRepository();
            MessageDigest digest = DigestUtils.getSha256Digest();
            ByteBuf buf = Unpooled.buffer();
            Enumeration<URL> resourceList = Minecraft.class.getClassLoader().getResources(
                    "assets/lapluma/journals");
            while (resourceList.hasMoreElements()) {
                URL r = resourceList.nextElement();
                JarURLConnection urlcon = (JarURLConnection) (r.openConnection());
                try (JarFile jar = urlcon.getJarFile()) {
                    Enumeration<JarEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if(name.endsWith(".journal") || (name.contains("avg") && name.endsWith(".png"))) {
                            try (InputStream str = jar.getInputStream(entry)) {
                                int b = str.read();
                                while (b != -1) {
                                    buf.writeByte(b);
                                    b = str.read();
                                }
                            }
                            System.out.println("Found " + name);
                        }else if(name.endsWith("icon/dialog_bubble.png")){
                            hasDialogBubbleProvided = true;
                        }
                    }
                }
            }

            for(ResourcePackRepository.Entry r : rpr.getRepositoryEntries()){
                if(r.getResourcePack() instanceof FileResourcePack){
                    FileResourcePack pack = (FileResourcePack) r.getResourcePack();
                    ZipFile jar = pack.getResourcePackZipFile();
                    Enumeration<? extends ZipEntry> entries = jar.entries();
                    while (entries.hasMoreElements()) {
                        ZipEntry entry = entries.nextElement();
                        String name = entry.getName();
                        if(name.endsWith(".journal") || (name.contains("avg") && name.endsWith(".png"))) {
                            try (InputStream str = jar.getInputStream(entry)) {
                                int b = str.read();
                                while (b != -1) {
                                    buf.writeByte(b);
                                    b = str.read();
                                }
                            }
                            System.out.println("Found " + name);
                        }else if(name.endsWith("icon/dialog_bubble.png")){
                            hasDialogBubbleProvided = true;
                        }
                    }
                }
            }
            if(rpr.getServerResourcePack() != null && rpr.getServerResourcePack() instanceof FileResourcePack){
                ZipFile jar = ((FileResourcePack) rpr.getServerResourcePack()).getResourcePackZipFile();
                Enumeration<? extends ZipEntry> entries = jar.entries();
                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    if(name.endsWith(".journal") || (name.contains("avg") && name.endsWith(".png"))) {
                        try (InputStream str = jar.getInputStream(entry)) {
                            int b = str.read();
                            while (b != -1) {
                                buf.writeByte(b);
                                b = str.read();
                            }
                        }
                        System.out.println("Found " + name);
                    }else if(name.endsWith("icon/dialog_bubble.png")){
                        hasDialogBubbleProvided = true;
                    }
                }
            }

            md5Key = Hex.encodeHexString(digest.digest(buf.array()));
        } catch (IOException e) {
            // OUTPUT FAILED
        }

//        getLogger().info("Hash Generated: " + md5Key);
        MD5HASH = md5Key;

        if(Minecraft.getMinecraft().world != null && !Minecraft.getMinecraft().isSingleplayer()){
            ProxyPacketHandler.sendPacket(-1,0, MD5HASH);
        }
    }

    /**
     * Forge will automatically look up and bind blocks to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Blocks {
      /*
          public static final MySpecialBlock mySpecialBlock = null; // placeholder for special block below
      */
    }

    /**
     * Forge will automatically look up and bind items to the fields in this class
     * based on their registry name.
     */
    @GameRegistry.ObjectHolder(MOD_ID)
    public static class Items {
      /*
          public static final ItemBlock mySpecialBlock = null; // itemblock for the block above
          public static final MySpecialItem mySpecialItem = null; // placeholder for special item below
      */
    }

    public static class Sounds{
        public static final SoundEvent BEEP = new SoundEvent(new ResourceLocation("lapluma","beep"));
        public static final SoundEvent CLICK = new SoundEvent(new ResourceLocation("lapluma","click"));
        public static final SoundEvent MUSIC = new SoundEvent(new ResourceLocation("lapluma","music"));

    }

    /**
     * This is a special class that listens to registry events, to allow creation of mod blocks and items at the proper time.
     */
    @Mod.EventBusSubscriber
    public static class ObjectRegistryHandler {

        @SubscribeEvent
        public static void onSoundSetup(SoundSetupEvent event) {
            try {
                SoundSystemConfig.setCodec("mp3", CodecJLayerMP3.class);
                SoundSystemConfig.setSoundFilesPackage("assets/lapluma/musics/");
            } catch (SoundSystemException soundsystemexception) {
                getLogger().log(Level.WARNING, soundsystemexception.getMessage());
            }
        }

        @SubscribeEvent
        public static void onSoundEvenrRegistration(RegistryEvent.Register<SoundEvent> event) {
            event.getRegistry().register(Sounds.BEEP.setRegistryName(new ResourceLocation("lapluma", "beep")));
            event.getRegistry().register(Sounds.CLICK.setRegistryName(new ResourceLocation("lapluma", "click")));
            event.getRegistry().register(Sounds.MUSIC.setRegistryName(new ResourceLocation("lapluma", "music")));
        }

        /**
         * Listen for the register event for creating custom items
         */
        @SubscribeEvent
        public static void addItems(RegistryEvent.Register<Item> event) {
           /*
             event.getRegistry().register(new ItemBlock(Blocks.myBlock).setRegistryName(MOD_ID, "myBlock"));
             event.getRegistry().register(new MySpecialItem().setRegistryName(MOD_ID, "mySpecialItem"));
            */
        }

        /**
         * Listen for the register event for creating custom blocks
         */
        @SubscribeEvent
        public static void addBlocks(RegistryEvent.Register<Block> event) {
           /*
             event.getRegistry().register(new MySpecialBlock().setRegistryName(MOD_ID, "mySpecialBlock"));
            */
        }
    }
    /* EXAMPLE ITEM AND BLOCK - you probably want these in separate files
    public static class MySpecialItem extends Item {

    }

    public static class MySpecialBlock extends Block {

    }
    */
}
