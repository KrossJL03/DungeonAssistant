package bot.Repository;

import bot.CustomExceptionInterface;

public class RepositoryException extends RuntimeException implements CustomExceptionInterface {

    private RepositoryException(String message) {
        super(message);
    }

    public static RepositoryException createFailedToCloseConnection() {
        return new RepositoryException("Uh oh, I couldn't close my connection...");
    }

    public static RepositoryException createFailedToUpdate() {
        return new RepositoryException("Uh, could you say that again? I'm having trouble saving it");
    }


}
