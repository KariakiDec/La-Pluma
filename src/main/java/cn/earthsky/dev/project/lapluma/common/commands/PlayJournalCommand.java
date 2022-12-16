package cn.earthsky.dev.project.lapluma.common.commands;

import cn.earthsky.dev.project.lapluma.client.event.PlayJournalCommandEvent;
import cn.earthsky.dev.project.lapluma.common.JournalNamespace;
import cn.earthsky.dev.project.lapluma.common.text.ConversationLoader;
import cn.earthsky.dev.project.lapluma.common.text.ConversationStructure;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;


public class PlayJournalCommand extends CommandBase {





    @Override
    public String getName() {
        return "playJournal";
    }

    @Override
    public String getUsage(ICommandSender iCommandSender) {
        return "/playJournal <Journal Name>";
    }

    @Override
    public void execute(MinecraftServer minecraftServer, ICommandSender sender, String[] args) throws CommandException {
        if(args.length == 1){
            String journal = args[0];
            ConversationStructure str = JournalNamespace.get(journal);
            if(str != null){
                PlayJournalCommandEvent.toOpen = str;
                sender.sendMessage(new TextComponentString("§9正在读取 " + journal + ".journal"));
            }else{
                sender.sendMessage(new TextComponentString("§c无法找到对话文件 " + journal + ".journal"));
            }
        }else{
            sender.sendMessage(new TextComponentString("§e正确格式: /playJournal <Journal Name>"));
        }
    }
}
