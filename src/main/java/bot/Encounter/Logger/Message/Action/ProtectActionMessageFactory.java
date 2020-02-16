package bot.Encounter.Logger.Message.Action;

import bot.MessageInterface;
import bot.Encounter.ProtectActionResultInterface;
import org.jetbrains.annotations.NotNull;

class ProtectActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * Create message from protect action result
     *
     * @param result Protect action result
     *
     * @return MessageInterface
     */
    @NotNull MessageInterface createMessage(@NotNull ProtectActionResultInterface result)
    {
        ActionMessage message = new ActionMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s shields %s from the attacks!",
            result.getTargetName(),
            result.getProtectedName()
        ));
        message.add(getHitForFullDamageLine(result.getTargetName()));
        message.addBreak();

        if (result.getDamageResisted() > 0) {
            message.add(getDamageResistedLine(result.getDamageResisted()));
            message.addBreak();
        }

        message.add(getDamageDealtLine(result, true));
        addDeathSaveIfApplicable(message, result);
        message.add(getTargetStatusLine(result));
        message.endCodeBlock();

        message.add(String.format(
            "%s, %s has been protected. They take no damage this round.",
            result.getProtectedOwnerMention().getValue(),
            result.getProtectedName()
        ));

        return message;
    }
}
