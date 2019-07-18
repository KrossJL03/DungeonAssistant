package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

public class TextBlockFormatter
{
    /**
     * Make text bold
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeBold(String text)
    {
        return String.format("**%s**", text);
    }

    /**
     * Make text bold
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeBold(int text)
    {
        return String.format("**%d**", text);
    }

    /**
     * Make text bold and italic
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeBoldItalic(String text)
    {
        return String.format("***%s***", text);
    }

    /**
     * Make text code
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeCode(String text)
    {
        return String.format("`%s`", text);
    }

    /**
     * Make text italic
     *
     * @param text Text to format
     *
     * @return String
     */
    public @NotNull String makeItalic(String text)
    {
        return String.format("*%s*", text);
    }
}
