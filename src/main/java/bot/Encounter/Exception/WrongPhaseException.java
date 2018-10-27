package bot.Encounter.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Encounter.EncounterContext;

public class WrongPhaseException extends RuntimeException implements CustomExceptionInterface {

    private WrongPhaseException(String message) {
        super(message);
    }

    public static WrongPhaseException createForAttack() {
        return new WrongPhaseException(
            String.format(
                "You can only `%sattack` during the %s turn",
                CommandListener.COMMAND_KEY,
                EncounterContext.ATTACK_PHASE
            )
        );
    }

    public static WrongPhaseException createForDodge() {
        return new WrongPhaseException(
            String.format(
                "You can only `%sdodge` during the %s turn",
                CommandListener.COMMAND_KEY,
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
                "You can only `%sloot` during the %s turn",
                CommandListener.COMMAND_KEY,
                EncounterContext.LOOT_PHASE
            )
        );
    }

    public static WrongPhaseException createForProtect() {
        return new WrongPhaseException(
            String.format(
                "You can only `%sprotect` during the %s turn",
                CommandListener.COMMAND_KEY,
                EncounterContext.DODGE_PHASE
            )
        );
    }

}
