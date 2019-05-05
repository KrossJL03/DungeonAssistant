package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class ExplorerSlainException extends RuntimeException implements CustomExceptionInterface {

    private ExplorerSlainException(String message) {
        super(message);
    }

    public static ExplorerSlainException createFailedToAddKill(
        String characterName,
        String slayerName,
        String hostileName
    ) {
        return new ExplorerSlainException(
            String.format(
                "%s was knocked out by %s and was not present for %s's death",
                characterName,
                slayerName,
                hostileName
            )
        );
    }

    public static ExplorerSlainException createFailedToHeal(String characterName, String slayerName) {
        return new ExplorerSlainException(
            String.format(
                "%s was knocked out by %s and can only be healed by a reviving item",
                characterName,
                slayerName
            )
        );
    }

}
