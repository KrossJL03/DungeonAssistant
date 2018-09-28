package bot.Exception;

import net.dv8tion.jda.core.entities.User;

public class MultiplePlayerCharactersException extends RuntimeException implements EncounterException {
    public MultiplePlayerCharactersException(User player, String name) {
        super(
            String.format(
                "%s, you have already joined this encounter with %s. " +
                    "If you'd like to switch please talk to the DungeonMaster",
                player.getAsMention(),
                name
            )
        );
    }
}
