package bot;

import bot.Player.Player;
import bot.Player.PlayerManager;
import bot.Player.PlayerRepository;
import net.dv8tion.jda.core.entities.User;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class Command implements CommandInterface
{
    private String                      commandName;
    private String                      description;
    private ArrayList<CommandParameter> parameters;
    private ProcessManager              processManager;

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
        String lowerCommandName   = commandName.toLowerCase();
        String lowerCommandString = commandString.toLowerCase().trim();

        boolean startsWithName = lowerCommandString.startsWith(lowerCommandName + " ");
        boolean equalsName     = lowerCommandString.equals(lowerCommandName);

        return startsWithName | equalsName;
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
     * Get parameters from event
     *
     * @param event Event
     *
     * @return String[]
     */
    final protected @NotNull String[] getParametersFromEvent(@NotNull MessageReceivedEvent event)
    {
        String   parameterString = getParameterString(event);
        String[] splitMessage    = parameterString.length() > 0 ? parameterString.split("\\s+") : new String[0];

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
     * Get parameters from event
     *
     * @param event Event
     *
     * @return String
     */
    final protected @NotNull String getStringParameterFromEvent(@NotNull MessageReceivedEvent event)
    {
        String parameterString = getParameterString(event);

        if (parameterString.length() < 1 && getRequiredParameterCount() > 0) {
            throw CommandException.createMissingParameters(getFormattedCommand());
        }

        return parameterString;
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
     * Update player
     *
     * @param event Event
     *
     * @return Player
     */
    final protected @NotNull Player updatePlayer(@NotNull MessageReceivedEvent event)
    {
        String nickname = event.getMember().getNickname();
        String userId = event.getAuthor().getId();
        if (nickname == null) {
            nickname = event.getAuthor().getName();
        }

        PlayerManager.savePlayer(userId, nickname);

        return PlayerRepository.getPlayer(userId);
    }

    /**
     * Verify that the author is a mod
     *
     * @param author The author of the command
     *
     * @throws CommandException If not a mod
     */
    final protected void verifyMod(@NotNull User author) throws CommandException
    {
        if (!PlayerRepository.isModPlayer(author.getId())) {
            throw CommandException.createNotMod(getFormattedCommand());
        }
    }

    /**
     * Get formatted command
     *
     * @return String
     */
    private @NotNull String getFormattedCommand()
    {
        StringBuilder parameterBuilder = new StringBuilder();
        for (CommandParameter parameter : parameters) {
            parameterBuilder.append(parameter.getFormatted());
            parameterBuilder.append(" ");
        }
        return String.format("`%s%s %s`", MyProperties.COMMAND_PREFIX, commandName, parameterBuilder.toString().trim());
    }

    /**
     * Get string of parameters from event
     *
     * @param event Event to retrieve parameter string from
     *
     * @return String
     */
    private @NotNull String getParameterString(MessageReceivedEvent event)
    {
        return event.getMessage()
                    .getContentRaw()
                    .trim()
                    .substring(1)
                    .replaceAll(String.format("(?i)%s", commandName), "")
                    .trim();
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