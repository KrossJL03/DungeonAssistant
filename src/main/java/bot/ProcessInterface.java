package bot;

public interface ProcessInterface
{
    /**
     * Is this process locking updates to the database
     *
     * @return boolean
     */
    boolean isLockingDatabase();
}
