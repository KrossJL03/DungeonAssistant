package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class TextBlock implements MessageBlockInterface {

    private ArrayList<String> lines;

    /**
     * TextBlock constructor
     *
     * @param lines lines
     */
    TextBlock(@NotNull ArrayList<String> lines) {
        this.lines = lines;
    }

    /**
     * {@inheritDoc}
     */
    @NotNull
    public String getPrintout() {
        StringBuilder output = new StringBuilder();
        ArrayList<String> messageLines = this.lines;

        for (String line : messageLines) {
            if (!line.equals(MessageConstants.BREAK)) {
                output.append(line);
            }
            output.append(MessageConstants.NEWLINE);
        }
        return output.toString();
    }

}
