package bot.Encounter.Logger.Message.PhaseChange;

import bot.Encounter.PhaseChangeResult;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhaseChangeMessageBuilder
{
    private ArrayList<PhaseChangeMessageFactoryInterface> messageBuilders;

    /**
     * PhaseChangeMessageBuilder constructor
     */
    public @NotNull PhaseChangeMessageBuilder()
    {
        this.messageBuilders = new ArrayList<>();
        messageBuilders.add(new AttackPhaseStartMessageFactory());
        messageBuilders.add(new AttackPhaseEndMessageFactory());
        messageBuilders.add(new DodgePhaseStartMessageFactory());
        messageBuilders.add(new DodgePhaseEndMessageFactory());
        messageBuilders.add(new EndPhaseStartMessageFactory());
        messageBuilders.add(new JoinPhaseStartMessageFactory());
        messageBuilders.add(new LootPhaseStartMessageFactory());
    }

    /**
     * Log phase change
     *
     * @param result Phase change result
     *
     * @return String
     */
    public String buildPhaseChangeMessage(PhaseChangeResult result)
    {
        for (PhaseChangeMessageFactoryInterface messageBuilder : messageBuilders) {
            if (messageBuilder.handles(result.getPreviousPhase(), result.getNextPhase())) {
                return messageBuilder.createMessage(result).getAsString();
            }
        }

        throw PhaseChangeMessageBuilderException.createPhaseNotConfigured(
            result.getPreviousPhase().getPhaseName(),
            result.getNextPhase().getPhaseName()
        );
    }
}
