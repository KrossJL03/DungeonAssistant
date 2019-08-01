package bot.Registry.Logger.Message.Help;

import bot.*;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpRegistryMessageBuilder extends HelpMessageBuilder
{
    private TextFormatter textFormatter;

    /**
     * HelpEncounterMessageBuilder constructor.
     */
    public @NotNull HelpRegistryMessageBuilder()
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

        message.add(textFormatter.makeBold(WordUtils.capitalize("REGISTRY HELP PAGE")));
        message.add("Description");

        return message.getAsString();
    }
}
