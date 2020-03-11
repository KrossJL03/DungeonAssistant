package bot.Battle.Encounter;

import bot.Battle.ActionMessageFactory;
import bot.Battle.ActionResultInterface;
import bot.Battle.HurtActionResult;
import bot.Constant;
import bot.Mention;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class HurtActionMessageFactory extends ActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        HurtActionResult hurtActionResult = castResult(result);
        Message          message          = new Message();
        int              currentHp        = hurtActionResult.getCurrentHp();
        int              maxHp            = hurtActionResult.getMaxHp();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s takes %d dmg! [%s/%s]",
            hurtActionResult.getName(),
            hurtActionResult.getHurtHp(),
            currentHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : currentHp,
            maxHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : maxHp
        ));

        if (hurtActionResult.isSlain()) {
            message.add(String.format("%s was slain", hurtActionResult.getName()));
        }
        message.endCodeBlock();

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof HurtActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return HurtActionResult
     */
    private @NotNull HurtActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (HurtActionResult) result;
    }
}
