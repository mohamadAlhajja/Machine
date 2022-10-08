package Machine;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class SnackMachine extends VendingMachine implements IState{

    private Item[][] items = new Item[5][5];
    private int row;
    private int column;
    private ArrayList<Money> collect;

    public SnackMachine() throws IOException {
        super();
        this.FillMachineItems();
        this.FillMachineMoney();

    }

    @Override
    public void FillMachineMoney() throws IOException {
        super.AddMoney();
    }

    @Override
    public void FillMachineItems() throws IOException {
        super.AddItems();
        int itemInItems = 0;
        for(int i = 0; i < 5; i++)
        {
            for(int j = 0; j < 5 ; j ++) {
                items[i][j] = super.getItems().get(itemInItems);
                itemInItems ++;
            }
        }
    }


    /*
    represents the ready state, displays items to the customer, he can choose an item and pay for it here
     */
    @Override
    public int Ready()
    {
        if(super.getTotalBalance() != null)
        {
            boolean needs = false;
            for(int i = 0; i < super.getTotalBalance().size(); i ++)
            {
                if(super.getTotalBalance().get(i) instanceof CoinSlot)
                {
                    if(super.getTotalBalance().get(i).getAmount() < 5)
                        needs = true;
                }
            }
            if(needs)
            {
                System.out.println("Machine has lack in money, needs to be refilled");
                System.exit(0);

            }
            boolean Empty = true;
            for(int i = 0 ; i < 5; i ++)
                for(int j = 0; j < 5 && items[i][j].getAmount() != 0; j++)
                    Empty = false;
            if(Empty)
            {
                System.out.println("Snacks need to be refilled because machine is empty.");
                System.exit(0);
            }
            else
            {
                Scanner input = new Scanner(System.in);
                for(int i = 0 ; i < 5 ; i ++)
                {
                    for(int j = 0 ; j < 5 ; j ++)
                    {
                        System.out.print(""+items[i][j].getName()+"["+i+""+j+"] |\t");
                    }
                    System.out.println();
                }
                System.out.print("Code: ");
                int index = input.nextInt();
                row = index/10;
                column = index%10;

                try
                {
                    if(items[row][column].getAmount() == 0)
                    {
                        System.out.println(items[row][column].getName()+" not available");
                        return 0;
                    }
                    System.out.println(items[row][column].getName()+" exists with amount: "+items[row][column].getAmount()+", and price: "+items[row][column].getPrice()+"$");
                    System.out.println("Do you want to buy? (1 for yes)");
                    int choice = input.nextInt();
                    if(choice == 1)
                    {
                        System.out.println("How would you pay? \n1- cents 2- dollars 3- card");
                        int paymentMethod = input.nextInt();
                        if(paymentMethod == 1)
                        {
                            collect = new ArrayList<>();
                            System.out.println("Enter amount (machine only accepts 10c, 20c, 50c and 100c(1 dollar)) "); 
                            while(getMoneyValue(collect) < items[row][column].getPrice())
                            {
                                int cent = input.nextInt();
                                if(cent == 0)
                                {
                                    collect.removeAll(collect);
                                    row = 0;
                                    column = 0;
                                    System.out.println("Process cancelled ");
                                    return 0;
                                }
                                if(!validateCash(choice, cent))
                                {
                                    System.out.print("We don't accept this category of money, insert another ");
                                    continue;
                                }
                                boolean exists = false;
                                for(int i = 0; i < collect.size(); i++)
                                {
                                    if(collect.get(i).getCategory() == cent) {
                                        collect.get(i).setAmount(collect.get(i).getAmount() + 1);
                                        exists = true;
                                    }
                                }
                                if(exists == false)
                                    collect.add(new CoinSlot(cent, 1));

                                System.out.print("Your balance: "+getMoneyValue(collect)+" ");
                                if(getMoneyValue(collect) < items[row][column].getPrice())
                                    System.out.print("You need more, insert next(you can press 0 to calcell process and return your money):  ");
                            }
                            System.out.println();
                            return 1;
                        }
                        else if(paymentMethod == 2)
                        {
                            collect = new ArrayList<>();
                            System.out.print("\nEnter amount (machine only accepts 20$ and 50$) ");
                            while(getMoneyValue(collect) < items[row][column].getPrice())
                            {
                                int dollar = input.nextInt();
                                if(dollar == 0)
                                {
                                    collect.removeAll(collect);
                                    row = 0;
                                    column = 0;
                                    System.out.println("Process cancelled ");
                                    return 0;
                                }
                                if(!validateCash(choice, dollar))
                                {
                                    System.out.print("We don't accept this category of money, insert another ");
                                    continue;
                                }
                                boolean exists = false;
                                for(int i = 0; i < collect.size(); i++)
                                {
                                    if(collect.get(i).getCategory() == dollar) {
                                        collect.get(i).setAmount(collect.get(i).getAmount() + 1);
                                        exists = true;
                                    }
                                }
                                if(exists == false)
                                    collect.add(new NoteSlot(dollar, 1));

                                System.out.print("Your balance: "+getMoneyValue(collect)+" ");
                                if(getMoneyValue(collect) < items[row][column].getPrice())
                                    System.out.print("You need more, insert next (you can enter press 0 to cancel the process and return your money):  ");
                            }
                            System.out.println();
                            return 2;
                        }
                        else if(paymentMethod == 3)
                        {
                            return 3;
                        }
                        else
                        {
                            System.out.println("No such option");
                            return 0;
                        }
                    }
                }
                catch (IndexOutOfBoundsException ex)
                {
                    ex.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }
        return 0;
    }


    /*
    * @return the value of sum in dollars for an arraylist of money categories
    * */
    @Override
    public double getMoneyValue(ArrayList<Money> list) {
        double totalCash = 0.0;
        for(int i = 0; i < list.size(); i ++)
        {
            totalCash += list.get(i).CalculateInDollars();
        }
        return totalCash;

    }

    /*
    * @return true if the money entered by a customer is valid
    * */
    @Override
    public boolean validateCash(int choice, int category)
    {
        if(choice == 1)
        {
            if(category != 10 && category != 20 && category != 50 && category != 100)
            {
                return false;
            }
        }
        else if(choice == 2)
        {
            if (category != 20 && category != 50)
                return false;
        }
        return true;
    }

    /*
    * Calculate the change and returns it to the customer
    * */
    @Override
    public void AcceptCashAndChange(ArrayList<Money> Money) throws IOException {
        int tens=0, twenties=0, fifties=0, ones=0, twentyDollars = 0;
        items[row][column].setAmount(items[row][column].getAmount()-1);
        System.out.println("You can take your order");
        double value = getMoneyValue(Money);
        double difference = value - items[row][column].getPrice();
        if(difference != 0)
        {
            difference = (double)(Math.round((1000*difference)));
            difference = difference/1000;
            System.out.println("You have a remaining "+difference+"$");
            twentyDollars += (int)(difference/20);
            difference = difference%20;
            ones += (int)(difference/1);
            difference = difference%1;
            fifties += (int) (difference/0.5);
            difference = difference%0.5;
            twenties+= (int)(difference/0.2);
            difference = difference%0.2;
            tens += (int)(difference/0.1);
            difference = difference%0.1;
            if(difference > 0)
                tens += 1;

            int finalTens = tens;
            int finalFifties = fifties;
            int finalTwenties = twenties;
            int finalOnes = ones;
            int finalTwentyDollars = twentyDollars;
            ArrayList<Money> change = new ArrayList<Money>(){{
                try {
                    add(new CoinSlot(10, finalTens));
                    add(new CoinSlot(20, finalTwenties));
                    add(new CoinSlot(50, finalFifties));
                    add(new CoinSlot(100, finalOnes));
                    add(new NoteSlot(20, finalTwentyDollars));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }};
            super.SendChange(change);
            System.out.println("You can take your money :)");
        }
    }

    /*
    * In case the customer chose to pay using a card, we just pull the needed money and give him his order (this is not a full implementation since we don't hava
    * a machine that checks the card
    * */
    @Override
    public void AcceptCard() throws IOException {
        items[row][column].setAmount(items[row][column].getAmount()-1);
        System.out.println("You can take your order");
        super.setTotalCollectedFromCards(super.getTotalCollectedFromCards()+items[row][column].getPrice());
        System.out.println("You can take your card :)");
    }


    /*
    * in case Machine is empty, we can refill it
    * */
    @Override
    public void ReFillItem(ArrayList<Item> items, Item item) throws IOException {
        super.resetMachine();
        super.AddItems();
        super.AddMoney();
    }

    /*
    * working procedure of the machine
    * */
    @Override
    public void workFlow() throws IOException {
        collect = new ArrayList<>();
        int choice = Ready();
        if (choice == 1 || choice == 2) {
            AcceptCashAndChange(collect);
        } else if (choice == 3)
            AcceptCard();

    }
}