package bot.Exception;

public class NoHostileInEncounterException extends RuntimeException {

    private String name;

    public NoHostileInEncounterException(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
