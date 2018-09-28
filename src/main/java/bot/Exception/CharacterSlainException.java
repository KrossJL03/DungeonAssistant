package bot.Exception;

public class CharacterSlainException extends RuntimeException {

    private String characterName;
    private String slayerName;

    public CharacterSlainException(String characterName, String slayerName) {
        this.characterName = characterName;
        this.slayerName = slayerName;
    }

    public String getCharacterName() {
        return characterName;
    }

    public String getSlayerName() {
        return slayerName;
    }
}
