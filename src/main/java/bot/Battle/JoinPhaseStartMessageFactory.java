package bot.Battle;

import bot.Message;
import bot.MessageInterface;
import bot.MyProperties;
import bot.TextFormatter;
import org.jetbrains.annotations.NotNull;

abstract public class JoinPhaseStartMessageFactory implements PhaseChangeMessageFactoryInterface
{
    protected TextFormatter textFormatter;

    /**
     * Constructor.
     */
    protected JoinPhaseStartMessageFactory()
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
        addBattleStats(message, result);
        addPostMessageText(message, result);

        return message;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handles(@NotNull BattlePhase previousPhase, @NotNull BattlePhase nextPhase)
    {
        return nextPhase.isJoinPhase();
    }

    /**
     * Add post message text to a message
     *
     * @param message Message
     * @param result  Result
     */
    abstract protected void addPostMessageText(@NotNull Message message, @NotNull BattlePhaseChangeResult result);

    /**
     * Add battle stats to the message
     *
     * @param message Message
     * @param result  Phase change result
     */
    private void addBattleStats(@NotNull Message message, @NotNull BattlePhaseChangeResult result)
    {
        Tier tier = result.getTier();

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
            "%s %s %s [%d - %d]",
            MessageInterface.EMOJI_MEDAL,
            textFormatter.makeBold("TIER |"),
            tier.getName(),
            tier.getMinStatPointTotal(),
            tier.getMaxStatPointTotal()
        ));
    }
}
