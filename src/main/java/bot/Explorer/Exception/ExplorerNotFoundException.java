package bot.Explorer.Exception;

import bot.CommandListener;
import bot.CustomExceptionInterface;
import bot.Explorer.Explorer;
import bot.MyProperties;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ExplorerNotFoundException extends RuntimeException implements CustomExceptionInterface
{
    private ExplorerNotFoundException(String message)
    {
        super(message);
    }

    /**
     * Factory method for case "not found in database"
     *
     * @param name Name of explorer not found
     *
     * @return ExplorerNotFoundException
     */
    public static @NotNull ExplorerNotFoundException createNotInDatabase(@NotNull String name)
    {
        return new ExplorerNotFoundException(
            String.format(
                "I couldn't find '%s'... maybe try adding them with the `%screate` command later?",
                name,
                MyProperties.COMMAND_PREFIX
            )
        );
    }

    /**
     * Factory method for case "not found in database with alternatives"
     *
     * @param name      Name of explorer not found
     * @param explorers List of alternative explorers
     *
     * @return ExplorerNotFoundException
     */
    public static @NotNull ExplorerNotFoundException createNotInDatabaseWithAlternatives(
        @NotNull String name,
        @NotNull ArrayList<Explorer> explorers
    )
    {
        return new ExplorerNotFoundException(String.format(
            "I don't think I know %s, but I do know these characters: %s", name, explorers
        ));
    }
}
