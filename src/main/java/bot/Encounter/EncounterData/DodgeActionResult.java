package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeActionResult implements ActionResultInterface {

    private ArrayList<DodgeResult> dodgeResults;
    private Slayer                 targetSlayer;
    private String                 targetName;
    private int                    targetCurrentHp;
    private int                    targetDodgeDie;
    private int                    targetMaxHp;

    /**
     * DodgeActionResult constructor
     *
     * @param targetName      Target name
     * @param dodgeResults    Dodge results
     * @param targetDodgeDie  Target dodge die
     * @param targetCurrentHp Target current hp
     * @param targetMaxHp     Target max hp
     * @param targetSlayer    Target slayer
     */
    DodgeActionResult(
        @NotNull String targetName,
        @NotNull ArrayList<DodgeResult> dodgeResults,
        int targetDodgeDie,
        int targetCurrentHp,
        int targetMaxHp,
        @NotNull Slayer targetSlayer
    ) {
        this.dodgeResults = dodgeResults;
        this.targetCurrentHp = targetCurrentHp;
        this.targetDodgeDie = targetDodgeDie;
        this.targetMaxHp = targetMaxHp;
        this.targetName = targetName;
        this.targetSlayer = targetSlayer;
    }

    /**
     * Get all dodge results
     *
     * @return ArrayList
     */
    @NotNull
    public ArrayList<DodgeResult> getDodgeResults() {
        return dodgeResults;
    }

    /**
     * Get minimum roll needed for a successful dodge
     *
     * @return int
     */
    public int getMinSucessDodgeRoll() {
        return DodgeRoll.DODGE_ROLL_PASS;
    }

    /**
     * Get target's current hitpoints
     *
     * @return int
     */
    public int getTargetCurrentHp() {
        return targetCurrentHp;
    }

    /**
     * Get target dodge die
     *
     * @return int
     */
    public int getTargetDodgeDie() {
        return targetDodgeDie;
    }

    /**
     * Get target's max hitpoints
     *
     * @return int
     */
    public int getTargetMaxHp() {
        return targetMaxHp;
    }

    /**
     * Get target name
     *
     * @return String
     */
    @NotNull
    public String getTargetName() {
        return targetName;
    }

    /**
     * Get target slayer
     *
     * @return Slayer
     */
    @NotNull
    public Slayer getTargetSlayer() {
        return targetSlayer;
    }
}
