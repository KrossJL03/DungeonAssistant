package bot.Encounter.EncounteredCreature;

import bot.Encounter.GuardActionResultInterface;
import bot.Encounter.GuardResultInterface;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class GuardActionResult implements GuardActionResultInterface
{
    private DeathSaveRoll                   deathSaveRoll;
    private ArrayList<GuardResultInterface> guardResults;
    private int                             targetCurrentHp;
    private int                             targetDefense;
    private int                             targetMaxHp;
    private String                          targetName;
    private Slayer                          targetSlayer;

    /**
     * GuardActionResult constructor
     *
     * @param targetName      Target name
     * @param guardResults    Guard results
     * @param targetDefense   Target defense stat
     * @param targetCurrentHp Target current hp
     * @param targetMaxHp     Target max hp
     * @param targetSlayer    Target slayer
     */
    @NotNull GuardActionResult(
        @NotNull String targetName,
        @NotNull ArrayList<GuardResultInterface> guardResults,
        int targetDefense,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer,
        @Nullable DeathSaveRoll deathSaveRoll
    )
    {
        this.deathSaveRoll = deathSaveRoll;
        this.guardResults = guardResults;
        this.targetCurrentHp = targetCurrentHp;
        this.targetDefense = targetDefense;
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
        return guardResults.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getDamageDealt()
    {
        int totalDamageDealt = 0;
        for (GuardResultInterface result : guardResults) {
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
        for (GuardResultInterface result : guardResults) {
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
    public @NotNull ArrayList<GuardResultInterface> getGuardResults()
    {
        return guardResults;
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
    public int getTargetDefense()
    {
        return targetDefense;
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
}
