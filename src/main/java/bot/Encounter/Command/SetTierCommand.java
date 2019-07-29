package bot.Encounter.Command;

import bot.CommandParameter;
import bot.Encounter.EncounterHolder;
import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Tier.TierRegistry;
import bot.Encounter.TierInterface;
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
     */
    SetTierCommand(
        @NotNull ProcessManager processManager,
        @NotNull EncounterHolder holder,
        @NotNull EncounterLogger logger
    )
    {
        super(
            processManager,
            holder,
            logger,
            "tier",
            new ArrayList<CommandParameter>()
            {
                {
                    add(new CommandParameter("TierName", true));
                }
            },
            "Set tier. Current Tiers: Newbie",
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
