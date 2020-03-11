package bot.Battle.PlayerVsPlayer;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.Battle.PhaseChangeMessageFactoryInterface;
import bot.Message;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

class AttackPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    AttackPhaseStartMessageFactory()
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

        message.add(textFormatter.makeBold("ATTACK TURN!"));
        message.addBreak();
        message.add("May the best chiot win!");
        message.add(String.format(
            "%s Use %s to attack. Ex: %s",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND,
            textFormatter.makeCode(String.format("%sattack [TargetName]", MyProperties.COMMAND_PREFIX)),
            textFormatter.makeCode(String.format("%sattack Stanley", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "%s Items cannot be used in PVP battles.",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND
        ));
        message.add(String.format(
            "%s Abilities cannot be used in PVP battles.",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND
        ));

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull BattlePhase previousPhase, @NotNull BattlePhase nextPhase)
    {
        return nextPhase.isAttackPhase();
    }
}
