package bot.Battle.Logger.Message.Action;

import bot.Battle.EncounteredCreature.LootActionResult;
import bot.Battle.EncounteredCreature.LootRoll;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class LootActionMessageFactory extends ActionMessageFactory
{
    private LootRollLineFactory lootRollLineFactory;

    /**
     * Constructor.
     */
    LootActionMessageFactory()
    {
        this.lootRollLineFactory = new LootRollLineFactory();
    }

    /**
     * Create message from loot action result
     *
     * @param result Loot action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull LootActionResult result)
    {
        if (result.noLoot()) {
            throw ActionMessageBuilderException.createNoLoot(result.getName());
        }

        ActionMessage message = new ActionMessage();

        message.add(result.getMention().getValue());
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s helped slay %d hostile%s!",
            codeFormatter.makeYellow(result.getName()),
            result.getKillCount(),
            result.getKillCount() > 1 ? "s" : ""
        ));
        message.add(String.format(
            "they get to roll %d %s",
            result.getLootRollCount(),
            codeFormatter.makeCyan(String.format("loot di%se", result.getKillCount() > 1 ? "c" : ""))
        ));

        message.addBreak();
        for (LootRoll subResult : result.getLootRolls()) {
            message.add(lootRollLineFactory.getLootRollLine(subResult));
        }
        message.addBreak();

        if (result.hasFinalBlows()) {
            ArrayList<String> formattedKills = new ArrayList<>();
            for (String finalBlowName : result.getFinalBlowNames()) {
                formattedKills.add(codeFormatter.makeRed(finalBlowName));
            }
            message.add(String.format(
                "ALSO they earned %dc for landing the final blow%s on %s!",
                result.getFinalBlowBonus(),
                result.getFinalBlowNames().size() > 1 ? "s" : "",
                String.join(",  ", formattedKills)
            ));
        }
        message.add("Congratulations!");
        message.endCodeBlock();

        return message;
    }
}
