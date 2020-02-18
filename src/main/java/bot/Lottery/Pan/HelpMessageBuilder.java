package bot.Lottery.Pan;

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

        message.add(textFormatter.makeBold(WordUtils.capitalize("Pan's Scavenger Hunt")));
        message.add(
            "TBD"
        );
        message.addBreak();

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
