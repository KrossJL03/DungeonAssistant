package bot.Battle.Encounter;

import bot.Battle.ActionMessageFactory;
import bot.Battle.ActionResultInterface;
import bot.Battle.Mention;
import bot.CustomException;
import bot.Message;
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
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        LootActionResult lootActionResult = castResult(result);

        if (lootActionResult.noLoot()) {
            throw new CustomException(String.format(
                "I can't seem to find %s's loot... That's a problem",
                lootActionResult.getName()
            ));
        }

        Message message = new Message();

        message.add(lootActionResult.getMention().getValue());
        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s helped slay %d hostile%s!",
            codeFormatter.makeYellow(lootActionResult.getName()),
            lootActionResult.getKillCount(),
            lootActionResult.getKillCount() > 1 ? "s" : ""
        ));
        message.add(String.format(
            "they get to roll %d %s",
            lootActionResult.getLootRollCount(),
            codeFormatter.makeCyan(String.format("loot di%se", lootActionResult.getKillCount() > 1 ? "c" : ""))
        ));

        message.addBreak();
        for (LootRoll subResult : lootActionResult.getLootRolls()) {
            message.add(lootRollLineFactory.getLootRollLine(subResult));
        }
        message.addBreak();

        if (lootActionResult.hasFinalBlows()) {
            ArrayList<String> formattedKills = new ArrayList<>();
            for (String finalBlowName : lootActionResult.getFinalBlowNames()) {
                formattedKills.add(codeFormatter.makeRed(finalBlowName));
            }
            message.add(String.format(
                "ALSO they earned %dc for landing the final blow%s on %s!",
                lootActionResult.getFinalBlowBonus(),
                lootActionResult.getFinalBlowNames().size() > 1 ? "s" : "",
                String.join(",  ", formattedKills)
            ));
        }
        message.add("Congratulations!");
        message.endCodeBlock();

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof LootActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return LootActionResult
     */
    private LootActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (LootActionResult) result;
    }
}
