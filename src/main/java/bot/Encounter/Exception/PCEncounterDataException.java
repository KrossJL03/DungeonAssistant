package bot.Encounter.Exception;

import bot.Constant;
import bot.CustomExceptionInterface;

public class PCEncounterDataException extends RuntimeException implements CustomExceptionInterface {

    private PCEncounterDataException(String message) {
        super(message);
    }

    public static PCEncounterDataException createStatOutOfBounds(String name, String statName) {
        return new PCEncounterDataException(
            String.format(
                "%s's %s must be between %d and %d!",
                name,
                statName,
                Constant.getStatMin(statName),
                Constant.getStatMax(statName)
            )
        );
    }

    public static PCEncounterDataException invalidStatName(String name) {
        return new PCEncounterDataException(String.format("%s is not the name of a stat", name));
    }

}
