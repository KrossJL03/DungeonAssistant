package bot.Entity;

import net.dv8tion.jda.core.entities.User;

public class PCEncounterData implements EncounterDataInterface {

    private PlayerCharacter        playerCharacter;
    private int                    currentHp;
    private int                    currentActions;
    private boolean                hasProtect;
    private EncounterDataInterface slayer;

    public PCEncounterData(PlayerCharacter playerCharacter) {
        this.playerCharacter = playerCharacter;
        this.currentHp = playerCharacter.getHitpoints();
        this.hasProtect = true;
        this.currentActions = 0;
    }

    public int getAgility() {
        return this.playerCharacter.getAgility();
    }

    public int getAttackDice() {
        return this.playerCharacter.getStrength() + 10;
    }

    public int getCritDamage() {
        return (int) Math.floor(this.getAttackDice() * 1.5);
    }

    public int getCurrentHP() {
        return this.currentHp;
    }

    public int getDodgeDice() {
        return ((int) Math.floor(this.playerCharacter.getAgility() / 2)) + 10;
    }

    public int getEndurance() {
        return (int) Math.floor(this.playerCharacter.getDefense() / 2);
    }

    public int getMaxHP() {
        return this.playerCharacter.getHitpoints();
    }

    public String getName() {
        return this.playerCharacter.getName();
    }

    public User getOwner() {
        return this.playerCharacter.getOwner();
    }

    public EncounterDataInterface getSlayer() {
        // todo throw an exception if no slayer
        return this.slayer;
    }

    public boolean hasActions() {
        return this.currentActions > 0;
    }

    public void heal(int hitpoints) {
        this.currentHp += hitpoints;
    }

    public void hurt(int hitpoints) {
        this.currentHp -= hitpoints;
    }

    public boolean isAbleToProtect() {
        return this.hasProtect;
    }

    public boolean isCrit(int rollToHit) {
        return rollToHit >= this.getMinCrit();
    }

    public boolean isDead() {
        return currentHp < 1;
    }

    public void resetActions(boolean isAttackPhase) {
        this.currentActions = isAttackPhase ? this.getNumberOfActions() : 1;
    }

    public int rollDamage() {
        return (int) Math.floor(Math.random() * this.getAttackDice()) + 1;
    }

    public int rollDodge() {
        return (int) Math.floor(Math.random() * this.getDodgeDice()) + 1;
    }

    public int rollToHit() {
        return (int) Math.floor(Math.random() * 20) + 1;
    }

    public int takeDamage(EncounterDataInterface attacker, int damage) {
        damage = (int) Math.floor(damage - this.getEndurance());
        if (this.currentHp > 0 && this.currentHp - damage < 0) {
            this.slayer = attacker;
        }
        this.currentHp -= damage;
        return damage;
    }

    public void useAction() { this.currentActions--; }

    public void useAllActions() { this.currentActions = 0; }

    public void useProtect() {
        this.hasProtect = false;
    }

    private int getMinCrit() {
        return 20 - ((int) Math.floor(this.playerCharacter.getWisdom() / 4));
    }

    private int getNumberOfActions() {
        return (int) Math.floor(this.playerCharacter.getAgility() / 10) + 1;
    }
}
