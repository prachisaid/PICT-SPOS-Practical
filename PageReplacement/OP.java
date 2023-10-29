public class OP {
    public static void main(String[] args) {
        
    }

    private static boolean search(int key, int[] arr){
        for(int i = 0; i < arr.length; i++){
            if(arr[i] == key) return true;
        }

        return false;
    }

    private static int predict(int[] pages, int[] set, int n, int index){
        int res = -1;
        int farthest = index;

        for(int i = 0; i < set.length; i++){
            int j = index;

            for(j = index; j < n; j++){
                if(set[i] == pages[j]){
                    if(j > farthest){
                        farthest = j;
                        res = i;
                    }
                    break;
                }
            }

            if(j == n){
                return i;
            }
        }

        return (res == -1) ? 0 : res;
    }

    private void optimalPage(int[] pages, int n, int capacity){
        int[] set = new int[capacity];

        int hit = 0;
        int index = 0;

        for(int i = 0; i < n; i++){
            if(search(pages[i], set)){
                hit++;
                continue;
            }
            
            if(index < capacity) {
                set[index++] = pages[i];
            }
            else{
                int j = predict(pages, set, n, (i + 1));
                set[j] = pages[i];
            }
        }
    }
}
