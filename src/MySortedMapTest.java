import dsa.iface.IIterator;

import java.util.ArrayList;

public class MySortedMapTest {
    public static void main(String[] args) {
        IMySortedMap<Integer, Integer> mySortedMap = new MySortedMap<>();

        System.out.println("Put key 3: "+mySortedMap.put(3, 3));
        System.out.println("Put key 2: "+mySortedMap.put(2, 2));
        System.out.println("Put key 1: "+mySortedMap.put(1, 1));
        System.out.println("Put key 4: "+mySortedMap.put(4, 4));
        System.out.println("Remove key 4: " + mySortedMap.remove(4));
        System.out.println("Get key 2: " + mySortedMap.get(2));
        System.out.println("Update Key 2: " + mySortedMap.put(2, 5));
        System.out.println("The size is "+mySortedMap.size());

        IIterator<Integer> it1 = mySortedMap.getKeysInKeyOrder();
        ArrayList<Integer> keys= new ArrayList<>();
        while (it1.hasNext()){
            Integer key = it1.next();
            keys.add(key);
        }
        System.out.println("Keys in order is "+ keys);


        ArrayList<Integer> keys1 = new ArrayList<>();
        IIterator<Integer> it = mySortedMap.getKeysInInsertionOrder();
        while (it.hasNext()){
            Integer key = it.next();
            keys1.add(key);
        }
        System.out.println(keys1);

    }
}
