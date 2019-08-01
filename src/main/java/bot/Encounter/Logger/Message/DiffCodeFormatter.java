package bot.Encounter.Logger.Message;

import bot.CodeFormatterInterface;
import org.jetbrains.annotations.NotNull;

public class DiffCodeFormatter implements CodeFormatterInterface
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
     * Format text to be grey
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeGrey(@NotNull String text)
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