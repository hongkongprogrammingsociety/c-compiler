package hk.hkprog.ir;

/**
 * Load constant instruction
 */
public class IRLoadConstantInstruction extends IRInstruction {
    private Object constant;
    private IRType type;
    
    public IRLoadConstantInstruction(String result, Object constant, IRType type) {
        super(result);
        this.constant = constant;
        this.type = type;
    }
    
    public Object getConstant() { return constant; }
    public IRType getType() { return type; }
    
    @Override
    public String toString() {
        return String.format("%s = load %s %s", result, type.getName(), constant);
    }
}