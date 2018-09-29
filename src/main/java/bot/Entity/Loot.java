package bot.Entity;

public class Loot {

    private String item;
    private int quantity;

    Loot(String item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public String getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }
}
