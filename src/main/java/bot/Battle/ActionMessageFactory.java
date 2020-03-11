package bot.Battle;

import bot.CustomException;
import bot.MLCodeFormatter;
import bot.Mention;
import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

abstract public class ActionMessageFactory
{
    protected MLCodeFormatter codeFormatter;

    /**
     * Constructor.
     */
    protected ActionMessageFactory()
    {
        this.codeFormatter = new MLCodeFormatter();
    }

    /**
     * Does this factory handle the given result
     *
     * @param result Result
     *
     * @return boolean
     */
    public abstract boolean handles(@NotNull ActionResultInterface result);

    /**
     * Assert handles
     *
     * @param result Result
     */
    final protected void assertHandles(@NotNull ActionResultInterface result)
    {
        if (!handles(result)) {
            throw new CustomException("I don't know how to say what I'm thinking...");
        }
    }

    /**
     * Create message from action result
     *
     * @param result    Action result
     * @param dmMention DM mention
     *
     * @return Message
     */
    protected abstract @NotNull MessageInterface createMessage(
        @NotNull ActionResultInterface result,
        @NotNull Mention dmMention
    );
}
