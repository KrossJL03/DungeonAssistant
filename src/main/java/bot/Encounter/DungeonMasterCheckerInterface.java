package bot.Encounter;

import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

public interface DungeonMasterCheckerInterface
{
    /**
     * Get dungeon master from event
     *
     * @param event Event
     *
     * @return Role
     *
     * @throws EncounterException If dungeon master role is not found
     */
    @NotNull Role getDungeonMaster(MessageReceivedEvent event) throws EncounterException;

    /**
     * Is the author of the message a dungeon master
     *
     * @param event Event
     *
     * @return boolean
     */
    boolean isDungeonMaster(@NotNull MessageReceivedEvent event);
}
