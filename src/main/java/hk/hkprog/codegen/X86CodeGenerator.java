package hk.hkprog.codegen;

import hk.hkprog.ir.*;
import java.util.*;

/**
 * Generates x86-64 assembly code from intermediate representation
 */
public class X86CodeGenerator {
    
    public AssemblyProgram generate(IRProgram program) {
        AssemblyProgram assembly = new AssemblyProgram();
        
        // Create text section for code
        AssemblySection textSection = new AssemblySection(".text", true);
        assembly.addSection(textSection);
        
        // Create data section for string literals
        AssemblySection dataSection = new AssemblySection(".data", false);
        assembly.addSection(dataSection);
        
        // Generate code for each function
        for (IRFunction function : program.getFunctions()) {
            generateFunction(function, textSection, dataSection);
        }
        
        return assembly;
    }
    
    private void generateFunction(IRFunction function, AssemblySection textSection, AssemblySection dataSection) {
        // Function label
        textSection.addInstruction(".global " + function.getName());
        textSection.addInstruction(function.getName() + ":");
        
        // Function prologue
        textSection.addInstruction("    push %rbp");
        textSection.addInstruction("    mov %rsp, %rbp");
        
        // Process instructions
        for (IRInstruction instr : function.getInstructions()) {
            generateInstruction(instr, textSection, dataSection);
        }
        
        // Function epilogue (if no explicit return)
        if (function.getInstructions().isEmpty() || 
            !(function.getInstructions().get(function.getInstructions().size() - 1) instanceof IRReturnInstruction)) {
            textSection.addInstruction("    mov %rbp, %rsp");
            textSection.addInstruction("    pop %rbp");
            textSection.addInstruction("    ret");
        }
    }
    
    private void generateInstruction(IRInstruction instr, AssemblySection textSection, AssemblySection dataSection) {
        if (instr instanceof IRCallInstruction) {
            generateCallInstruction((IRCallInstruction) instr, textSection, dataSection);
        } else if (instr instanceof IRReturnInstruction) {
            generateReturnInstruction((IRReturnInstruction) instr, textSection);
        } else if (instr instanceof IRLoadConstantInstruction) {
            generateLoadConstantInstruction((IRLoadConstantInstruction) instr, textSection);
        }
    }
    
    private void generateCallInstruction(IRCallInstruction callInstr, AssemblySection textSection, AssemblySection dataSection) {
        String functionName = callInstr.getFunctionName();
        String[] arguments = callInstr.getArguments();
        
        if ("printf".equals(functionName)) {
            // Handle printf specifically
            if (arguments.length > 0) {
                String formatString = arguments[0];
                // Remove quotes if present
                if (formatString.startsWith("\"") && formatString.endsWith("\"")) {
                    formatString = formatString.substring(1, formatString.length() - 1);
                }
                
                // Create string literal in data section
                String labelName = "str_" + Math.abs(formatString.hashCode());
                dataSection.addInstruction(labelName + ":");
                dataSection.addInstruction("    .ascii \"" + formatString + "\\0\"");
                
                // Generate printf call
                textSection.addInstruction("    mov $" + labelName + ", %rdi");
                textSection.addInstruction("    xor %rax, %rax");  // No vector arguments
                textSection.addInstruction("    call printf");
            }
        } else {
            // Regular function call
            textSection.addInstruction("    call " + functionName);
        }
    }
    
    private void generateReturnInstruction(IRReturnInstruction returnInstr, AssemblySection textSection) {
        String value = returnInstr.getValue();
        if (value != null) {
            // Try to parse as integer constant
            try {
                int intValue = Integer.parseInt(value);
                textSection.addInstruction("    mov $" + intValue + ", %rax");
            } catch (NumberFormatException e) {
                // Handle variable or expression
                textSection.addInstruction("    mov " + value + ", %rax");
            }
        }
        
        textSection.addInstruction("    mov %rbp, %rsp");
        textSection.addInstruction("    pop %rbp");
        textSection.addInstruction("    ret");
    }
    
    private void generateLoadConstantInstruction(IRLoadConstantInstruction loadInstr, AssemblySection textSection) {
        String result = loadInstr.getResult();
        Object constant = loadInstr.getConstant();
        
        if (constant instanceof Integer) {
            textSection.addInstruction("    mov $" + constant + ", " + result);
        } else if (constant instanceof String) {
            textSection.addInstruction("    mov $" + constant + ", " + result);
        }
    }
}