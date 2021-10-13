package DiscordBot.command.commands.music;

import DiscordBot.Config;
import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.lavaplayer.PlayerManager;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.print.attribute.URISyntax;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class PlayCommand implements ICommand
{
    @Override
    public List<String> getAliases() {
        return List.of("play", "p");
    }

    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel = ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();
        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(ctx.getArgs().isEmpty())
        {
            channel.sendMessage("Correct usage is ~play <youtube link>").queue();
            return;
        }

        if(!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in voice channel").queue();
            return;
        }

        if(!selfVoiceState.inVoiceChannel())
        {
            final AudioManager audioManager = ctx.getGuild().getAudioManager();
            final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel();
            audioManager.openAudioConnection(memberVoiceChannel);
            channel.sendMessageFormat("Connection to \uD83D\uDD0A `%s`", memberVoiceChannel.getName()).queue();
        }

        if(ctx.getArgs().size() == 0)
        {
            channel.sendMessage("Add youtube video to command").queue();
            return;
        }

        String link = String.join(" ", ctx.getArgs());

        if(!isUrl(link))
        {
            link = "ytsearch:" + link;
        }

        PlayerManager.getInstance().loadAndPlay(channel, link);

    }
    @Override
    public String getName()
    {
        return "play";
    }

    @Override
    public String getHelp() {
        return "Play youtube video, need join bot to voice channel\n" +
                "Usage "+ Config.get("prefix") + "play <youtube video url>\n" +
                 Config.get("prefix")+ "play <search request>";
    }

    private boolean isUrl(String url)
    {
        try
        {
            new URI(url);
            return true;
        }
        catch (URISyntaxException e)
        {
            return false;
        }
    }
}
