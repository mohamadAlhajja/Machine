package Machine;

import java.io.IOException;
import java.util.ArrayList;

public interface IState {

     void FillMachineMoney() throws IOException;

     void FillMachineItems() throws IOException;

     int Ready();

     double getMoneyValue(ArrayList<Money> list);

     boolean validateCash(int choice, int category);

     void AcceptCashAndChange(ArrayList<Money> Money) throws IOException;

     void AcceptCard() throws IOException;

     void ReFillItem(ArrayList<Item> items, Item item) throws IOException;

}