package bot.Encounter;

import bot.Encounter.Logger.EncounterLogger;
import bot.Encounter.Logger.Mention;
import bot.Explorer.Explorer;
import bot.Hostile.HostileManager;
import bot.Player.Player;
import net.dv8tion.jda.core.entities.MessageChannel;
import net.dv8tion.jda.core.entities.Role;
import org.jetbrains.annotations.NotNull;

public class EncounterManager
{
    private Encounter       encounter;
    private EncounterLogger logger;

    /**
     * EncounterManager constructor
     *
     * @param encounter Encounter
     * @param logger    Encounter logger
     */
    public @NotNull EncounterManager(
        @NotNull Encounter encounter,
        @NotNull EncounterLogger logger
    )
    {
        this.encounter = encounter;
        this.logger = logger;
    }

    /**
     * Add hostile to encounter
     *
     * @param speciesName Hostile species name
     * @param nickname    Hostile name
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void addHostile(@NotNull String speciesName, @NotNull String nickname) throws EncounterPhaseException
    {
        encounter.addHostile(HostileManager.getHostile(speciesName), nickname);
    }

    /**
     * Attack action
     *
     * @param player      Player
     * @param hostileName Hostile name
     *
     * @throws EncounterPhaseException If not attack phase
     */
    public void attackAction(@NotNull Player player, @NotNull String hostileName) throws EncounterPhaseException
    {
        encounter.attackAction(player, hostileName);
    }

    /**
     * Create encounter
     *
     * @param channel       Encounter channel
     * @param dungeonMaster Dungeon master role
     */
    public void createEncounter(@NotNull MessageChannel channel, @NotNull Role dungeonMaster)
    {
        logger.setChannel(channel);
        logger.setDmMention(Mention.createForRole(dungeonMaster.getId()));
        encounter = new Encounter();
        encounter.setListener(new ActionListener(logger));
        logger.logCreateEncounter();
    }

    /**
     * Dodge action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    public void dodgeAction(@NotNull Player player) throws EncounterPhaseException
    {
        encounter.dodgeAction(player);
    }

    /**
     * Dodge pass action
     */
    public void dodgePassAction()
    {
        encounter.dodgePassAction();
    }

    /**
     * Request dodge pass action
     *
     * @param player Player
     */
    public void dodgePassActionHelp(@NotNull Player player)
    {
        logger.pingDmDodgePass(player);
    }

    /**
     * End current player action
     */
    public void endCurrentPlayersAction()
    {
        encounter.useCurrentExplorerAction();
    }

    /**
     * End current player turn
     */
    public void endCurrentPlayersTurn()
    {
        encounter.useAllCurrentExplorerActions();
    }

    /**
     * End encounter
     */
    public void endEncounter()
    {
        encounter.startEndPhaseForced();
    }

    /**
     * Heal encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void heal(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        encounter.heal(name, hitpoints);
    }

    /**
     * Heal all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllExplorers(int hitpoints)
    {
        encounter.healAllExplorers(hitpoints);
    }

    /**
     * Heal all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to heal
     */
    public void healAllHostiles(int hitpoints)
    {
        encounter.healAllHostiles(hitpoints);
    }

    /**
     * Hurt encountered creature with given name by given amount of hitpoints
     *
     * @param name      Encountered creature name
     * @param hitpoints Hitpoints
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void hurt(@NotNull String name, int hitpoints) throws EncounterPhaseException
    {
        encounter.hurt(name, hitpoints);
    }

    /**
     * Hurt all active explorers by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllExplorers(int hitpoints)
    {
        encounter.hurtAllExplorers(hitpoints);
    }

    /**
     * Hurt all active hostiles by a given amount
     *
     * @param hitpoints Hitpoints to hurt
     */
    public void hurtAllHostiles(int hitpoints)
    {
        encounter.hurtAllHostiles(hitpoints);
    }

    /**
     * Join encounter
     *
     * @param explorer Explorer
     *
     * @throws EncounterPhaseException If encounter is over or has not started
     */
    public void joinEncounter(@NotNull Explorer explorer) throws EncounterPhaseException
    {
        encounter.join(explorer);
    }

    /**
     * Leave encounter
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void leaveEncounter(@NotNull Player player) throws EncounterPhaseException
    {
        encounter.leave(player);
    }

    /**
     * Loot action
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If not loot phase
     */
    public void lootAction(@NotNull Player player) throws EncounterPhaseException
    {
        encounter.lootAction(player);
    }

    /**
     * Modify stat
     *
     * @param name     Name of creature to modify stat for
     * @param statName Name of stat to modify
     * @param statMod  Mod to apply to stat
     */
    public void modifyStat(@NotNull String name, @NotNull String statName, int statMod)
    {
        encounter.modifyStat(name, statName, statMod);
    }

    /**
     * Protect action
     *
     * @param player Owner of current explorer
     * @param name   Name of explorer to protect
     *
     * @throws EncounterPhaseException If not dodge phase
     */
    public void protectAction(@NotNull Player player, @NotNull String name) throws EncounterPhaseException
    {
        encounter.protectAction(player, name);
    }

    /**
     * Rejoin player
     *
     * @param player Player
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void rejoinEncounter(@NotNull Player player) throws EncounterPhaseException
    {
        encounter.rejoin(player);
    }

    /**
     * Remove hostile with given name
     *
     * @param name Name of hostile to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void removeHostile(@NotNull String name) throws EncounterPhaseException
    {
        encounter.removeHostile(name);
    }

    /**
     * Remove explorer with given name
     *
     * @param name Name of explorer to remove
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void removeExplorer(@NotNull String name) throws EncounterPhaseException
    {
        encounter.removeExplorer(name);
    }

    /**
     * Set max player count
     *
     * @param maxPlayerCount Max player count
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void setMaxPlayerCount(int maxPlayerCount) throws EncounterPhaseException
    {
        encounter.setMaxPlayerCount(maxPlayerCount);
    }

    /**
     * Skip current player turn
     *
     * @throws EncounterPhaseException If encounter is over
     */
    public void skipPlayerTurn() throws EncounterPhaseException
    {
        encounter.skipCurrentPlayerTurn();
    }

    /**
     * Start attack phase
     */
    public void startAttackPhase()
    {
        encounter.startAttackPhase();
    }

    /**
     * Start dodge phase
     */
    public void startDodgePhase()
    {
        encounter.startDodgePhase();
    }

    /**
     * Start encounters
     *
     * @param channel     Encounter channel
     * @param mentionRole Mention role
     */
    public void startEncounter(@NotNull MessageChannel channel, @NotNull Role mentionRole)
    {
        logger.setChannel(channel);
        logger.setEveryoneMention(Mention.createForRole(mentionRole.getId()));
        encounter.setListener(new ActionListener(logger));
        encounter.startJoinPhase();
    }

    /**
     * View encounter summary
     */
    public void viewEncounterSummary()
    {
        logger.logSummary(encounter.getAllExplorers(), encounter.getAllHostiles());
    }

    /**
     * Request item use
     *
     * @param player Player
     */
    public void pingDmItemUsed(@NotNull Player player)
    {
        logger.pingDmItemUsed(player);
    }

}
