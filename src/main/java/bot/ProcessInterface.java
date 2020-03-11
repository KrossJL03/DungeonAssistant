package bot;

import org.jetbrains.annotations.NotNull;

public interface ProcessInterface
{
    /**
     * Get process name
     *
     * @return String
     */
    @NotNull String getProcessName();

    /**
     * Is this process locking updates to the database
     *
     * @return boolean
     */
    boolean isLockingDatabase();

    /**
     * Is process
     *
     * @param processName Process name to compare
     *
     * @return boolean
     */
    boolean isProcess(@NotNull String processName);

    /**
     * Can multiple of the same process run at the same time
     *
     * @return boolean
     */
    boolean isExclusiveProcess();

    /**
     * Can this process be removed
     *
     * @return boolean
     */
    boolean isRemovable();
}
