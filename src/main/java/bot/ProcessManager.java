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
        if (process.isExclusiveProcess()) {
            String           processName    = process.getProcessName();
            ProcessInterface currentProcess = getProcess(processName);
            if (currentProcess.isRemovable()) {
                throw new CustomException(String.format("Another %s is currently in progress.", processName));
            }
            processes.remove(process);
        }

        processes.add(process);
    }

    /**
     * Get process by name
     *
     * @param processName Process name
     *
     * @return ProcessInterface
     */
    @NotNull ProcessInterface getProcess(@NotNull String processName)
    {
        for (ProcessInterface process : processes) {
            if (process.isProcess(processName)) {
                return process;
            }
        }

        throw new CustomException(String.format("There is no active %s currently", processName));
    }

    /**
     * Does a process of this type currently exist
     *
     * @param processName Process name
     *
     * @return boolean
     */
    boolean hasProcess(@NotNull String processName)
    {
        for (ProcessInterface process : processes) {
            if (process.isProcess(processName)) {
                return true;
            }
        }

        return false;
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
}
