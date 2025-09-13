package hk.hkprog.ir;

/**
 * Represents a parameter in a function
 */
public class IRParameter {
    private String name;
    private IRType type;
    
    public IRParameter(String name, IRType type) {
        this.name = name;
        this.type = type;
    }
    
    public String getName() { return name; }
    public IRType getType() { return type; }
}