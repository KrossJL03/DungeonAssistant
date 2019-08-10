package bot.Encounter.DungeonMasterChecker;

import bot.Encounter.DungeonMasterCheckerInterface;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DungeonMasterChecker implements DungeonMasterCheckerInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Role getDungeonMaster(MessageReceivedEvent event)
    {
        List<Role> roles = event.getGuild().getRolesByName("Dungeon Master", false);
        if (!roles.isEmpty()) {
            return roles.get(0);
        }
        throw DungeonMasterCheckerException.createDmNotFound();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isDungeonMaster(@NotNull MessageReceivedEvent event)
    {
        Member member = event.getMember();
        if (member == null) {
            return false;
        }

        return member.getRoles().indexOf(getDungeonMaster(event)) > -1;
    }
}
