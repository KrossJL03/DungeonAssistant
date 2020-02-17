package bot.Battle;

import bot.Battle.Logger.Mention;
import bot.CustomException;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

public class EncounterPhaseException extends CustomException
{
    /**
     * EncounterPhaseException constructor
     *
     * @param message Message
     */
    private @NotNull EncounterPhaseException(@NotNull String message)
    {
        super(message);
    }

    /**
     * Factory method for "final phase"
     *
     * @return EncounterPhaseException
     */
    public static @NotNull EncounterPhaseException createFinalPhase()
    {
        return new EncounterPhaseException(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%screate encounter` command",
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for "not attack phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotAttackPhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sattack` during the %s turn",
                MyProperties.COMMAND_PREFIX,
                EncounterPhaseInterface.ATTACK_PHASE
            )
        );
    }

    /**
     * Factory method for "not dodge phase"
     *
     * @return EncounterPhaseException
     */
    public static @NotNull EncounterPhaseException createNotDodgePhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sdodge` during the %s turn",
                MyProperties.COMMAND_PREFIX,
                EncounterPhaseInterface.DODGE_PHASE
            )
        );
    }

    /**
     * Factory method for "not initiative phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotInitiativePhase()
    {
        return new EncounterPhaseException("There is no initiative currently.");
    }

    /**
     * Factory method for "not join phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotJoinPhase(Mention mention)
    {
        return new EncounterPhaseException(
            String.format(
                "Sorry %s, looks like you missed your chance. It's too late to join this encounter.",
                mention.getValue()
            )
        );
    }

    /**
     * Factory method for "not loot phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotLootPhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sloot` during the %s turn",
                MyProperties.COMMAND_PREFIX,
                EncounterPhaseInterface.LOOT_PHASE
            )
        );
    }

    /**
     * Factory method for "not loot phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotPassablePhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%spass` during the %s turn",
                MyProperties.COMMAND_PREFIX,
                EncounterPhaseInterface.DODGE_PHASE
            )
        );
    }

    /**
     * Factory method for "not protect phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotProtectPhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sprotect` during the %s turn",
                MyProperties.COMMAND_PREFIX,
                EncounterPhaseInterface.DODGE_PHASE
            )
        );
    }

    /**
     * Factory method for "not started"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotStarted()
    {
        return new EncounterPhaseException("Hold your Rudi! This encounter hasn't even started yet.");
    }

    /**
     * Factory method for "set tier after create phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createSetTierAfterCreatePhase()
    {
        return new EncounterPhaseException("Tier must be set before the encounter has started");
    }

    /**
     * Factory method for "start current phase"
     *
     * @return EncounterPhaseException
     */
    public static @NotNull EncounterPhaseException createStartCurrentPhase(@NotNull String phase)
    {
        return new EncounterPhaseException(String.format("The %s turn is already in progress", phase));
    }

    /**
     * Factory method for "start in progress encounter"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createStartInProgressEncounter()
    {
        return new EncounterPhaseException("Hold your Capra! This encounter is already in progress.");
    }

    /**
     * Factory method for "start without max players"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createStartWithoutMaxPlayers()
    {
        return new EncounterPhaseException(
            String.format(
                "Wait, I don't know how many players to have. DM could you tell me using `%smaxPlayers`?",
                MyProperties.COMMAND_PREFIX
            )
        );
    }
}
