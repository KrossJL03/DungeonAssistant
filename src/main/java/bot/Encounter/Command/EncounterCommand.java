package bot.Encounter.Command;

import bot.Command;
import bot.Encounter.Encounter;
import bot.Encounter.EncounterHolder;
import bot.CommandParameter;
import bot.Encounter.EncounterInterface;
import bot.Encounter.Logger.EncounterLogger;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

abstract class EncounterCommand extends Command
{
    private EncounterHolder             holder;
    private EncounterLogger             logger;
    private boolean                     isDmExclusive;

    /**
     * EncounterCommand constructor
     *
     * @param holder        Encounter holder
     * @param logger        Encounter Logger
     * @param commandName   Command name
     * @param parameters    Parameters
     * @param description   Command description
     * @param isDmExclusive Is command only usable by dungeon masters
     */
    protected EncounterCommand(
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description,
        boolean isDmExclusive
    )
    {
        super(commandName, parameters, description);
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
        List<Role> roles = event.getGuild().getRolesByName("Dungeon Master", false);
        if (!roles.isEmpty()) {
            return roles.get(0);
        }
        throw EncounterCommandException.createDmNotFound();
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
        throw EncounterCommandException.createWrongEncounterType(getFormattedCommand(), encounter.getEncounterType());
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
            throw EncounterCommandException.createDmCommand(getFormattedCommand());
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
        return event.getMember().getRoles().indexOf(getDungeonMaster(event)) > -1;
    }
}
