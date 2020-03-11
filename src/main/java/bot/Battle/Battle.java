package bot.Battle;

import bot.Capitalizer;
import bot.CustomException;
import bot.Explorer.Explorer;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public abstract class Battle implements BattleInterface
{
    final static String PROCESS_NAME = "battle";

    protected BattleLogger                      logger;
    private   ExplorerRoster<CombatExplorer>    explorerRoster;
    private   InitiativeTrackerInterface        initiative;
    private   InitiativeTrackerFactoryInterface initiativeFactory;
    private   BattlePhaseManager                phaseManager;

    /**
     * Constructor.
     *
     * @param logger            Logger
     * @param initiativeFactory Initiative factory
     * @param phaseManager      Phase manager
     */
    protected Battle(
        @NotNull BattleLogger logger,
        @NotNull InitiativeTrackerFactoryInterface initiativeFactory,
        @NotNull BattlePhaseManager phaseManager
    )
    {
        this.explorerRoster = new ExplorerRoster<>();
        this.initiativeFactory = initiativeFactory;
        this.logger = logger;
        this.phaseManager = phaseManager;

        this.initiative = initiativeFactory.createNull();

        logger.logCreateBattle();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void attackAction(@NotNull Player player, @NotNull String targetName) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();
        if (!phaseManager.isAttackPhase()) {
            throw EncounterException.createWrongPhase("attack", BattlePhase.ATTACK_PHASE);
        }

        CombatExplorer currentExplorer = getCurrentExplorer(player);

        AttackActionResult result = doAttack(currentExplorer, targetName);

        logger.logAction(result);
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void endBattle(@NotNull Player player)
    {
        preEndPhase(player);

        BattlePhaseChange result = phaseManager.startEndPhase();

        clearInitiative();
        notifyListenerOfPhaseChange(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public @NotNull ArrayList<CombatExplorer> getAllExplorers()
    {
        return explorerRoster.getAllExplorers();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public @NotNull String getProcessName()
    {
        return PROCESS_NAME;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isNull()
    {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isOver()
    {
        return phaseManager.isFinalPhase();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public boolean isProcess(@NotNull String processName)
    {
        return processName.equals(PROCESS_NAME);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public boolean isExclusiveProcess()
    {
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    final public boolean isRemovable()
    {
        return isOver();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void join(@NotNull Explorer explorer, @Nullable String nickname) throws BattlePhaseException
    {
        phaseManager.assertJoinablePhase(explorer, isAlwaysJoinable());

        String capitalNickname = nickname != null
                                 ? Capitalizer.nameCaseIfLowerCase(nickname)
                                 : null;
        CombatExplorer newExplorer = createExplorer(explorer, capitalNickname);

        explorerRoster.addExplorer(newExplorer);

        postJoin(newExplorer);

        JoinActionResult result = new JoinActionResult(newExplorer, explorerRoster.isFull());
        logger.logAction(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void kick(@NotNull String name)
    {
        phaseManager.assertNotFinalPhase();

        CombatExplorer target = explorerRoster.kick(name);
        initiative.remove(target);
        target.useAllActions();
        logger.logKickedPlayer(target.getOwner());
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void leave(@NotNull Player player)
    {
        phaseManager.assertNotFinalPhase();

        CombatExplorer explorer = explorerRoster.markAsLeft(player);
        initiative.remove(explorer);
        logger.logLeftBattle(explorer.getName());
        handleEndOfAction();
    }

    /**
     * Log summary
     */
    public void logSummary()
    {
        logger.logSummary(getAllCreatures());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void rejoin(@NotNull Player player)
    {
        phaseManager.assertNotFinalPhase();

        CombatExplorer explorer = explorerRoster.markAsReturned(player);
        if (phaseManager.isInitiativePhase()) {
            addToInitiative(explorer);
        }

        logger.logRejoinBattle(explorer.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPartySize(int amount) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

        explorerRoster.setMaxPartySize(amount);
        logger.logSetPartySize(amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTier(@NotNull Tier tier) throws CustomException
    {
        if (!phaseManager.isCreatePhase()) {
            throw new CustomException("Tier must be set before the encounter has started");
        }

        explorerRoster.setTier(tier);
        logger.logSetTier(tier);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startAttackPhase() throws BattlePhaseException
    {
        phaseManager.assertAttackPhaseMayStart();

        preAttackPhase();

        BattlePhaseChange result = phaseManager.startAttackPhase();

        for (CombatExplorer explorer : explorerRoster.getAllExplorers()) {
            explorer.resetActions(false);
        }

        restartInitiative();
        notifyListenerOfPhaseChange(result);
    }

    /**
     * {@inheritDoc}
     *
     * @param channel
     */
    @Override
    public void startJoinPhase(@NotNull MessageChannel channel) throws BattlePhaseException
    {
        phaseManager.assertJoinPhaseMayStart();

        preJoinPhase();

        BattlePhaseChange result = phaseManager.startJoinPhase();

        logger.setChannel(channel);
        clearInitiative();
        notifyListenerOfPhaseChange(result);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useAllCurrentExplorerActions()
    {
        phaseManager.assertInitiativePhase();

        CombatExplorer explorer = getCurrentExplorer();
        explorer.useAllActions();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useCurrentExplorerAction()
    {
        phaseManager.assertInitiativePhase();

        CombatExplorer explorer = getCurrentExplorer();
        explorer.useAction();
        handleEndOfAction();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void useItemAction(@NotNull Player player)
    {
        if (!phaseManager.isAttackPhase()) {
            return;
        }

        CombatExplorer explorer = getCurrentExplorer(player);

        logger.pingDmItemUsed(player);
        explorer.useAction();
        handleEndOfAction();
    }

    /**
     * Add explorer to initiative
     *
     * @param explorer Explorer
     */
    final protected void addToInitiative(@NotNull CombatExplorer explorer)
    {
        initiative.add(explorer);
    }

    /**
     * Assert that players have joined
     *
     * @throws CustomException If no players have joined
     */
    final protected void assertPlayersHaveJoined() throws CustomException
    {
        if (haveNoPlayersJoined()) {
            throw new CustomException("Wait, we can't start yet! No players have joined!");
        }
    }

    /**
     * Clear initiative
     */
    final protected void clearInitiative()
    {
        initiative = initiativeFactory.createNull();
    }

    /**
     * Create explorer
     *
     * @param explorer Explorer
     * @param nickname Explorer nickname
     *
     * @return CombatExplorer
     */
    abstract protected CombatExplorer createExplorer(@NotNull Explorer explorer, @Nullable String nickname);

    /**
     * Do attack target
     *
     * @param explorer   The attacker
     * @param targetName The name of the target
     *
     * @return AttackActionResult
     */
    protected abstract @NotNull AttackActionResult doAttack(
        @NotNull CombatExplorer explorer,
        @NotNull String targetName
    );

    /**
     * Get all active explorers
     *
     * @return ArrayList
     */
    final protected @NotNull ArrayList<CombatExplorer> getActiveExplorers()
    {
        return explorerRoster.getActiveExplorers();
    }

    /**
     * Get creature
     *
     * @param name Name of creature to find
     *
     * @return EncounterCreatureInterface
     *
     * @throws CustomException If creature with name not found
     */
    final protected @NotNull CombatCreature getCreature(@NotNull String name) throws CustomException
    {
        for (CombatCreature creature : getAllCreatures()) {
            if (creature.isName(name)) {
                return creature;
            }
        }

        throw new CustomException(String.format(
            "I couldn't find any creatures with the name '%s' in this battle...",
            name
        ));
    }

    /**
     * Get current explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getCurrentExplorer()
    {
        return initiative.getCurrentExplorer();
    }

    /**
     * Get current explorer
     *
     * @param player The player that should be the current explorer's owner
     *
     * @return CombatExplorer
     *
     * @throws CustomException If the given player is not the owner of the current explorer
     */
    final protected @NotNull CombatExplorer getCurrentExplorer(@NotNull Player player) throws CustomException
    {
        CombatExplorer explorer = initiative.getCurrentExplorer();
        if (!explorer.isOwner(player)) {
            throw new CustomException(String.format("%s it is not your turn, please be patient.", player.mention()));
        }

        return explorer;
    }

    /**
     * Get encountered explorer by name
     *
     * @param name Name of explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getExplorer(@NotNull String name)
    {
        return explorerRoster.getExplorer(name);
    }

    /**
     * Get encountered explorer by player
     *
     * @param player Owner of explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getExplorer(@NotNull Player player)
    {
        return explorerRoster.getExplorer(player);
    }

    /**
     * Get next explorer
     *
     * @return CombatExplorer
     */
    final protected @NotNull CombatExplorer getNextExplorer()
    {
        return initiative.getNextExplorer();
    }

    /**
     * Handle the end of an action
     */
    abstract protected void handleEndOfAction();

    /**
     * Has at least one active explorer
     *
     * @return boolean
     */
    final protected boolean hasAtLeastOneActiveExplorer()
    {
        return explorerRoster.hasAtLeastOneActiveExplorer();
    }

    /**
     * Has multiple active explorer
     *
     * @return boolean
     */
    final protected boolean hasMultipleActiveExplorers()
    {
        return explorerRoster.hasMultipleActiveExplorers();
    }

    /**
     * Can players join this encounter at any time
     */
    abstract protected boolean isAlwaysJoinable();

    /**
     * Is a player with the given player id in the battle
     */
    final protected boolean isPlayerInBattle(@NotNull Player player)
    {
        return explorerRoster.contains(player);
    }

    /**
     * Log action
     *
     * @param result Action result
     */
    final protected void logAction(@NotNull ActionResultInterface result)
    {
        logger.logAction(result);
    }

    /**
     * Notify listener of phase change
     *
     * @param phaseChange Phase change result
     */
    final protected void notifyListenerOfPhaseChange(BattlePhaseChange phaseChange)
    {
        BattleContext context = new BattleContext(
            getBattleStyle(),
            isAlwaysJoinable(),
            explorerRoster.getMaxPartySize(),
            explorerRoster.getCurrentPartySize()
        );

        BattlePhaseChangeResult result = new BattlePhaseChangeResult(
            phaseChange,
            context,
            getAllCreatures(),
            explorerRoster.getTier()
        );

        logger.logPhaseChange(result);

        if (phaseManager.isInitiativePhase()) {
            CombatExplorer explorer = getCurrentExplorer();
            if (!explorer.isActive() || !explorer.hasActions()) {
                explorer = getNextExplorer();
            }
            logger.pingPlayerTurn(explorer);
        }
    }

    /**
     * Handle any additional post join related processes
     *
     * @param explorer Explorer that has just joined
     */
    abstract protected void postJoin(@NotNull CombatExplorer explorer);

    /**
     * Handle any additional pre attack phase related processes
     */
    abstract protected void preAttackPhase();

    /**
     * Handle any additional pre end phase forced related processes
     */
    abstract protected void preEndPhase(@NotNull Player player);

    /**
     * Handle any additional pre join phase related processes
     */
    abstract protected void preJoinPhase();

    /**
     * Remove encountered explorer from battle
     *
     * @param explorer Explorer to remove
     *
     * @throws BattlePhaseException If encounter is over
     */
    final protected void removeExplorer(CombatExplorer explorer) throws BattlePhaseException
    {
        phaseManager.assertNotFinalPhase();

        explorerRoster.remove(explorer);
        initiative.remove(explorer);
        explorer.useAllActions();
        logger.logRemovedExplorer(explorer.getName());
        handleEndOfAction();
    }

    /**
     * Restart initiative
     */
    final protected void restartInitiative()
    {
        initiative = initiativeFactory.create(explorerRoster.getActiveExplorers());
    }

    /**
     * Start end phase
     */
    final protected void startEndPhase()
    {
        BattlePhaseChange result = phaseManager.startEndPhase();

        clearInitiative();
        notifyListenerOfPhaseChange(result);
    }

    /**
     * Have players joined
     *
     * @return boolean
     */
    private boolean haveNoPlayersJoined()
    {
        return phaseManager.isJoinPhase() && !explorerRoster.hasAtLeastOneActiveExplorer();
    }
}