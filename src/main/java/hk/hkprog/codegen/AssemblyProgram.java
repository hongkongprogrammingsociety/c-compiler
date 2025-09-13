package hk.hkprog.codegen;

import hk.hkprog.ir.*;
import java.util.*;

/**
 * Represents an assembly program with sections and instructions
 */
public class AssemblyProgram {
    private List<AssemblySection> sections;
    private Map<String, String> symbols;
    
    public AssemblyProgram() {
        this.sections = new ArrayList<>();
        this.symbols = new HashMap<>();
    }
    
    public void addSection(AssemblySection section) {
        sections.add(section);
    }
    
    public void addSymbol(String name, String value) {
        symbols.put(name, value);
    }
    
    public List<AssemblySection> getSections() {
        return sections;
    }
    
    public Map<String, String> getSymbols() {
        return symbols;
    }
    
    public AssemblySection getTextSection() {
        return sections.stream()
            .filter(s -> ".text".equals(s.getName()))
            .findFirst()
            .orElse(null);
    }
    
    public AssemblySection getDataSection() {
        return sections.stream()
            .filter(s -> ".data".equals(s.getName()))
            .findFirst()
            .orElse(null);
    }
}