package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class LootException extends RuntimeException implements CustomExceptionInterface {
    private LootException(String message) {
        super(message);
    }

    public static LootException createReroll(String mention) {
        return new LootException(String.format("%s, your loot has already been rolled", mention));
    }

    public static LootException createNoKills(String mention) {
        return new LootException(
            String.format(
                "Sorry %s, looks like you weren't present for any hostile kills. You don't get any loot",
                mention
            )
        );
    }
}
