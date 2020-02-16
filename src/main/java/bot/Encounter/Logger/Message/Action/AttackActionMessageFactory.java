package bot.Encounter.Logger.Message.Action;

import bot.Encounter.AttackActionResultInterface;
import bot.Encounter.Logger.Mention;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class AttackActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * Create message from attack action result
     *
     * @param result    Action result
     * @param dmMention DM mention
     *
     * @return ActionMessage
     */
    public @NotNull MessageInterface createMessage(
        @NotNull AttackActionResultInterface result,
        @NotNull Mention dmMention
    )
    {
        ActionMessage message = new ActionMessage();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s attacks %s!",
            codeFormatter.makeYellow(result.getAttackerName()),
            codeFormatter.makeRed(result.getTargetName())
        ));
        message.add(String.format(
            "d%d %s rolled %d [%s]",
            result.getHitDie(),
            codeFormatter.makeCyan("hit dice"),
            result.getHitRoll(),
            codeFormatter.makeYellow(result.getHitTypeString()).toUpperCase()
        ));

        if (result.isHit()) {
            if (result.isCrit()) {
                message.add(String.format("MAX DAMAGE %d!!", result.getDamageRoll()));
            } else {
                message.add(String.format(
                    "d%d %s rolled %d",
                    result.getDamageDie(),
                    codeFormatter.makeCyan("dmg dice"),
                    result.getDamageRoll()
                ));
            }
        }
        message.addBreak();

        if (result.isFail()) {
            message.add(String.format("well... that's %s", codeFormatter.makeRed("unfortunate")));
            message.endCodeBlock();
            message.add(String.format(
                "Sucks to be you! Look out for the %s at the end of the round...",
                dmMention.getValue()
            ));
        } else {
            if (result.getDamageDealt() > 0) {
                message.add(getDamageDealtLine(result, false));
            }
            message.add(getTargetStatusLine(result));
            message.endCodeBlock();
        }

        return message;
    }
}
