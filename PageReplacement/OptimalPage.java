class OptimalPage {

	static boolean search(int key, int[] fr)
	{
		for (int i = 0; i < fr.length; i++)
			if (fr[i] == key)
				return true;
		return false;
	}

	static int predict(int pages[], int[] set, int n, int index)
	{
		int res = -1, farthest = index;
		for (int i = 0; i < set.length; i++) {
			int j;
			for (j = index; j < n; j++) {
				if (set[i] == pages[j]) {
					if (j > farthest) {
						farthest = j;
						res = i;
					}
					break;
				}
			}

			if (j == n)
				return i;
		}

		return (res == -1) ? 0 : res;
	}

	static void optimalPage(int pages[], int n, int capacity){
		int[] set = new int[capacity];

		int hit = 0;
		int index = 0;
		for (int i = 0; i < n; i++) {

			if (search(pages[i], set)) {
				hit++;
				continue;
			}

			if (index < capacity)
				set[index++] = pages[i];

			else {
				int j = predict(pages, set, n, i + 1);
				set[j] = pages[i];
			}
		}
		System.out.println("No. of hits = " + hit);
		System.out.println("No. of misses = " + (n - hit));
	}

	public static void main(String[] args){
		int pg[] = { 7, 0, 1, 2, 0, 3, 0, 4, 2, 3, 0, 3, 2 };
		int pn = pg.length;
		int fn = 4;
		optimalPage(pg, pn, fn);
	}
}