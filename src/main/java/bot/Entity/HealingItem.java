package bot.Entity;

import bot.Exception.ConsumerIsDeadException;

public class HealingItem implements ConsumableInterface {

    private String  name;
    private float   hpRestored;
    private boolean revives;

    public HealingItem(String name, float hpRestored, boolean revives) {
        this.name = name;
        this.hpRestored = hpRestored;
        this.revives = revives;
    }

    public String consume(PCEncounterData owner, EncounterDataInterface consumer) {
        StringBuilder output = new StringBuilder();
        if (consumer.isDead()) {
            if (!this.revives) {
                throw new ConsumerIsDeadException();
            } else {
                output.append(System.getProperty("line.separator"));
                output.append(String.format("***%s has been revived at half HP! You earned the 'Zombie' title!***", consumer.getName()));
            }
        }
        int hpRestored = this.healsPercentage() ? this.getHealingByPercentage(consumer.getMaxHP()) : (int) this.hpRestored;
        consumer.heal(hpRestored);
        if (owner.getName().equals(consumer.getName())) {
            output.insert(0, String.format("%s eats %s and regains **%d** HP! [%d/%d]", consumer.getName(), this.name, hpRestored, consumer.getCurrentHP(), consumer.getMaxHP()));
        } else {
            if (consumer instanceof HostileEncounterData) {
                output.insert(0, String.format("%s steals %s's %s and regains **%d** HP! [%d/%d]", consumer.getName(), owner.getName(), this.name, hpRestored, consumer.getCurrentHP(), consumer.getMaxHP()));
            } else {
                output.insert(0, String.format("%s feeds %s to %s and regains **%d** HP! [%d/%d]", owner.getName(), this.name, consumer.getName(), hpRestored, consumer.getCurrentHP(), consumer.getMaxHP()));
            }
        }
        return output.toString();
    }

    private boolean healsPercentage() {
        return hpRestored < 1;
    }

    private int getHealingByPercentage(int maxHP) {
        return (int) (Math.floor(maxHP * this.hpRestored) + 1);
    }

}
