package bot.Encounter.Command;

import bot.Command;
import bot.Encounter.DungeonMasterChecker.DungeonMasterChecker;
import bot.Encounter.Encounter;
import bot.Encounter.EncounterHolder;
import bot.CommandParameter;
import bot.Encounter.EncounterInterface;
import bot.Encounter.Logger.EncounterLogger;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract class EncounterCommand extends Command
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      holder;
    private EncounterLogger      logger;
    private boolean              isDmExclusive;

    /**
     * EncounterCommand constructor
     *
     * @param processManager Processed manager
     * @param holder         Encounter holder
     * @param logger         Encounter Logger
     * @param dmChecker      Dungeon master checker
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     * @param isDmExclusive  Is command only usable by dungeon masters
     */
    protected EncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description,
        boolean isDmExclusive
    )
    {
        super(processManager, commandName, parameters, description);
        this.dmChecker = dmChecker;
        this.holder = holder;
        this.isDmExclusive = isDmExclusive;
        this.logger = logger;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        if (isDmExclusive) {
            ensureDungeonMaster(event);
        }
        execute(event);
    }

    /**
     * Is this command exclusive to dungeon masters
     */
    boolean isDmExclusive()
    {
        return isDmExclusive;
    }

    /**
     * Execute command
     *
     * @param event Event
     */
    abstract protected void execute(MessageReceivedEvent event);

    /**
     * Get dungeon master
     *
     * @param event Event
     *
     * @return Role
     *
     * @throws EncounterCommandException If dungeon master role is not found
     */
    final protected @NotNull Role getDungeonMaster(MessageReceivedEvent event)
    {
        return dmChecker.getDungeonMaster(event);
    }

    /**
     * Get encounter
     *
     * @return EncounterInterface
     */
    protected @NotNull EncounterInterface getEncounter()
    {
        return holder.getEncounter();
    }

    /**
     * Get hostile encounter
     *
     * @return Encounter
     *
     * @throws EncounterCommandException If encounter is not hostile encounter
     */
    final protected @NotNull Encounter getHostileEncounter()
    {
        EncounterInterface encounter = getEncounter();
        if (encounter instanceof Encounter) {
            return (Encounter) encounter;
        }
        throw EncounterCommandException.createWrongEncounterType(getCommandName(), encounter.getEncounterType());
    }

    /**
     * Get logger
     *
     * @return EncounterLogger
     */
    final protected @NotNull EncounterLogger getLogger()
    {
        return logger;
    }

    /**
     * Ensure that author is a Dungeon Master
     *
     * @param event Event
     *
     * @throws EncounterCommandException If not dungeon master
     */
    private void ensureDungeonMaster(MessageReceivedEvent event)
    {
        if (!isDungeonMaster(event)) {
            throw EncounterCommandException.createDmCommand(getCommandName());
        }
    }

    /**
     * Is the author of the message a dungeon master
     *
     * @param event Event
     *
     * @return boolean
     */
    private boolean isDungeonMaster(@NotNull MessageReceivedEvent event)
    {
        return dmChecker.isDungeonMaster(event);
    }
}
