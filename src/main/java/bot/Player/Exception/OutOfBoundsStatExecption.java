package bot.Player.Exception;

import bot.CustomExceptionInterface;

public class OutOfBoundsStatExecption extends RuntimeException implements CustomExceptionInterface {
    public OutOfBoundsStatExecption(String message) {
        super(message);
    }
}
