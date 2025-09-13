package hk.hkprog.ir;

/**
 * Return instruction
 */
public class IRReturnInstruction extends IRInstruction {
    private String value;
    
    public IRReturnInstruction(String value) {
        super(null);
        this.value = value;
    }
    
    public String getValue() { return value; }
    
    @Override
    public String toString() {
        return value != null ? "return " + value : "return";
    }
}