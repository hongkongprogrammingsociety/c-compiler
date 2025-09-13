package hk.hkprog.ir;

import java.util.*;

/**
 * Represents the entire program in intermediate representation
 */
public class IRProgram {
    private List<IRFunction> functions;
    private List<IRGlobalVariable> globalVariables;
    
    public IRProgram() {
        this.functions = new ArrayList<>();
        this.globalVariables = new ArrayList<>();
    }
    
    public void addFunction(IRFunction function) {
        functions.add(function);
    }
    
    public void addGlobalVariable(IRGlobalVariable variable) {
        globalVariables.add(variable);
    }
    
    public List<IRFunction> getFunctions() {
        return functions;
    }
    
    public List<IRGlobalVariable> getGlobalVariables() {
        return globalVariables;
    }
    
    public IRFunction getMainFunction() {
        return functions.stream()
            .filter(f -> "main".equals(f.getName()))
            .findFirst()
            .orElse(null);
    }
}