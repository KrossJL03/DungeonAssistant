package bot.Encounter.Exception;

import bot.CustomExceptionInterface;

public class HostileNicknameInUseException extends RuntimeException implements CustomExceptionInterface {

    public HostileNicknameInUseException(String nickname) {
        super(String.format("There's already a hostile named %s in this battle", nickname));
    }

}
