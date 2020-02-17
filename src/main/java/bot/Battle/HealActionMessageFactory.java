package bot.Battle;

import bot.Constant;
import bot.Message;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class HealActionMessageFactory extends ActionMessageFactory
{
    /**
     * Constructor.
     */
    public HealActionMessageFactory()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
    {
        HealActionResult healActionResult = castResult(result);
        Message          message          = new Message();
        int              currentHp        = healActionResult.getCurrentHp();
        int              maxHp            = healActionResult.getMaxHp();

        message.startCodeBlock(codeFormatter.getStyle());
        message.add(String.format(
            "%s heals %d points! [%s/%s]",
            healActionResult.getName(),
            healActionResult.getHealedHp(),
            currentHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : currentHp,
            maxHp > Constant.HOSTILE_MAX_VISIBLE_HITPOINTS ? "???" : maxHp
        ));

        if (healActionResult.wasTargetRevived()) {
            message.add(String.format(
                "%s has been revived! They have earned the %s title.",
                healActionResult.getName(),
                codeFormatter.makeCyan("Zombie")
            ));
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
        return result instanceof HealActionResult;
    }

    /**
     * Cast result
     *
     * @param result Result to cast
     *
     * @return HealActionResult
     */
    private HealActionResult castResult(@NotNull ActionResultInterface result)
    {
        assertHandles(result);

        return (HealActionResult) result;
    }
}
