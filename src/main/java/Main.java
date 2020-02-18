import bot.CommandListener;
import bot.CommandProvider;
import bot.MyProperties;
import net.dv8tion.jda.core.JDA;
import net.dv8tion.jda.core.JDABuilder;

public class Main
{
    public static void main(String[] arguments) throws Exception
    {
        CommandProvider commandProvider = new CommandProvider();
        CommandListener commandListener = new CommandListener(commandProvider.getCommands());

        JDA api = new JDABuilder(MyProperties.TOKEN).build();
        api.addEventListener(commandListener);
    }
}