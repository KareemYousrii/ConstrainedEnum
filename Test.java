import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Set;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.LinkedList;

public class Test
{
	public static void main(String[]args)
	{
            
//		Set <Character> variables = new LinkedHashSet <Character> (Arrays.asList(new Character [] {'X', 'Y', 'Z', 'W'}));
//		Set <String> sets = new LinkedHashSet <String> (Arrays.asList(new String [] {"XYZ", "XY", "YZ"}));
//		HashMap <String, Integer> costs = new HashMap <String, Integer> ();
//		
//		/* Add costs to the map */
//                
//                costs.put("XYZ", 5);
//
//		costs.put("XY", 4);
//		costs.put("YZ", 7);
//
//		costs.put("X", 2);
//		costs.put("Y", 3);
//		costs.put("Z", 2);
//		costs.put("W", 2);
            
              Set <Character> variables = new LinkedHashSet <Character> (Arrays.asList(new Character [] {'X', 'Y', 'Z'}));
		Set <String> sets = new LinkedHashSet <String> (Arrays.asList(new String [] {"XY", "YZ", "XZ"}));
		HashMap <String, Integer> costs = new HashMap <String, Integer> ();
                
		costs.put("XY", 7);
		costs.put("YZ", 4);
                costs.put("XZ", 3);

		costs.put("X", 2);
		costs.put("Y", 3);
		costs.put("Z", 2);

		StringConstrict sc = new StringConstrict(variables, sets, costs);
                int x = sc.enumerate();
                System.out.println(x);
//                
//                System.out.println();
                
//                StringConstrictExact sce = new StringConstrictExact(variables, sets, costs);
//                int y = sce.constrain();
//                System.out.println("result: " + y);
	}
}