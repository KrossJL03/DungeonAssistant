package bot.Encounter.Logger.Message.PhaseChange;

import bot.CommandListener;
import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.EncounteredHostileInterface;
import bot.Encounter.Logger.Message.*;
import bot.Encounter.PhaseChangeResult;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class DodgePhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private MLCodeFormatter codeFormatter;
    private TextFormatter   textFormatter;

    /**
     * DodgePhaseStartMessageFactory constructor.
     */
    @NotNull DodgePhaseStartMessageFactory()
    {
        this.codeFormatter = new MLCodeFormatter();
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
        return nextPhase.isDodgePhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result)
    {
        PhaseChangeMessage message     = new PhaseChangeMessage();
        int                totalDamage = 0;

        message.add(textFormatter.makeBold("DODGE TURN!"));
        message.add(String.format(
            "Please %s to try to avoid the attack, or %s to sacrifice yourself to save someone else. Ex: %s",
            textFormatter.makeCode(String.format("%sdodge", MyProperties.COMMAND_PREFIX)),
            textFormatter.makeCode(String.format("%sprotect [CharacterName]", MyProperties.COMMAND_PREFIX)),
            textFormatter.makeCode(String.format("%sprotect Cocoa", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "To use items use %s and the DM will be pinged to help out. Ex: %s",
            textFormatter.makeCode("rp!use"),
            textFormatter.makeCode("rp!use SmokeBomb")
        ));
        message.add(String.format(
            "To use abilities to automatically skip a dodge round use %s to have the DM assist you.",
            textFormatter.makeCode(String.format("%sdodgePass", MyProperties.COMMAND_PREFIX))
        ));

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format("%s attack the party!", codeFormatter.makeRed("Hostiles")));
        message.addNewLine();
        for (EncounteredHostileInterface hostile : result.getHostiles()) {
            if (!hostile.isSlain()) {
                totalDamage += hostile.getAttackRoll();
                message.add(String.format(
                    "d%-2d %s %2d dmg from %s!",
                    hostile.getAttackDice(),
                    PhaseChangeMessage.DOUBLE_ARROW,
                    hostile.getAttackRoll(),
                    codeFormatter.makeRed(hostile.getName())
                ));
            }
        }
        message.addNewLine();
        message.add(String.format("combined attacks add up to %d dmg!!", totalDamage));
        message.endCodeBlock();

        return message;
    }
}
