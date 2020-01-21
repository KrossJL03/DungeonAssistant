package bot.Battle.EncounteredCreature;

import bot.Battle.DodgeActionResultInterface;
import bot.Battle.DodgeResultInterface;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeActionResult implements DodgeActionResultInterface
{
    private ArrayList<DodgeResultInterface> dodgeResults;
    private Slayer                          targetSlayer;
    private String                          targetName;
    private int                             targetCurrentHp;
    private int                             targetDodgeDie;
    private int                             targetMaxHp;
    private boolean                         isForcedFail;

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
     */
    @NotNull DodgeActionResult(
        @NotNull String targetName,
        @NotNull ArrayList<DodgeResultInterface> dodgeResults,
        int targetDodgeDie,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer,
        boolean isForcedFail
    )
    {
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
    public int getTargetDodgeDie()
    {
        return targetDodgeDie;
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
}
