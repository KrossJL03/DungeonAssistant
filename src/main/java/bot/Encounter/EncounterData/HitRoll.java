package bot.Encounter.EncounterData;

class HitRoll {

    private int HIT_ROLL_MISS = 2;
    private int HIT_ROLL_FAIL = 1;

    private int minCrit;
    private int roll;

    HitRoll(int roll, int minCrit) {
        this.minCrit = minCrit;
        this.roll = roll;
    }

    int getRoll() {
        return this.roll;
    }

    boolean isCrit() {
        return this.roll >= this.minCrit;
    }

    boolean isFail() {
        return this.roll <= HIT_ROLL_FAIL;
    }

    boolean isHit() {
        return this.roll > HIT_ROLL_MISS;
    }

    boolean isMiss() {
        return !this.isHit();
    }
}
