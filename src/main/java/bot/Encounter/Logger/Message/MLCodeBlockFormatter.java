package bot.Encounter.Logger.Message;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

public class MLCodeBlockFormatter implements CodeBlockFormatter
{
    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getStyle()
    {
        return "ml";
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
        return String.format("\"%s\"", text);
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
        return String.format("(* %s *)", text);
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
        return String.format("\'%s\'", text).replace(" ", "\' \'");
    }

    /**
     * Format text to be yellow
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeYellow(@NotNull String text)
    {
        return WordUtils.capitalizeFully(text);
    }
}
