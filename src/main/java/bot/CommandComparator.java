package bot;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

class CommandComparator implements Comparator<Command>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull Command command1, @NotNull Command command2)
    {
        return command1.getCommandName().compareTo(command2.getCommandName());
    }
}