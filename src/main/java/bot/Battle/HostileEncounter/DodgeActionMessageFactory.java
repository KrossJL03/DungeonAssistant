package bot.Battle.HostileEncounter;

import bot.Battle.ActionResultInterface;
import bot.Battle.CombatActionMessageFactory;
import bot.Battle.Mention;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class DodgeActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        DodgeActionResult dodgeActionResult = castResult(result);
        Message           message           = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
        if (dodgeActionResult.isForceFail()) {
            message.add(String.format(
                "%s was distracted, they %s to %s the attacks!",
                codeFormatter.makeYellow(dodgeActionResult.getTargetName()),
                codeFormatter.makeYellow("failed"),
                codeFormatter.makeYellow("dodge")
            ));
            message.add(getHitForFullDamageLine(dodgeActionResult.getTargetName()));
        } else {
            message.add(String.format(
                "%s attempts to %s %d attacks!",
                codeFormatter.makeYellow(dodgeActionResult.getTargetName()),
                codeFormatter.makeYellow("dodge"),
                dodgeActionResult.getAttackCount()
            ));
            message.add(String.format(
                "d%d %s %s",
                dodgeActionResult.getTargetDodgeDie(),
                codeFormatter.makeCyan("dodge dice"),
                codeFormatter.makeGrey(String.format("success = %d", dodgeActionResult.getMinSuccessDodgeRoll()))
            ));
            message.addBreak();
            for (DodgeResult subResult : dodgeActionResult.getDodgeResults()) {
                message.add(getDodgeResultLine(subResult));
            }
        }

        if (dodgeActionResult.getDamageResisted() > 0) {
            message.addBreak();
            message.add(getDamageResistedLine(dodgeActionResult.getDamageResisted()));
        }

        message.addBreak();
        message.add(getDamageDealtLine(dodgeActionResult, true));
        addDeathSaveIfApplicable(message, dodgeActionResult);
        message.add(getTargetStatusLine(dodgeActionResult));
        message.endCodeBlock();

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof DodgeActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return DodgeActionResult
     */
    private DodgeActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (DodgeActionResult) result;
    }

    /**
     * Get dodge result line
     *
     * @param result Dodge result
     *
     * @return String
     */
    private @NotNull String getDodgeResultLine(@NotNull DodgeResult result)
    {
        StringBuilder output    = new StringBuilder();
        int           dodgeRoll = result.getTargetDodgeRoll();

        output.append(String.format("%2d %s ", dodgeRoll, Message.DOUBLE_ARROW));

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
