package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class PlayerCharacterSlainException extends RuntimeException implements CustomExceptionInterface {

    private PlayerCharacterSlainException(String message) {
        super(message);
    }

    public static PlayerCharacterSlainException createFailedToAddKill(
        String characterName,
        String slayerName,
        String hostileName
    ) {
        return new PlayerCharacterSlainException(
            String.format(
                "%s was knocked out by %s and was not present for %s's death",
                characterName,
                slayerName,
                hostileName
            )
        );
    }

    public static PlayerCharacterSlainException createFailedToHeal(String characterName, String slayerName) {
        return new PlayerCharacterSlainException(
            String.format(
                "%s was knocked out by %s and can only be healed by a reviving item",
                characterName,
                slayerName
            )
        );
    }

}
