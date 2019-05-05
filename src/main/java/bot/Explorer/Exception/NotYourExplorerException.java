package bot.Explorer.Exception;

import bot.CustomExceptionInterface;

public class NotYourExplorerException extends RuntimeException implements CustomExceptionInterface {
    private NotYourExplorerException(String message) {
        super(message);
    }

    public static NotYourExplorerException createForNameTaken(String explorerName, String ownerName) {
        return new NotYourExplorerException(
            String.format(
                "%s already exists and belongs too %s. Perhaps consider using a nickname for your character?",
                explorerName,
                ownerName
            )
        );
    }
}
