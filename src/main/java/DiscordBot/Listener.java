package DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class Listener extends ListenerAdapter
{
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        System.out.println("Im ready");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event) {
        User user = event.getAuthor();

//        if(user.isBot() || event.isWebhookMessage())
//            return;

//        if(event.getMessage().getMentionedUsers().equals(event.getGuild().getSelfMember()))
//            event.getChannel().sendMessageFormat("Ну, а я прошепчу... «НЕТ!»").queue();

        String raw = event.getMessage().getContentRaw();
        if(raw.startsWith(Bot.prefix))
        {
            manager.handle(event);
        }
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event)
    {
        final TextChannel channel = event.getChannel();
        channel.sendMessageFormat("%s\nУже ничего не изменить!\nНикаких компромиссов. Даже перед лицом Армагеддона.",event.getAuthor()).queue();
    }
}
