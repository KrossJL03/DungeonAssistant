package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;

public class EncounterStatusException extends RuntimeException implements CustomExceptionInterface {

    private EncounterStatusException(String message) {
        super(message);
    }

    public static EncounterStatusException createInProgress() {
        return new EncounterStatusException("Hold your Capra! This encounter is already in progress.");
    }

    public static EncounterStatusException createNotStarted() {
        return new EncounterStatusException("Hold your Rudi! This encounter hasn't even started yet.");
    }

    public static EncounterStatusException createIsOver() {
        return new EncounterStatusException(
            String.format(
                "This encounter is over. If you'd like to start a new one use the `%screate encounter` command",
                CommandListener.COMMAND_KEY
            )
        );
    }
}
