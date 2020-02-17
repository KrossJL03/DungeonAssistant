package bot.Battle.Logger.Message.Action;

import bot.Battle.HostileEncounter.LootRoll;
import bot.Battle.Logger.Message.MLCodeFormatter;
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
    public @NotNull String getLootRollsMessage(@NotNull ArrayList<LootRoll> rolls)
    {
        Message message = new Message();
        message.startCodeBlock(codeFormatter.getStyle());

        for (LootRoll roll : rolls) {
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
    @NotNull String getLootRollLine(@NotNull LootRoll roll)
    {
        StringBuilder output = new StringBuilder();
        Loot          loot   = roll.getLoot();

        output.append(String.format(
            "%3s %s %2d --> ",
            "d" + roll.getDie(),
            ActionMessage.DOUBLE_ARROW,
            roll.getRoll()
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
