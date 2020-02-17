package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class PhaseChangeMessageBuilder
{
    private ArrayList<PhaseChangeMessageFactoryInterface> factories;

    /**
     * Constructor.
     *
     * @param factories Factories
     */
    public PhaseChangeMessageBuilder(ArrayList<PhaseChangeMessageFactoryInterface> factories)
    {
        this.factories = new ArrayList<>(factories);
    }

    /**
     * Log phase change
     *
     * @param result Phase change result
     *
     * @return String
     *
     * @throws CustomException If no factory exists to handle the phase change
     */
    @NotNull String buildPhaseChangeMessage(@NotNull BattlePhaseChangeResult result) throws CustomException
    {
        for (PhaseChangeMessageFactoryInterface messageBuilder : factories) {
            if (messageBuilder.handles(result.getPreviousPhase(), result.getNextPhase())) {
                return messageBuilder.createMessage(result).getAsString();
            }
        }

        throw new CustomException(String.format(
            "So... we're going from %s to %s but I forgot how to make it look pretty.",
            result.getPreviousPhase().getPhaseName(),
            result.getNextPhase().getPhaseName()
        ));
    }
}
