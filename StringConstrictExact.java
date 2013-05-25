import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.Stack;

public class StringConstrictExact
{
    Set <Character> variables;
    Set <String> sets;
    HashMap <String, Integer> costs;
    PriorityQueue <Node> queue;
    String [] instances;
    
    HashMap <String, int[]> index_mappings;
    
    int max = 0;

    public StringConstrictExact(Set <Character> variables, Set <String> sets, HashMap <String, Integer> costs) 
    {
        this.variables = variables;
        this.sets = sets;
        this.costs = costs;
        index_mappings = new HashMap <String, int[]> ();
        
        generateInstances();
        mapCharToIndex();
        System.out.println("exit constructor");
    }

    /* Generate instances of the elements of all given values */
    private String [] generateInstances()
    {   
        int num_instances = 1;
        Iterator var_iter;

        /* Generate a 2D array containing the elements of each variable */
        var_iter = variables.iterator();
        String elements [][] = new String [variables.size()][];

        for(int i = 0; var_iter.hasNext(); i++ )
        {
            int constraint = costs.get(var_iter.next() + "");
            num_instances *= constraint;
            elements[i] = new String [constraint];

            for(int j = 0; j < constraint; j++)
            {
                elements[i][j] = "" + j;
            }
        }

        /* Create a priority queue with the required size */
        instances = new String [num_instances];

        /* Permute the elements of each two variables at a time */
        ArrayList <String> res;
        String [] tmp = elements[0];

        for(int i = 1; i < elements.length; i++)
        {   
            res = new ArrayList <String> ();
            for(int j = 0; j < tmp.length; j++)
            {
                for(int k = 0; k < elements[i].length; k++)
                {
                    res.add( tmp[j] + elements[i][k]);
                }
            }

            tmp = res.toArray(new String [res.size()]);
        }
            
        instances = tmp;
        return instances;
    }

    public void constrain()
    {
        boolean [] curr = new boolean[instances.length];
        curr[0] = true;
        
        Set [] currValues = new Set [costs.size() - variables.size()];
        
        for(int i = 0; i < costs.size() - variables.size(); i++) {
            currValues[i] = new HashSet <String> ((int)(instances.length / 0.75) + 1);
        }
        
        constrainHelper(curr, 1, currValues);
    }
    
    public void constrainHelper(boolean [] current, int size, Set [] currValues)
    {
        Stack <boolean []> currInstances = new Stack();
        ArrayList <Object []> additions = null;
        
        for(int i = 1; i < instances.length; i++)
        {
            if(current[i]) {
                continue;
            }

            else {
                boolean [] new_value = new boolean[instances.length];
                System.arraycopy(current, 0, new_value, 0, instances.length);
                new_value[i] = true;
                
                additions = addCount(instances[i], currValues);


                if(!this.satisfiesConstraint(currValues)) {

                    if(size > max) {
                        max = size;
                    }
                }

                else {
                    currInstances.push(new_value);
                }
            }
        }
        
        while(!currInstances.isEmpty())
        {
            constrainHelper(currInstances.pop(), size + 1, currValues);
        }
        
        for(int i = 0; i < additions.size() && additions != null; i++)
        {
            Object [] tmp = additions.get(i);
            currValues[(Integer)tmp[0]].remove((String)tmp[1]);
        }
    }

    public boolean satisfiesConstraint(Set [] currValues) 
    {   
        int index = 0;
        for(Iterator <String> iter = sets.iterator(); iter.hasNext(); index++)
        {
            String curr_set = iter.next();
            
            if(currValues[index].size() > costs.get(curr_set)) {
                return false;
            }
        }
        
        return true;
    }
    
    private ArrayList <Object []> addCount(String inst, Set [] currValues)
    {
        int index = 0;
        ArrayList <Object []> additions = new ArrayList <Object []> ();
        for(Iterator <String> iter = sets.iterator(); iter.hasNext(); index++)
        {
            String curr_set = iter.next();
            
            int [] indices = index_mappings.get(curr_set);
            
            String res = ""; 
            for(int j = 0; j < indices.length; j++)
            {
                res += inst.charAt(indices[j]);
            }
            
            if(!currValues[index].contains(res)) {
                currValues[index].add(res);
                Object [] tmp = {index, res};
                additions.add(tmp);
            }
        }
        return additions;
    }
    
    private void mapCharToIndex()
    {
        HashMap <Character, Integer> var_mappings = new HashMap <Character, Integer> ();
        Iterator <Character> var_iter = variables.iterator();
        
        for(int i = 0; i < variables.size(); i++) {
            var_mappings.put(var_iter.next(), i);
        }
        
        Iterator <String> sets_iter = sets.iterator();
        
        for(int i = 0; i < sets.size(); i++) {
            
            String tmp = sets_iter.next();
            int [] indices = new int[tmp.length()];
            
            for(int j = 0; j < tmp.length(); j++) {
                indices[j] = var_mappings.get(tmp.charAt(j));
            }
            index_mappings.put(tmp, indices);   
        }
    }
}