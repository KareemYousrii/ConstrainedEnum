import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.Stack;
import sun.security.util.Length;

public class StringConstrictsad
{
	Set <String> variables;
	Set <String> sets;
	HashMap <String, Integer> costs;
        PriorityQueue <Node> queue;
        LinkedList <Instance> permutations;
        LinkedList <Instance> used;

	Node root;

	public StringConstrict(Set <String> variables, Set <String> sets, HashMap <String, Integer> costs) 
        {

		this.variables = variables;
		this.sets = sets;
		this.costs = costs;

		/* Node length comparator */
		Comparator <Node> comparator = new NodeSetComparator(); 

		int num_sets = sets.size();
	 	queue = new PriorityQueue <Node> (num_sets, comparator);

		/* Create a node for each set */
		Iterator iter = sets.iterator();
		while (iter.hasNext()) {

			String tmp = (String) iter.next();
			int constraint = costs.get(tmp);
			queue.add(new Node(tmp, constraint));
		}
                
                /* Convert queue into Array */
                root = queue.peek();
                Node [] nodes = new Node [queue.size()];
                for(int i = 0; !queue.isEmpty(); i++)
                {
                    nodes[i] = (Node)queue.poll();
                }
                
                /* Generate the tree */
		for(int i = 0; i < nodes.length; i++)
		{
			int min_len = Integer.MAX_VALUE;
			LinkedList <Node> adjacent = new LinkedList <Node> ();
			Node currNode = nodes[i];

			for(int j = 0; j < i; j++)
			{
				Node tmpNode = nodes[j];

				/* If set A is strictly a subset of B */
				if(tmpNode.getSet().contains(currNode.getSet()) && !(tmpNode.getSet().equals(currNode.getSet()))) {

					if(tmpNode.getSet().length() < min_len)
                                        {
                                            adjacent = new LinkedList <Node> (); //Reset the LinkedList
                                            min_len = tmpNode.getSet().length();
                                        }
                                
					/* Add the parent to the adjacency list */
						adjacent.add(nodes[j]);
				}
			}

			/* Add the nodes to the adjacent list of the parent nodes */
			while(!adjacent.isEmpty()) {
				Node tmp = (Node) adjacent.pop();
				tmp.getAdj().add(currNode);
                                System.out.println(currNode.getSet() + " is the child of " + tmp.getSet());
			}
		}
	}
        
        /* Generate permutation of the elements of all given values */
        public LinkedList <Instance> generatePermutations()
        {   
            int num_permutations = 1;
            Iterator var_iter;
            
            /* Generate a 2D array containing the elements of each variable */
            var_iter = variables.iterator();
            String elements [][] = new String [variables.size()][];
            
            for(int i = 0; var_iter.hasNext(); i++ )
            {
                int constraint = (Integer) costs.get((String)var_iter.next());
                num_permutations *= constraint;
                elements[i] = new String [constraint];
                
                for(int j = 0; j < constraint; j++)
                {
                    elements[i][j] = "" + j;
                }
            }
            
            /* Create a priority queue with the required size */
            permutations = 
                    new LinkedList <Instance> ();
            used = new LinkedList <Instance> ();
            
            /* Permute the elements of each two variables at a time */
            ArrayList <String> result;
            String [] tmp = elements[0];
            
            for(int i = 1; i < elements.length; i++)
            {   
                result = new ArrayList <String> ();
                for(int j = 0; j < tmp.length; j++)
                {
                    for(int k = 0; k < elements[i].length; k++)
                    {
                        result.add( tmp[j] + elements[i][k]);
                    }
                }
                
                tmp = result.toArray(new String [result.size()]);
            }
            
              for(int i = 0; i < tmp.length; i++) {
                  permutations.add(new Instance(tmp[i]));
              }
              
              Iterator <Instance> it = permutations.iterator();
              while(it.hasNext())
              {
                System.out.println(it.next().value);  
              }
              
              return permutations;
        }
        
        public int enumerate()
        {
            enumerate(root);
            return permutations.size();
        }
        
        public void enumerate(Node n)
        {
            LinkedList <Node> adj = n.getAdj();
            
            /* If it's a leaf node, return its constraint */
            if(adj.size() == 0) {
                return ;
            }
            
            /* Else, return the constraint of its children combined */
            Iterator <Node> iter = adj.iterator();
            int rec_constraint = 1; //malhash lazma

            while(iter.hasNext()) {
                  enumerate(iter.next());
            }
            
            rec_constraint = 1;
            for(int i = 0; i < n.getSet().length(); i++)
            {
                rec_constraint *= costs.get(n.getSet().charAt(i) + "");
            }
            
            if (n.getConstraint() < rec_constraint) {
                
                int times = rec_constraint - n.getConstraint();
                for(int i = 0; i < times; i++)
                    satisfyConstraint(n);
            }

            else if (n.getConstraint() > rec_constraint) {
                n.setConstraint(rec_constraint);
            }

            return;
        }
        
        public int [] mapCharToIndex(String set)
        {
            int [] indices = new int [set.length()];
            
            for(int i = 0; i < set.length();i++) {
                Iterator <String> var_iter = variables.iterator();
                int index = 0;
                
                while(var_iter.hasNext())
                {
                    if(set.charAt(i) == var_iter.next().charAt(0)) {
                        indices[i] = index;
                        break;
                    }
                    index++;   
                }
            }
            return indices;       
        }
        
        /* The number of instances to be removed inorder to remove a value of a 
         * String 'set' is dependent upon the number of variables not within set */
        public int getTargetCount(String set) 
        {
            int target_count = 1;
            
            for(Iterator <String> var_iter = variables.iterator(); var_iter.hasNext();) {
                
                String tmp = var_iter.next();
                
                if(!set.contains(tmp)) {
                    target_count *= costs.get(tmp) ;
                }
            }
            
            return target_count;
        }
        
        /* Compare two permutations, one represented as a Permutation 
         * and the other as an array of chars */
        public boolean subset(Instance curr, char [] values, int [] indices) 
        {
            
            boolean flag = true;
            for( int i = 0; i < indices.length; i++ ) {

                if( curr.value.charAt(indices[i]) != values[i] ) {

                    flag = false;
                    break;
                }

            }
            return flag;
        }
        
        public int updateCount(Node n)
        {
            int [] indices = mapCharToIndex(n.getSet());
            Set <String> instances = new LinkedHashSet <String> ();

            for(Iterator <Instance> iter = permutations.listIterator(); iter.hasNext();)
            {
                String tmp = iter.next().value;
                String res = "";
                
                for(int i = 0; i < indices.length; i++) {
                    res += tmp.charAt(indices[i]);
                }
                
                instances.add(res);
            }
            
            return instances.size();
        }
        
        /* minus awel char fel set of variables */
        public void satisfyConstraint(Node n)
        {
            /* mapping from variables to indices of the String */
            int [] indices = mapCharToIndex(n.getSet());
            
            int target_count = getTargetCount(n.getSet());
            
            /* A pointer to the permutation we are currently checking against */
            int pointer = 0;
            
            /* The maximum count of instances of these values */
            int max_count = 0;
            char [] max_values = new char[indices.length];
            Stack <Instance> max = new Stack <Instance> ();
            
            /* Iterate over the used permutation, checking against each one */
            for(Iterator <Instance> iter = used.listIterator(); iter.hasNext(); pointer++) {
                
                /* Keep track of an element in the list */
                Instance curr = iter.next();
                
                /* If this permutation has been used by the current set, 
                 * move on to the next permutation
                 */
                if(curr.used_by.contains(n.getSet())) {
                    continue;
                }
                
                /* Count of instances of these values */
                int count = 0;
                
                /* Extract the values for the reference permutation */
                char [] values = new char [indices.length];
                for(int i = 0; i < indices.length; i++) {
                        values[i] = curr.value.charAt(indices[i]);
                }
                
                Instance tmp;
                
                /* Iterate over the list of used permutations 
                 * looking for instances with the specified values,
                 * should one be found, increment the counter
                 */
                for(Iterator <Instance> iter_2 = used.listIterator(pointer); iter_2.hasNext();) {
                    
                    tmp = iter_2.next();
                    
                    /* If the current permutation is 
                     * a subset of the reference permutation */
                    boolean flag = subset(tmp, values, indices);
                  
                    if(flag) {
                        count++;
                        max.add(tmp);
                        
                        if(count == target_count) {
                            break;
                        }
                    }
                }
                
                /* Maintain the global maximum */
                if (count > max_count) {
                    max_count = count;
                    
                    /* Copy values into max_values */
                    System.arraycopy(values, 0, max_values, 0, values.length);
                    
                        if(max_count == target_count) {
                            break;
                        }
                }
                
                pointer++;
            }
            
            /* All the permutations were satisfied from the 'used' list */
            if(max_count == target_count) {
                while(!max.isEmpty())
                    max.pop().used_by.add(n.getSet());
            }
            
            else if (max_count < target_count && max_count > 0) {
                
                int count = 0;
                for(Iterator <Instance> iter = permutations.listIterator(); iter.hasNext();) {
                    /* Keep track of an element in the list */
                    Instance curr = iter.next();
                    
                    /* If the current permutation is 
                     * a subset of the reference permutation */
                    
                    boolean flag = subset(curr, max_values, indices);

                    if(flag) {
                        curr.used_by.add(n.getSet());
                        used.add(curr);
                        iter.remove();
                        count++;
                        
                        if(count == target_count)
                            break;
                    }
                }
            }
            
            /* We have found none */
            else {
                
                Instance head = permutations.pollFirst();
                head.used_by.add(n.getSet());
                used.add(head);
                
                char [] values = new char [indices.length];
                   
                for(int i = 0; i < indices.length; i++) {
                    values[i] = head.value.charAt(indices[i]);
                }
                
                int count = 1; //We already removed one instance
                for(Iterator <Instance> iter = permutations.listIterator(); iter.hasNext();)
                {
                    Instance curr = iter.next();
                    
                    /* If the current permutation is 
                     * a subset of the reference permutation */
                    
                    boolean flag = subset(curr, values, indices);
                    
                    if(flag) {
                        curr.used_by.add(n.getSet());
                        used.add(curr);
                        iter.remove();
                        count++;
                        
                        if(count == target_count)
                            break;
                    }
                }
            }
            
        }
}
