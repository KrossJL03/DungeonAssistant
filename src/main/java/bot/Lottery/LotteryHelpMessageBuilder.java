package bot.Lottery;

import bot.CommandInterface;
import bot.HelpMessageBuilder;
import bot.MarkdownCodeFormatter;
import bot.Message;
import bot.TextFormatter;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LotteryHelpMessageBuilder extends HelpMessageBuilder
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    @NotNull LotteryHelpMessageBuilder()
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
        return getCommandsMessage("member", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold(WordUtils.capitalize("Lottery Help Page")));
        message.add(
            "Hello, do you need help rolling a lottery? Then you've come to the right place. " +
            "Here you can list of all commands for rolling random prizes."
        );
        message.addBreak();

        return message.getAsString();
    }
}
