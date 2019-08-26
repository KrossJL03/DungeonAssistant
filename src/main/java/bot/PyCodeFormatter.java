package bot;

import org.jetbrains.annotations.NotNull;

public class PyCodeFormatter implements CodeFormatterInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "py";
    }

    /**
     * Format text to be cyan
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeCyan(@NotNull String text)
    {
        return String.format("'%s'", text);
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
        return String.format("#%s", text);
    }

    /**
     * Format text to be orange
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeOrange(@NotNull String text)
    {
        return String.format("@%s", text);
    }
}
