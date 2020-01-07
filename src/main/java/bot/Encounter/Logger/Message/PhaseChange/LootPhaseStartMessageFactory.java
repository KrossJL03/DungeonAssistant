package bot.Encounter.Logger.Message.PhaseChange;

import bot.CommandListener;
import bot.Encounter.EncounterPhaseInterface;
import bot.Encounter.PhaseChangeResult;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class LootPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * LootPhaseStartMessageFactory constructor.
     */
    @NotNull LootPhaseStartMessageFactory()
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
        return nextPhase.isLootPhase();
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
        message.add("Great work everyone! You did it!");
        message.addNewLine();
        message.add(textFormatter.makeBold("LOOT TURN!"));
        message.add(String.format(
            "Please use %s to harvest materials from the hostiles.",
            textFormatter.makeCode(String.format("%sloot", MyProperties.COMMAND_PREFIX))
        ));
        message.add("There is no turn order and if you are unable to roll now you may do so later.");

        return message;
    }
}
