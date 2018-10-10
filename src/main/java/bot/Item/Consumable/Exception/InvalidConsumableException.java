package bot.Item.Consumable.Exception;

import bot.CustomExceptionInterface;

public class InvalidConsumableException extends RuntimeException implements CustomExceptionInterface {

    private InvalidConsumableException(String message) {
        super(message);
    }

    public static InvalidConsumableException createExclusivelyBuyable(int buyValue) {
        return new InvalidConsumableException(
            String.format("If an item is buyable for %d it must also be sellable", buyValue)
        );
    }

    public static InvalidConsumableException createExclusivelySellable(int sellValue) {
        return new InvalidConsumableException(
            String.format("If an item is sellable for %d it must also be buyable", sellValue)
        );
    }

    public static InvalidConsumableException createHealPercentOutOfBounds(float percentHealed) {
        return new InvalidConsumableException(
            String.format("Percent healed cannot be greated than 1 (%s)", percentHealed)
        );
    }

    public static InvalidConsumableException createMissingName() {
        return new InvalidConsumableException("All items must have a name");
    }

    public static InvalidConsumableException createMultipleHealingTypes(int hitpointsHealed, float percentHealed) {
        return new InvalidConsumableException(
            String.format(
                "An items cannot heal both %d hitpoints and %f percent, please pick one",
                hitpointsHealed,
                percentHealed * 100
            )
        );
    }

}
