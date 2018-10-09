package bot.Entity;

public interface EncounterDataInterface {

    int getAttackDice();
    int getCurrentHP();
    int getMaxHP();
    String getName();
    EncounterDataInterface getSlayer();
    boolean isSlain();
    void heal(int hitpoints);
    void heal(float percent);
    void hurt(int hitpoints);
    int rollDamage();
    int takeDamage(EncounterDataInterface attacker, int damage);
}
