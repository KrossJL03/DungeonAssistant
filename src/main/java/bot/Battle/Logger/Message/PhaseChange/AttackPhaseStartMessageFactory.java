package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class AttackPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * AttackPhaseStartMessageFactory constructor.
     */
    @NotNull AttackPhaseStartMessageFactory()
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

        message.add(textFormatter.makeBold("ATTACK TURN!"));
        message.add(String.format(
            "Please use %s to attack. Ex: %s",
            textFormatter.makeCode(String.format("%sattack [TargetName]", MyProperties.COMMAND_PREFIX)),
            textFormatter.makeCode(String.format("%sattack Stanley", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "To use items use %s and the DM will be pinged to help out. Ex: %s",
            textFormatter.makeCode("rp!use"),
            textFormatter.makeCode("rp!use BreadLoaf")
        ));

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
        return nextPhase.isAttackPhase();
    }
}
