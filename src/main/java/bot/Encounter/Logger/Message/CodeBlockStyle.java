package bot.Encounter.Logger.Message;

import org.jetbrains.annotations.NotNull;

class CodeBlockStyle {

    private static String STYLE_DIFF   = "diff";
    private static String STYLE_MD     = "md";
    private static String STYLE_ML     = "ml";
    private static String STYLE_PROLOG = "prolog";

    private String style;

    /**
     * CodeBlockStyle constructor
     *
     * @param style Style type
     */
    private CodeBlockStyle(@NotNull String style) {
        this.style = style;
    }

    /**
     * ML CodeBlockStyle factory method
     */
    static CodeBlockStyle buildMlStyle() {
        return new CodeBlockStyle(STYLE_ML);
    }

    /**
     * Get style
     *
     * @return String
     */
    @NotNull
    String getStyle() {
        return this.style;
    }
}
