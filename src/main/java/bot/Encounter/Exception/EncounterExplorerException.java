package bot.Encounter.Exception;

import bot.Constant;
import bot.CustomExceptionInterface;

public class EncounterExplorerException extends RuntimeException implements CustomExceptionInterface {

    private EncounterExplorerException(String message) {
        super(message);
    }

    public static EncounterExplorerException createStatOutOfBounds(String name, String statName) {
        return new EncounterExplorerException(
            String.format(
                "%s's %s must be between %d and %d!",
                name,
                statName,
                Constant.getStatMin(statName),
                Constant.getStatMax(statName)
            )
        );
    }

    public static EncounterExplorerException invalidStatName(String name) {
        return new EncounterExplorerException(String.format("%s is not the name of a stat", name));
    }

}
