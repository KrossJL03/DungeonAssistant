package bot.Hostile;

public class Loot{

    // todo ItemInterface instead of string
    private String item;
    private int diceRoll;
    private int quantity;

    Loot(int diceRoll, String item, int quantity) {
        this.diceRoll = diceRoll;
        this.item = item;
        this.quantity = quantity;
    }

    int getDiceRoll() { return this.diceRoll; }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isSameItem(Loot loot) {
        return loot.getItem().equals(this.item);
    }

    public String toString() {
        return String.format("x%d %s", this.quantity, this.item);
    }
}
