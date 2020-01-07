package bot.Encounter.Logger.Message.PhaseChange;

import bot.CommandListener;
import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.PhaseChangeResult;
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
    public boolean handles(
        @NotNull EncounterPhaseInterface previousPhase,
        @NotNull EncounterPhaseInterface nextPhase
    )
    {
        return nextPhase.isAttackPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result)
    {
        PhaseChangeMessage message = new PhaseChangeMessage();

        message.add(textFormatter.makeBold("ATTACK TURN!"));
        message.add(String.format(
            "Please use %s to attack. Ex: %s",
            textFormatter.makeCode(String.format("%sattack [HostileName]", MyProperties.COMMAND_PREFIX)),
            textFormatter.makeCode(String.format("%sattack Stanley", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "To use items use %s and the DM will be pinged to help out. Ex: %s",
            textFormatter.makeCode("rp!use"),
            textFormatter.makeCode("rp!use BreadLoaf")
        ));

        return message;
    }
}
