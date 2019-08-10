package bot;

import bot.Encounter.Logger.Message.DiffCodeFormatter;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpDefaultMessageBuilder extends bot.HelpMessageBuilder
{
    private DiffCodeFormatter codeFormatter;
    private TextFormatter     textFormatter;

    /**
     * HelpDefaultMessageBuilder constructor.
     */
    @NotNull HelpDefaultMessageBuilder()
    {
        super(new MarkdownCodeFormatter());
        this.codeFormatter = new DiffCodeFormatter();
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

        message.add(textFormatter.makeBold("Recent Changes"));
        message.startCodeBlock(codeFormatter.getStyle());

        message.add(codeFormatter.makeGreen(
            "?create character command has been renamed to ?create explorer"
        ));
        message.add(codeFormatter.makeGreen(
            "Stats for the ?create explorer command have been reorganized to match the stat sheets"
        ));
        message.add(codeFormatter.makeGreen(
            "?view characters has been renamed to ?view explorers"
        ));
        message.add(codeFormatter.makeGreen(
            "?view explorers can be used to view all, yours, or another players explorers"
        ));
        message.add(codeFormatter.makeGreen(
            "The ?guard command has been added for dodge turns. Find out more in ?help encounter"
        ));
        message.addBreak();
        message.add(codeFormatter.makeGrey("Last Update 2019-08-04"));
        message.endCodeBlock();

        message.addBreak();
        message.add("Use the help commands below to find out more about one of Dungeon Assistant's features.");

        return message.getAsString();
    }
}
