import java.util.Arrays;

class Fenwick {
	Fenwick left;
	Fenwick right;
	final int lo;
	final int hi;
	int acc;

	// Question 2.1

	// Constructeur
	Fenwick(int lo, int hi,int d) {
		this.lo = lo;
		this.hi = hi;
		if (hi-lo == 1) {
			acc = lo;
		}
		else {
			left = new Fenwick(lo,(lo+hi)/2);
			right = new Fenwick((lo+hi)/2,hi);
			acc = left.acc + right.acc;
		}
	}
	Fenwick(int lo, int hi) {
		assert lo < hi;
		this.lo = lo;
		this.hi = hi;
		this.acc = 0;
		if (hi > lo + 1) {
			int A = (hi + lo) / 2;
			this.left = new Fenwick(lo, A);
			this.right = new Fenwick(A, hi);
		}
	}

	// Question 2.2

	// renvoie la feuille associée à l'intervalle [i,i+1[ si elle existe et null sinon.
	Fenwick get(int i) {
		if (i < lo || i >= hi)
			return null;
		Fenwick cur = this;
		while (cur.left != null)
			cur = (cur.right.lo <= i) ? cur.right : cur.left;
		return cur;
	}
	Fenwick get2(int i) {
		if(lo ==i && hi ==i +1) {
			return this;
		}
		else {
			if((lo+hi)/2>i && left != null) {
				return left.get(i);
			}
			else if( (lo+hi)/2<=i && right != null) {
				return right.get(i);
			}
			return null;
		}
	}

	// Question 2.3

	// augmente la valeur stockée dans la case d'indice i du tableau
	// et actualise l'arbre pour maintenir les propriétés d'un arbre de Fenwick.
	void inc(int i) {
		if( lo<= i && i <hi) {
			acc +=1 ;
			if(hi-lo >= 2) {
				left.inc(i);
				right.inc(i);
			}
		}
	}

	// Question 2.4

	// renvoie la somme des valeurs des cases d'indice < i
	int cumulative(int i) {
		if (i >= hi) return acc;
		if( lo< i && i <hi) {
			if(hi-lo == 1) {
				return acc;
			}
			if((lo+hi)/2>i ) {
				return left.cumulative(i);
			}
			else if( (lo+hi)/2<=i) {
				return left.acc  + right.cumulative(i);
			}
		}
		return 0;
	}

}

class CountInversions {

	// Question 3.1 :

	// implémentation naive, quadratique pour calculer le nombre d'inversions
	static int countInversionsNaive(int[] a) {
		int inv = 0;
		for(int i = 0; i< a.length;i++) {
			for(int j = i+1; j< a.length;j++) {
				if(a[i]>a[j]) inv ++;
			}
		}
		return inv;
	}

	// Question 3.2 :

	// une implémentation moins naive, mais supposant une plage d'éléments pas trop
	// grande espace O(max-min)

	static int countInversionsFen(int[] a) {
		if (a.length == 0) return 0;
		int count = 0;
		int min = a[0],max = a[0];
		for(int x: a ) {
			if(x<min) min = x;
			if(x>max) max = x;
		}
		Fenwick Fk = new Fenwick(min,max+1);
		for(int i = a.length-1;i>=0;i--) {
			Fk.inc(a[i]);
			count += Fk.cumulative(a[i]);
		}
		return count; 
	}

	// Question 3.3

	// une implémentation encore meilleure, sans rien supposer sur les éléments
	// cette fois
	static int countInversionsBest(int[] a) {
		int[] b = Arrays.copyOf(a,a.length);
		Arrays.sort(b);
		
		for(int i = 0; i< a.length;i++) {
			int j = Arrays.binarySearch(b,a[i]);
			a[i]=j;
		}
		
		return countInversionsFen(a);
	}

}
