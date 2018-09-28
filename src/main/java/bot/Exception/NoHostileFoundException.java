package bot.Exception;

public class NoHostileFoundException extends RuntimeException implements EncounterException {
    public NoHostileFoundException(String speciesName) {
        super(
            String.format(
                "I'm not familiar with %s, could you describe them for me using the `$createHostile` command?",
                speciesName
            )
        );
    }
}
