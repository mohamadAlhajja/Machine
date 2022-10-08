package Machine;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CoinSlot extends Money {
    private List<Integer> accepted = new ArrayList(){{
        add(10);
        add(20);
        add(50);
        add(100);
    }};

    public CoinSlot(int category, int amount) throws IOException {
        try {
            if (!accepted.contains(category))
                throw new IOException();
            else {
                this.setCategory(category);
                this.setAmount(amount);
            }
        }catch (IOException ex){
            System.out.println("Machine only accepts 10c, 20c, 50c, 1$ from coins\n");
        }
    }
	@Override
    public double CalculateInDollars() {
        return (double)(this.getCategory()*this.getAmount())/100;
    }
}