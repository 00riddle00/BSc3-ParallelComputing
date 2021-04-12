/*
 * Main.java
 * @author Tomas Giedraitis, MIF INFO 3k.
 * Lab. darbas nr. 2
 *
 * Užduotis:
 *
 * Realizuoti paskirtojo varianto gijų sinchronizacijos primityvą.
 * Viena klasė realizuoja sinchr. primityvą, kita/os jį panaudoja - 
 * demonstruoja teisingą veikimą (sugalvoti kokią nors modelinę situaciją). 
 * Įvykio laukimas realizuojamas Object.wait(), informavimas apie įvykį 
 * Object.notify/all() kreipiniais. Jau egzistuojančių (standartinių) 
 * sinchronizacinių klasių panaudojimas neleistinas (pvz., util.concurrent)
 * Programa veikia konsoliniu režimu. Į konsolę išvedami esminiai įvykiai.
 *
 * Užduoties variantas:
 *
 * Multipleksinis žiedinis buferis kaip pranešimo perdavimo kanalas  
 * tarp vienos gijos - siuntėjo, bei N (parametras) gijų gavėjų. 
 * Kiekvienas pranešimas laikomas nuskaitytu, jeigu jį nuskaitė VISOS gijos 
 * - gavėjos. Gija, vykdanti skaitymo veiksmą nurodo savo numerį iš intervalo
 * 0..N-1.
 *
 */

package lab2;

import java.util.ArrayList;


public class Main {

	public static void main(String[] args) {
        Buffer buffer = new Buffer();
        Producer.begin(buffer);
        User.begin(buffer); 
    }
}


class Utils {

    public static int getMin(ArrayList<Integer> intArray) {
        int min = intArray.get(0);

        for (int i = 1; i < intArray.size(); i++) {
            min = intArray.get(i);
        }

        return min;
    }
}


class Buffer {

    public static final int bufferSize = 5;

    int dataProducedCount;
    ArrayList<Integer> usageInfo;
    ArrayList<String> producedData;

    public Buffer() {
        this.usageInfo = new ArrayList<>();

        for (int i = 0; i < User.userCount; i++) {
            usageInfo.add(0);
        }

        this.producedData = new ArrayList<>();

        for (int i = 0; i < bufferSize; i++) {
            producedData.add("");
        }
    }

    public synchronized void produce(String dataItem) {

        int min = Utils.getMin(usageInfo);

        // check if the producer can produce
        while (dataProducedCount - min >= bufferSize) {
            try {
                this.wait();
                min = Utils.getMin(usageInfo);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        producedData.add(dataItem);
        dataProducedCount++;
        System.out.println(String.format("[Producer]: produced item %03d", dataProducedCount));

        this.notifyAll();
    }

    public String consume(int userIndex) {
        int dataConsumedCount = usageInfo.get(userIndex);

        // check if the user can consume
        synchronized(this) {
            while (dataConsumedCount >= dataProducedCount) {
                try {
                    this.wait();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            usageInfo.set(userIndex, ++dataConsumedCount);
            System.out.println(String.format("[User%d]: consumed item %03d", userIndex, dataConsumedCount));
            this.notifyAll();
        }

        return producedData.get(dataConsumedCount - 1);
    }
}


class Producer extends Thread {

    public static final int dataItemTotalCount = 10;

    int dataProducedCount;
    Buffer buffer;

    public Producer(Buffer buffer) {
        this.buffer = buffer;
    }

    // produce 
    public void run() {
        while (dataProducedCount < dataItemTotalCount) {
            String dataItem = String.format("%03d", dataProducedCount);
            buffer.produce(dataItem);
            dataProducedCount++;
        }
        System.out.println("[Producer]: all " + dataItemTotalCount + " items were produced.");
    }

    public static void begin(Buffer buffer) {
        Producer producer = new Producer(buffer);
        producer.start();
    }
}


class User extends Thread {

    public static final int userCount = 5;

    int userIndex;
    int dataConsumedCount;
    String currentDataItem;
    Buffer buffer;

    public User(int userIndex, Buffer buffer) {
        this.userIndex = userIndex;
        this.buffer = buffer;
    }

    // consume 
    public void run() {
        while (dataConsumedCount < Producer.dataItemTotalCount) {
            currentDataItem = buffer.consume(userIndex);
            dataConsumedCount++;
        }
        System.out.println("[User" + userIndex + "]: consumed all " + Producer.dataItemTotalCount + " items");
    }

    public static void begin(Buffer buffer) {

        // create the required number of users 
        // (equal to `userCount` variable)
        try {
            User u0 = new User(0, buffer);
            User u1 = new User(1, buffer);
            User u2 = new User(2, buffer);
            User u3 = new User(3, buffer);
            User u4 = new User(4, buffer);

            u0.start(); u1.start(); u2.start(); u3.start(); u4.start();
            u0.join();  u1.join();  u2.join();  u3.join();  u4.join();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
