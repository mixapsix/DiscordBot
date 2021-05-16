package DiscordBot.command.commands.music;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class PlayCommand implements ICommand
{

    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();
        if(!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in voice channel, run ~join command").queue();
            return;
        }
        if(!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in voice channel").queue();
            return;
        }
        if(!memberVoiceState.getChannel().equals(selfVoiceState.getChannel()))
        {
            channel.sendMessage("You need to be in same voice channel").queue();
            return;
        }
        if(ctx.getArgs().size() == 0)
        {
            channel.sendMessage("Add youtube video to command").queue();
            return;
        }
        PlayerManager.getInstance().loadAndPlay(channel, ctx.getArgs().get(0));
    }
    @Override
    public String getName()
    {
        return "play";
    }
}
