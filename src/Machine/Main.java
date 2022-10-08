package Machine;
import java.io.IOException;

public class Main {
    public static void main(String args[]) throws IOException {
        VendingMachine machine = new SnackMachine();

        while(true)
            machine.workFlow();
    }
}
