import java.awt.Color;
import java.io.FileNotFoundException;

public class TestWallCollisionEvent {

	static public void main(String[] args) throws FileNotFoundException {
		CheckAssertions.check();
		
		System.out.print("WallCollisionEvent...            ");
		System.out.flush();

		{		
			Billiard billiard = new Billiard();

			Ball b = new Ball(.5, .5, 1, 2, .1, 1, Color.BLACK);
			billiard.balls.add(b);
			
			// Pour que le compteur de collision ne soit pas nul
			b.collideWall(Direction.Horizontal);
			b.collideWall(Direction.Horizontal);
			
			Event e = new WallCollisionEvent(billiard, 1.0, b, Direction.Horizontal);

			assert e.isValid() : "Un évènement ne peut pas être caduc à sa création";
			e.process();

			assert b.x == .5 && b.y == .5 : "WallCollisionEvent.process ne doit pas modifier la position des billes";
			assert b.vx == 1 && b.vy == -2 : "WallCollisionEvent.process doit appeler Ball.collideWall correctement";
			
			assert billiard.eventQueue.size() == 2 : "Il faut prévoir les nouvelles collisions";
			
		}

		{
			Billiard billiard = new Billiard();

			Ball b = new Ball(.5, .5, 1, 2, .1, 1, Color.BLACK);
			billiard.balls.add(b);
			
			// Pour que le compteur de collision ne soit pas nul
			b.collideWall(Direction.Horizontal);
			b.collideWall(Direction.Horizontal);
			b.collideWall(Direction.Horizontal);
			b.collideWall(Direction.Horizontal);
			
			Event e = new WallCollisionEvent(billiard, 1.0, b, Direction.Vertical);

			assert e.isValid() : "Un évènement ne peut pas être caduc à sa création";
			e.process();

			assert b.x == .5 && b.y == .5 : "WallCollisionEvent.process"
					+ " ne doit pas modifier la position des billes";
			assert b.vx == -1 && b.vy == 2 : "WallCollisionEvent.process"
					+ " doit appeler Ball.collideWall correctement";
			
			assert billiard.eventQueue.size() == 2 : "Il faut prévoir les nouvelles collisions";

		}

		{
			Billiard billiard = new Billiard();

			Ball b = new Ball(.5, .5, 1, 2, .1, 1, Color.BLACK);
			billiard.balls.add(b);

			Event e = new WallCollisionEvent(billiard, 1.0, b, Direction.Horizontal);
			b.collideWall(Direction.Horizontal);
			assert !e.isValid() : "Un évènement concernant une bille doit être caduc"
					+ " si la bille subit une collision après la création de l'évènement";
		}

		System.out.println("OK");
	}
}
