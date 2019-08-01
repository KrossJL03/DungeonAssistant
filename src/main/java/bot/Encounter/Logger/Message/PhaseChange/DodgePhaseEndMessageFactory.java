package bot.Encounter.Logger.Message.PhaseChange;

import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.Logger.Message.*;
import bot.Encounter.PhaseChangeResult;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class DodgePhaseEndMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * DodgePhaseEndMessageFactory constructor.
     */
    @NotNull DodgePhaseEndMessageFactory()
    {
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(
        @NotNull EncounterPhaseInterface previousPhase,
        @NotNull EncounterPhaseInterface nextPhase
    )
    {
        return previousPhase.isDodgePhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result)
    {
        PhaseChangeMessage message = new PhaseChangeMessage();

        message.add(textFormatter.makeBold("DODGE TURN IS OVER!"));
        message.add("You may take this time to RP amongst yourselves. The ATTACK turn will begin shortly.");

        return message;
    }
}
