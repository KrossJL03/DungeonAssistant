package bot.Battle.Encounter;

import bot.Battle.BattleCommand;
import bot.Battle.DungeonMasterChecker;
import bot.Battle.Tier;
import bot.Battle.TierRegistry;
import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class SetTierCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param dmChecker      Dungeon master checker
     */
    SetTierCommand(@NotNull ProcessManager processManager, @NotNull DungeonMasterChecker dmChecker)
    {
        super(
            processManager,
            dmChecker,
            "tier",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TierName", true));
                }
            },
            "Set tier. Current Tiers: Rookie, Novice",
            true
        );
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void execute(@NotNull MessageReceivedEvent event)
    {
        String[] parameters = getParametersFromEvent(event);
        String   tierName   = parameters[0];
        Tier     tier       = TierRegistry.getTier(tierName);

        getBattle().setTier(tier);
    }
}
