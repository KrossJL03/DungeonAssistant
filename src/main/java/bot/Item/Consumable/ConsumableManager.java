package bot.Item.Consumable;

import bot.Entity.EncounterDataInterface;
import bot.Entity.PCEncounterData;
import bot.Exception.CharacterSlainException;
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

    public static void useItem(ConsumableItem item, PCEncounterData user, EncounterDataInterface recipient) {
        boolean usedOnSelf = user.equals(recipient);

        if (item.isHealing()) {
            if (recipient.isSlain() && !item.isReviving()) {
                throw CharacterSlainException.createFailedToHeal(recipient.getName(), recipient.getSlayer().getName());
            } else if (!usedOnSelf && item.isUserHealed()) {
                user.heal(item.isPercentHealing() ? item.getPercentHealed() : item.getHitpointsHealed());
            } else {
                // todo "the guild leader in charge takes a phoenix feather out of their bag, reviving [Member player]! You're back with half HP, and get the "Zombie" title."
                recipient.heal(item.isPercentHealing() ? item.getPercentHealed() : item.getHitpointsHealed());
            }
        }

        if (item.isDamaging()) {
            recipient.hurt(item.getDamage());
        }

//        if (item.isTempStatBoost()) {
//            // todo
//        }
//
//        if (item.isProtecting()) {
//            if (!usedOnSelf) {
//                // todo results in protect action
//            } else {
//                // todo results in successful dodge action
//            }
//        }
//
//        if (item.isDmPinged()) {
//            // todo
//        }

    }

}
