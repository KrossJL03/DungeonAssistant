package bot;

import bot.Encounter.Logger.Message.MessageInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract public class HelpMessageBuilder implements HelpMessageBuilderInterface
{
    private MarkdownCodeFormatter codeFormatter;

    /**
     * HelpEncounterMessageBuilder constructor.
     */
    protected @NotNull HelpMessageBuilder(@NotNull MarkdownCodeFormatter codeFormatter)
    {
        this.codeFormatter = codeFormatter;
    }

    /**
     * Get commands message
     *
     * @param commandType Title for the group of commands
     * @param commands    Commands to add
     *
     * @return MessageInterface
     */
    final protected @NotNull MessageInterface getCommandsMessage(
        String commandType,
        ArrayList<CommandInterface> commands
    )
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format("%s COMMANDS", commandType).toUpperCase());
        message.addLine();

        for (CommandInterface command : commands) {
            message.add(formatCommand(command));
        }

        message.endCodeBlock();

        return message;
    }

    /**
     * Format commands
     *
     * @param command HelpCommand to format
     *
     * @return String
     */
    private String formatCommand(CommandInterface command)
    {
        StringBuilder parameterBuilder = new StringBuilder();
        for (CommandParameter parameter : command.getParameters()) {
            String parameterString = parameter.isRequired()
                                     ? codeFormatter.makeYellow(parameter.getName())
                                     : codeFormatter.makeBlue(parameter.getName());
            parameterBuilder.append(parameterString);
            parameterBuilder.append(" ");
        }

        Message message = new Message();
        message.add(String.format(
            "%s%s %s",
            CommandListener.COMMAND_KEY,
            command.getCommandName(),
            parameterBuilder.toString().trim()
        ));
        message.add(codeFormatter.makeGrey(command.getDescription()));

        return message.getAsString();
    }
}
