package bot.Exception;

public class NoHostileFoundException extends RuntimeException {

    private String speciesName;

    public NoHostileFoundException(String speciesName) {
        this.speciesName = speciesName;
    }

    public String getSpeciesName() {
        return speciesName;
    }
}
