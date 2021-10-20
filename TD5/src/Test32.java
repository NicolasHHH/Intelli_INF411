public class Test32 extends TestClosest {

	Test32 () {
		name = "closestNaive()";
	}
	
	double[] closest(KDTree tree, double[] a) {
		return KDTree.closestNaive(tree, a);
	}
	
	public static void main(String[] args) {
		Test32 test = new Test32();
		test.testClosest(10, 10);
		test.testClosest(100, 100);
		test.testClosest(20000, 1000);
	}

}