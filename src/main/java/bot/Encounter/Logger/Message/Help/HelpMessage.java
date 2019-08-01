package bot.Encounter.Logger.Message.Help;

import bot.Encounter.Logger.Message.MessageInterface;
import org.jetbrains.annotations.NotNull;

public class HelpMessage implements MessageInterface
{
    private StringBuilder stringBuilder;

    /**
     * HelpMessage constructor.
     */
    @NotNull HelpMessage() {
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
