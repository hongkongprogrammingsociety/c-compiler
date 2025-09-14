package org.hkprog;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import org.hkprog.antlr.CLexer;
import org.hkprog.antlr.CParser;

import org.hkprog.codegen.AssemblyProgram;
import org.hkprog.codegen.X86CodeGenerator;
import org.hkprog.elf.ELFWriter;
import org.hkprog.ir.IRFunction;
import org.hkprog.ir.IRGenerator;
import org.hkprog.ir.IRGlobalVariable;
import org.hkprog.ir.IRInstruction;
import org.hkprog.ir.IRParameter;
import org.hkprog.ir.IRProgram;
import org.hkprog.ir.IRVariable;

/**
 * Main C Compiler class that orchestrates the compilation process
 * from C source code to ELF executable
 */
public class CCompiler {
    
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.err.println("Usage: java CCompiler <input.c> <output>");
            System.exit(1);
        }
        
        String inputFile = args[0];
        String outputFile = args[1];
        
        CCompiler compiler = new CCompiler();
        compiler.compile(inputFile, outputFile);
    }
    
    public void compile(String inputFile, String outputFile) throws Exception {
        System.out.println("Compiling " + inputFile + " to " + outputFile);
        
        // 1. Parse C source code
        CharStream input = CharStreams.fromFileName(inputFile);
        CLexer lexer = new CLexer(input);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        CParser parser = new CParser(tokens);
        
        ParseTree tree = parser.compilationUnit();
        
        // 2. Generate IR from AST
        IRGenerator irGenerator = new IRGenerator();
        IRProgram program = irGenerator.generateIR(tree);
        
        // Dump IR as tree for debugging
        dumpIRProgram(program);
        
        // 3. Generate x86-64 assembly
        X86CodeGenerator codeGen = new X86CodeGenerator();
        AssemblyProgram assembly = codeGen.generate(program);
        
        // 4. Generate ELF binary
        ELFWriter elfWriter = new ELFWriter();
        elfWriter.writeELF(assembly, outputFile);
        
        System.out.println("Compilation completed successfully!");
    }
    
    /**
     * Dumps the IR program structure as a tree for debugging purposes
     */
    private void dumpIRProgram(IRProgram program) {
        System.out.println("\n=== IR Program Tree ===");
        
        // Dump global variables
        if (!program.getGlobalVariables().isEmpty()) {
            System.out.println("├── Global Variables:");
            for (int i = 0; i < program.getGlobalVariables().size(); i++) {
                IRGlobalVariable var = program.getGlobalVariables().get(i);
                boolean isLast = (i == program.getGlobalVariables().size() - 1);
                String prefix = isLast ? "└── " : "├── ";
                System.out.println("│   " + prefix + var.getName() + " : " + var.getType().getName() + 
                    (var.getInitialValue() != null ? " = " + var.getInitialValue() : ""));
            }
        }
        
        // Dump functions
        System.out.println("├── Functions:");
        for (int i = 0; i < program.getFunctions().size(); i++) {
            IRFunction function = program.getFunctions().get(i);
            boolean isLastFunction = (i == program.getFunctions().size() - 1);
            String functionPrefix = isLastFunction ? "└── " : "├── ";
            
            System.out.println("│   " + functionPrefix + function.getName() + "() : " + function.getReturnType().getName());
            
            // Dump parameters
            if (!function.getParameters().isEmpty()) {
                System.out.println("│   │   ├── Parameters:");
                for (int j = 0; j < function.getParameters().size(); j++) {
                    IRParameter param = function.getParameters().get(j);
                    boolean isLastParam = (j == function.getParameters().size() - 1);
                    String paramPrefix = isLastParam ? "└── " : "├── ";
                    System.out.println("│   │   │   " + paramPrefix + param.getName() + " : " + param.getType().getName());
                }
            }
            
            // Dump local variables
            if (!function.getLocalVariables().isEmpty()) {
                System.out.println("│   │   ├── Local Variables:");
                int varCount = 0;
                for (IRVariable var : function.getLocalVariables().values()) {
                    boolean isLastVar = (++varCount == function.getLocalVariables().size());
                    String varPrefix = isLastVar ? "└── " : "├── ";
                    System.out.println("│   │   │   " + varPrefix + var.getName() + " : " + var.getType().getName());
                }
            }
            
            // Dump instructions
            if (!function.getInstructions().isEmpty()) {
                String instrHeader = (function.getLocalVariables().isEmpty()) ? "└── " : "└── ";
                System.out.println("│   │   " + instrHeader + "Instructions:");
                for (int j = 0; j < function.getInstructions().size(); j++) {
                    IRInstruction instr = function.getInstructions().get(j);
                    boolean isLastInstr = (j == function.getInstructions().size() - 1);
                    String instrPrefix = isLastInstr ? "└── " : "├── ";
                    System.out.println("│   │   │   " + instrPrefix + instr.toString());
                }
            }
        }
        
        System.out.println("======================\n");
    }
}