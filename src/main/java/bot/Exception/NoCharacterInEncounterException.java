package bot.Exception;

public class NoCharacterInEncounterException extends RuntimeException {

    private String name;

    public NoCharacterInEncounterException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}