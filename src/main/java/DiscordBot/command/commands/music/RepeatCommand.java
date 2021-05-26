package DiscordBot.command.commands.music;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.lavaplayer.GuildMusicManager;
import DiscordBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.List;

public class RepeatCommand implements ICommand
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

        final GuildMusicManager musicManager = PlayerManager.getInstance().getMusicManager(ctx.getGuild());
        final boolean nowRepeating = !musicManager.scheduler.repeating;

        musicManager.scheduler.repeating = nowRepeating;

        channel.sendMessageFormat("The player has been set to **%s**", nowRepeating ? "repeating" : "not repeating").queue();
    }

    @Override
    public String getName() {
        return "repeat";
    }

    @Override
    public String getHelp() {
        return "Loops current song";
    }

    @Override
    public List<String> getAliases() {
        return List.of("r");
    }
}
