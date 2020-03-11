package bot.Battle.Encounter;

import bot.Battle.BattleCommand;
import bot.Battle.BattleInterface;
import bot.Battle.DungeonMasterChecker;
import bot.CommandParameter;
import bot.CustomException;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

abstract class EncounterCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Processed manager
     * @param dmChecker      Dungeon master checker
     * @param commandName    HelpCommand name
     * @param parameters     Parameters
     * @param description    HelpCommand description
     * @param isDmExclusive  Is command only usable by dungeon masters
     */
    protected EncounterCommand(
        @NotNull ProcessManager processManager,
        @NotNull DungeonMasterChecker dmChecker,
        @NotNull String commandName,
        @NotNull ArrayList<CommandParameter> parameters,
        @NotNull String description,
        boolean isDmExclusive
    )
    {
        super(processManager, dmChecker, commandName, parameters, description, isDmExclusive);
    }

    /**
     * Get encounter
     *
     * @return Battle
     *
     * @throws CustomException If encounter is not hostile encounter
     */
    final protected @NotNull Encounter getEncounter()
    {
        BattleInterface encounter = getBattle();
        if (encounter instanceof Encounter) {
            return (Encounter) encounter;
        }

        throw createWrongBattleStyleException(getCommandName(), encounter.getBattleStyle());
    }
}
