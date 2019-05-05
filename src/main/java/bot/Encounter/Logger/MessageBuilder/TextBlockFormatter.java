package bot.Encounter.Logger.MessageBuilder;

import org.jetbrains.annotations.NotNull;

public class TextBlockFormatter {

    /**
     * Make text bold
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeBold(String text) {
        return String.format("**%s**", text);
    }

    /**
     * Make text bold and italic
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeBoldItalic(String text) {
        return String.format("***%s***", text);
    }

    /**
     * Make text italic
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeItalic(String text) {
        return String.format("*%s*", text);
    }
}
