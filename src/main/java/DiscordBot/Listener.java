package DiscordBot;

import DiscordBot.command.CommandContext;
import DiscordBot.command.commands.Quotes;
import DiscordBot.command.commands.music.QueueCommands;
import me.duncte123.botcommons.BotCommons;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Emote;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.guild.member.update.GuildMemberUpdateNicknameEvent;
import net.dv8tion.jda.api.events.message.MessageUpdateEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageUpdateEvent;
import net.dv8tion.jda.api.events.role.RoleCreateEvent;
import net.dv8tion.jda.api.events.user.update.UserUpdateNameEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;

public class Listener extends ListenerAdapter
{
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        System.out.println("Im ready");
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event)
    {
        User user = event.getAuthor();
        JDA jda = event.getJDA();
        List<User> mentionedUsers = event.getMessage().getMentionedUsers();

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
            jda.shutdown();
            BotCommons.shutdown(event.getJDA());
        }

        List<Emote> emotes = jda.getEmotes();
        Random rnd = new Random();
        int collectionId;
        int emojiCount;

        if(event.getAuthor().getId().equals("255260574978408448"))
        {
            event.getMessage().addReaction(jda.getEmoteById("717020055417651341")).queue();
            return;
        }

        for (User mentionedUser : mentionedUsers) {
            if(mentionedUser.equals(jda.getUserById("839545278422974535"))){
                new Quotes().handle(new CommandContext(event,List.of()));
            }
        }

        collectionId = rnd.nextInt(emotes.size() - 1);
        if(rnd.nextInt(10) % 5 == 0)
            event.getMessage().addReaction(emotes.get(collectionId)).queue();

    }

    @Override
    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event)
    {
        final TextChannel channel = event.getChannel();
        channel.sendMessageFormat("%s\nУже ничего не изменить!\nНикаких компромиссов. Даже перед лицом Армагеддона.",event.getAuthor()).queue();
    }

    @Override
    public void onRoleCreate(@NotNull RoleCreateEvent event)
    {
        JDA jda = event.getJDA();
        TextChannel textChannel = jda.getTextChannelById(Config.get("chat_id"));
        textChannel.sendMessageFormat("Создана новая роль %s", event.getRole());
    }
}
