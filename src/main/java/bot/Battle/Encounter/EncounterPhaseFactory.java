package bot.Battle.Encounter;

import bot.Battle.BattlePhaseFactoryInterface;
import org.jetbrains.annotations.NotNull;

class EncounterPhaseFactory implements BattlePhaseFactoryInterface
{
    /**
     * {@inheritDoc}
     */
    public @NotNull EncounterPhase createAttackPhase()
    {
        return new EncounterPhase(EncounterPhase.ATTACK_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull EncounterPhase createCreatePhase()
    {
        return new EncounterPhase(EncounterPhase.CREATE_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull EncounterPhase createEndPhase()
    {
        return new EncounterPhase(EncounterPhase.END_PHASE);
    }

    /**
     * {@inheritDoc}
     */
    public @NotNull EncounterPhase createJoinPhase()
    {
        return new EncounterPhase(EncounterPhase.JOIN_PHASE);
    }

    /**
     * Phase constructor (DODGE_PHASE)
     *
     * @return EncounterPhase
     */
    @NotNull EncounterPhase createDodgePhase()
    {
        return new EncounterPhase(EncounterPhase.DODGE_PHASE);
    }

    /**
     * Phase constructor (LOOT_PHASE)
     *
     * @return EncounterPhase
     */
    @NotNull EncounterPhase createLootPhase()
    {
        return new EncounterPhase(EncounterPhase.LOOT_PHASE);
    }

    /**
     * Phase constructor (RP_PHASE)
     *
     * @return EncounterPhase
     */
    @NotNull EncounterPhase createRpPhase()
    {
        return new EncounterPhase(EncounterPhase.RP_PHASE);
    }
}
