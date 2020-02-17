package bot.Battle.HostileEncounter;

import bot.Battle.BattlePhaseChange;
import bot.Battle.BattlePhaseException;
import bot.Battle.BattlePhaseManager;
import bot.CustomException;
import org.jetbrains.annotations.NotNull;

class EncounterPhaseManager extends BattlePhaseManager
{
    private EncounterPhaseFactory factory;
    private EncounterPhase        phase;

    /**
     * Constructor.
     */
    EncounterPhaseManager()
    {
        super(new EncounterPhaseFactory());
    }

    /**
     * Assert that the dodge phase may start
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the battle is currently in the dodge phase
     */
    void assertDodgePhaseMayStart() throws CustomException
    {
        assertInProgressPhase();
        if (phase.isDodgePhase()) {
            throw BattlePhaseException.createStartCurrentPhase(phase.getPhaseName());
        }
    }

    /**
     * Is the current phase the dodge phase
     *
     * @return boolean
     */
    boolean isDodgePhase()
    {
        return phase.isDodgePhase();
    }

    /**
     * Is the current phase the loot phase
     *
     * @return boolean
     */
    boolean isLootPhase()
    {
        return phase.isLootPhase();
    }

    /**
     * Start dodge phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the battle is currently in the dodge phase
     */
    @NotNull BattlePhaseChange startDodgePhase() throws CustomException
    {
        assertDodgePhaseMayStart();

        return changePhase(factory.createDodgePhase());
    }

    /**
     * Start loot phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in it's final phase
     */
    @NotNull BattlePhaseChange startLootPhase() throws CustomException
    {
        assertNotFinalPhase();

        return changePhase(factory.createLootPhase());
    }

    /**
     * Start RP phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in it's final phase
     */
    @NotNull BattlePhaseChange startRpPhase() throws CustomException
    {
        assertNotFinalPhase();

        return changePhase(factory.createRpPhase());
    }
}
