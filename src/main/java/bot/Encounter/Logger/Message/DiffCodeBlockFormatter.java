package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

public class DiffCodeBlockFormatter implements CodeBlockFormatter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "diff";
    }

    /**
     * Format text to be gray
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeGray(@NotNull String text)
    {
        return String.format("--- %s", text);
    }

    /**
     * Format text to be green
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeGreen(@NotNull String text)
    {
        return String.format("+ %s", text);
    }

    /**
     * Format text to be red
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeRed(@NotNull String text)
    {
        return String.format("- %s", text);
    }
}