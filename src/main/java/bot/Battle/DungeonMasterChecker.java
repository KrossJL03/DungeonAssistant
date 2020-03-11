package bot.Battle;

import bot.CustomException;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonMasterChecker
{
    /**
     * Get dungeon master from event
     *
     * @param event Event
     *
     * @return Role
     *
     * @throws CustomException If dungeon master role is not found
     */
    @NotNull Role getDungeonMaster(@NotNull MessageReceivedEvent event) throws CustomException
    {
        List<Role> roles = event.getGuild().getRolesByName("Dungeon Master", false);
        if (!roles.isEmpty()) {
            return roles.get(0);
        }

        throw new CustomException(
            "How can we play without a DungeonMaster? I don't see that role anywhere..."
        );
    }

    /**
     * Is the author of the message a dungeon master
     *
     * @param event Event
     *
     * @return boolean
     */
    boolean isDungeonMaster(@NotNull MessageReceivedEvent event)
    {
        Member member = event.getMember();
        if (member == null) {
            return false;
        }

        return member.getRoles().indexOf(getDungeonMaster(event)) > -1;
    }
}
