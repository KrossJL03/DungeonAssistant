package bot.Encounter.Logger.Message.Summary;

import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class SummaryMessage implements MessageInterface
{
    private StringBuilder stringBuilder;

    /**
     * SummaryMessage constructor
     */
    @NotNull SummaryMessage() {
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Add line
     *
     * @param line Line
     */
    void add(@NotNull String line) {
        stringBuilder.append(line);
        stringBuilder.append(NEWLINE);
    }

    /**
     * Add break
     */
    void addBreak() {
        stringBuilder.append(NEWLINE);
    }

    /**
     * Add new line
     */
    void addLine() {
        stringBuilder.append(LINE);
        stringBuilder.append(NEWLINE);
    }

    /**
     * End code block
     */
    void endCodeBlock() {
        stringBuilder.append(CODE_BRACKET);
        stringBuilder.append(NEWLINE);
    }

    /**
     * Start code block
     *
     * @param codeStyle Code style
     */
    void startCodeBlock(@NotNull String codeStyle) {
        stringBuilder.append(CODE_BRACKET);
        stringBuilder.append(codeStyle);
        stringBuilder.append(NEWLINE);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getAsString()
    {
        return stringBuilder.toString();
    }
}
