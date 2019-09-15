package bot.Encounter;

import org.apache.commons.text.WordUtils;
import org.jetbrains.annotations.NotNull;

/**
 * Helper class to capitalize strings
 */
public class Capitalizer
{
    /**
     * Capitalize a string if it contains no capitals
     *
     * @param text String to capitalize
     *
     * @return String
     */
    public static @NotNull String nameCaseIfLowerCase(@NotNull String text)
    {
        return text.toLowerCase().equals(text)
               ? WordUtils.capitalizeFully(text)
               : text;
    }
}
