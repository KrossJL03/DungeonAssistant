package bot.Encounter.Logger.Message.Help;

import bot.CommandInterface;
import bot.CommandListener;
import bot.CommandParameter;
import bot.IniCodeFormatter;
import bot.TextFormatter;
import bot.HelpMessageBuilderInterface;
import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class HelpMessageBuilder implements HelpMessageBuilderInterface
{
    private IniCodeFormatter codeFormatter;
    private TextFormatter    textFormatter;

    /**
     * HelpMessageBuilder constructor.
     */
    public @NotNull HelpMessageBuilder()
    {
        this.codeFormatter = new IniCodeFormatter();
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildAdminHelpMessage(
        @NotNull ArrayList<CommandInterface> adminCommands,
        @NotNull ArrayList<CommandInterface> memberCommands
    )
    {
        HelpMessage message = new HelpMessage();

        message = addDescription(message);
        message = addCommands(message, "admin", adminCommands);
        message = addCommands(message, "member", memberCommands);

        return message.getAsString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String buildMemberHelpMessage(@NotNull ArrayList<CommandInterface> commands)
    {
        HelpMessage message = new HelpMessage();

        message = addDescription(message);
        message = addCommands(message, "member", commands);

        return message.getAsString();
    }

    /**
     * Add commands message to help message
     *
     * @param message     Help message to append
     * @param commandType Title for the group of commands
     * @param commands    Commands to add
     *
     * @return HelpMessage
     */
    private HelpMessage addCommands(HelpMessage message, String commandType, ArrayList<CommandInterface> commands)
    {
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(WordUtils.capitalize(String.format("%s COMMANDS", commandType)));
        for (CommandInterface command : commands) {
            message.add(formatCommand(command));
        }
        message.endCodeBlock();

        return message;
    }

    /**
     * Add description to help message
     *
     * @param message Help message to append
     *
     * @return HelpMessage
     */
    private HelpMessage addDescription(HelpMessage message)
    {
        message.add(textFormatter.makeBold(WordUtils.capitalize("ENCOUNTER HELP PAGE")));
        message.add("Useful links:");
        message.add("https://skaiaexplorers.wixsite.com/skyexplorers/bot-guide");
        message.add("https://skaiaexplorers.wixsite.com/skyexplorers/stats-and-effects");
        message.add("https://skaiaexplorers.wixsite.com/skyexplorers/registering-a-character");
        message.add("https://skaiaexplorers.wixsite.com/skyexplorers/how-to-battle");

        return message;
    }

    /**
     * Format commands
     *
     * @param command Command to format
     *
     * @return String
     */
    private String formatCommand(CommandInterface command)
    {
        StringBuilder parameterBuilder = new StringBuilder();
        for (CommandParameter parameter : command.getParameters()) {
            parameterBuilder.append(codeFormatter.makeBlue(parameter.getName()));
            parameterBuilder.append(" ");
        }

        return String.format(
            "   %s%-16s %-16s",
            CommandListener.COMMAND_KEY,
            command.getCommandName(),
            parameterBuilder.toString().trim()
        );
    }
}
