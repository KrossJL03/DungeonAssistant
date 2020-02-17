package bot.Battle.HostileEncounter;

import bot.Battle.BattlePhase;
import bot.Battle.BattlePhaseChangeResult;
import bot.Battle.PhaseChangeMessageFactoryInterface;
import bot.Message;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class LootPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    LootPhaseStartMessageFactory()
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

        message.add(textFormatter.makeBoldItalic("THE BATTLE IS OVER!!!"));
        message.addBreak();
        message.add("Great work everyone! You did it!");
        message.addBreak();
        message.add(textFormatter.makeBold("LOOT TURN!"));
        message.add(String.format(
            "Please use %s to harvest materials from the hostiles.",
            textFormatter.makeCode(String.format("%sloot", MyProperties.COMMAND_PREFIX))
        ));
        message.add("There is no turn order and if you are unable to roll now you may do so later.");

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
        return ((EncounterPhase) nextPhase).isLootPhase();
    }
}
