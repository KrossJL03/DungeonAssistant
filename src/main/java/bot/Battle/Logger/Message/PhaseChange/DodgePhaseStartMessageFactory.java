package bot.Battle.Logger.Message.PhaseChange;

import bot.Battle.BattlePhase;
import bot.Battle.CombatCreature;
import bot.Battle.HostileEncounter.EncounterPhase;
import bot.Battle.HostileEncounter.EncounteredHostile;
import bot.Battle.Logger.Message.MLCodeFormatter;
import bot.Battle.BattlePhaseChangeResult;
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
    public @NotNull MessageInterface createMessage(@NotNull BattlePhaseChangeResult result)
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
        for (CombatCreature creature : result.getCreatures()) {
            if (creature instanceof EncounteredHostile && !creature.isSlain()) {
                totalDamage += ((EncounteredHostile) creature).getAttackRoll();
                message.add(String.format(
                    "d%-2d %s %2d dmg from %s!",
                    creature.getAttackDice(),
                    PhaseChangeMessage.DOUBLE_ARROW,
                    ((EncounteredHostile) creature).getAttackRoll(),
                    codeFormatter.makeRed(creature.getName())
                ));
            }
        }
        message.addNewLine();
        message.add(String.format("combined attacks add up to %d dmg!!", totalDamage));
        message.endCodeBlock();

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
        return ((EncounterPhase) nextPhase).isDodgePhase();
    }
}
