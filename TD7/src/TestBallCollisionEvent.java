import java.awt.Color;
import java.io.FileNotFoundException;

public class TestBallCollisionEvent {

	static public void main(String[] args) throws FileNotFoundException {
		CheckAssertions.check();
	
		System.out.print("BallCollisionEvent...            ");
		System.out.flush();

		{
			Billiard billiard = new Billiard();
			Ball a = new Ball(.1, .1, 1, 2, .05, 1, Color.BLACK);
			Ball b = new Ball(.2, .1, -1, 2, .05, 1, Color.BLACK);
			Ball c = new Ball(.4, .3, 0, 0, .05, .1, Color.BLACK);

			
			billiard.balls.add(a);
			billiard.balls.add(b);
			billiard.balls.add(c);

			
			Event e = new BallCollisionEvent(billiard, 1.0, a, b);

			assert e.isValid() : "Un évènement ne peut pas être caduc à sa création";
			e.process();

			assert a.x == .1 && a.y == .1 && b.x == .2 && b.y == .1 : "BallCollisionEvent.process"
					+ " ne doit pas modifier la position des billes";
			assert Math.abs(a.vx  + 1) < 1e-6 && a.vy == 2 && Math.abs(b.vx- 1) < 1e-6 && b.vy == 2 : "BallCollisionEvent.process"
					+ " doit appeler Ball.collideBall correctement";
			
			assert billiard.eventQueue.size() == 5 : "Il faut prévoir toutes les nouvelles collisions";

		}

		{
			Billiard billiard = new Billiard();

			Ball a = new Ball(10, 10, 1, 2, 1, 1, Color.BLACK);
			Ball b = new Ball(20, 10, -1, 2, 1, 1, Color.BLACK);
			Ball c = new Ball(0, 0, 0, 0, 1, 1, Color.BLACK);
			Event e = new BallCollisionEvent(billiard, 1.0, a, b);
			c.collideBall(a);
			assert !e.isValid() : "Un évènement concernant une bille doit être caduc"
					+ "si la bille subit une collision après la création de l'évènement";
		}

		{
			Billiard billiard = new Billiard();

			Ball a = new Ball(10, 10, 1, 2, 1, 1, Color.BLACK);
			Ball b = new Ball(20, 10, -1, 2, 1, 1, Color.BLACK);
			Ball c = new Ball(0, 0, 0, 0, 1, 1, Color.BLACK);
			Event e = new BallCollisionEvent(billiard, 1.0, a, b);
			c.collideBall(b);
			assert !e.isValid() : "Un évènement concernant une bille doit être caduc"
					+ "si la bille subit une collision après la création de l'évènement";
		}

		System.out.println("OK");
		System.out.flush();
		
		System.out.print("Ultimate test...                 ");

		{ 
			Billiard billiard = new Billiard("./TD7/init/p100-2K.txt");
			
			billiard.eventQueue.add(new StopEvent(billiard, 100.0));
			billiard.run();

			assert 0.6665313465324731 == billiard.balls.firstElement().x : "first element error";
			
			System.out.println("OK");
		
		}

	}
}
