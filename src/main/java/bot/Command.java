package bot;

import bot.Player.Player;
import bot.Player.PlayerRepository;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Command implements CommandInterface
{
    private ProcessManager              processManager;
    private ArrayList<CommandParameter> parameters;
    private String                      commandName;
    private String                      description;

    /**
     * HelpCommand constructor
     *
     * @param processManager Process manager
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     */
    protected Command(
        @NotNull ProcessManager processManager,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description
    )
    {
        this.commandName = commandName;
        this.description = description;
        this.parameters = parameters;
        this.processManager = processManager;
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
    public boolean isCommand(@NotNull String commandString)
    {
        return commandString.toLowerCase().startsWith(commandName.toLowerCase());
    }

    /**
     * Add process to manager
     *
     * @param process Process
     */
    final protected void addProcessToManager(@NotNull ProcessInterface process)
    {
        processManager.addProcess(process);
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
     * Is database locked
     *
     * @return boolean
     */
    final protected boolean isDatabaseLocked()
    {
        return processManager.isDatabaseLocked();
    }

    /**
     * Remove process to manager
     *
     * @param process Process
     */
    final protected void removeProcessToManager(@NotNull ProcessInterface process)
    {
        processManager.removeProcess(process);
    }

    /**
     * Get required parameter count
     *
     * @return int
     */
    private int getRequiredParameterCount()
    {
        int requiredParameterCount = 0;
        for (CommandParameter parameter : parameters) {
            if (parameter.isRequired()) {
                requiredParameterCount++;
            }
        }

        return requiredParameterCount;
    }
}