package bot;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Command implements CommandInterface
{
    private ArrayList<CommandParameter> parameters;
    private String                      commandName;
    private String                      description;

    /**
     * Command constructor
     *
     * @param commandName   Command name
     * @param parameters    Parameters
     * @param description   Command description
     */
    protected Command(
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description
    )
    {
        this.commandName = commandName;
        this.description = description;
        this.parameters = parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getCommandName()
    {
        return commandName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getDescription()
    {
        return description;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CommandParameter> getParameters()
    {
        return parameters;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isCommandName(@NotNull String name)
    {
        return commandName.toLowerCase().equals(name.toLowerCase());
    }

    /**
     * Get formatted command
     *
     * @return String
     */
    final protected @NotNull String getFormattedCommand()
    {
        StringBuilder parameterBuilder = new StringBuilder();
        for (CommandParameter parameter : parameters) {
            parameterBuilder.append(parameter.getFormatted());
            parameterBuilder.append(" ");
        }
        return String.format("`%s%s %s`", CommandListener.COMMAND_KEY, commandName, parameterBuilder.toString().trim());
    }

    /**
     * Get parameters from event
     *
     * @param event Event
     *
     * @return String[]
     */
    final protected @NotNull String[] getParametersFromEvent(@NotNull MessageReceivedEvent event)
    {
        String[] splitMessage = event.getMessage().getContentRaw().replace(commandName, "").split("\\s+");

        if (splitMessage.length < getRequiredParameterCount()) {
            throw CommandException.createMissingParameters(getFormattedCommand());
        }

        return splitMessage;
    }

    /**
     * Get player from event
     *
     * @param event Event
     *
     * @return Player
     */
    final protected @NotNull Player getPlayerFromEvent(@NotNull MessageReceivedEvent event)
    {
        return PlayerRepository.getPlayer(event.getAuthor().getId());
    }

    /**
     * Get required parameter count
     *
     * @return int
     */
    private int getRequiredParameterCount()
    {
        int requiredParameterCount = 0;
        for(CommandParameter parameter : parameters) {
            if (parameter.isRequired()) {
                requiredParameterCount++;
            }
        }

        return requiredParameterCount;
    }
}