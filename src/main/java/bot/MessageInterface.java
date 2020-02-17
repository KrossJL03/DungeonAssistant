package bot;

import org.jetbrains.annotations.NotNull;

public interface MessageInterface
{
    String CODE_BRACKET               = "```";
    String DOUBLE_ARROW               = "»";
    String EMOJI_CROSSED_SWORDS       = ":crossed_swords:";
    String EMOJI_MEDAL                = ":medal:";
    String EMOJI_SMALL_ORANGE_DIAMOND = ":small_orange_diamond:";
    String EMOJI_WARNING              = ":warning:";
    String EMOJI_WING_LEFT            = ":wingleft:";
    String EMPTY_HEALTH_ICON          = "─";
    String FULL_HEALTH_ICON           = "█";
    String LINE                       = "------------------------------------";
    String NEWLINE                    = System.getProperty("line.separator");

    /**
     * Get as string
     *
     * @return String
     */
    @NotNull String getAsString();
}
