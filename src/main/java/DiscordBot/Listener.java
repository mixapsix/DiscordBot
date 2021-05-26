package DiscordBot;

import me.duncte123.botcommons.BotCommons;
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

        if(user.isBot() || event.isWebhookMessage())
            return;
        String raw = event.getMessage().getContentRaw();
        if(raw.startsWith(Config.get("prefix")))
        {
            manager.handle(event);
        }
        if(raw.equals(Config.get("prefix") + "shutdown") && event.getAuthor().getId().equals(Config.get("owner_id")))
        {
            System.out.println("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
        }
    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event)
    {
        final TextChannel channel = event.getChannel();
        channel.sendMessageFormat("%s\nУже ничего не изменить!\nНикаких компромиссов. Даже перед лицом Армагеддона.",event.getAuthor()).queue();
    }

    @Override
    public void onUserUpdateName(@NotNull UserUpdateNameEvent event)
    {
        JDA jda = event.getJDA();
        TextChannel textChannel = jda.getTextChannelById(Config.get("chat_id"));
        textChannel.sendMessageFormat("%s\nСменил ник с %s на %s\nТак от меня не скроешься, я слежу за тобой", event.getUser(), event.getOldName(), event.getNewName());
    }
}
