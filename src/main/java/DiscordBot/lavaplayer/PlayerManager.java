package DiscordBot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerManager
{
    private static PlayerManager INSTANCE;

    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager()
    {
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild)
    {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) ->
        {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
            return guildMusicManager;
        });
    }

    public void loadAndPlay(TextChannel channel,  String trackUrl)
    {
        final GuildMusicManager musicManager = this.getMusicManager(channel.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler()
        {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                musicManager.scheduler.queue(audioTrack);
                channel.sendMessage("Adding to queue: ")
                        .append(audioTrack.getInfo().title)
                        .append(" by ")
                        .append(audioTrack.getInfo().author)
                        .queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist)
            {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                int songNumber = 1;
                String result = "```Adding to queue:\n";
//                channel.sendMessage("Adding to queue: `")
//                        .append(String.valueOf(tracks.size()))
//                        .append("` tracks from playlist `")
//                        .append(audioPlaylist.getName())
//                        .append('`')
//                        .queue();

                for (final AudioTrack track : tracks)
                {
                    musicManager.scheduler.queue(track);

                    result += String.format("%s \"%s\"\n",songNumber++, String.valueOf(track.getInfo().title));
                }
                result +="```";

                channel.sendMessage(result).queue();
            }

            @Override
            public void noMatches()
            {

            }

            @Override
            public void loadFailed(FriendlyException e)
            {
                channel.sendMessage("Load failed");
            }
        });
    }


    public static PlayerManager getInstance()
    {
        if(INSTANCE == null)
            INSTANCE= new PlayerManager();
        return INSTANCE;
    }
}
