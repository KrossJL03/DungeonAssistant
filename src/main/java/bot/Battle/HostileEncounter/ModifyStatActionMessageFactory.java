package bot.Battle.HostileEncounter;

import bot.Battle.ActionMessageFactory;
import bot.Battle.ActionResultInterface;
import bot.Battle.Mention;
import bot.Battle.ModifyStatActionResult;
import bot.Constant;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class ModifyStatActionMessageFactory extends ActionMessageFactory
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        ModifyStatActionResult modifyStatActionResult = castResult(result);
        Message                message                = new Message();

        message.add(String.format(
            "%s's %s has gone %s by %d, making it %s!",
            modifyStatActionResult.getCreatureName(),
            modifyStatActionResult.getStatName(),
            modifyStatActionResult.getStatMod() > 0 ? "up" : "down",
            modifyStatActionResult.getStatMod(),
            modifyStatActionResult.isHitpointStat() &&
            modifyStatActionResult.getStatValue() > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS
            ? "???"
            : modifyStatActionResult.getStatValue()
        ));

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull ActionResultInterface result)
    {
        return result instanceof ModifyStatActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return ModifyStatActionResult
     */
    private ModifyStatActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (ModifyStatActionResult) result;
    }
}
