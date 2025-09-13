package hk.hkprog.ir;

/**
 * Represents a variable (local or global)
 */
public class IRVariable {
    private String name;
    private IRType type;
    private boolean isGlobal;
    
    public IRVariable(String name, IRType type, boolean isGlobal) {
        this.name = name;
        this.type = type;
        this.isGlobal = isGlobal;
    }
    
    public String getName() { return name; }
    public IRType getType() { return type; }
    public boolean isGlobal() { return isGlobal; }
}