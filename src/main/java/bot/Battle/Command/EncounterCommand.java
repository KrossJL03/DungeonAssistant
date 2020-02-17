package bot.Battle.Command;

import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.HostileEncounter.HostileEncounter;
import bot.Command;
import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class EncounterCommand extends Command
{
    private DungeonMasterChecker dmChecker;
    private EncounterHolder      holder;
    private boolean              isDmExclusive;

    /**
     * Constructor.
     *
     * @param processManager Processed manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     * @param isDmExclusive  Is command only usable by dungeon masters
     */
    protected EncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
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
     * Get battle
     *
     * @return BattleInterface
     */
    protected @NotNull BattleInterface getBattle()
    {
        return holder.getEncounter();
    }

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
     * Get hostile encounter
     *
     * @return Battle
     *
     * @throws EncounterCommandException If encounter is not hostile encounter
     */
    final protected @NotNull HostileEncounter getHostileEncounter()
    {
        BattleInterface encounter = getBattle();
        if (encounter instanceof HostileEncounter) {
            return (HostileEncounter) encounter;
        }

        throw EncounterCommandException.createWrongEncounterType(getCommandName(), encounter.getBattleStyle());
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
