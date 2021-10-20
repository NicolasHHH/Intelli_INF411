
// 3.2 COLLISION ENTRE DEUX BILLES 

public class BallCollisionEvent extends Event {
	Ball a, b;
	// ...
	int nbA; 
	int nbB;
	
	
	BallCollisionEvent(Billiard billard, double time, Ball a, Ball b) {
		super(billard, time);
		
		this.a = a;
		this.b = b;
		// TODO (2 lignes)
		nbA = a.nbCollisions(); 
		nbB = b.nbCollisions();
	}
	
	@Override
	boolean isValid() {
		// TODO (1 ligne)
		return (a.nbCollisions()==nbA && b.nbCollisions()==nbB );
	}
	
	@Override
	void process() {
		
		// TODO (3 lignes)
		a.collideBall(b);
		billiard.predictCollisions(a);
		billiard.predictCollisions(b);
	}

}

