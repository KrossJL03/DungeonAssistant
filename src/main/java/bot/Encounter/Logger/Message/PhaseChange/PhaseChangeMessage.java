package bot.Encounter.Logger.Message.PhaseChange;

import bot.MessageInterface;
import org.jetbrains.annotations.NotNull;

class PhaseChangeMessage implements MessageInterface
{
    private StringBuilder stringBuilder;

    /**
     * PhaseChangeMessage constructor
     */
    @NotNull PhaseChangeMessage()
    {
        this.stringBuilder = new StringBuilder();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getAsString()
    {
        return stringBuilder.toString();
    }

    /**
     * Add line
     *
     * @param line Line
     */
    void add(@NotNull String line)
    {
        stringBuilder.append(line);
        stringBuilder.append(NEWLINE);
    }

    /**
     * Add new line
     */
    void addNewLine()
    {
        stringBuilder.append(NEWLINE);
    }

    /**
     * End code block
     */
    void endCodeBlock()
    {
        stringBuilder.append(CODE_BRACKET);
        stringBuilder.append(NEWLINE);
    }

    /**
     * Start code block
     *
     * @param codeStyle Code style
     */
    void startCodeBlock(@NotNull String codeStyle)
    {
        stringBuilder.append(CODE_BRACKET);
        stringBuilder.append(codeStyle);
        stringBuilder.append(NEWLINE);
    }
}
