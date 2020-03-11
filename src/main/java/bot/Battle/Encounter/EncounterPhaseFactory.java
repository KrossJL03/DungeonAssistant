package bot.Battle.Encounter;

import bot.Battle.BattlePhaseFactoryInterface;
import org.jetbrains.annotations.NotNull;

public class EncounterPhaseFactory implements BattlePhaseFactoryInterface
{
    /**
     * Phase constructor (ATTACK_PHASE)
     *
     * @return EncounterPhase
     */
    public @NotNull EncounterPhase createAttackPhase()
    {
        return new EncounterPhase(EncounterPhase.ATTACK_PHASE);
    }

    /**
     * Phase constructor (CREATE_PHASE)
     *
     * @return EncounterPhase
     */
    public @NotNull EncounterPhase createCreatePhase()
    {
        return new EncounterPhase(EncounterPhase.CREATE_PHASE);
    }

    /**
     * Phase constructor (END_PHASE)
     *
     * @return EncounterPhase
     */
    public @NotNull EncounterPhase createEndPhase()
    {
        return new EncounterPhase(EncounterPhase.END_PHASE);
    }

    /**
     * Phase constructor (JOIN_PHASE)
     *
     * @return EncounterPhase
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
