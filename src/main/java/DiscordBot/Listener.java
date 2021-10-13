package DiscordBot;

import DiscordBot.command.CommandContext;
import DiscordBot.command.commands.Quotes;
import DiscordBot.command.commands.music.QueueCommands;
import DiscordBot.database.SQLiteDataSource;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Random;

public class Listener extends ListenerAdapter
{
    private static final Logger LOGGER = LoggerFactory.getLogger(Listener.class);
    private final CommandManager manager = new CommandManager();

    @Override
    public void onReady(@NotNull ReadyEvent event)
    {
        LOGGER.info("{} is ready", event.getJDA().getSelfUser().getAsTag());

        // print internal state
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        StatusPrinter.print(lc);
    }

    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent event)
    {
        User user = event.getAuthor();
        JDA jda = event.getJDA();
        List<User> mentionedUsers = event.getMessage().getMentionedUsers();
        List<Emote> emotes = jda.getEmotes();

        if (user.isBot() || event.isWebhookMessage()) {
            return;
        }

        final long guildId = event.getGuild().getIdLong();
        String prefix = Prefixes.PREFIXES.computeIfAbsent(guildId, this::getPrefix);
        String raw = event.getMessage().getContentRaw();

        if (raw.equalsIgnoreCase(prefix + "shutdown")
                && user.getId().equals(Config.get("owner_id"))) {
            LOGGER.info("Shutting down");
            event.getJDA().shutdown();
            BotCommons.shutdown(event.getJDA());
            return;
        }

        if (raw.startsWith(prefix))
        {
            manager.handle(event, prefix);
        }

        Random rnd = new Random();


        if(event.getAuthor().getId().equals("255260574978408448"))
        {
            event.getMessage().addReaction(jda.getEmoteById("717020055417651341")).queue();
        }

        for (User mentionedUser : mentionedUsers) {
            if(mentionedUser.equals(jda.getUserById("839545278422974535")))
            {
                new Quotes().handle(new CommandContext(event,List.of()));
            }
        }

        if(Config.get("EMOJI_STIKER").equals("true") && !event.getAuthor().getId().equals("255260574978408448")) {
            int collectionId = rnd.nextInt(emotes.size() - 1);
            if ((rnd.nextInt(100) % TextParse.tryParseInt(Config.get("EMOJI_COEFFICIENT"))) == 0)
                event.getMessage().addReaction(emotes.get(collectionId)).queue();
        }
    }

    private String getPrefix(long guildId) {
        try (final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                // language=SQLite
                .prepareStatement("SELECT prefix FROM guild_settings WHERE guild_id = ?")) {

            preparedStatement.setString(1, String.valueOf(guildId));

            try (final ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getString("prefix");
                }
            }

            try (final PreparedStatement insertStatement = SQLiteDataSource
                    .getConnection()
                    // language=SQLite
                    .prepareStatement("INSERT INTO guild_settings(guild_id) VALUES(?)")) {

                insertStatement.setString(1, String.valueOf(guildId));

                insertStatement.execute();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Config.get("prefix");
    }
//
//    @Override
//    public void onGuildMessageUpdate(@NotNull GuildMessageUpdateEvent event)
//    {
//        final TextChannel channel = event.getChannel();
//        channel.sendMessageFormat("%s\nУже ничего не изменить!\nНикаких компромиссов. Даже перед лицом Армагеддона.",event.getAuthor()).queue();
//    }
//
//    @Override
//    public void onRoleCreate(@NotNull RoleCreateEvent event)
//    {
//        JDA jda = event.getJDA();
//        TextChannel textChannel = jda.getTextChannelById(Config.get("chat_id"));
//        textChannel.sendMessageFormat("Создана новая роль %s", event.getRole());
//    }
}
