package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.Battle.HostileEncounter.EncounterPhase;
import bot.MessageInterface;
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
    public @NotNull MessageInterface createMessage(@NotNull BattlePhaseChangeResult result)
    {
        PhaseChangeMessage message = new PhaseChangeMessage();

        message.add(textFormatter.makeBold("DODGE TURN IS OVER!"));
        message.add("The next turn will begin shortly.");

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(
        @NotNull BattlePhase previousPhase,
        @NotNull BattlePhase nextPhase
    )
    {
        return ((EncounterPhase) previousPhase).isDodgePhase() && ((EncounterPhase) nextPhase).isRpPhase();
    }
}
