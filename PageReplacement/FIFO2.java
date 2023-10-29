import java.util.*;

public class FIFO2 {
    public static void main(String[] args) {
        int[] pages = {4, 5, 6, 7, 8, 9};
        System.out.println(pageFaults(pages, 6, 3));
    }

    private static int pageFaults(int[] pages, int n, int capacity){
        HashSet<Integer> set = new HashSet<>(capacity);
        Queue<Integer> queue = new LinkedList<>();

        int pageFault = 0;

        for(int i = 0; i < n; i++){
            if(set.size() < capacity){
                if(!set.contains(pages[i])){
                    set.add(pages[i]);
                    queue.add(pages[i]);
                    pageFault++;
                }
            }
            else{
                if(!set.contains(pages[i])){
                    int val = queue.peek();

                    queue.remove();
                    set.remove(val);
                    
                    set.add(pages[i]);
                    queue.add(pages[i]);
                    pageFault++;
                }
            }
        }

        return pageFault;
    }
}
