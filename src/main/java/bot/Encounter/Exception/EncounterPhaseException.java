package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Encounter.Encounter;
import org.jetbrains.annotations.NotNull;

public class EncounterPhaseException extends RuntimeException implements CustomExceptionInterface {

    private EncounterPhaseException(String message) {
        super(message);
    }

    public static EncounterPhaseException createEndPhase() {
        return new EncounterPhaseException(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%screate encounter` command",
                CommandListener.COMMAND_KEY
            )
        );
    }

    public static EncounterPhaseException createNotAttackPhase() {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sattack` during the %s turn",
                CommandListener.COMMAND_KEY,
                Encounter.ATTACK_PHASE
            )
        );
    }

    public static EncounterPhaseException createNotDodgePhase() {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sdodge` during the %s turn",
                CommandListener.COMMAND_KEY,
                Encounter.DODGE_PHASE
            )
        );
    }

    public static EncounterPhaseException createNotItemUsablePhase(String itemName, String usablePhase) {
        return new EncounterPhaseException(
            String.format(
                "%s can only be used during the %s turn",
                itemName,
                usablePhase
            )
        );
    }

    public static EncounterPhaseException createNotLootPhase() {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sloot` during the %s turn",
                CommandListener.COMMAND_KEY,
                Encounter.LOOT_PHASE
            )
        );
    }

    public static EncounterPhaseException createNotProtectPhase() {
        return new EncounterPhaseException(
            String.format(
                "You can only `%sprotect` during the %s turn",
                CommandListener.COMMAND_KEY,
                Encounter.DODGE_PHASE
            )
        );
    }

    public static EncounterPhaseException createNotStarted() {
        return new EncounterPhaseException("Hold your Rudi! This encounter hasn't even started yet.");
    }

    public static EncounterPhaseException createStartInProgressEncounter() {
        return new EncounterPhaseException("Hold your Capra! This encounter is already in progress.");
    }

    /**
     * Factory method for "set tier after create phase"
     *
     * @return EncounterPhaseException
     */
    public static @NotNull EncounterPhaseException createSetTierAfterCreatePhase() {
        return new EncounterPhaseException("Tier must be set before the encounter has started");
    }
}
