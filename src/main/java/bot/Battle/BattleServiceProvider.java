package bot.Battle;

import bot.Battle.Encounter.EncounterServiceProvider;
import bot.Battle.Pvp.PvpServiceProvider;
import bot.Command;
import bot.CommandProviderInterface;
import bot.PrivateLogger;
import bot.ProcessManager;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BattleServiceProvider implements CommandProviderInterface
{
    private EncounterServiceProvider encounterServiceProvider;
    private PvpServiceProvider       pvpServiceProvider;

    /**
     * Constructor.
     *
     * @param processManager Process manager
     */
    public BattleServiceProvider(@NotNull ProcessManager processManager)
    {
        DungeonMasterChecker dmChecker     = new DungeonMasterChecker();
        PrivateLogger        privateLogger = new PrivateLogger(new HelpMessageBuilder());

        this.encounterServiceProvider = new EncounterServiceProvider(
            processManager,
            privateLogger,
            dmChecker
        );

        this.pvpServiceProvider = new PvpServiceProvider(
            processManager,
            privateLogger,
            dmChecker
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<Command> getCommands()
    {
        return new ArrayList<Command>()
        {
            {
                addAll(encounterServiceProvider.getCommands());
                addAll(pvpServiceProvider.getCommands());
            }
        };
    }
}
