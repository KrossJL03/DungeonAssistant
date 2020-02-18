package bot.Registry.Review;

import bot.Command;
import bot.MarkdownCodeFormatter;
import bot.Message;
import bot.TextFormatter;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpMessageBuilder extends bot.HelpMessageBuilder
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    @NotNull HelpMessageBuilder()
    {
        super(new MarkdownCodeFormatter());
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildAdminCommandsMessage(@NotNull ArrayList<Command> commands)
    {
        return getCommandsMessage("mod", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold(WordUtils.capitalize("Viewing Help Page")));
        message.add(
            "Hi there! Was there some information you wanted to look up? Well you've come to the right place! " +
            "This help page is here to show you all the commands that allow you to look up information saved in " +
            "this bot."
        );

        return message.getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildMemberCommandsMessage(@NotNull ArrayList<Command> commands)
    {
        return getCommandsMessage("member", commands).getAsString();
    }
}
