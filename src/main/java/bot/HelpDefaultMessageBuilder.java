package bot;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpDefaultMessageBuilder extends bot.HelpMessageBuilder
{
    private TextFormatter textFormatter;

    /**
     * HelpDefaultMessageBuilder constructor.
     */
    @NotNull HelpDefaultMessageBuilder()
    {
        super(new MarkdownCodeFormatter());
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildAdminCommandsMessage(@NotNull ArrayList<CommandInterface> commands)
    {
        return getCommandsMessage("admin", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildMemberCommandsMessage(@NotNull ArrayList<CommandInterface> commands)
    {
        return getCommandsMessage("help", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold(WordUtils.capitalize("Welcome to Dungeon Assistant!")));
        message.add("Dungeon Assistant is a helper bot for the SkyExplorers discord server. " +
                    "Currently they help out with encounters but there will be more to come in the future.");
        message.addBreak();
        message.add("Use the help commands below to find out more about one of Dungeon Assistant's features.");

        return message.getAsString();
    }
}
