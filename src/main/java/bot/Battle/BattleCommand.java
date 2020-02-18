package bot.Battle;

import bot.Command;
import bot.CommandParameter;
import bot.CustomException;
import bot.ProcessManager;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public abstract class BattleCommand extends Command
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
    protected BattleCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description,
        boolean isDmExclusive
    )
    {
        super(processManager, commandName, parameters, description, isDmExclusive);

        this.dmChecker = dmChecker;
        this.holder = holder;
        this.isDmExclusive = isDmExclusive;
    }

    /**
     * Throw an exception for trying to use a command on the wrong battle style
     *
     * @param formattedCommand Formatted command
     * @param battleStyle      Battle style
     *
     * @return CustomException
     */
    protected static @NotNull CustomException createWrongBattleStyleException(
        @NotNull String formattedCommand,
        @NotNull String battleStyle
    )
    {
        return new CustomException(String.format(
            "The `%s` command cannot be used for a %s battle",
            formattedCommand,
            battleStyle
        ));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void handle(@NotNull MessageReceivedEvent event)
    {
        if (isDmExclusive) {
            ensureDungeonMaster(event);
        }

        execute(event);
    }

    /**
     * Is this command exclusive to dungeon masters
     */
    public boolean isDmExclusive()
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
     */
    final protected @NotNull Role getDungeonMaster(MessageReceivedEvent event)
    {
        return dmChecker.getDungeonMaster(event);
    }

    /**
     * Ensure that author is a Dungeon Master
     *
     * @param event Event
     *
     * @throws CustomException If not dungeon master
     */
    private void ensureDungeonMaster(MessageReceivedEvent event)
    {
        if (!isDungeonMaster(event)) {
            throw new CustomException(String.format(
                "Only the Dungeon Master can use the `%s` command",
                getCommandName()
            ));
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
