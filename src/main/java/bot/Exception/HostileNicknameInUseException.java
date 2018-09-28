package bot.Exception;

public class HostileNicknameInUseException extends RuntimeException implements EncounterException {
    public HostileNicknameInUseException(String nickname) {
        super(String.format("There's already a hostile named %s in this battle", nickname));
    }
}
