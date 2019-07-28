package bot;

import org.jetbrains.annotations.NotNull;

public class CommandParameter
{
    private String  name;
    private boolean required;

    /**
     * CommandParameter constructor
     *
     * @param name     Name
     * @param required Is the parameter required
     */
    public CommandParameter(@NotNull String name, boolean required)
    {
        this.required = required;
        this.name = name;
    }

    /**
     * Get name
     *
     * @return String
     */
    public @NotNull String getName()
    {
        return name;
    }

    /**
     * Get name
     *
     * @return String
     */
    @NotNull String getFormatted()
    {
        return required ? String.format("[%s]", name) : String.format("(%s)", name);
    }

    /**
     * Is parameter required
     *
     * @return boolean
     */
    boolean isRequired()
    {
        return required;
    }
}
