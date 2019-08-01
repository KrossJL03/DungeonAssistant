package bot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ProcessManager
{
    private ArrayList<ProcessInterface> processes;

    /**
     * ProcessManager constructor.
     */
    public ProcessManager()
    {
        this.processes = new ArrayList<>();
    }

    /**
     * Add process
     *
     * @param process Process to add
     */
    void addProcess(@NotNull ProcessInterface process)
    {
        processes.add(process);
    }

    /**
     * Is the database currently locked
     *
     * @return boolean
     */
    boolean isDatabaseLocked()
    {
        for (ProcessInterface process : processes) {
            if (process.isLockingDatabase()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Remove process
     *
     * @param process Process to add
     */
    void removeProcess(@NotNull ProcessInterface process)
    {
        processes.remove(process);
    }
}
