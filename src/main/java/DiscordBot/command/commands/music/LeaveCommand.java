package DiscordBot.command.commands.music;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.lavaplayer.GuildMusicManager;
import DiscordBot.lavaplayer.PlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import java.util.List;

public class LeaveCommand implements ICommand
{

    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(!selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I need to be in voice channel, run ~join command").queue();
            return;
        }

        final Member member = ctx.getMember();;
        final GuildVoiceState memberVoiceState = member.getVoiceState();

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

        Guild guild = ctx.getGuild();

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(guild);

        musicManager.scheduler.repeating = false;
        musicManager.scheduler.queue.clear();
        musicManager.audioPlayer.stopTrack();


        final AudioManager audioManager = ctx.getGuild().getAudioManager();

        audioManager.closeAudioConnection();

        channel.sendMessageFormat("Bot leave from channel \uD83D\uDD0A `%s`", memberVoiceState.getChannel().getName()).queue();

    }

    @Override
    public String getName()
    {
        return "leave";
    }

    @Override
    public String getHelp()
    {
        return "Leave bot from channel";
    }

    @Override
    public List<String> getAliases()
    {
        return List.of("l");
    }
}
