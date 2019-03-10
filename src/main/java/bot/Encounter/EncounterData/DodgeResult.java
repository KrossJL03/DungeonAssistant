package bot.Encounter.EncounterData;

import org.jetbrains.annotations.NotNull;

public class DodgeResult {

    private String hostileName;
    private int    dodgeRoll;
    private int    damageResisted;
    private int    hostileDamageRoll;

    DodgeResult(@NotNull String hostile, int dodgeRoll, int hostileDamageRoll, int damageResisted) {
        this.damageResisted = damageResisted;
        this.dodgeRoll = dodgeRoll;
        this.hostileName = hostile;
        this.hostileDamageRoll = hostileDamageRoll;
    }

    int getDamageDealt() {
        return this.hostileDamageRoll = this.damageResisted;
    }

    int getDamageResisted() {
        return damageResisted;
    }

    public int getDodgeRoll() {
        return dodgeRoll;
    }

    @NotNull
    public String getHostileName() {
        return hostileName;
    }

    public int getHostileDamageRoll() {
        return hostileDamageRoll;
    }

    public boolean isSuccess() {
        return this.dodgeRoll >= 10;
    }
}
