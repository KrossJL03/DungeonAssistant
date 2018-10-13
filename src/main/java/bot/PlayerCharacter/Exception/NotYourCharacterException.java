package bot.PlayerCharacter.Exception;

import bot.CustomExceptionInterface;

public class NotYourCharacterException extends RuntimeException implements CustomExceptionInterface {
    private NotYourCharacterException(String message) {
        super(message);
    }

    public static NotYourCharacterException createForNameTaken(String playerCharacterName, String ownerName) {
        return new NotYourCharacterException(
            String.format(
                "%s already exists and belongs too %s. Perhaps consider using a nickname for your character?",
                playerCharacterName,
                ownerName
            )
        );
    }
}
