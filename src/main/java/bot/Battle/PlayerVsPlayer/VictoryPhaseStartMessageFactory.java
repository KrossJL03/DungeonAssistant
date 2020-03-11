package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.Battle.PhaseChangeMessageFactoryInterface;
import bot.Message;
import bot.MessageInterface;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

class VictoryPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    VictoryPhaseStartMessageFactory()
    {
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull BattlePhaseChangeResult result)
    {
        Message message = new Message();

        message.add(textFormatter.makeBold("IT'S OVER!!!"));
        message.addBreak();
        message.add("Battle is over! Only one chiot stands, and that is...");

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull BattlePhase previousPhase, @NotNull BattlePhase nextPhase)
    {
        return nextPhase instanceof PvpPhase && ((PvpPhase) nextPhase).isVictoryPhase();
    }
}
