package DiscordBot.command.commands;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;

import java.util.List;

public class Help implements ICommand
{
    private final List<ICommand> commands;

    public Help(List<ICommand> commands) {
        this.commands = commands;
    }

    @Override
    public void handle(CommandContext ctx)
    {
        String result = "";
        for (ICommand command : commands)
        {
            if(command.getName().equals(this.getName()))
                continue;
            result +=  "~" + command.getName() + "\n";
        }
        ctx.getChannel().sendMessageFormat("%s\n%s", ctx.getAuthor(), result).queue();
    }

    @Override
    public String getName() {
        return "help";
    }
}
