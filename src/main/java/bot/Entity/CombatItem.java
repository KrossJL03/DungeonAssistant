package bot.Entity;

import bot.Exception.ConsumerIsDeadException;

public class CombatItem implements ConsumableInterface {

    private String name;
    private int damage;

    public CombatItem(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public String consume(PCEncounterData owner, EncounterDataInterface consumer) {
        if (consumer.isDead()) {
            throw new ConsumerIsDeadException();
        }
        consumer.takeDamage(owner, this.damage);
        return String.format("%s uses %s on %s and deals **%d** points of damage! [%d/%d]", consumer.getName(), owner.getName(), this.name, this.damage, consumer.getCurrentHP(), consumer.getMaxHP());
    }
}
