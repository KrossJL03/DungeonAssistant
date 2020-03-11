package bot.Battle.Encounter;

import bot.Battle.CombatCreature;
import bot.Battle.CombatExplorer;
import bot.Battle.DeathSaveRoll;
import bot.Battle.Slayer;
import bot.Explorer.Explorer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class EncounteredExplorer extends CombatExplorer
{
    private static int BONUS_FINAL_BLOW = 300;

    private ArrayList<EncounteredHostile> kills;
    private LootActionResult              loot;
    private ArrayList<EncounteredHostile> opponents;
    private int                           protectActions;

    /**
     * Constructor.
     *
     * @param explorer Explorer
     * @param nickname Optional nickname
     */
    EncounteredExplorer(@NotNull Explorer explorer, @Nullable String nickname)
    {
        super(explorer, nickname);

        this.kills = new ArrayList<>();
        this.opponents = new ArrayList<>();
        this.protectActions = 1;

        this.loot = new LootActionResult(getName(), getOwner());
    }

    /**
     * Dodge encountered hostile attacks
     *
     * @param hostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    public @NotNull DodgeActionResult dodge(@NotNull ArrayList<EncounteredHostile> hostiles)
    {
        assertHasActions();

        ArrayList<DodgeResult> dodgeResults = new ArrayList<>();
        for (EncounteredHostile hostile : hostiles) {
            int       damageResisted    = 0;
            int       hostileDamageRoll = hostile.getAttackRoll();
            DodgeRoll dodgeRoll         = rollToDodge();

            if (dodgeRoll.isFail() && !isSlain()) {
                damageResisted = hostileDamageRoll - takeDamage(hostile, hostileDamageRoll);
            }

            DodgeResult result = new DodgeResult(
                hostile.getName(),
                dodgeRoll,
                hostileDamageRoll,
                damageResisted
            );

            dodgeResults.add(result);
        }

        DeathSaveRoll deathSaveRoll = rollDeathSaveIfApplicable();

        useAction();

        return new DodgeActionResult(
            this,
            dodgeResults,
            false,
            deathSaveRoll
        );
    }

    /**
     * Get loot
     *
     * @return LootActionResult
     */
    public @NotNull LootActionResult getLoot()
    {
        return loot;
    }

    /**
     * Add hostile as opponent when they qualify for loot
     *
     * @param opponent Opponent that was fought against
     *
     * @throws EncounteredExplorerException If explorer is not active
     */
    void addOpponent(@NotNull EncounteredHostile opponent) throws EncounteredExplorerException
    {
        if (!isActive()) {
            throw EncounteredExplorerException.createNotPresentForOpponent(getName(), opponent.getName());
        } else if (!kills.contains(opponent) && !opponents.contains(opponent)) {
            opponents.add(opponent);
        }
    }

    /**
     * Automatically fail to dodge encountered hostile attacks
     *
     * @param hostiles Encountered hostiles to dodge
     *
     * @return DodgeActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull DodgeActionResult failToDodge(@NotNull ArrayList<EncounteredHostile> hostiles)
    {
        assertHasActions();

        ArrayList<DodgeResult> dodgeResults = new ArrayList<>();
        for (EncounteredHostile hostile : hostiles) {
            int damageResisted    = 0;
            int hostileDamageRoll = hostile.getAttackRoll();

            if (!isSlain()) {
                damageResisted = hostileDamageRoll - takeDamage(hostile, hostileDamageRoll);
            }

            DodgeResult result = new DodgeResult(
                hostile.getName(),
                new DodgeRoll(0),
                hostileDamageRoll,
                damageResisted
            );

            dodgeResults.add(result);
        }

        DeathSaveRoll deathSaveRoll = rollDeathSaveIfApplicable();

        useAction();

        return new DodgeActionResult(
            this, dodgeResults,
            true,
            deathSaveRoll
        );
    }

    /**
     * If the creature is listed as an active opponent then save them as a kill for loot
     *
     * @param opponent Opponent to add to kills
     *
     * @throws EncounteredExplorerException If opponent is not slain
     */
    void finalizeKill(@NotNull EncounteredHostile opponent) throws EncounteredExplorerException
    {
        if (!opponent.isSlain()) {
            throw EncounteredExplorerException.createKillIsNotSlain(opponent.getName(), getName());
        }

        if (isActive() && opponents.contains(opponent)) {
            kills.add(opponent);
            opponents.remove(opponent);
        }
    }

    /**
     * Give this explorer a protect action
     */
    void giveProtectAction()
    {
        ++protectActions;
    }

    /**
     * Guard against encountered hostile attacks
     *
     * @param hostiles Encountered hostiles to guard against
     *
     * @return GuardActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     */
    @NotNull GuardActionResult guard(@NotNull ArrayList<EncounteredHostile> hostiles)
        throws EncounteredExplorerException
    {
        assertHasActions();

        ArrayList<GuardResult> guardResults = new ArrayList<>();
        for (EncounteredHostile hostile : hostiles) {
            int hostileDamageRoll = hostile.getAttackRoll();
            int damageResisted    = 0;

            if (!isSlain()) {
                damageResisted = hostileDamageRoll - takeGuardedDamage(hostile, hostileDamageRoll);
            }

            GuardResult result = new GuardResult(
                hostile.getName(),
                hostileDamageRoll,
                damageResisted
            );

            guardResults.add(result);
        }

        DeathSaveRoll deathSaveRoll = rollDeathSaveIfApplicable();

        useAction();

        return new GuardActionResult(
            getName(),
            guardResults,
            getGuardEndurance(),
            getCurrentHP(),
            getMaxHP(),
            getSlayer(),
            deathSaveRoll
        );
    }

    /**
     * Roll loot for kills
     */
    void lootKills()
    {
        ArrayList<CombatCreature> finalBlows = new ArrayList<>();
        ArrayList<LootRoll>       lootRolls  = new ArrayList<>();
        for (EncounteredHostile kill : kills) {
            lootRolls.addAll(kill.loot());
            if (kill.wasSlainBy(this)) {
                finalBlows.add(kill);
            }
        }

        loot = new LootActionResult(
            getName(),
            getOwner(),
            lootRolls,
            finalBlows,
            kills.size(),
            finalBlows.size() * BONUS_FINAL_BLOW
        );
    }

    /**
     * Protect recipient from encountered hostile attacks
     *
     * @param recipient Encountered explorer being protected
     * @param hostiles  Encountered hostiles to protect against
     *
     * @return ProtectActionResult
     *
     * @throws EncounteredExplorerException If explorer has no actions
     *                                      If explorer does not have a protect action available
     *                                      If explorer attempts to protect themselves
     *                                      If recipient is slain
     *                                      If recipient has no actions
     */
    @NotNull ProtectActionResult protect(
        @NotNull CombatExplorer recipient,
        @NotNull ArrayList<EncounteredHostile> hostiles
    ) throws EncounteredExplorerException
    {
        assertHasActions();
        if (!hasProtectActions()) {
            throw EncounteredExplorerException.createProtectAlreadyUsed();
        } else if (equals(recipient)) {
            throw EncounteredExplorerException.createProtectYourself();
        } else if (recipient.isSlain()) {
            throw EncounteredExplorerException.createProtectSlainExplorer(recipient.getName());
        } else if (!recipient.hasActions()) {
            throw EncounteredExplorerException.createProtectActionlessExplorer(recipient.getName());
        }

        int damageDealt    = 0;
        int damageResisted = 0;

        for (EncounteredHostile hostile : hostiles) {
            int damage = isSlain()
                         ? hostile.getAttackRoll()
                         : takeDamage(hostile, hostile.getAttackRoll());
            damageDealt += damage;
            damageResisted += hostile.getAttackRoll() - damage;
        }

        DeathSaveRoll deathSaveRoll = rollDeathSaveIfApplicable();

        useAction();
        useProtectAction();
        recipient.useAction();

        return new ProtectActionResult(
            getName(),
            recipient.getName(),
            recipient.getOwner().getUserId(),
            damageDealt,
            damageResisted,
            getCurrentHP(),
            getMaxHP(),
            getSlayer(),
            deathSaveRoll
        );
    }

    /**
     * Remove opponent that is no longer eligible for loot
     *
     * @param opponent Opponent that was fought against
     */
    void removeOpponent(@NotNull EncounteredHostile opponent) throws EncounteredExplorerException
    {
        opponents.remove(opponent);
    }

    /**
     * Get endurance
     *
     * @return int
     */
    private int getGuardEndurance()
    {
        return (int) Math.floor(getDefense() * .75);
    }

    /**
     * Does this explorer have any protect actions available
     *
     * @return boolean
     */
    private boolean hasProtectActions()
    {
        return protectActions > 0;
    }

    /**
     * Roll to dodge
     *
     * @return DodgeRoll
     */
    private @NotNull DodgeRoll rollToDodge()
    {
        return new DodgeRoll(roll(getDodgeDice()));
    }

    /**
     * Take guarded damage
     *
     * @return int Damage taken
     */
    private int takeGuardedDamage(@NotNull CombatCreature attacker, int damage)
    {
        damage = damage - getGuardEndurance();
        damage = Math.max(1, damage);

        hurt(damage);

        if (isSlain()) {
            addSlayer(new Slayer(attacker.getName()));
        }

        return damage;
    }

    /**
     * Use protect
     */
    private void useProtectAction()
    {
        if (!hasProtectActions()) {
            throw EncounteredExplorerException.createProtectAlreadyUsed();
        }

        protectActions--;
    }
}
