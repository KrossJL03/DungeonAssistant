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
        return getCommandsMessage("Dungeon Master", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold("Encounter Help Page"));
        message.add("Welcome! You must be here to learn more about participating in encounters!");
        message.addBreak();

        message.add(textFormatter.makeBold("New to Battling?"));
        message.add("If you haven't participated in a battle before and don't know where to start, fear not! " +
                    "The process is really quite simple, " +
                    "first thing you'll need to do is create a stat sheet for your explorer. " +
                    "Simply fill out the 'Equipment and Stat Sheet' for your explorer. " +
                    "But make sure you've read up about Stats and Effects " +
                    "so that know where you want your first 5 starting stats to go.");
        message.addBreak();

        message.add(textFormatter.makeBold("Helpful Links:"));
        message.add(String.format(
            "- Equipment and Stat Sheet: %s",
            textFormatter.makeLinkPreviewless(
                "https://www.deviantart.com/dancinginblue/art/SE-Equipment-and-Stat-Sheet-565622019"
            )
        ));
        message.add(String.format(
            "- Registering a Character Guide: %s",
            textFormatter.makeLinkPreviewless(
                "https://skaiaexplorers.wixsite.com/skyexplorers/registering-a-character"
            )
        ));
        message.add(String.format(
            "- Stats and Effects: %s",
            textFormatter.makeLinkPreviewless(
                "https://skaiaexplorers.wixsite.com/skyexplorers/stats-and-effects"
            )
        ));
        message.add(String.format(
            "- How to Battle: %s",
            textFormatter.makeLinkPreviewless(
                "https://skaiaexplorers.wixsite.com/skyexplorers/how-to-battle"
            )
        ));
        message.addBreak();

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
