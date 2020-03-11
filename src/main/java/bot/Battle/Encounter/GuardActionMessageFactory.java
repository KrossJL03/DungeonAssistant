package bot.Battle.Encounter;

import bot.Battle.ActionResultInterface;
import bot.Battle.CombatActionMessageFactory;
import bot.Mention;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class GuardActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        GuardActionResult guardActionResult = castResult(result);
        Message           message           = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s puts their %s to brace for %d attacks!",
            codeFormatter.makeYellow(guardActionResult.getTargetName()),
            codeFormatter.makeYellow("guard up"),
            guardActionResult.getAttackCount()
        ));
        message.add(String.format(
            "%s goes up by half for this round making it %d",
            codeFormatter.makeCyan("dmg resisted"),
            guardActionResult.getTargetDefense()
        ));
        message.addBreak();
        for (GuardResult subResult : guardActionResult.getGuardResults()) {
            message.add(getGuardResultLine(subResult));
        }

        message.addBreak();
        message.add(getDamageResistedLine(guardActionResult.getDamageResisted()));

        message.addBreak();
        message.add(getDamageDealtLine(guardActionResult, true));
        addDeathSaveIfApplicable(message, guardActionResult);
        message.add(getTargetStatusLine(guardActionResult));
        message.endCodeBlock();

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof GuardActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return GuardActionResult
     */
    private GuardActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (GuardActionResult) result;
    }

    /**
     * Get guard result line
     *
     * @param result Guard result
     *
     * @return String
     */
    private @NotNull String getGuardResultLine(@NotNull GuardResult result)
    {
        return String.format(
            "%2d %s %2d dmg from '%s'",
            result.getAttackerDamageRoll(),
            Message.DOUBLE_ARROW,
            result.getDamageDealt(),
            result.getAttackerName()
        );
    }
}
