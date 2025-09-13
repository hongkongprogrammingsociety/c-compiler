package hk.hkprog.codegen;

import java.util.*;

/**
 * Represents a section in the assembly program
 */
public class AssemblySection {
    private String name;
    private List<String> instructions;
    private List<Byte> data;
    private boolean isCode;
    
    public AssemblySection(String name, boolean isCode) {
        this.name = name;
        this.isCode = isCode;
        this.instructions = new ArrayList<>();
        this.data = new ArrayList<>();
    }
    
    public void addInstruction(String instruction) {
        instructions.add(instruction);
    }
    
    public void addData(byte[] bytes) {
        for (byte b : bytes) {
            data.add(b);
        }
    }
    
    public String getName() { return name; }
    public List<String> getInstructions() { return instructions; }
    public List<Byte> getData() { return data; }
    public boolean isCode() { return isCode; }
    
    public byte[] getDataAsBytes() {
        byte[] result = new byte[data.size()];
        for (int i = 0; i < data.size(); i++) {
            result[i] = data.get(i);
        }
        return result;
    }
}