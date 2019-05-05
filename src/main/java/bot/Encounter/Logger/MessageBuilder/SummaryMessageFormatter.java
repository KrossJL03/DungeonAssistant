package bot.Encounter.Logger.MessageBuilder;

import org.jetbrains.annotations.NotNull;

class SummaryMessageFormatter implements CodeBlockFormatter {

    private CodeBlockStyle style;

    /**
     * ActionMessageFormatter constructor
     */
    SummaryMessageFormatter() {
        this.style = CodeBlockStyle.buildDiffStyle();
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
     * Format text to be gray
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeGray(@NotNull String text) {
        return String.format("--- %s", text);
    }

    /**
     * Format text to be green
     *
     * @param text Text to format
     *
     * @return String
     */
    @NotNull
    String makeGreen(@NotNull String text) {
        return String.format("+ %s", text);
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
        return String.format("- %s", text);
    }
}