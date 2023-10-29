import java.util.*;

public class LRU2 {
    public static void main(String[] args) {
        int[] pages = {7, 8, 9, 10};
        System.out.println(pageFaults(pages, 4, 2));
    }

    private static int pageFaults(int[] pages, int n, int capacity){
        HashSet<Integer> set = new HashSet<>(capacity);
        HashMap<Integer, Integer> map = new HashMap<>();

        int pageFault = 0;
        for(int i = 0; i < n; i++){

            if(set.size() < capacity){
                if(!set.contains(pages[i])){
                    set.add(pages[i]);
                    pageFault++;
                }
                
                map.put(pages[i], i);
            }
            else{
                if(!set.contains(pages[i])){
                    int lru = Integer.MAX_VALUE;
                    int val = Integer.MIN_VALUE;

                    Iterator<Integer> it = set.iterator();

                    while(it.hasNext()){
                        int temp = it.next();

                        if(map.get(temp) < lru){
                            lru = map.get(temp);
                            val = temp;
                        }
                    }
                    
                    set.remove(val);
                    set.add(pages[i]);
                    map.remove(val);
                    pageFault++;
                }

                map.put(pages[i], i);
            }
        }

        return pageFault;
    }
}
