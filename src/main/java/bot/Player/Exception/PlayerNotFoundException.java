package bot.Player.Exception;

import bot.CustomExceptionInterface;

public class PlayerNotFoundException extends RuntimeException implements CustomExceptionInterface {

    private PlayerNotFoundException(String message) {
        super(message);
    }

    public static PlayerNotFoundException createNotInDatabase() {
        return new PlayerNotFoundException(
            "Um... I'm sorry, do I know you? Try introducing yourself by saying `$hello` first."
        );
    }

}
