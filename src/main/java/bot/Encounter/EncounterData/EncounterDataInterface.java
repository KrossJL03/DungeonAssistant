package bot.Encounter.EncounterData;

public interface EncounterDataInterface {

    int getAttackDice();
    int getCurrentHP();
    int getMaxHP();
    String getName();
    EncounterDataInterface getSlayer();
    boolean isSlain();
    int heal(int hitpoints);
    int heal(float percent);
    void hurt(int hitpoints);
    int rollDamage();
    int takeDamage(EncounterDataInterface attacker, int damage);
}
