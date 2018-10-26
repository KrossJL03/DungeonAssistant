package bot.Item.Consumable;

import bot.Item.Consumable.Exception.InvalidConsumableException;
import bot.Item.Consumable.Exception.ItemNotFoundException;

import java.util.ArrayList;

public class ConsumableManager {

    static void createItem(
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
        boolean dodges,
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
        if (shortDescription == null) {
            shortDescription = ConsumableManager.buildShortDescription(hitpointsHealed, damage);
        }
        ConsumableRepository.insertItem(
            name,
            image,
            description,
            shortDescription,
            buyValue,
            sellValue,
            seasonalMonth,
            usablePhase,
            damage,
            hitpointsHealed,
            tempStatBoost,
            uses,
            percentHealed,
            dodges,
            healsUser,
            pingDM,
            protects,
            recipientRequired,
            revives
        );
    }

    public static ArrayList<ConsumableItem> getAllItems() {
        return ConsumableRepository.getAllItems();
    }

    public static ConsumableItem getItem(String itemName) {
        ConsumableItem item = ConsumableRepository.getItem(itemName);
        if (item == null) {
            throw ItemNotFoundException.createForConsumable(itemName);
        }
        return item;
    }

    private static String buildShortDescription(int hitpointsHealed, int damage) {
        String output = null;
        if (hitpointsHealed > 0) {
            output = String.format("Restores %d HP when eaten.", hitpointsHealed);
        }
        if (damage > 0) {
            output = String.format("Deals %s damage", damage);
        }
        return output;
    }

}
