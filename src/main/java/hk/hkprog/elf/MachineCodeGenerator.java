package hk.hkprog.elf;

import hk.hkprog.codegen.AssemblySection;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Generates machine code from assembly instructions
 */
public class MachineCodeGenerator {
    private Map<String, Long> symbols;
    private long currentAddress;
    
    public MachineCodeGenerator() {
        this.symbols = new HashMap<>();
        this.currentAddress = 0x400000 + 0x1000; // Base address + offset
    }
    
    public byte[] generateMachineCode(AssemblySection textSection) {
        ByteArrayOutputStream codeBuffer = new ByteArrayOutputStream();
        
        // First pass: collect symbols
        for (String instruction : textSection.getInstructions()) {
            if (instruction.endsWith(":")) {
                String symbol = instruction.substring(0, instruction.length() - 1);
                symbols.put(symbol, currentAddress + codeBuffer.size());
            } else if (!instruction.trim().startsWith(".")) {
                // Estimate instruction size (simplified)
                codeBuffer.write(new byte[estimateInstructionSize(instruction)], 0, estimateInstructionSize(instruction));
            }
        }
        
        // Second pass: generate actual machine code
        codeBuffer.reset();
        
        for (String instruction : textSection.getInstructions()) {
            if (instruction.endsWith(":") || instruction.trim().startsWith(".")) {
                continue; // Skip labels and directives
            }
            
            byte[] machineCode = translateInstruction(instruction.trim());
            try {
                codeBuffer.write(machineCode);
            } catch (Exception e) {
                // Fallback: write NOPs
                codeBuffer.write(new byte[estimateInstructionSize(instruction)], 0, estimateInstructionSize(instruction));
            }
        }
        
        return codeBuffer.toByteArray();
    }
    
    private byte[] translateInstruction(String instruction) {
        String[] parts = instruction.split("\\s+");
        String opcode = parts[0];
        
        switch (opcode) {
            case "push":
                if (parts[1].equals("%rbp")) {
                    return new byte[]{0x55}; // push %rbp
                }
                break;
                
            case "pop":
                if (parts[1].equals("%rbp")) {
                    return new byte[]{0x5d}; // pop %rbp
                }
                break;
                
            case "mov":
                if (parts[1].equals("%rsp,") && parts[2].equals("%rbp")) {
                    return new byte[]{0x48, (byte)0x89, (byte)0xe5}; // mov %rsp, %rbp
                } else if (parts[1].equals("%rbp,") && parts[2].equals("%rsp")) {
                    return new byte[]{0x48, (byte)0x89, (byte)0xec}; // mov %rbp, %rsp
                } else if (parts[1].startsWith("$") && parts[2].equals("%rdi")) {
                    // mov $label, %rdi - simplified
                    return new byte[]{0x48, (byte)0xc7, (byte)0xc7, 0x00, 0x00, 0x00, 0x00}; // mov immediate to %rdi
                } else if (parts[1].startsWith("$") && parts[2].equals("%rax")) {
                    // mov $immediate, %rax
                    try {
                        int value = Integer.parseInt(parts[1].substring(1));
                        if (value == 0) {
                            return new byte[]{0x48, (byte)0x31, (byte)0xc0}; // xor %rax, %rax (mov $0, %rax)
                        } else {
                            return new byte[]{0x48, (byte)0xc7, (byte)0xc0, 
                                (byte)(value & 0xff), (byte)((value >> 8) & 0xff), 
                                (byte)((value >> 16) & 0xff), (byte)((value >> 24) & 0xff)}; // mov immediate to %rax
                        }
                    } catch (NumberFormatException e) {
                        return new byte[]{0x48, (byte)0xc7, (byte)0xc0, 0x00, 0x00, 0x00, 0x00};
                    }
                }
                break;
                
            case "xor":
                if (parts[1].equals("%rax,") && parts[2].equals("%rax")) {
                    return new byte[]{0x48, (byte)0x31, (byte)0xc0}; // xor %rax, %rax
                }
                break;
                
            case "call":
                if (parts[1].equals("printf")) {
                    // call printf - simplified (will need linking)
                    return new byte[]{(byte)0xe8, 0x00, 0x00, 0x00, 0x00}; // call relative
                } else {
                    return new byte[]{(byte)0xe8, 0x00, 0x00, 0x00, 0x00}; // call relative
                }
                
            case "ret":
                return new byte[]{(byte)0xc3}; // ret
                
            default:
                // Unknown instruction, return NOPs
                return new byte[]{(byte)0x90}; // nop
        }
        
        // Default case
        return new byte[]{(byte)0x90}; // nop
    }
    
    private int estimateInstructionSize(String instruction) {
        String opcode = instruction.trim().split("\\s+")[0];
        
        switch (opcode) {
            case "push":
            case "pop":
            case "ret":
                return 1;
            case "mov":
            case "xor":
                return 3;
            case "call":
                return 5;
            default:
                return 1;
        }
    }
}