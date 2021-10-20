public class Test33 extends TestClosest {
	
	Test33() {
		name = "closest() [version optimisee]";
	}

	double[] closest(KDTree tree, double[] a) {
		return KDTree.closest(tree, a);
	}
	
	public static void main(String[] args) {
		Test33 test = new Test33();
		test.testClosest(10, 10);
		test.testClosest(100, 100);
		test.testClosest(20000, 1000);
		test.testClosest(100000, 100000);
	}

}