package bot.Exception;

public class CharacterSlainException extends RuntimeException implements EncounterException {
    public CharacterSlainException(String characterName, String slayerName) {
        super(String.format("%s was slain by %s", characterName, slayerName));
    }
}
