import java.io.FileNotFoundException;

class GraphicalBilliard extends Billiard {

	GraphicalBilliard() {
		super();
	}

	GraphicalBilliard(String path) throws FileNotFoundException {
		super(path);
	}

	@Override
	void initialize() {
		super.initialize();

		StdDraw.setCanvasSize(600, 600);
		StdDraw.enableDoubleBuffering();
		eventQueue.add(new RedrawEvent(this, 0.0));
	}

	public static void main(String[] args) throws FileNotFoundException {
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/billiards2.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/billiards4.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/billiards5.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/brownian.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/brownian2.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diagonal.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diagonal1.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diagonal2.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diffusion.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diffusion2.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/diffusion3.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p10.txt");
	    // GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p100-.5K.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p100-.125K.txt");
		 //GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p100-2K.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p1000-.5K.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p1000-2K.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/p2000.txt");
		 //GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/pendulum.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/squeeze.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/squeeze2.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/wallbouncing.txt");
		// GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/wallbouncing2.txt");
		GraphicalBilliard billiard = new GraphicalBilliard("./TD7/init/wallbouncing3.txt");

		billiard.run();
	}

}
