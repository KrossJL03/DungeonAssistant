package bot.Encounter.Logger.Message.PhaseChange;

import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.Logger.Message.*;
import bot.Encounter.PhaseChangeResult;
import org.jetbrains.annotations.NotNull;

public class EndPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextBlockFormatter textFormatter;

    /**
     * PhaseChangeMessageBuilder constructor
     */
    @NotNull EndPhaseStartMessageFactory()
    {
        this.textFormatter = new TextBlockFormatter();
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
        return nextPhase.isEndPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result)
    {
        PhaseChangeMessage message = new PhaseChangeMessage();

        message.add(textFormatter.makeBoldItalic("THE BATTLE IS OVER!!!"));
        message.addNewLine();
        message.add("Well... sorry guys. Looks like the hostiles were too much for you this time around.");

        return message;
    }
}
