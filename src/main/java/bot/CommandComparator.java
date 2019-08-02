package bot;

import org.jetbrains.annotations.NotNull;

import java.util.Comparator;

class CommandComparator implements Comparator<CommandInterface>
{
    /**
     * {@inheritDoc}
     */
    @Override
    public int compare(@NotNull CommandInterface command1, @NotNull CommandInterface command2)
    {
        return command1.getCommandName().compareTo(command2.getCommandName());
    }
}