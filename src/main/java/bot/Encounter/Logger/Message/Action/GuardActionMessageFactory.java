package bot.Encounter.Logger.Message.Action;

import bot.Encounter.GuardActionResultInterface;
import bot.Encounter.GuardResultInterface;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class GuardActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * Create message from guard action result
     *
     * @param result Guard action result
     *
     * @return MessageInterface
     */
    public @NotNull MessageInterface createMessage(@NotNull GuardActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s puts their %s to brace for %d attacks!",
            codeFormatter.makeYellow(result.getTargetName()),
            codeFormatter.makeYellow("guard up"),
            result.getAttackCount()
        ));
        message.add(String.format(
            "%s goes up by half for this round making it %d",
            codeFormatter.makeCyan("dmg resisted"),
            result.getTargetDefense()
        ));
        message.addBreak();
        for (GuardResultInterface subActionData : result.getGuardResults()) {
            message.add(getGuardResultLine(subActionData));
        }

        message.addBreak();
        message.add(getDamageResistedLine(result.getDamageResisted()));

        message.addBreak();
        message.add(getDamageDealtLine(result, true));
        addDeathSaveIfApplicable(message, result);
        message.add(getTargetStatusLine(result));
        message.endCodeBlock();

        return message;
    }

    /**
     * Get guard result line
     *
     * @param result Guard result
     *
     * @return String
     */
    private @NotNull String getGuardResultLine(@NotNull GuardResultInterface result)
    {
        return String.format(
            "%2d %s %2d dmg from '%s'",
            result.getAttackerDamageRoll(),
            ActionMessage.DOUBLE_ARROW,
            result.getDamageDealt(),
            result.getAttackerName()
        );
    }
}
