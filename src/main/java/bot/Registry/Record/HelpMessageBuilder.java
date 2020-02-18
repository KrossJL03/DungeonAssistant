package bot.Registry.Record;

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

        message.add(textFormatter.makeBold(WordUtils.capitalize("Recording Help Page")));
        message.add(
            "Hi! Do you have some changes you'd like to make to my records? " +
            "This is the place to come if you need help with that!"
        );
        message.addBreak();

        message.add(textFormatter.makeBold("Questions About Creating an Explorer?"));
        message.add(
            String.format(
                "When you use the %s command you will need to supply some information related to battling. ",
                textFormatter.makeCode("?create explorer")
            ) +
            String.format(
                "You can find out more about battling by using the %s command. ",
                textFormatter.makeCode("?help encounter")
            ) +
            "But you can find the links you'll need below as well."
        );
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
