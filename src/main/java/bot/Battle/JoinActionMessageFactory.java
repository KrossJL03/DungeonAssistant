package bot.Battle;

import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class JoinActionMessageFactory extends ActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        JoinActionResult joinActionResult = castResult(result);
        Message          message          = new Message();

        CombatExplorer explorer = joinActionResult.getExplorer();
        message.add(String.format(
            "%s: %s has been added! %s",
            (Mention.createForPlayer(explorer.getOwner().getUserId())).getValue(),
            explorer.getName(),
            getExplorerPrintout(explorer)
        ));
        if (joinActionResult.isRosterFull()) {
            message.add("***THE ROSTER IS NOW FULL!***");
        }

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof JoinActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return JoinActionResult
     */
    private JoinActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (JoinActionResult) result;
    }

    /**
     * Get explorer printout
     *
     * @param explorer Explorer
     *
     * @return String
     */
    private @NotNull String getExplorerPrintout(@NotNull CombatExplorer explorer)
    {
        // todo cleanup using Message
        int    nameBuffer = (int) Math.floor(15 + explorer.getName().length() / 2);
        String output     = "";
        output += "```md";
        output += Message.NEWLINE;
        output += nameBuffer < 29 ?
                  String.format("%" + nameBuffer + "s", explorer.getName()) :
                  explorer.getName();
        output += Message.NEWLINE;
        output += "=============================";
        output += Message.NEWLINE;
        output += "  HP | STR | WIS | AGI | DEF ";
        output += Message.NEWLINE;
        output += String.format(
            "%4s | %2s  | %2s  | %2s  | %2s",
            explorer.getMaxHP(),
            explorer.getStrength(),
            explorer.getWisdom(),
            explorer.getAgility(),
            explorer.getDefense()
        );
        output += Message.NEWLINE;
        output += "=============================";
        output += Message.NEWLINE;
        output += String.format("ATK Dice:  %2d  ", explorer.getAttackDice());
        output += String.format("Min Crit:   %2d", explorer.getMinCrit());
        output += Message.NEWLINE;
        output += String.format("DOD Dice:  %2d  ", explorer.getDodgeDice());
        output += String.format("# of Turns: %2d", explorer.getMaxActions());
        output += Message.NEWLINE;
        output += "```";
        return output;
    }
}
