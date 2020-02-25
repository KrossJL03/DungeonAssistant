package bot.Battle;

import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class AttackActionMessageFactory extends CombatActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        AttackActionResult attackActionResult = castResult(result);
        Message            message            = new Message();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s attacks %s!",
            codeFormatter.makeYellow(attackActionResult.getAttackerName()),
            codeFormatter.makeRed(attackActionResult.getTargetName())
        ));
        message.add(String.format(
            "d%d %s rolled %d [%s]",
            attackActionResult.getHitDie(),
            codeFormatter.makeCyan("hit dice"),
            attackActionResult.getHitRoll(),
            codeFormatter.makeYellow(attackActionResult.getHitTypeString()).toUpperCase()
        ));

        if (attackActionResult.isHit()) {
            if (attackActionResult.isCrit()) {
                message.add(String.format("MAX DAMAGE %d!!", attackActionResult.getDamageRoll()));
            } else {
                message.add(String.format(
                    "d%d %s rolled %d",
                    attackActionResult.getDamageDie(),
                    codeFormatter.makeCyan("dmg dice"),
                    attackActionResult.getDamageRoll()
                ));
            }
        }
        message.addBreak();

        if (attackActionResult.isFail()) {
            message.add(String.format("well... that's %s", codeFormatter.makeRed("unfortunate")));
            message.endCodeBlock();
            message.add(String.format(
                "Sucks to be you! Look out for the %s at the end of the round...",
                dmMention.getValue()
            ));
        } else {
            if (attackActionResult.getDamageResisted() > 0) {
                message.add(getDamageResistedLine(attackActionResult.getDamageResisted()));
                message.addBreak();
            }
            if (attackActionResult.getDamageDealt() > 0) {
                message.add(getDamageDealtLine(attackActionResult, false));
            }
            message.add(getTargetStatusLine(attackActionResult));
            message.endCodeBlock();
        }

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof AttackActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return AttackActionResult
     */
    private AttackActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (AttackActionResult) result;
    }
}
