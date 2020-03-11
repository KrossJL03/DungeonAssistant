package bot;

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

    /**
     * Capitalize the first character in a string
     *
     * @param text String to capitalize
     *
     * @return String
     */
    static @NotNull String capitalizeFirstCharacter(@NotNull String text)
    {
        return text.substring(0, 1).toUpperCase() + text.substring(1);
    }
}
