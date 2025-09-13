package hk.hkprog.ir;

import java.util.*;

/**
 * Represents a function in the intermediate representation
 */
public class IRFunction {
    private String name;
    private IRType returnType;
    private List<IRParameter> parameters;
    private List<IRInstruction> instructions;
    private Map<String, IRVariable> localVariables;
    
    public IRFunction(String name, IRType returnType) {
        this.name = name;
        this.returnType = returnType;
        this.parameters = new ArrayList<>();
        this.instructions = new ArrayList<>();
        this.localVariables = new HashMap<>();
    }
    
    public void addParameter(IRParameter parameter) {
        parameters.add(parameter);
    }
    
    public void addInstruction(IRInstruction instruction) {
        instructions.add(instruction);
    }
    
    public void addLocalVariable(String name, IRVariable variable) {
        localVariables.put(name, variable);
    }
    
    // Getters
    public String getName() { return name; }
    public IRType getReturnType() { return returnType; }
    public List<IRParameter> getParameters() { return parameters; }
    public List<IRInstruction> getInstructions() { return instructions; }
    public Map<String, IRVariable> getLocalVariables() { return localVariables; }
}