package bot.Encounter.Logger.Message.PhaseChange;

import bot.CommandListener;
import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.PhaseChangeResult;
import bot.Encounter.TierInterface;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class JoinPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * JoinPhaseStartMessageFactory constructor.
     */
    @NotNull JoinPhaseStartMessageFactory()
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
        return nextPhase.isJoinPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull PhaseChangeResult result)
    {
        PhaseChangeMessage message = new PhaseChangeMessage();
        TierInterface      tier    = result.getTier();

        message.add(textFormatter.makeBold("BATTLE TIME!"));
        message.addNewLine();
        message.add(String.format(
            "To bring a character to battle, use %s.",
            textFormatter.makeCode(String.format("%sjoin [CharacterName]", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "Only characters registered prior to the battle may join. " +
            "The %s command cannot be used while a battle is in progress.",
            textFormatter.makeCode(String.format("%screate character", MyProperties.COMMAND_PREFIX))
        ));
        message.add(
            "You may join a battle at any time before the battle has ended and as long as there are slots open!"
        );
        message.addNewLine();
        message.add(String.format(
            "This dungeon has a max capacity of %s players. ",
            textFormatter.makeBold(String.format("%d", result.getMaxPlayerCount()))
        ));
        message.add(String.format(
            "Tier is set to %s! All explorers must have a stat point total between %s and %s",
            textFormatter.makeBold(tier.getName()),
            textFormatter.makeBold(tier.getMinStatPointTotal()),
            textFormatter.makeBold(tier.getMaxStatPointTotal())
        ));

        return message;
    }
}
