package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class DodgeActionResult implements ActionResultInterface {

    private ArrayList<DodgeResult> dodgeResults;
    private String                 pcName;
    private int                    pcCurrentHp;
    private int                    pcDodgeDie;
    private int                    pcMaxHp;

    DodgeActionResult(
        @NotNull String pcName,
        @NotNull ArrayList<DodgeResult> dodgeResults,
        int pcDodgeDie,
        int currentHp,
        int maxHp
    ) {
        this.dodgeResults = dodgeResults;
        this.pcCurrentHp = currentHp;
        this.pcDodgeDie = pcDodgeDie;
        this.pcMaxHp = maxHp;
        this.pcName = pcName;
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
     * Get number of hostile attacks
     *
     * @return int
     */
    public int getHostileAttackCount() {
        return this.dodgeResults.size();
    }

    /**
     * Get PC's current hp
     *
     * @return int
     */
    public int getPcCurrentHp() {
        return pcCurrentHp;
    }

    /**
     * Get PC dodge die
     *
     * @return int
     */
    public int getPcDodgeDie() {
        return pcDodgeDie;
    }

    /**
     * Get PC's max hp
     *
     * @return int
     */
    public int getPcMaxHp() {
        return pcMaxHp;
    }

    /**
     * Get PC name
     *
     * @return String
     */
    @NotNull
    public String getPcName() {
        return pcName;
    }

    /**
     * Get total damage dealt to PC by hostile attacks
     *
     * @return int
     */
    public int getTotalDamageDealt() {
        int totalDamageDealt = 0;
        for (DodgeResult result : this.dodgeResults) {
            totalDamageDealt += result.getDamageDealt();
        }
        return totalDamageDealt;
    }

    /**
     * Get total damage resisted by PC
     *
     * @return int
     */
    public int getTotalDamageResisted() {
        int totalDamageDealt = 0;
        for (DodgeResult result : this.dodgeResults) {
            totalDamageDealt += result.getDamageResisted();
        }
        return totalDamageDealt;
    }

    /**
     * Is PC slain
     *
     * @return boolean
     */
    public boolean isPcSlain() {
        return this.pcCurrentHp <= 0;
    }
}
