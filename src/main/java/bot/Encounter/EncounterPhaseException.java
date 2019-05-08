package bot.Encounter;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import org.jetbrains.annotations.NotNull;

class EncounterPhaseException extends RuntimeException implements EncounterExceptionInterface
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
     * Factory method for "end phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createEndPhase()
    {
        return new EncounterPhaseException(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%screate encounter` command",
                CommandListener.COMMAND_KEY
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
                CommandListener.COMMAND_KEY,
                Encounter.ATTACK_PHASE
            )
        );
    }

    /**
     * Factory method for "not dodge phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createNotDodgePhase()
    {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sdodge` during the %s turn",
                CommandListener.COMMAND_KEY,
                Encounter.DODGE_PHASE
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
        return new EncounterPhaseException(
            "There is no turn order currently, so there is no current player to be skipped.");
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
                CommandListener.COMMAND_KEY,
                Encounter.LOOT_PHASE
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
                CommandListener.COMMAND_KEY,
                Encounter.DODGE_PHASE
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
     * Factory method for "start in progress encounter"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createStartInProgressEncounter()
    {
        return new EncounterPhaseException("Hold your Capra! This encounter is already in progress.");
    }

    /**
     * Factory method for "start current phase"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createStartCurrentPhase(@NotNull String phase)
    {
        return new EncounterPhaseException(String.format("The %s turn is already in progress", phase));
    }

    /**
     * Factory method for "start without hostiles"
     *
     * @return EncounterPhaseException
     */
    static @NotNull EncounterPhaseException createStartWithoutHostiles()
    {
        return new EncounterPhaseException(
            String.format(
                "Uh, wait. Who are we fighting again? Tell me using `%saddHostile [species]`.",
                CommandListener.COMMAND_KEY
            )
        );
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
                CommandListener.COMMAND_KEY
            )
        );
    }
}
