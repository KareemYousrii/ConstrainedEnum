import java.util.LinkedList;

public class Node
{
	private String set;
	private int constraint;
	private LinkedList <Node> adjList;

	public Node(String s, int c) {
		this.set = s;
		this.constraint = c;
                adjList = new <Node> LinkedList();
	}

        public void setConstraint(int constraint) {
            this.constraint = constraint;
        }

	public String getSet() {
		return set;
	}

	public int getConstraint() {
		return constraint;
	}

	public LinkedList getAdj() {
		return adjList;
	}
}

//        int [] indices = mapCharToIndex(s);
//        int [] count = new int [substrings.length];
//        
//        
//        for(int i = 0; i < substrings.length; i++) {
//            
//            String value = "";
//            for(int j = 0; j < indices.length; j++) {
//                value += substrings[i].charAt(indices[j]);
//            }
//            
//            int base = getBase(s);
//            int index = Integer.parseInt(value, base);
//            index %= count.length;
//            count[index]++;
//        }
//        
//        int sum = 0;
//        
//        for(int i = 0; i < count.length; i++) {
//            sum += count[i];
//        }
//        
//        return sum;
//
//
//        int [] indices = mapCharToIndex(s);
//        Set <String> tmp_instances = new LinkedHashSet <String> ();
//
//        for(int i = 0; i < substrings.length; i++)
//        {
//            String tmp = substrings[i];
//            String res = "";
//
//            for(int j = 0; j < indices.length; j++) {
//                res += tmp.charAt(indices[j]);
//            }
//
//            tmp_instances.add(res);
//        }
//
//        return tmp_instances.size();