package hk.hkprog.ir;

/**
 * Represents a global variable
 */
public class IRGlobalVariable extends IRVariable {
    private Object initialValue;
    
    public IRGlobalVariable(String name, IRType type, Object initialValue) {
        super(name, type, true);
        this.initialValue = initialValue;
    }
    
    public Object getInitialValue() { return initialValue; }
}