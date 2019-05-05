package bot.Encounter.Logger.MessageBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

class Message {

    private ArrayList<MessageBlockInterface> blocks;

    /**
     * MessageBuilder constructor
     *
     * @param messages Messages
     */
    Message(@NotNull ArrayList<MessageBlockInterface> messages) {
        this.blocks = messages;
    }

    /**
     * Get printout
     *
     * @return String
     */
    @NotNull
    String getPrintout() {
        StringBuilder output = new StringBuilder();
        for (MessageBlockInterface message : this.blocks) {
            output.append(message.getPrintout());
            output.append(MessageConstants.NEWLINE);
        }
        return output.toString();
    }

}
