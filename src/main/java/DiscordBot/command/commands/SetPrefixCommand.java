package DiscordBot.command.commands;

import DiscordBot.Config;
import DiscordBot.Prefixes;
import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.database.SQLiteDataSource;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.TextChannel;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class SetPrefixCommand implements ICommand
{
    @Override
    public void handle(CommandContext ctx)
    {
        final TextChannel channel = ctx.getChannel();
        List<String> args = ctx.getArgs();
        Member member = ctx.getMember();

        if(!ctx.getAuthor().getId().equals(Config.get("OWNER_ID")))
        {
            channel.sendMessageFormat("У тебя нет власти надо мной %s", member.getUser()).queue();
            return;
        }

        if(args.isEmpty())
        {
            channel.sendMessage("Missing args").queue();
            return;
        }

        final String newPrefix = String.join("", args);
        updatePrefix(ctx.getGuild().getIdLong(), newPrefix);

        channel.sendMessageFormat("New prefix has been set to %s", newPrefix).queue();

    }

    @Override
    public String getName() {
        return "setprefix";
    }

    @Override
    public String getHelp() {
        return "Sets the prefix for this server\n";
    }

    private void updatePrefix(long guildId, String newPrefix)
    {
        Prefixes.PREFIXES.put(guildId, newPrefix);

        try(final PreparedStatement preparedStatement = SQLiteDataSource
                .getConnection()
                .prepareStatement("UPDATE guild_settings SET prefix = ? WHERE guild_id = ?")) {

            preparedStatement.setString(1, newPrefix);
            preparedStatement.setString(2, String.valueOf(guildId));

            preparedStatement.executeUpdate();
        }catch (SQLException e)
        {
            e.printStackTrace();
        }
    }
}
