package bot.Battle.Encounter;

import bot.Battle.BattlePhaseChangeResult;
import bot.Message;
import bot.MessageInterface;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

class JoinPhaseStartMessageFactory extends bot.Battle.JoinPhaseStartMessageFactory
{
    /**
     * Constructor.
     */
    JoinPhaseStartMessageFactory()
    {
        super();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void addPostMessageText(@NotNull Message message, @NotNull BattlePhaseChangeResult result)
    {
        message.addBreak();
        message.add(String.format(
            "%s %s",
            MessageInterface.EMOJI_WARNING,
            textFormatter.makeBold("Newbie Grace Period!")
        ));
        message.add(String.format(
            "The first %s minutes are dedicated to new players!",
            textFormatter.makeBold(String.format("%s minutes", MyProperties.NEWBIE_GRACE_PERIOD))
        ));
        message.add(String.format(
            "%s If you have participated in less than %s you are welcome to join at this time! " +
            "Take your time and please ask for help if needed!",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND,
            textFormatter.makeBold(String.format("%d battles", MyProperties.NEWBIE_MAX_BATTLES))
        ));
        message.add(String.format(
            "%s Any more experienced members who join during this time will be kicked. " +
            "Make sure to wait until the DungeonMaster gives you the okay to join!",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND
        ));
    }
}
