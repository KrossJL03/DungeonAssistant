package bot.Item.Consumable;

import bot.Item.Consumable.Exception.InvalidConsumableException;

public class ConsumableManager {

    static void createItem(
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
        if (name == null) {
            throw InvalidConsumableException.createMissingName();
        } else if (hitpointsHealed > 0 && percentHealed > 0) {
            throw InvalidConsumableException.createMultipleHealingTypes(hitpointsHealed, percentHealed);
        } else if (percentHealed > 1) {
            throw InvalidConsumableException.createHealPercentOutOfBounds(percentHealed);
        } else if (buyValue > 0 && sellValue == 0) {
            throw InvalidConsumableException.createExclusivelyBuyable(buyValue);
        } else if (sellValue > 0 && buyValue == 0) {
            throw InvalidConsumableException.createExclusivelySellable(sellValue);
        }
        ConsumableRepository.insertItem(
            name,
            image,
            description,
            buyValue,
            sellValue,
            seasonalMonth,
            usablePhase,
            damage,
            hitpointsHealed,
            tempStatBoost,
            uses,
            percentHealed,
            healsUser,
            pingDM,
            protects,
            recipientRequired,
            revives
        );
    }

    public static ConsumableItem getItem(String itemName) {
        return ConsumableRepository.getItem(itemName);
    }

}
