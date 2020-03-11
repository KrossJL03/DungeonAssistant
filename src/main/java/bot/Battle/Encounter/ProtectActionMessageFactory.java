package bot.Battle.Encounter;

import bot.Battle.ActionResultInterface;
import bot.Battle.CombatActionMessageFactory;
import bot.Mention;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class ProtectActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        ProtectActionResult protectActionResult = castResult(result);
        Message             message             = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s shields %s from the attacks!",
            protectActionResult.getTargetName(),
            protectActionResult.getProtectedName()
        ));
        message.add(getHitForFullDamageLine(protectActionResult.getTargetName()));
        message.addBreak();

        if (protectActionResult.getDamageResisted() > 0) {
            message.add(getDamageResistedLine(protectActionResult.getDamageResisted()));
            message.addBreak();
        }

        message.add(getDamageDealtLine(protectActionResult, true));
        addDeathSaveIfApplicable(message, protectActionResult);
        message.add(getTargetStatusLine(protectActionResult));
        message.endCodeBlock();

        message.add(String.format(
            "%s, %s has been protected. They take no damage this round.",
            protectActionResult.getProtectedOwnerMention().getValue(),
            protectActionResult.getProtectedName()
        ));

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof ProtectActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return ProtectActionResult
     */
    private @NotNull ProtectActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (ProtectActionResult) result;
    }
}
