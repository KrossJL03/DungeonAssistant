package bot;

import org.jetbrains.annotations.NotNull;

public class MarkdownCodeFormatter implements CodeFormatterInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "md";
    }

    /**
     * Format text to be blue
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull String makeBlue(@NotNull String text)
    {
        return String.format("<%s>", text);
    }

    /**
     * Format text to be cyan and orange
     *
     * @param cyanText   Text to format cyan
     * @param orangeText Text to format orange
     *
     * @return String
     */
    @NotNull String makeCyanAndOrange(@NotNull String cyanText, @NotNull String orangeText)
    {
        return String.format("[%s](%s)", cyanText, orangeText);
    }

    /**
     * Format text to be grey
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull String makeGrey(@NotNull String text)
    {
        return String.format("> %s", text);
    }

    /**
     * Format text to be yellow
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull String makeYellow(@NotNull String text)
    {
        return String.format("< %s >", text);
    }
}