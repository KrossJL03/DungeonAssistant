package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.MessageInterface;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class EndPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * EndPhaseStartMessageFactory constructor.
     */
    @NotNull EndPhaseStartMessageFactory()
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

        message.add(textFormatter.makeBoldItalic("THE BATTLE IS OVER!!!"));
        message.addNewLine();
        message.add("Well... sorry guys. Looks like the hostiles were too much for you this time around.");

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
        return nextPhase.isEndPhase();
    }
}
