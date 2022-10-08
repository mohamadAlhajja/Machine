package Machine;

import Machine.Money;
import Machine.Money.*;
import Machine.NoteSlot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public abstract class VendingMachine
{

    private ArrayList<Money> totalBalance;
    private double balanceInDollars;
    private ArrayList<Money> collectedCash;
    private double totalCollectedCash;
    private ArrayList<Item> items;
    private double totalCollectedFromCards;

    VendingMachine()
    {
        resetMachine();
    }

    public ArrayList<Money> getTotalBalance() {
        return totalBalance;
    }


    public void SendChange(ArrayList<Money> change)
    {
        for(int i = 0; i < change.size(); i++)
        {
            for(int j = 0; j < this.totalBalance.size(); j++)
            {
                if(this.totalBalance.get(j).getCategory() == change.get(i).getCategory())
                {
                    this.totalBalance.get(j).setAmount(this.totalBalance.get(j).getAmount()-change.get(i).getAmount());
                    break;
                }
            }
        }
    }

    public double getBalanceInDollars() {
        double totalCash = 0;
        for(int i = 0; i < this.totalBalance.size(); i ++)
        {
            totalCash += this.totalBalance.get(i).CalculateInDollars();
        }
        this.balanceInDollars = totalCash;
        return balanceInDollars;
    }


    public void CollectCash(Money collected)
    {
        if(this.collectedCash == null)
            this.collectedCash = new ArrayList<Money>();
        boolean exists = false;
        for(int i = 0; i < this.collectedCash.size(); i++)
        {
            if(this.collectedCash.get(i).getCategory() == collected.getCategory())
            {
                this.collectedCash.get(i).setAmount(this.getCollectedCash().get(i).getAmount() + collected.getAmount());
                exists = true;
                break;
            }
        }
        if(exists == false)
            this.collectedCash.add(collected);
        exists = false;
        for(int i = 0; i < this.totalBalance.size(); i++)
        {
            if(this.totalBalance.get(i).getCategory() == collected.getCategory())
            {
                this.totalBalance.get(i).setAmount(this.totalBalance.get(i).getAmount() + collected.getAmount());
                exists = true;
                break;
            }
        }
        if(exists == false)
            this.totalBalance.add(collected);
    }

    public ArrayList<Money> getCollectedCash() {
        return collectedCash;
    }

    public double getTotalCollectedCash() {
        double totalCash = 0;
        for(int i = 0; i < this.collectedCash.size(); i ++)
        {
            totalCash += this.collectedCash.get(i).CalculateInDollars();
        }
        this.totalCollectedCash = totalCash;
        return totalCollectedCash;
    }


    /*
    * to basically have some money to return the change for anu customer who buy
    * */
    public void AddMoney() throws IOException {
        this.totalBalance = new ArrayList<Money>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("src/Money.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            if(data[1].charAt(0) == 'c')
            {
                Money newCent = new CoinSlot(Integer.parseInt(data[0]), Integer.parseInt(data[2]));
                boolean contains = false;
                for(int i = 0; i < totalBalance.size(); i ++)
                {
                    if(totalBalance.get(i).getCategory() == newCent.getCategory())
                    {
                        contains = true;
                        totalBalance.get(i).setAmount(totalBalance.get(i).getAmount() + newCent.getAmount());
                    }
                }
                if(!contains)
                    totalBalance.add(newCent);
            }
            else if(data[1].charAt(0) == '$')
            {
                if(Integer.parseInt(data[0]) == 1)
                {
                    Money newCent = new CoinSlot(100, Integer.parseInt(data[2]));
                    boolean contains = false;
                    for(int i = 0; i < totalBalance.size(); i ++)
                    {
                        if(totalBalance.get(i).getCategory() == newCent.getCategory())
                        {
                            contains = true;
                            totalBalance.get(i).setAmount(totalBalance.get(i).getAmount() + newCent.getAmount());
                        }
                    }
                    if(!contains)
                        totalBalance.add(newCent);
                }
                else
                {
                    Money newNote = new NoteSlot(Integer.parseInt(data[0]), Integer.parseInt(data[2]));
                    boolean contains = false;
                    for(int i = 0; i < totalBalance.size(); i ++)
                    {
                        if(totalBalance.get(i).getCategory() == newNote.getCategory())
                        {
                            contains = true;
                            totalBalance.get(i).setAmount(totalBalance.get(i).getAmount() + newNote.getAmount());
                        }
                    }
                    if(!contains)
                        totalBalance.add(newNote);
                }
            }
        }
        csvReader.close();
    }

    /*
    * filling items in the machine
    * */
    public ArrayList<Item> getItems()
    {
        return items;
    }

    public void AddItems() throws IOException {
        this.items = new ArrayList<Item>();
        String row;
        BufferedReader csvReader = new BufferedReader(new FileReader("src/items.csv"));
        while ((row = csvReader.readLine()) != null) {
            String[] data = row.split(",");
            Item newItem = new Item(data[0], Integer.parseInt(data[1]), Double.parseDouble(data[2]));
            this.items.add(newItem);
        }
        csvReader.close();
    }

    public void resetMachine()
    {
        this.totalBalance = null;
        this.balanceInDollars = 0;
        this.totalBalance = null;
        this.items = null;
        this.totalCollectedCash = 0;
    }

    public double getTotalCollectedFromCards() {
        return totalCollectedFromCards;
    }

    public void setTotalCollectedFromCards(double totalCollectedFromCards) {
        this.totalCollectedFromCards = totalCollectedFromCards;
    }

    public abstract void workFlow() throws IOException;

}