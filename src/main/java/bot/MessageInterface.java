package bot;

import org.jetbrains.annotations.NotNull;

public interface MessageInterface
{
    String LINE    = "------------------------------------";
    String NEWLINE = System.getProperty("line.separator");

    String CODE_BRACKET      = "```";
    String DOUBLE_ARROW      = "»";
    String EMPTY_HEALTH_ICON = "─";
    String FULL_HEALTH_ICON  = "█";

    /**
     * Get as string
     *
     * @return String
     */
    @NotNull String getAsString();
}
