package DiscordBot.command.commands;

import DiscordBot.command.CommandContext;
import DiscordBot.command.ICommand;
import net.dv8tion.jda.api.JDA;

public class PingCommand implements ICommand
{

    @Override
    public void handle(CommandContext ctx)
    {
        JDA jda = ctx.getJDA();
        jda.getRestPing().queue((ping) -> ctx.getChannel()
        .sendMessageFormat("%s\nRest ping: %sms\nWS ping: %sms",ctx.getEvent().getAuthor(), ping, jda.getGatewayPing()).queue());
    }

    @Override
    public String getName() {
        return "ping";
    }

    @Override
    public String getHelp() {
        return "Ping from bot to server";
    }
}
