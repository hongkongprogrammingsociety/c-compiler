package hk.hkprog.ir;

/**
 * Function call instruction
 */
public class IRCallInstruction extends IRInstruction {
    private String functionName;
    private String[] arguments;
    
    public IRCallInstruction(String result, String functionName, String... arguments) {
        super(result);
        this.functionName = functionName;
        this.arguments = arguments;
    }
    
    public String getFunctionName() { return functionName; }
    public String[] getArguments() { return arguments; }
    
    @Override
    public String toString() {
        return String.format("%s = call %s(%s)", 
            result != null ? result : "", 
            functionName, 
            String.join(", ", arguments));
    }
}