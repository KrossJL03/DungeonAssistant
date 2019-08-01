package bot;

import org.jetbrains.annotations.NotNull;

public class IniCodeFormatter implements CodeFormatter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "ini";
    }

    /**
     * Format text to be blue
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeBlue(@NotNull String text)
    {
        return String.format("[%s]", text);
    }

    /**
     * Format text to be grey
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeGrey(@NotNull String text)
    {
        return String.format("; %s", text);
    }
}