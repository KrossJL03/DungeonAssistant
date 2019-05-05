package bot.Encounter.Logger.MessageBuilder;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

class ActionMessageFormatter implements CodeBlockFormatter {

    private CodeBlockStyle style;

    /**
     * ActionMessageFormatter constructor
     */
    ActionMessageFormatter() {
        this.style = CodeBlockStyle.buildMlStyle();
    }

    /**
     * Get style
     *
     * @return CodeBlockStyle
     */
    @NotNull
    CodeBlockStyle getStyle() {
        return this.style;
    }

    /**
     * Format text to be cyan
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeCyan(@NotNull String text) {
        return String.format("\"%s\"", text);
    }

    /**
     * Format text to be gray
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeGray(@NotNull String text) {
        return String.format("(* %s *)", text);
    }

    /**
     * Format text to be red
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeRed(@NotNull String text) {
        return String.format("\'%s\'", text).replace(" ", "\' \'");
    }

    /**
     * Format text to be yellow
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeYellow(@NotNull String text) {
        return WordUtils.capitalizeFully(text);
    }
}
