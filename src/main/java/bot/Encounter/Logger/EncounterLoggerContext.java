package bot.Encounter.Logger;

import bot.Exception.ContextChannelNotSetException;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;

public class EncounterLoggerContext {

    private MessageChannel channel;
    private Mention        dungeonMasterMention;

    /**
     * EncounterLoggerContext constructor
     */
    public EncounterLoggerContext() {}

    MessageChannel getChannel() {
        if (channel == null) {
            throw new ContextChannelNotSetException();
        }
        return channel;
    }

    /**
     * Get dungeon master mention
     *
     * @return Mention
     */
    Mention getDungeonMasterMention() {
        return this.dungeonMasterMention;
    }

    /**
     * Set channel
     *
     * @param channel Message channel
     */
    public void setChannel(MessageChannel channel) {
        this.channel = channel;
    }

    /**
     * Set dungeon master mention
     *
     * @param dungeonMaster Mention
     */
    public void setDungeonMasterMention(Role dungeonMaster) {
        this.dungeonMasterMention = new Mention(dungeonMaster);
    }
}
