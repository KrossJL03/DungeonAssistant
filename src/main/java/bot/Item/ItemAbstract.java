package bot.Item;

public class ItemAbstract {

    private int    buyValue;
    private int    sellValue;
    private String description;
    private String image;
    private String name;
    private int    seasonalMonth;

    protected ItemAbstract(
        String name,
        String image,
        String description,
        int buyValue,
        int sellValue,
        int seasonalMonth
        ) {
        this.buyValue = buyValue;
        this.description = description;
        this.image = image;
        this.name = name;
        this.sellValue = sellValue;
        this.seasonalMonth = seasonalMonth;
    }

    int getBuyValue() {
        return buyValue;
    }

    int getSellValue() {
        return sellValue;
    }

    String getDescription() {
        return description;
    }

    String getImage() {
        return image;
    }

    String getName() {
        return name;
    }

    int getSeasonalMonth() {
        return seasonalMonth;
    }

}
