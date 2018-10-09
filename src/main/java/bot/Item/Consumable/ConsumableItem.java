package bot.Item.Consumable;

import bot.Item.ItemAbstract;

class ConsumableItem extends ItemAbstract {

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
        super(name, image, description, buyValue, sellValue, seasonalMonth);
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

    int getDamage() {
        return damage;
    }

    int getHitpointsHealed() {
        return hitpointsHealed;
    }

    float getPercentHealed() {
        return percentHealed;
    }

    int getTempStatBoost() {
        return tempStatBoost;
    }

    int getUses() {
        return uses;
    }

    boolean isDamaging() {
        return this.damage > 0;
    }

    boolean isDmPinged() {
        return this.pingDM;
    }

    boolean isHealing() {
        return this.isPointHealing() || this.isPercentHealing();
    }

    boolean isPercentHealing() {
        return this.percentHealed > 0;
    }

    boolean isProtecting() {
        return this.protects;
    }

    boolean isRecipientRequired() {
        return this.recipientRequired;
    }

    boolean isReviving() {
        return this.revives;
    }

    boolean isTempStatBoost() {
        return this.tempStatBoost > 0;
    }

    boolean isUserHealed() {
        return this.healsUser;
    }

    boolean isUsablePhase(String phase) {
        return this.usablePhase.equals(phase);
    }

    private boolean isPointHealing() {
        return this.hitpointsHealed > 0;
    }

}