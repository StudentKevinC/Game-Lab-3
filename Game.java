// Game.java
public class Game {
    private String name;
    private String category;
    private int price;
    private int quality;

    public Game(String name, String category, int price, int quality) {
        this.name = name;
        this.category = category;
        this.price = price;
        this.quality = quality;
    }

    public String getName() { return name; }
    public String getCategory() { return category; }
    public int getPrice() { return price; }
    public int getQuality() { return quality; }

    @Override
    public String toString() {
        return name + "," + category + "," + price + "," + quality;
    }
}