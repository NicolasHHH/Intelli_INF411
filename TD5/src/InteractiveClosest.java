import java.util.LinkedList;

class InteractiveClosest implements DrawListener {

	int nbpoints;
	KDTree tree;
	boolean keytypedbug;

	InteractiveClosest(int nbpoints) {
		this.nbpoints = nbpoints;
		keytypedbug = true;
		inittree();
	}
	

	void inittree() {
		System.out.printf("Initialisation avec %d points.%n", nbpoints);
		tree = null;
		for (int i = 0; i < nbpoints; i++) {
			tree = KDTree.insert(tree, new double[] { Math.random(), Math.random() });
		}
	}

	public void drawTree(KDTree tree, double minx, double maxx, double miny, double maxy) {
		if (tree == null)
			return;

		if (tree.point[0] < minx || tree.point[0] > maxx || tree.point[1] < miny || tree.point[1] > maxy) {
			System.out.println("Error at depth " + tree.depth);
			System.out.println("We should have " + minx + " < point[0] <" + maxx + " and " + miny + " < point[1] < "
					+ maxy + " but");
			if (tree.point[0] < minx)
				System.out.println("point[0] = " + tree.point[0] + " is not > " + minx);
			if (tree.point[0] > maxx)
				System.out.println("point[0] = " + tree.point[0] + " is not < " + maxx);
			if (tree.point[1] < miny)
				System.out.println("point[1] = " + tree.point[1] + " is not > " + miny);
			if (tree.point[1] > maxy)
				System.out.println("point[1] = " + tree.point[1] + " is not < " + maxy);
			draw.setPenColor(Draw.RED);
			draw.filledRectangle((minx+maxx)/2, (miny+maxy)/2, (maxx-minx)/2, (maxy-miny)/2);
			draw.show();
			throw (new RuntimeException("Arbre incorrect. point" + tree.pointToString() + ", depth=" + tree.depth));
		}

		draw.setPenColor(Draw.LIGHT_GRAY);
		if (tree.depth % 2 == 0)

		{
			if (tree.left != null || tree.right != null)
				draw.line(tree.point[0], miny, tree.point[0], maxy);
			drawTree(tree.left, minx, tree.point[0], miny, maxy);
			drawTree(tree.right, tree.point[0], maxx, miny, maxy);
		} else {
			if (tree.left != null || tree.right != null)
				draw.line(minx, tree.point[1], maxx, tree.point[1]);
			drawTree(tree.left, minx, maxx, miny, tree.point[1]);
			drawTree(tree.right, minx, maxx, tree.point[1], maxy);
		}
		draw.setPenColor(Draw.BLACK);
		draw.filledCircle(tree.point[0], tree.point[1], .002);

	}

	public void draw() {
		draw.clear();
		drawTree(tree, 0.0, 1.0, 0.0, 1.0);
	}

	@Override
	public void mousePressed(double x, double y) {
		mouseDragged(x, y);
	}

	@Override
	public void mouseDragged(double x, double y) {
		trace.clear();
		champions.clear();
		double[] closest = KDTree.closest(tree, new double[] { x, y });
		// System.out.printf("%f, %f%n", closest[0], closest[1]);
		this.draw();
		draw.setPenColor(Draw.BOOK_LIGHT_BLUE);
		for (double[] c : trace)
			draw.filledCircle(c[0], c[1], .005);

		draw.setPenColor(Draw.PRINCETON_ORANGE);
		champions.addLast(closest);
		double[] prevc = null;
		for (double[] c : champions) {
			draw.filledCircle(c[0], c[1], .005);
			if (prevc != null)
				draw.line(c[0], c[1], prevc[0], prevc[1]);
			prevc = c;
		}

		draw.setPenColor(Draw.BOOK_RED);
		draw.filledCircle(closest[0], closest[1], .005);
		draw.show();
	}

	@Override
	public void mouseReleased(double x, double y) {
	}

	@Override
	public void mouseClicked(double x, double y) {
	}

	@Override
	public void keyTyped(char c) {
		keytypedbug = !keytypedbug;
		if (keytypedbug)
			return;

		if (c == '+')
			nbpoints = nbpoints + nbpoints / 2 + 1;
		else if (c == '-')
			nbpoints = nbpoints - nbpoints / 3;

		inittree();
		draw();
		draw.show();
	}

	@Override
	public void keyPressed(int keycode) {
	}

	@Override
	public void keyReleased(int keycode) {
	}

	static void trace(double[] point, double[] champion) {
		if (dotrace) {
			trace.addLast(point);
			champions.addLast(champion);
		}
	}

	static Draw draw;
	static boolean dotrace = false;
	static LinkedList<double[]> trace = new LinkedList<double[]>();
	static LinkedList<double[]> champions = new LinkedList<double[]>();

	public static void main(String[] args) {
		draw = new Draw();
		InteractiveClosest.dotrace = true;
		draw.setCanvasSize(700, 700);
		draw.enableDoubleBuffering();
		InteractiveClosest obj = new InteractiveClosest(200);
		obj.draw();
		draw.show();
		draw.addListener(obj);
	}

}
