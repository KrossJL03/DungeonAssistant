package bot.Battle.Encounter;

import bot.Battle.CombatActionResultInterface;
import bot.Battle.DeathSaveRoll;
import bot.Battle.Slayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

class DodgeActionResult implements CombatActionResultInterface
{
    private DeathSaveRoll          deathSaveRoll;
    private ArrayList<DodgeResult> dodgeResults;
    private boolean                isForcedFail;
    private int                    targetCurrentHp;
    private int                    targetDodgeDie;
    private int                    targetMaxHp;
    private String                 targetName;
    private Slayer                 targetSlayer;

    /**
     * Constructor.
     *
     * @param target        Target
     * @param dodgeResults  Dodge results
     * @param isForcedFail  Was this action a forced fail
     * @param deathSaveRoll Death save roll
     */
    DodgeActionResult(
        @NotNull EncounteredExplorer target,
        @NotNull ArrayList<DodgeResult> dodgeResults,
        boolean isForcedFail,
        @Nullable DeathSaveRoll deathSaveRoll
    )
    {
        this.deathSaveRoll = deathSaveRoll;
        this.dodgeResults = dodgeResults;
        this.isForcedFail = isForcedFail;
        this.targetCurrentHp = target.getCurrentHP();
        this.targetDodgeDie = target.getDodgeDice();
        this.targetMaxHp = target.getMaxHP();
        this.targetName = target.getName();
        this.targetSlayer = target.getSlayer();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        int totalDamageDealt = 0;
        for (DodgeResult result : dodgeResults) {
            totalDamageDealt += result.getDamageDealt();
        }

        return totalDamageDealt;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageResisted()
    {
        int totalDamageResisted = 0;
        for (DodgeResult result : dodgeResults) {
            totalDamageResisted += result.getDamageResisted();
        }

        return totalDamageResisted;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathMinSaveRoll()
    {
        return rolledDeathSave() ? deathSaveRoll.getMinSaveRoll() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveDie()
    {
        return rolledDeathSave() ? deathSaveRoll.getDie() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDeathSaveRoll()
    {
        return rolledDeathSave() ? deathSaveRoll.getRoll() : -1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetCurrentHp()
    {
        return targetCurrentHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getTargetMaxHp()
    {
        return targetMaxHp;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getTargetName()
    {
        return targetName;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull Slayer getTargetSlayer()
    {
        return targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetExplorer()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isTargetSlain()
    {
        return targetCurrentHp < 1;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean rolledDeathSave()
    {
        return deathSaveRoll != null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean survivedDeathSave()
    {
        return rolledDeathSave() && deathSaveRoll.survived();
    }

    /**
     * Get number of attacks
     *
     * @return int
     */
    int getAttackCount()
    {
        return dodgeResults.size();
    }

    /**
     * Get individual dodge results
     *
     * @return ArrayList<DodgeResult>
     */
    @NotNull ArrayList<DodgeResult> getDodgeResults()
    {
        return dodgeResults;
    }

    /**
     * Get minimum roll needed for a successful dodge
     *
     * @return int
     */
    int getMinSuccessDodgeRoll()
    {
        return DodgeRoll.DODGE_ROLL_PASS;
    }

    /**
     * Get target dodge die
     *
     * @return int
     */
    int getTargetDodgeDie()
    {
        return targetDodgeDie;
    }

    /**
     * Was this action a forced fail
     *
     * @return boolean
     */
    boolean isForceFail()
    {
        return isForcedFail;
    }
}
