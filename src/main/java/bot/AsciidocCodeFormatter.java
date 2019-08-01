package bot;

import org.jetbrains.annotations.NotNull;

public class AsciidocCodeFormatter implements CodeFormatterInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "asciidoc";
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
        return String.format("= %s =", text);
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
        return String.format("[%s]", text);
    }
}