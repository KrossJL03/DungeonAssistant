package bot.Encounter.Phase;

import bot.Encounter.EncounterPhaseInterface;
import org.jetbrains.annotations.NotNull;

public class EncounterPhaseFactory
{
    /**
     * Phase constructor (ATTACK_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createAttackPhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.ATTACK_PHASE);
    }

    /**
     * Phase constructor (CREATE_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createCreatePhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.CREATE_PHASE);
    }

    /**
     * Phase constructor (DODGE_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createDodgePhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.DODGE_PHASE);
    }

    /**
     * Phase constructor (END_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createEndPhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.END_PHASE);
    }

    /**
     * Phase constructor (JOIN_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createJoinPhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.JOIN_PHASE);
    }

    /**
     * Phase constructor (LOOT_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createLootPhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.LOOT_PHASE);
    }

    /**
     * Phase constructor (RP_PHASE)
     *
     * @return Phase
     */
    public static @NotNull EncounterPhaseInterface createRpPhase()
    {
        return new EncounterPhase(EncounterPhaseInterface.RP_PHASE);
    }
}
