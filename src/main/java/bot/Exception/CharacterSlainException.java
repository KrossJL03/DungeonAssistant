package bot.Exception;

import bot.CustomExceptionInterface;

public class CharacterSlainException extends RuntimeException implements EncounterException, CustomExceptionInterface {
    private CharacterSlainException(String message) {
        super(message);
    }

    public static CharacterSlainException createFailedToAddKill(
        String characterName,
        String slayerName,
        String hostileName
    ) {
        return new CharacterSlainException(
            String.format(
                "%s was knocked out by %s and was not present for %s's death",
                characterName,
                slayerName,
                hostileName
            )
        );
    }

    public static CharacterSlainException createFailedToHeal(String characterName, String slayerName) {
        return new CharacterSlainException(
            String.format(
                "%s was knocked out by %s and can only be healed by a reviving item",
                characterName,
                slayerName
            )
        );
    }
}
