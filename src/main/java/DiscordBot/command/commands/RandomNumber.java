package DiscordBot.command.commands;

import DiscordBot.Config;
import DiscordBot.TextParse;
import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;

import java.util.List;

public class RandomNumber implements ICommand
{

    @Override
    public void handle(CommandContext ctx)
    {
        List<String> args = ctx.getArgs();
        if(ctx.getArgs().size() == 2)
        {
            if (TextParse.tryParseInt(args.get(0)) == -1 || TextParse.tryParseInt(args.get(1)) == -1)
            {
                ctx.getChannel().sendMessage(String.valueOf(getRandomNumber(0, 100))).queue();
                return;
            }
            int first = Integer.parseInt(args.get(0));
            int second = Integer.parseInt(args.get(1));
            if (first > second)
                ctx.getChannel().sendMessage(String.valueOf(getRandomNumber(second, first))).queue();
            else
                ctx.getChannel().sendMessage(String.valueOf(getRandomNumber(first, second))).queue();
        }
        else
            ctx.getChannel().sendMessage(String.valueOf(getRandomNumber(0, 100))).queue();
    }

    @Override
    public String getName() {
        return "random";
    }

    @Override
    public String getHelp() {
        return "Get random value from (1-100)\n" +
                "Or type "+ Config.get("prefix") + "random <> <> to get value in range";
    }

    public int getRandomNumber(int min, int max)
    {
        return (int) Math.round(Math.random() * (max - min) + min);
    }
}
