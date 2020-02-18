package bot.Battle;

import bot.Command;
import bot.MarkdownCodeFormatter;
import bot.Message;
import bot.TextFormatter;
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
        return getCommandsMessage("Dungeon Master", commands).getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildDescriptionMessage()
    {
        Message message = new Message();

        message.add(textFormatter.makeBold("Battle Help Page"));
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
        message.add(String.format(
            "- Regular Battle Tracker: %s",
            textFormatter.makeLinkPreviewless(
                "https://docs.google.com/spreadsheets/d/1kYh71g4pb1TNP9egOilpvk4eF_1h2O20-nBEbikX4gw/edit?usp=sharing"
            )
        ));
        message.addBreak();

        return message.getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildMemberCommandsMessage(@NotNull ArrayList<Command> commands)
    {
        return getCommandsMessage("player", commands).getAsString();
    }
}
