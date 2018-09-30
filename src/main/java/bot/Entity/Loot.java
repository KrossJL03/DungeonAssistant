package bot.Entity;

public class Loot{

    private String item;
    private int quantity;

    Loot(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public void addQuantity(int quantity) {
        this.quantity = quantity;
    }

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
