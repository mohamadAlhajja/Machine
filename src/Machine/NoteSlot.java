package Machine;

import java.io.IOException;

public class NoteSlot extends Money {

    public NoteSlot(int category, int amount) throws IOException {
        try {
            if (category != 20 && category != 50) {
                throw new IOException();
            } else {
                this.setCategory(category);
                this.setAmount(amount);
            }
        }
        catch (IOException ex)
        {
            System.out.println("Machine only accepts 20$ and 50$ from notes\n");
        }
    }
    @Override
    public double CalculateInDollars() {

        return (double)(this.getCategory() * this.getAmount());
    }
}
