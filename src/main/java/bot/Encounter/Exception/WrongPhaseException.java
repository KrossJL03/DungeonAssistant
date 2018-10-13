package bot.Encounter.Exception;

import bot.CustomExceptionInterface;
import bot.Encounter.EncounterContext;
import bot.Exception.EncounterException;

public class WrongPhaseException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    private WrongPhaseException(String message) {
        super(message);
    }

    public static WrongPhaseException createForAttack() {
        return new WrongPhaseException(
            String.format(
                "You can only `$attack` during the %s turn",
                EncounterContext.ATTACK_PHASE
            )
        );
    }

    public static WrongPhaseException createForDodge() {
        return new WrongPhaseException(
            String.format(
                "You can only `$dodge` during the %s turn",
                EncounterContext.DODGE_PHASE
            )
        );
    }

    public static WrongPhaseException createForItem(String itemName, String usablePhase) {
        return new WrongPhaseException(
            String.format(
                "%s can only be used during the %s turn",
                itemName,
                usablePhase
            )
        );
    }

    public static WrongPhaseException createForLoot() {
        return new WrongPhaseException(
            String.format(
                "You can only `$loot` during the %s turn",
                EncounterContext.LOOT_PHASE
            )
        );
    }

    public static WrongPhaseException createForProtect() {
        return new WrongPhaseException(
            String.format(
                "You can only `$protect` during the %s turn",
                EncounterContext.DODGE_PHASE
            )
        );
    }
}
