package bot.Battle;

import bot.Message;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

public class JoinPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    private TextFormatter textFormatter;

    /**
     * Constructor.
     */
    public JoinPhaseStartMessageFactory()
    {
        this.textFormatter = new TextFormatter();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull MessageInterface createMessage(@NotNull BattlePhaseChangeResult result)
    {
        Message message = new Message();
        Tier    tier    = result.getTier();

        message.add(textFormatter.makeBold("BATTLE TIME!"));
        message.addBreak();
        message.add(String.format(
            "%s Use %s to join!",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND,
            textFormatter.makeCode(String.format("%sjoin [CharacterName] <Nickname>", MyProperties.COMMAND_PREFIX))
        ));
        message.add(String.format(
            "%s Use %s to register an explorer before a battle starts in order to participate.",
            MessageInterface.EMOJI_SMALL_ORANGE_DIAMOND,
            textFormatter.makeCode(String.format("%screate character", MyProperties.COMMAND_PREFIX))
        ));
        if (result.isAlwaysJoinable()) {
            message.add(
                "You may join a battle at any time before the battle ends and as long as there are slots open!"
            );
        }
        message.addBreak();
        message.add(String.format(
            "%s %s %s",
            MessageInterface.EMOJI_CROSSED_SWORDS,
            textFormatter.makeBold("BATTLE TYPE |"),
            result.getBattleType()
        ));
        message.add(String.format(
            "%s %s %d players",
            MessageInterface.EMOJI_WING_LEFT,
            textFormatter.makeBold("DUNGEON CAP |"),
            result.getMaxPartySize()
        ));
        message.add(String.format(
            "%s %s %s",
            MessageInterface.EMOJI_MEDAL,
            textFormatter.makeBold("TIER |"),
            tier.getName()
        ));
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

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(
        @NotNull BattlePhase previousPhase,
        @NotNull BattlePhase nextPhase
    )
    {
        return nextPhase.isJoinPhase();
    }
}
