import dsa.iface.IIterator;
import dsa.iface.IList;
import dsa.iface.IPosition;
import dsa.impl.DLinkedList;
import dsa.impl.ListIterator;

public class MySortedMap <K extends Comparable<K>,V> implements IMySortedMap<K, V> {


    protected Entry<K,V> root = this.newEntry(null,null,null);

    protected int size;

    private Entry<K,V> getEntryByKey(K key ) {
        if (isInternal(find(root(),key))){
            return find(root(),key);
        } else{
            return null;
        }
    }


    private void inorderTraversal(Entry<K,V> e, IList<K> list) {

        if(e != null) {

            inorderTraversal(e.left ,list) ;
            if (e.key != null){
                list.insertLast(e.key);
            }
            inorderTraversal(e.right ,list) ;
        }
    }

    private void inInsertionOrderTraversal(Entry<K,V> e, IList<K> keyList) {
        if(e != null) {
            inInsertionOrderTraversal(e.left, keyList);
            if (e.key != null){
                if (keyList.isEmpty()) {
                    keyList.insertFirst(e.key);
                } else {
                    IPosition<K> pos = keyList.first();
                    while (pos != null && e.insertionTime > getEntryByKey(pos.element()).insertionTime) {
                        pos = keyList.next(pos);
                    }
                    if (pos == null) {
                        keyList.insertLast(e.key);
                    }
                    else {
                        keyList.insertBefore(pos, e.key);
                    }
            }

            }
            inInsertionOrderTraversal(e.right, keyList);
        }

    }

    @Override
    public V get(K key) {
        Entry<K,V> e = getEntryByKey( key );
        return e == null ? null : e.value;
    }

    @Override
    public IIterator<K> getKeysInKeyOrder() {
        IList<K> keyList = new DLinkedList<>();
        inorderTraversal(root , keyList);
        return new ListIterator<>( keyList );
    }

    @Override
    public IIterator<K> getKeysInInsertionOrder() {
        IList<K> keyList = new DLinkedList<>();
        inInsertionOrderTraversal(root , keyList);
        return new ListIterator<>( keyList );
    }


    @Override
    public V remove(K key) {
        Entry<K,V> e = getEntryByKey(key );

        if (isInternal(e)){
            if (isExternal(left(e)) || isExternal(right(e))){

                removeEntry(e);
                restructure(e);
            }else{
                Entry<K,V> current = right(e);
                while (isInternal(left(current))){
                    current = left(current);
                }
                replace(e, current.key(),current.value());
                removeEntry(current);
                restructure(current);
            }

        }
        return e.value;
    }

    @Override
    public V put(K key, V value) {
        Entry<K,V> e = find(root(),key);
        Entry<K,V> n = find(root(),key);

        while (n != root){
            n.height = Math.max(n.left == null ? 0 : (left(n)).height, n.right == null ? 0 : (right(n)).height)+1;
            n = n.parent;
        }

        if ( isExternal(e)){
            expandExternal(e,key,value);
            Entry<K,V> node = findUnbalanced(e);
            if (node != null){
                restructure(node);
            }
            return value;
        }
        else {
            return replace(e,key,value);
        }

    }

    public V replace(Entry<K,V> e, K key, V value) {
        V toReturn = e.value;
        e.value = value;
        e.key = key;
        e.insertionTime = System.nanoTime();
        return toReturn;
    }

    private Entry<K,V> findUnbalanced(Entry<K,V> e){
        while (Math.abs((right(e)).height - (left(e)).height) <= 1 && e != root){
            e = parent(e);

        }
        if (e == root){

            if((((right(e))).height-((left(e))).height)>1||((right(e))).height-((left(e))).height<-1){
                return e;
            }
            else
                return null;
        }
        return e;
    }

    private void restructure( Entry<K,V> n ) {

        Entry<K, V> e = n;

        Entry<K, V> temp = e;

        e.height = Math.max(e.left == null ? 0 : (e.left).height, e.right == null ? 0 : (e.right).height) + 1;
        while (temp != root()) {
            (temp.parent).height = Math.max((temp.parent).left == null ? 0 : (((temp.parent).left)).height, (temp.parent).right == null ? 0 : (((temp.parent).right)).height) + 1;
            temp = temp.parent;
        }

        if (e != null) {
            if (((right(e))).height - ((left(e))).height > 1) {

                if (right(e) != null && ((right(right(e)))).height < ((left(right(e)))).height) {
                    right_rotate(right(e));
                    left_rotate(e);
                } else {
                    left_rotate(e);
                }
            }
            if (((left(e))).height - ((right(e))).height > 1) {

                if (left(e) != null && ((left(left(e)))).height < ((right(left(e)))).height) {
                    left_rotate(left(e));
                    right_rotate(e);
                } else {
                    right_rotate(e);
                }

            }
        }

        Entry<K, V> eRoot = findUnbalanced(e);

        if (eRoot != null) {
            restructure(eRoot);
        }
    }



    private void left_rotate(Entry<K,V> e){

        Entry<K,V> temp1 = e;

        Entry<K,V> temp2 = e.right;

        Entry<K,V> temp3 = e.right.left;

        Entry<K,V> temp4 =  e.parent;

        if (e ==root){

            temp1.right = temp3;
            // 2 = o (null)

            if (hasRight(temp2) == true){

                temp3.parent = temp1;

            }

            temp2.left = temp1;

            temp1.parent = temp2;

            root = temp2;


        } else{
            temp1.right = temp3;

            if (hasRight(temp2)){
                temp3.parent = temp1;
            }

            temp2.left = temp1;

            temp1.parent = temp2;

            if (temp4.left == e){

                temp4.left = temp2;

                temp2.parent = temp4;

            }else if (temp4.right == e){

                temp4.right = temp2;

                temp2.parent = temp4;

            }
        }
        temp1.height = Math.max(temp1.left == null ? 0 : (left(temp1)).height, (temp1.right == null ? 0 : (right(temp1)).height)) + 1;
        temp2.height = Math.max(temp2.left == null ? 0 : (left(temp2)).height, (temp2.right == null ? 0 : (right(temp2)).height)) + 1;
        if (temp4 != null){
            temp4.height = Math.max(temp4.left == null ? 0 : (left(temp4)).height, (temp4.right == null ? 0 : (right(temp4)).height)) + 1;
        }

    }

    private void right_rotate(Entry<K,V> e){

        Entry<K,V> temp1 = e;

        Entry<K,V> temp2 = e.left;

        Entry<K,V> temp3 = e.left.right;

        Entry<K,V> temp4 =  e.parent;

        if (e ==root){

            temp1.left = temp3;
            if (hasLeft(temp2)) {

                temp3.parent = temp1;

            }

            temp2.right = temp1;

            temp1.parent = temp2;

            root = temp2;

        }else{
            temp1.left = temp3;

            if (hasRight(temp2)){
                temp3.parent = temp1;
            }

            temp2.right = temp1;

            temp1.parent = temp2;

            if (left(temp4) == e){

                temp4.left = temp2;

                temp2.parent = temp4;

            }else if ((right(temp4) == e)){

                temp4.right = temp2;

                temp2.parent = temp4;


            }
        }

        temp1.height = Math.max(temp1.left == null ? 0 : (left(temp1)).height, (temp1.right == null ? 0 : (right(temp1)).height))+1;
        temp2.height = Math.max(temp2.left == null ? 0 : (left(temp2)).height, (temp2.right == null ? 0 : (right(temp2)).height))+1;
        if (temp4 != null){
            temp4.height = Math.max(temp4.left == null ? 0 : (left(temp4)).height, (temp4.right == null ? 0 : (right(temp4)).height))+1;
        }
    }

    public V removeEntry(Entry<K,V> e) {
        if (this.isExternal(e.left)) {
            if (e == this.root) {
                this.root = e.right;
                e.right.parent = null;
            } else if (e.parent.left ==e) {
                e.parent.left = e.right;
                e.right.parent = e.parent;
            } else {
                e.parent.right = e.right;
                e.right.parent = e.parent;
            }
        } else {
            if (!this.isExternal(e.right)) {
                throw new RuntimeException("Cannot remove a node with two internal children.");
            }

            if (e == this.root) {
                this.root = e.left;
                e.left.parent = null;
            } else if (e.parent.left == e) {
                e.parent.left = e.left;
                e.left.parent = e.parent;
            } else {
                e.parent.right = e.left;
                e.left.parent = e.parent;
            }
        }

        this.size -= 1;
        return e.value;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    protected Entry<K,V> find(Entry<K,V> start, K key) {
        if (this.isExternal(start)) {
            return start;
        } else {
            int result = key.compareTo(start.key);
            if (result < 0) {
                return this.find(this.left(start), key);
            } else {
                return result > 0 ? this.find(this.right(start), key) : start;
            }
        }
    }

    public Entry<K,V> left(Entry<K,V>  e) {
        if (e.left == null) {
            throw new RuntimeException("This position does not have left child.");
        } else {
            return e.left;
        }
    }

    public Entry<K,V> right( Entry<K,V>  e) {
        if (e.right == null) {
            throw new RuntimeException("This position does not have right child.");
        } else {
            return e.right;
        }
    }

    public boolean isExternal(Entry<K,V> e) {
        return !this.hasLeft(e) && !this.hasRight(e);
    }

    public boolean isInternal(Entry<K,V> e) {
        return this.hasLeft(e) || this.hasRight(e);
    }

    public boolean hasLeft(Entry<K,V> e) {
        return e.left != null;
    }

    public boolean hasRight(Entry<K,V> e) {
        return e.right != null;
    }

    public void expandExternal(Entry<K,V> e, K key, V value) {
        if (this.isInternal(e)) {
            throw new RuntimeException("Not an external node");
        } else {
            e.key = key;
            e.value = value;
            e.left = this.newEntry(null,null,e);
            e.right = this.newEntry(null, null,e);
            this.size += 1;
        }
    }

    public Entry<K,V> parent(Entry<K,V> e) {
        if (e == null) {
            throw new RuntimeException("Cannot find parent of null position.");
        } else {
            return e.parent;
        }
    }

    public Entry<K,V> root() {
        if (this.root == null) {
            throw new RuntimeException("This tree has no root.");
        } else {
            return this.root;
        }
    }

    protected Entry<K,V> newEntry(K key, V value,Entry<K,V> parent) {
        return new Entry(key,value, parent);
    }

    private class Entry<K,V> {
        K key; // store the key ...
        V value; // ... and the value ...
        long insertionTime;// ... and the time this entry was inserted

        int height;

        Entry<K,V> parent;
        Entry<K,V> left;

        Entry<K,V> right;
        // constructor
        Entry(K key, V value,Entry<K,V> p) {
            this(key,value,null,p,null);
        }
        Entry(K key, V value, Entry<K,V> left,Entry<K,V> parent,Entry<K,V> right){
            this.key = key;
            this.value = value;
            this.parent = parent;
            this.left = left;
            insertionTime = System.nanoTime();
            this.right = right;
            this.height = 0;
        }

        public K key() {
            return this.key;
        }
        public V value() {
            return this.value;
        }
    }


}
