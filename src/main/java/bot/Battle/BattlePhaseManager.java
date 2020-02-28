package bot.Battle;

import bot.CustomException;
import bot.Explorer.Explorer;
import org.jetbrains.annotations.NotNull;

public class BattlePhaseManager
{
    protected BattlePhaseFactoryInterface factory;
    protected BattlePhase                 phase;

    /**
     * Constructor.
     *
     * @param factory Phase factory
     */
    protected BattlePhaseManager(@NotNull BattlePhaseFactoryInterface factory)
    {
        this.factory = factory;

        this.phase = factory.createCreatePhase();
    }

    /**
     * Assert that the current phase is an initiative phase
     */
    public void assertInitiativePhase()
    {
        assertNotFinalPhase();
        if (!phase.isInitiativePhase()) {
            throw BattlePhaseException.createNotInitiativePhase();
        }
    }

    /**
     * Assert the current phase is not a final phase
     *
     * @throws CustomException If the battle is in it's final phase
     */
    public void assertNotFinalPhase() throws CustomException
    {
        if (phase.isFinalPhase()) {
            throw BattlePhaseException.createFinalPhase();
        }
    }

    /**
     * Is the current phase the attack phase
     *
     * @return boolean
     */
    public boolean isAttackPhase()
    {
        return phase.isAttackPhase();
    }

    /**
     * Is final phase
     *
     * @return boolean
     */
    public boolean isFinalPhase()
    {
        return phase.isFinalPhase();
    }

    /**
     * Is the current phase an initiative phase
     *
     * @return boolean
     */
    public boolean isInitiativePhase()
    {
        return phase.isInitiativePhase();
    }

    /**
     * Is the current phase the join phase
     *
     * @return boolean
     */
    public boolean isJoinPhase()
    {
        return phase.isJoinPhase();
    }

    /**
     * Assert attack phase may start
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the current phase is the attack phase
     */
    void assertAttackPhaseMayStart() throws CustomException
    {
        assertInProgressPhase();
        if (phase.isAttackPhase()) {
            throw BattlePhaseException.createStartCurrentPhase(phase.getPhaseName());
        }
    }

    /**
     * Assert that the phase may be changed to join phase
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the current phase is the join phase
     */
    void assertJoinPhaseMayStart() throws CustomException
    {
        if (phase.isJoinPhase()) {
            throw BattlePhaseException.createStartCurrentPhase(phase.getPhaseName());
        } else if (!phase.isCreatePhase()) {
            throw new CustomException("Um... we're kind of in the middle of a battle already.");
        }
    }

    /**
     * Assert the current phase is a joinable phase
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the current phase is not the join phase and the battle is not always joinable
     */
    void assertJoinablePhase(@NotNull Explorer explorer, boolean isAlwaysJoinable) throws CustomException
    {
        assertInProgressPhase();
        if (!isAlwaysJoinable && !phase.isJoinPhase()) {
            throw BattlePhaseException.createNotJoinPhase(Mention.createForPlayer(explorer.getOwner().getUserId()));
        }
    }

    /**
     * Is the current phase the create phase
     *
     * @return boolean
     */
    boolean isCreatePhase()
    {
        return phase.isCreatePhase();
    }

    /**
     * Start attack phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the current phase is the attack phase
     */
    @NotNull BattlePhaseChange startAttackPhase() throws CustomException
    {
        assertAttackPhaseMayStart();

        return changePhase(factory.createAttackPhase());
    }

    /**
     * Start end phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in a final phase
     */
    @NotNull BattlePhaseChange startEndPhase()
    {
        assertNotFinalPhase();

        return changePhase(factory.createEndPhase());
    }

    /**
     * Start join phase
     *
     * @return BattlePhaseChange
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     *                         If the current phase is the join phase
     */
    @NotNull BattlePhaseChange startJoinPhase()
    {
        assertJoinPhaseMayStart();

        return changePhase(factory.createJoinPhase());
    }

    /**
     * Assert that the current phase is not a first phase or a final phase
     *
     * @throws CustomException If the battle is in a final phase
     *                         If the battle is still in the create phase
     */
    protected void assertInProgressPhase() throws CustomException
    {
        assertNotFinalPhase();
        if (phase.isCreatePhase()) {
            throw BattlePhaseException.createNotStarted();
        }
    }

    /**
     * Change current phase
     *
     * @param newPhase New phase
     *
     * @return BattlePhaseChange
     */
    @NotNull
    protected BattlePhaseChange changePhase(@NotNull BattlePhase newPhase)
    {
        BattlePhase previousPhase = phase;
        phase = newPhase;

        return new BattlePhaseChange(phase, previousPhase);
    }
}
