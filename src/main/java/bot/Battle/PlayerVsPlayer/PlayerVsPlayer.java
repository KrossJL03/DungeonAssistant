package bot.Battle.PlayerVsPlayer;

import bot.Battle.AttackActionResult;
import bot.Battle.Battle;
import bot.Battle.CombatCreature;
import bot.Battle.CombatExplorer;
import bot.Battle.HealActionResult;
import bot.Battle.HostileEncounter.EncounteredExplorer;
import bot.Battle.HurtActionResult;
import bot.Battle.Mention;
import bot.CustomException;
import bot.Explorer.Explorer;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class PlayerVsPlayer extends Battle
{
    /**
     * Constructor.
     */
    public PlayerVsPlayer(@NotNull MessageChannel channel, @NotNull Mention dmMention)
    {
        super(new PvpLogger(channel, dmMention), new InitiativeCycleFactory(), new PvpPhaseManager());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull ArrayList<CombatCreature> getAllCreatures()
    {
        return new ArrayList<>(getAllExplorers());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public @NotNull String getBattleStyle()
    {
        return "Player VS Player";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(@NotNull String name)
    {
        CombatCreature creature = getCreature(name);
        if (creature instanceof EncounteredExplorer) {
            removeExplorer((EncounteredExplorer) creature);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void skipCurrentPlayerTurn()
    {
        phaseManager.assertInitiativePhase();

        CombatExplorer explorer = getCurrentExplorer();
        if (phaseManager.isAttackPhase()) {
            explorer.useAllActions();
            logger.logActionAttackSkipped(explorer.getName());
            handleEndOfAction();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected CombatExplorer createExplorer(@NotNull Explorer explorer, @Nullable String nickname)
    {
        return new CombatExplorer(explorer, nickname);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected @NotNull AttackActionResult doAttack(@NotNull CombatExplorer attacker, @NotNull String targetName)
    {
        CombatExplorer target = getExplorer(targetName);

        return attacker.attack(target);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void handleEndOfAction()
    {
        if (!phaseManager.isJoinPhase()) {
            if (!hasMultipleActiveExplorers()) {
                startEndPhase();
            } else if (phaseManager.isInitiativePhase()) {
                CombatExplorer currentExplorer = getCurrentExplorer();
                if (currentExplorer.isActive() && currentExplorer.hasActions()) {
                    logger.logActionsRemaining(currentExplorer.getName(), currentExplorer.getRemainingActions());
                } else {
                    logger.pingPlayerTurn(getNextExplorer());
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isAlwaysJoinable()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHeal(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postHurt(@NotNull CombatCreature target, @NotNull HurtActionResult result)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postJoin(@NotNull CombatExplorer explorer)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void postRevive(@NotNull CombatCreature target, @NotNull HealActionResult result)
    {
        // do nothing
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preAttackPhase() throws CustomException
    {
        if (!hasMultipleActiveExplorers()) {
            throw new CustomException(
                "You can't have a Player VS Player fight with just one player. That's not how this works."
            );
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void preJoinPhase()
    {
        // do nothing
    }
}
