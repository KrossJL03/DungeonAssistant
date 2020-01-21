package bot.Battle.Command;

import bot.CommandParameter;
import bot.Battle.DungeonMasterChecker.DungeonMasterChecker;
import bot.Battle.EncounterHolder;
import bot.Battle.Logger.EncounterLogger;
import bot.Battle.Tier.TierRegistry;
import bot.Battle.TierInterface;
import bot.ProcessManager;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SetTierCommand extends EncounterCommand
{
    /**
     * SetTierCommand constructor
     *
     * @param processManager Process manager
     * @param holder         Encounter holder
     * @param logger         Encounter logger
     * @param dmChecker      Dungeon master checker
     */
    SetTierCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger,
        @NotNull DungeonMasterChecker dmChecker
    )
    {
        super(
            processManager,
            holder,
            logger,
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
    public void execute(@NotNull MessageReceivedEvent event) throws EncounterCommandException
    {
        String[]      parameters = getParametersFromEvent(event);
        String        tierName   = parameters[0];
        TierInterface tier       = TierRegistry.getTier(tierName);

        getEncounter().setTier(tier);
    }
}
