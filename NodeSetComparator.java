import java.util.Comparator;

public class NodeSetComparator implements Comparator <Node> {
	
	public int compare(Node x, Node y) {

		String x_set = x.getSet();
		String y_set = y.getSet();

		if(x_set.length() > y_set.length())
			return -1;

		else 
			return 1;
	}
}