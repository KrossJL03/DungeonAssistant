package bot.Battle.Logger.Message.Action;

import bot.Battle.CombatExplorer;
import bot.Battle.JoinActionResult;
import bot.Battle.Logger.Mention;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class JoinActionMessageFactory extends ActionMessageFactory
{
    /**
     * Create message from join action result
     *
     * @param result Join action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull JoinActionResult result)
    {
        ActionMessage message = new ActionMessage();

        CombatExplorer explorer = result.getExplorer();
        message.add(String.format(
            "%s: %s has been added! %s",
            (Mention.createForPlayer(explorer.getOwner().getUserId())).getValue(),
            explorer.getName(),
            getExplorerPrintout(explorer)
        ));
        if (result.isRosterFull()) {
            message.add("***THE ROSTER IS NOW FULL!***");
        }

        return message;
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
        output += ActionMessage.NEWLINE;
        output += nameBuffer < 29 ?
                  String.format("%" + nameBuffer + "s", explorer.getName()) :
                  explorer.getName();
        output += ActionMessage.NEWLINE;
        output += "=============================";
        output += ActionMessage.NEWLINE;
        output += "  HP | STR | WIS | AGI | DEF ";
        output += ActionMessage.NEWLINE;
        output += String.format(
            "%4s | %2s  | %2s  | %2s  | %2s",
            explorer.getMaxHP(),
            explorer.getStrength(),
            explorer.getWisdom(),
            explorer.getAgility(),
            explorer.getDefense()
        );
        output += ActionMessage.NEWLINE;
        output += "=============================";
        output += ActionMessage.NEWLINE;
        output += String.format("ATK Dice:  %2d  ", explorer.getAttackDice());
        output += String.format("Min Crit:   %2d", explorer.getMinCrit());
        output += ActionMessage.NEWLINE;
        output += String.format("DOD Dice:  %2d  ", explorer.getDodgeDice());
        output += String.format("# of Turns: %2d", explorer.getMaxActions());
        output += ActionMessage.NEWLINE;
        output += "```";
        return output;
    }
}
