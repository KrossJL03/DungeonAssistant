package bot;

import org.jetbrains.annotations.NotNull;

public class XlCodeFormatter implements CodeFormatterInterface
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "xl";
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
}
