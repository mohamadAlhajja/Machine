package Machine;

public abstract class Money {
    private int Category;
    private int Amount;

    public int getCategory() {
        return Category;
    }

    public void setCategory(int category) {
        Category = category;
    }

    public int getAmount() {
        return Amount;
    }

    public void setAmount(int amount) {
        Amount = amount;
    }

    public Money() {}
    public abstract double CalculateInDollars();
}