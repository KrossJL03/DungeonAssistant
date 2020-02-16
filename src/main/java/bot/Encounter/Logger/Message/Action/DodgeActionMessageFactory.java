package bot.Encounter.Logger.Message.Action;

import bot.Encounter.DodgeActionResultInterface;
import bot.Encounter.DodgeResultInterface;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class DodgeActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * Create message from dodge action result
     *
     * @param result Dodge action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull DodgeActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        if (result.isForceFail()) {
            message.add(String.format(
                "%s was distracted, they %s to %s the attacks!",
                codeFormatter.makeYellow(result.getTargetName()),
                codeFormatter.makeYellow("failed"),
                codeFormatter.makeYellow("dodge")
            ));
            message.add(getHitForFullDamageLine(result.getTargetName()));
        } else {
            message.add(String.format(
                "%s attempts to %s %d attacks!",
                codeFormatter.makeYellow(result.getTargetName()),
                codeFormatter.makeYellow("dodge"),
                result.getAttackCount()
            ));
            message.add(String.format(
                "d%d %s %s",
                result.getTargetDodgeDie(),
                codeFormatter.makeCyan("dodge dice"),
                codeFormatter.makeGrey(String.format("success = %d", result.getMinSucessDodgeRoll()))
            ));
            message.addBreak();
            for (DodgeResultInterface subActionData : result.getDodgeResults()) {
                message.add(getDodgeResultLine(subActionData));
            }
        }

        if (result.getDamageResisted() > 0) {
            message.addBreak();
            message.add(getDamageResistedLine(result.getDamageResisted()));
        }

        message.addBreak();
        message.add(getDamageDealtLine(result, true));
        addDeathSaveIfApplicable(message, result);
        message.add(getTargetStatusLine(result));
        message.endCodeBlock();

        return message;
    }

    /**
     * Get dodge result line
     *
     * @param result Dodge result
     *
     * @return String
     */
    private @NotNull String getDodgeResultLine(@NotNull DodgeResultInterface result)
    {
        StringBuilder output    = new StringBuilder();
        int           dodgeRoll = result.getTargetDodgeRoll();

        output.append(String.format("%2d %s ", dodgeRoll, ActionMessage.DOUBLE_ARROW));

        if (result.isSuccess()) {
            output.append(String.format(
                "%s! %s",
                codeFormatter.makeYellow("DODGED"),
                codeFormatter.makeGrey(String.format("no dmg from %s", result.getAttackerName()))
            ));
        } else {
            output.append(String.format(
                "%s! %2d dmg from '%s'",
                codeFormatter.makeRed("FAIL"),
                result.getAttackerDamageRoll(),
                result.getAttackerName()
            ));
        }

        return output.toString();
    }
}
