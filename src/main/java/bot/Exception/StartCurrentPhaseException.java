package bot.Exception;

public class StartCurrentPhaseException extends RuntimeException {

    private String phase;

    public StartCurrentPhaseException(String phase) {
        this.phase = phase;
    }

    public String getPhase() {
        return phase;
    }
}
