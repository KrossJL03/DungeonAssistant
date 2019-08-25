package bot;

import org.jetbrains.annotations.NotNull;

public class Message implements MessageInterface
{
    private StringBuilder stringBuilder;

    /**
     * Message constructor.
     */
    public @NotNull Message()
    {
        this.stringBuilder = new StringBuilder();
    }

    /**
     * Add line
     *
     * @param line Line
     */
    public void add(@NotNull String line)
    {
        stringBuilder.append(line);
        stringBuilder.append(NEWLINE);
    }

    /**
     * Add break
     */
    public void addBreak()
    {
        stringBuilder.append(NEWLINE);
    }

    /**
     * Add new line
     */
    void addLine()
    {
        stringBuilder.append(LINE);
        stringBuilder.append(NEWLINE);
    }

    /**
     * End code block
     */
    public void endCodeBlock()
    {
        stringBuilder.append(CODE_BRACKET);
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

    /**
     * Start code block
     *
     * @param codeStyle Code style
     */
    public void startCodeBlock(@NotNull String codeStyle)
    {
        stringBuilder.append(CODE_BRACKET);
        stringBuilder.append(codeStyle);
        stringBuilder.append(NEWLINE);
    }
}
