
import java.util.LinkedList;

class Instance {
    
    String value;
    LinkedList <String> used_by;
    
    public Instance()
    {
        this.value = "";
        this.used_by = new LinkedList <String> ();
    }
    
    public Instance(String perm)
    {
        this.value = perm;
        this.used_by = new LinkedList <String> ();
    }
    
    public String toString()
    {
        return "Instance: "  + value + " " + used_by;
    }
}
