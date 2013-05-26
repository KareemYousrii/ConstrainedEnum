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
    HashMap [] currValues;
    
    int max = 0;

    public StringConstrictExact(Set <Character> variables, Set <String> sets, HashMap <String, Integer> costs) 
    {
        this.variables = variables;
        this.sets = sets;
        this.costs = costs;
        index_mappings = new HashMap <String, int[]> ();
        currValues = new HashMap [costs.size() - variables.size()];
        
        generateInstances();
        mapCharToIndex();
        
        for(int i = 0; i < costs.size() - variables.size(); i++) {
            currValues[i] = new HashMap <String, Integer> ((int)(instances.length / 0.75) + 1);
        }
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

    public int constrain()
    {
        boolean [] curr = new boolean[instances.length];
        curr[0] = true;
        
        constrainHelper(curr, 1, 0);
        return max;
    }
    
    public void constrainHelper(boolean [] current, int size, int index){
        Stack <boolean []> currInstances = new Stack <boolean []>();
        Stack <Integer> addedInstance = new Stack <Integer> ();
        addCount(instances[index]);
        
        if(!this.satisfiesConstraint()) {
            if(size - 1 > max) {
                max = size - 1;
            }
        }
        
        else 
        {
            for(int i = 1; i < instances.length; i++)
            {
                if(current[i]) {
                    continue;
                }

                else {
                    boolean [] new_value = new boolean[instances.length];
                    System.arraycopy(current, 0, new_value, 0, instances.length);
                    new_value[i] = true;

                    currInstances.push(new_value);
                    addedInstance.push(i);
                }
            } 
        }
        
        while(!currInstances.isEmpty())
        {
            constrainHelper(currInstances.pop(), size + 1, addedInstance.pop());
        }
        
        removeCount(instances[index]);
    }

    public boolean satisfiesConstraint() 
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
    
    private void addCount(String inst)
    {
        int index = 0;
        for(Iterator <String> iter = sets.iterator(); iter.hasNext(); index++)
        {
            String curr_set = iter.next();
            
            int [] indices = index_mappings.get(curr_set);
            
            String res = ""; 
            for(int j = 0; j < indices.length; j++)
            {
                res += inst.charAt(indices[j]);
            }
            
            Integer prev_value = (Integer)currValues[index].get(res);
            if(prev_value == null) {
                currValues[index].put(res, 1);
            }
            else {
                currValues[index].put(res, prev_value + 1);
            }
        }
    }
    
        private void removeCount(String inst)
    {
        int index = 0;
        for(Iterator <String> iter = sets.iterator(); iter.hasNext(); index++)
        {
            String curr_set = iter.next();
            
            int [] indices = index_mappings.get(curr_set);
            
            String res = ""; 
            for(int j = 0; j < indices.length; j++)
            {
                res += inst.charAt(indices[j]);
            }
            
            Integer prev_value = (Integer)currValues[index].get(res);
            
            if(prev_value == 1) {
                currValues[index].remove(res);
            }
            else {
                currValues[index].put(res, prev_value - 1);
            }
        }
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