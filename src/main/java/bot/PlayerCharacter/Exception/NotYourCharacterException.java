package bot.PlayerCharacter.Exception;

public class NotYourCharacterException extends RuntimeException {
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
