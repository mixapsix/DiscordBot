package DiscordBot.command.commands.music;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.managers.AudioManager;

public class JoinCommand implements ICommand
{

    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel =  ctx.getChannel();
        final Member self = ctx.getSelfMember();
        final GuildVoiceState selfVoiceState = self.getVoiceState();

        if(selfVoiceState.inVoiceChannel())
        {
            channel.sendMessage("I`m already in a voice channel").queue();
            return;
        }

        final Member member = ctx.getMember();
        final GuildVoiceState memberVoiceState = member.getVoiceState();

        if(!memberVoiceState.inVoiceChannel())
        {
            channel.sendMessage("You need to be in a voice channel").queue();
            return;
        }

        final AudioManager audioManager = ctx.getGuild().getAudioManager();
        final VoiceChannel memberVoiceChannel = memberVoiceState.getChannel();


        audioManager.openAudioConnection(memberVoiceChannel);
        channel.sendMessageFormat("Connection to \uD83D\uDD0A %s", memberVoiceChannel.getName()).queue();
    }

    @Override
    public String getName()
    {
        return "join";
    }
}
