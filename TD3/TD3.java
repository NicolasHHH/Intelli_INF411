
/* TD3. Classes disjointes (Union-Find) et connectivité d'un réseau 
 * Ce fichier contient 4 classes :
 * 	- PRNG : générateur de nombres pseudo-aléatoires,
 * 	- UnionFind : classes disjointes (vues en cours),
 * 	- Network : un réseau avec une méthode pour effectuer des appels 
 * 	aléatoires, et une autre qui renvoie le nombre d'éléments de 
 * 	la composante connexe d'un membre du réseau,
 * 	- NetworkSimulation : on observe l'évolution de la connectivité 
 * 	du réseau à mesure que l'on ajoute des liens (appels) aléatoirement. 
 */

class PRNG { // Générateur de nombres pseudo-aléatoires (Lagged Fibonacci Generator)
	
	private int[] state; // état des derniers termes
	private final int stateSize = 55; // taille de l'état
	private final int modulus; // base du calcul
	// Dans le cas où le générateur est utilisé pour générer un réseau,
	// modulus est le nombre d'utilisateurs i.e. la taille de la population.
	
	private int index = 0; // indice de la case à modifier au prochain appel à getNext
	private boolean warmUpDone = false; // y a-t-il déjà eu 55 appels (au moins) à getNext ?
	// non (false) / oui (true)
	// constructeur à partir du champ modulus
	PRNG(int modulus) {
		this.modulus = modulus;
		state = new int[stateSize];
	}

	// Question 1
	// calcule le terme suivant, met a jour l'état interne, met a jour l'indice, et
	// renvoie ce terme
	int getNext() {
		
		if( warmUpDone == false) {
			int i = index;
			state[i] = (((((300007 * i + 900021) % modulus) * i 
						+ 700018) % modulus) * i + 200007) % modulus;
			if (index == 54) {
				warmUpDone = true;
				return state[index++];
			}
		}
		else {
			state[(index%55)] = (state[((index-24)%55)]+ state[((index-55)%55)] )% modulus;
			
		}
		index = index+1;
		return state[((index-1)%55)];
	}
}

class UnionFind { // classes disjointes (vues en cours, amphi 3)

	// Question 3.1 (le champ size doit être ajouté)
	private int[] link; // le tableau des liens
	private int[] rank; // le tableau des rangs 
	private int numClasses; // nombre de classes
	private int[] size; // nombre d'éléments de chaque classe disjointe (président)

	// Question 3.1 (le constructeur doit-être modifié pour initialiser size)
	// constructeur
	UnionFind(int n) {
		if (n < 0)
			throw new IllegalArgumentException();
		link = new int[n];
		rank = new int[n];
		size = new int[n];
		for (int i = 0; i < n; i++) {
			link[i] = i;
			rank[i] = 0;
			size[i] = 1;
		}
		numClasses = n;
	}

	// renvoie le nombre de classes
	int numClasses() {
		return numClasses;
	}

	// renvoie le représentant d'un élément i
	int find(int i) {
		if (i < 0 || i >= link.length)
			throw new ArrayIndexOutOfBoundsException(i);
		int p = link[i];
		if (p == i)
			return i;
		int r = find(p);
		link[i] = r; // compression de chemin
		return r;
	}

	// teste si deux éléments i et j sont dans la même classe
	boolean sameClass(int i, int j) {
		return find(i) == find(j);
	}

	// renvoie le cardinal de l'ensemble
	int cardinal() {
		return link.length;
	}
	
	// Question 3.1 (la méthode union doit être modifiée pour mettre à jour size)
	// fait l'union des classes des deux éléments i et j
	void union(int i, int j) {
		int ri = find(i);
		int rj = find(j);
		if (ri != rj) {
			numClasses--;
			if (rank[ri] < rank[rj]) { // rj devient le représentant
				link[ri] = rj;
				size[rj] = size[rj] + size[ri];
			} 
			else {
				link[rj] = ri; // ri devient le représentant
				size[ri]  = size[rj] + size[ri];
				if (rank[ri] == rank[rj]) {
					rank[ri]++;
				}
			}
		}
		
	}

	// Question 3.1
	// renvoie le nombre d'éléments de la classe de l'élément i
	int getSize(int i) {
		return size[find(i)];
	}
}

class Network { // représente un réseau téléphonique

	UnionFind relations; // structure de classes disjointes (UnionFind)
	PRNG prng; // generateur de nombres pseudo-aleatoires (PRNG)
	int nbCalls = 0; // nombre d'appels
	int nbDistinctCalls = 0; // nombre d'appels vers un numero distinct
	int caller = 0; // le numéro de l'appelant du dernier appel
	int callee = 0; // appele

	Network(int size) {
		relations = new UnionFind(size);
		prng = new PRNG(size);
	}

	// Question 2.1
	// utilise le PRNG pour simuler un appel en établissant le lien 
	// entre les deux numeros générés.
	void nextCall() {
		caller =  prng.getNext();
		callee =  prng.getNext();
		relations.union(caller, callee);
		nbCalls ++;
		if (caller != callee) {
			nbDistinctCalls ++;
		}
		
	}

	// Question 3.2
	// renvoie la taille de la composante connexe (autrement dit la classe) de i
	int getSize(int i) {
		int ri = relations.find(i);
		return relations.getSize(ri);
	}

}

class NetworkSimulation { // simulations sur le réseau

	static Network network;
	
	// Question 2.2
	// renvoie le nombre d'appels effectués jusqu'à un appel entrant ou sortant du président
	static int simulation22(int president, int population) {
		
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'a obtenir un appel entrant ou sortant du président.
		while (network.caller != president && network.callee != president) {
			network.nextCall();
		}
		return network.nbDistinctCalls;
		
	}

	// Question 3.3
	// fait des appels jusqu'à un appel entrant ou sortant du président 
	// et renvoie la taille de la composante connexe du président 
	
	
	
	static int simulation33(int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'a obtenir un appel entrant
		// ou sortant du président.
		while (network.caller != president && network.callee != president) {
			network.nextCall();
		}
		return network.getSize(president);
		
		
		
	}

	// Question 4
	// renvoie le nombre d'appels effectués (on ignore les appels au répondeur) jusqu'à ce que le président soit connecté à 99% du réseau
	static int simulation4(int president, int population) {
		// initialisation du réseau
		network = new Network(population);
		// remplissons le réseau de relations jusqu'à ce que la classe
		// du président contienne 99% de la population.
		while (network.getSize(president)< 0.99*population) {
			network.nextCall();
		}
		return network.nbDistinctCalls;
		
		
	}
}

// Question 5 (optionnelle)
// une variante de PRNG permettant d'aller jusqu'à modulus = 100000000

class PRNGLarge {
	
	private int[] state; // état des derniers termes
	private final int stateSize = 55; // taille de l'état
	private final long modulus; 
	
	private long index = 0; // indice de la case à modifier au prochain appel à getNext
	private boolean warmUpDone = false; 
	
	PRNGLarge(long modulus) {
		this.modulus = modulus;
		state = new int[stateSize];
	}
	
	int getNext() {
		int i = (int)(index%55);
		int j = (int)((index-24)%55);
		int k = (int)((index-55)%55);
		
		if( warmUpDone == false) {
			int l = (int)index;
			state[l] = (int)( (((((300007*index+900021)%modulus)*index+700018)%modulus) * index + 200007) % modulus);
			if (l == 54) {
				warmUpDone = true;
				index++;
				return state[l];
			}
		}
		else {
			state[i] = (int)((state[j]+ state[k])% modulus);
		}
		index = index+1;
		return state[i];
	}
	
	// note : bien que les calculs nécessitent maintenant le type long en interne
	// la méthode getNext continue de renvoyer une valeur de type int 
	//(int)(((((300007 * i + 900021) % modulus) * i 
	          // + 700018) % modulus) * i + 200007) % modulus;
	
}
