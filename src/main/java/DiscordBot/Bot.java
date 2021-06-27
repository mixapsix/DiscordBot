package DiscordBot;

import DiscordBot.database.SQLiteDataSource;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;

import javax.security.auth.login.LoginException;
import java.sql.SQLException;

public class Bot
{
    public static JDA jda;

    private Bot() throws LoginException, SQLException {

        SQLiteDataSource.getConnection();

        jda = JDABuilder.createDefault(System.getenv("TOKEN")).build();
        jda.getPresence().setStatus(OnlineStatus.IDLE);
        jda.getPresence().setActivity(Activity.playing("вы заперты со мной"));
        jda.addEventListener(new Listener());
    }

    public static void main(String[] args) throws LoginException, SQLException
    {
        new Bot();
    }
}
