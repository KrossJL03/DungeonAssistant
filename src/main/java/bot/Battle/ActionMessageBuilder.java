package bot.Battle;

import bot.CustomException;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ActionMessageBuilder
{
    private ArrayList<ActionMessageFactory> factories;

    /**
     * Constructor.
     */
    protected ActionMessageBuilder(@NotNull ArrayList<ActionMessageFactory> factories)
    {
        this.factories = new ArrayList<>(factories);
    }

    /**
     * Build action message
     *
     * @param result    Action result
     * @param dmMention Dm mention
     *
     * @return String
     *
     * @throws CustomException If action builder does not exist for action result
     */
    String buildActionMessage(@NotNull ActionResultInterface result, @NotNull Mention dmMention)
        throws CustomException
    {
        for (ActionMessageFactory factory : factories) {
            if (factory.handles(result)) {
                return factory.createMessage(result, dmMention).getAsString();
            }
        }

        throw new CustomException("I don't know how to log that action");
    }
}
