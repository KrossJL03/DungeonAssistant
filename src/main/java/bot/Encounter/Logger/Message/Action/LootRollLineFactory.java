package bot.Encounter.Logger.Message.Action;

import bot.Encounter.Logger.Message.MLCodeFormatter;
import bot.Encounter.LootRollInterface;
import bot.Hostile.Loot;
import bot.Message;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LootRollLineFactory
{
    protected MLCodeFormatter codeFormatter;

    /**
     * Constructor.
     */
    public @NotNull LootRollLineFactory()
    {
        this.codeFormatter = new MLCodeFormatter();
    }

    /**
     * Get loot roll message
     *
     * @param rolls Loot rolls
     *
     * @return String
     */
    public @NotNull String getLootRollsMessage(@NotNull ArrayList<LootRollInterface> rolls)
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());

        for (LootRollInterface roll : rolls) {
            message.add(getLootRollLine(roll));
        }

        message.endCodeBlock();

        return message.getAsString();
    }

    /**
     * Get loot roll line
     *
     * @param roll Loot roll
     *
     * @return String
     */
    @NotNull String getLootRollLine(@NotNull LootRollInterface roll)
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
                loot.getItem(),
                codeFormatter.makeRed(roll.getKillName())
            ));
        }

        return output.toString();
    }
}
