package bot.Battle.Logger.Message.PhaseChange;

import bot.CustomException;

class PhaseChangeMessageBuilderException extends CustomException
{
    /**
     * PhaseChangeMessageBuilderException constructor
     *
     * @param message Error message
     */
    private PhaseChangeMessageBuilderException(String message)
    {
        super(message);
    }

    /**
     * Factory method for phase not configured
     *
     * @param previousPhaseName Name of previous phase
     * @param nextPhaseName     Name of next phase
     *
     * @return PhaseChangeMessageBuilderException
     */
    static PhaseChangeMessageBuilderException createPhaseNotConfigured(String previousPhaseName, String nextPhaseName)
    {
        return new PhaseChangeMessageBuilderException(String.format(
            "So... we're going from %s to %s but I forgot how to make it look pretty.",
            previousPhaseName,
            nextPhaseName
        ));
    }
}
