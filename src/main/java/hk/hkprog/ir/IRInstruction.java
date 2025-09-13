package hk.hkprog.ir;

/**
 * Base class for all IR instructions
 */
public abstract class IRInstruction {
    protected String result;
    
    public IRInstruction(String result) {
        this.result = result;
    }
    
    public String getResult() {
        return result;
    }
    
    public abstract String toString();
}