package bot.Encounter.EncounteredCreature;

import bot.Encounter.DodgeActionResultInterface;
import bot.Encounter.DodgeResultInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class DodgeActionResult implements DodgeActionResultInterface
{
    private DeathSaveRoll                   deathSaveRoll;
    private ArrayList<DodgeResultInterface> dodgeResults;
    private boolean                         isForcedFail;
    private int                             targetCurrentHp;
    private int                             targetDodgeDie;
    private int                             targetMaxHp;
    private String                          targetName;
    private Slayer                          targetSlayer;

    /**
     * DodgeActionResult constructor
     *
     * @param targetName      Target name
     * @param dodgeResults    Dodge results
     * @param targetDodgeDie  Target dodge die
     * @param targetCurrentHp Target current hp
     * @param targetMaxHp     Target max hp
     * @param targetSlayer    Target slayer
     * @param isForcedFail    Was this action a forced fail
     * @param deathSaveRoll   Death save roll
     */
    @NotNull DodgeActionResult(
        @NotNull String targetName,
        @NotNull ArrayList<DodgeResultInterface> dodgeResults,
        int targetDodgeDie,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer,
        boolean isForcedFail,
        @Nullable DeathSaveRoll deathSaveRoll
    )
    {
        this.deathSaveRoll = deathSaveRoll;
        this.dodgeResults = dodgeResults;
        this.isForcedFail = isForcedFail;
        this.targetCurrentHp = targetCurrentHp;
        this.targetDodgeDie = targetDodgeDie;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.targetSlayer = targetSlayer;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getAttackCount()
    {
        return dodgeResults.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        int totalDamageDealt = 0;
        for (DodgeResultInterface result : dodgeResults) {
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
        for (DodgeResultInterface result : dodgeResults) {
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
    public @NotNull ArrayList<DodgeResultInterface> getDodgeResults()
    {
        return dodgeResults;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getMinSucessDodgeRoll()
    {
        return DodgeRoll.DODGE_ROLL_PASS;
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
    public int getTargetDodgeDie()
    {
        return targetDodgeDie;
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
    public boolean isForceFail()
    {
        return isForcedFail;
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
}
