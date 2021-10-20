
// 3.1 COLLISION AVEC UNE BANDE

public class WallCollisionEvent extends Event {
	// Les champs suivants sont hérités de Event:
	// Billiard billiard;
	// final double time;
	
	Direction direction; // Horizontal, vertical
	Ball ball; 			 // le ball qui intervient dans l'évènement
	int nbCollisions;    // pour mettre à jour celui du ball ? 
	
	WallCollisionEvent(Billiard billiard, double time, Ball ball, Direction direction) {
		// constructeur hérité;
		super(billiard, time); 
		
		// constructions propres
		this.ball = ball;
		this.direction = direction;
		this.nbCollisions = ball.nbCollisions();
	}
	
	@Override
	boolean isValid() {
		// TODO (1 ligne)
		return this.nbCollisions == ball.nbCollisions();
	}
	
	@Override
	void process() {
		// TODO (2 lignes)
		// collideWall 
		ball.collideWall(direction);
		billiard.predictCollisions(ball);
		
	}
}
