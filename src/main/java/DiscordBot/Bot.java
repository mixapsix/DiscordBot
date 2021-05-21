package DiscordBot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;

public class Bot
{
    public static JDA jda;
    public static String prefix = "~";

    private Bot(String token) throws LoginException
    {
        jda = JDABuilder.createDefault(token).build();
        jda.getPresence().setStatus(OnlineStatus.IDLE);
        jda.getPresence().setActivity(Activity.playing("вы заперты со мной"));
        jda.addEventListener(new Listener());
    }

    public static void main(String[] args) throws LoginException
    {
        new Bot(args[0]);
    }
}
