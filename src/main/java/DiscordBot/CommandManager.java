package DiscordBot;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import DiscordBot.command.commands.Help;
import DiscordBot.command.commands.Quotes;
import DiscordBot.command.commands.PingCommand;
import DiscordBot.command.commands.RandomNumber;
import DiscordBot.command.commands.music.JoinCommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandManager
{
    private final List<ICommand> commands = new ArrayList<>();

    public CommandManager()
    {
        addCommand(new PingCommand());
        addCommand(new Quotes());
        addCommand(new RandomNumber());
        addCommand(new JoinCommand());
        addCommand(new Help(commands));
    }


    private void addCommand(ICommand cmd)
    {
        boolean nameFound = this.commands.stream().anyMatch((it) -> it.getName().equalsIgnoreCase(cmd.getName()));

        if(nameFound)
            throw new IllegalArgumentException("Command with this name already exist");

        commands.add(cmd);
    }

    @Nullable
    private ICommand getCommand(String search)
    {
        String searchLower = search.toLowerCase();
        for (ICommand cmd : commands)
        {
            if(cmd.getName().equals(searchLower) || cmd.getAliases().equals(searchLower))
                return cmd;
        }
        return null;
    }

    void handle(GuildMessageReceivedEvent event)
    {
        String[] split = event.getMessage().getContentRaw()
                .replaceFirst(Bot.prefix, "")
                .split("\\s+");

        String invoke = split[0].toLowerCase();
        ICommand cmd = getCommand(invoke);
        if (cmd!= null)
        {
            event.getChannel().sendTyping().queue();
            List<String> args = Arrays.asList(split).subList(1, split.length);

            CommandContext ctx = new CommandContext(event, args);

            cmd.handle(ctx);
        }
    }

}
