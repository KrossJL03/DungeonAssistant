package bot.Encounter.Logger.Message.Help;

import bot.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpEncounterMessageBuilder extends HelpMessageBuilder
{
    private TextFormatter textFormatter;

    /**
     * HelpEncounterMessageBuilder constructor.
     */
    public @NotNull HelpEncounterMessageBuilder()
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
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold("Encounter Help Page"));
        message.add("Helpful links:");
        message.add("--- https://skaiaexplorers.wixsite.com/skyexplorers/bot-guide");
        message.add("--- https://skaiaexplorers.wixsite.com/skyexplorers/stats-and-effects");
        message.add("--- https://skaiaexplorers.wixsite.com/skyexplorers/registering-a-character");
        message.add("--- https://skaiaexplorers.wixsite.com/skyexplorers/how-to-battle");

        return message.getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildMemberCommandsMessage(@NotNull ArrayList<CommandInterface> commands)
    {
        return getCommandsMessage("player", commands).getAsString();
    }
}
