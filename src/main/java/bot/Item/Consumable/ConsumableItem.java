package bot.Item.Consumable;

import bot.Item.ItemAbstract;

public class ConsumableItem extends ItemAbstract {

    private String  usablePhase;
    private int     damage;
    private int     hitpointsHealed;
    private int     tempStatBoost;
    private int     uses;
    private float   percentHealed;
    private boolean healsUser;
    private boolean pingDM;
    private boolean protects;
    private boolean recipientRequired;
    private boolean revives;

    ConsumableItem(
        String name,
        String image,
        String description,
        String shortDescription,
        int buyValue,
        int sellValue,
        int seasonalMonth,
        String usablePhase,
        int damage,
        int hitpointsHealed,
        int tempStatBoost,
        int uses,
        float percentHealed,
        boolean healsUser,
        boolean pingDM,
        boolean protects,
        boolean recipientRequired,
        boolean revives
    ) {
        super(name, image, description, shortDescription, buyValue, sellValue, seasonalMonth);
        this.damage = damage;
        this.healsUser = healsUser;
        this.hitpointsHealed = hitpointsHealed;
        this.percentHealed = percentHealed;
        this.pingDM = pingDM;
        this.protects = protects;
        this.recipientRequired = recipientRequired;
        this.revives = revives;
        this.tempStatBoost = tempStatBoost;
        this.usablePhase = usablePhase;
        this.uses = uses;
    }

    public int getDamage() {
        return this.damage;
    }

    public int getHitpointsHealed() {
        return this.hitpointsHealed;
    }

    public float getPercentHealed() {
        return this.percentHealed;
    }

    int getTempStatBoost() {
        return this.tempStatBoost;
    }

    public String getUsablePhase() {
        return this.usablePhase;
    }

    int getUses() {
        return this.uses;
    }

    public boolean isDamaging() {
        return this.damage > 0;
    }

    public boolean isDmPinged() {
        return this.pingDM;
    }

    public boolean isHealing() {
        return this.isPointHealing() || this.isPercentHealing();
    }

    public boolean isPercentHealing() {
        return this.percentHealed > 0;
    }

    public boolean isProtecting() {
        return this.protects;
    }

    public boolean isRecipientRequired() {
        return this.recipientRequired;
    }

    public boolean isReviving() {
        return this.revives;
    }

    public boolean isTempStatBoost() {
        return this.tempStatBoost > 0;
    }

    public boolean isUserHealed() {
        return this.healsUser;
    }

    private boolean isPointHealing() {
        return this.hitpointsHealed > 0;
    }

}