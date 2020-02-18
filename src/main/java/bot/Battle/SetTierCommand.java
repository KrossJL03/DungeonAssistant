package bot.Battle;

import bot.CommandParameter;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetTierCommand extends BattleCommand
{
    /**
     * Constructor.
     *
     * @param processManager Process manager
     * @param holder         Battle holder
     * @param dmChecker      Dungeon master checker
     */
    public SetTierCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
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
    public void execute(@NotNull MessageReceivedEvent event)
    {
        String[] parameters = getParametersFromEvent(event);
        String   tierName   = parameters[0];
        Tier     tier       = TierRegistry.getTier(tierName);

        getBattle().setTier(tier);
    }
}
