package bot.Encounter;

import bot.Exception.ContextChannelNotSetException;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

public class EncounterLoggerContext {

    private MessageChannel channel;
    private Role           dungeonMaster;

    public EncounterLoggerContext() {
    }

    MessageChannel getChannel() {
        if (channel == null) {
            throw new ContextChannelNotSetException();
        }
        return channel;
    }

    String getDungeonMasterMention() {
        return dungeonMaster.getAsMention();
    }

    void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    void setDungeonMaster(Role dungeonMaster) {
        this.dungeonMaster = dungeonMaster;
    }
}
