package bot.Encounter.Logger.Message.Action;

import bot.Encounter.Logger.Message.MessageInterface;
import bot.Encounter.LootActionResultInterface;
import bot.Encounter.LootRollInterface;
import bot.Hostile.Loot;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class LootActionMessageFactory extends ActionMessageFactory
{
    /**
     * Create message from loot action result
     *
     * @param result Loot action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull LootActionResultInterface result)
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
        for (LootRollInterface subAction : result.getLootRolls()) {
            message.add(getLootRollLine(subAction));
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

    /**
     * Get loot roll line
     *
     * @param roll Loot roll
     *
     * @return String
     */
    private @NotNull String getLootRollLine(@NotNull LootRollInterface roll)
    {
        StringBuilder output = new StringBuilder();
        Loot          loot   = roll.getLoot();

        output.append(String.format(
            "%3s %s %2d --> ",
            "d" + roll.getLootDie(),
            ActionMessage.DOUBLE_ARROW,
            roll.getLootRoll()
        ));

        if (loot.getItem() == null || loot.getItem().equals("null")) {
            output.append(codeFormatter.makeGrey(String.format("nothing from %s", roll.getKillName())));
        } else {
            output.append(String.format(
                "x%d %s from %s",
                loot.getQuantity(),
                codeFormatter.makeYellow(loot.getItem()),
                codeFormatter.makeRed(roll.getKillName())
            ));
        }

        return output.toString();
    }
}
