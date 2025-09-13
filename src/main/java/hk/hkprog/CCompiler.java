package hk.hkprog;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.*;
import com.hoodlum.antlr.CLexer;
import com.hoodlum.antlr.CParser;
import hk.hkprog.ir.*;
import hk.hkprog.codegen.*;
import hk.hkprog.elf.*;

import java.io.*;
import java.nio.file.*;

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
        
        // 3. Generate x86-64 assembly
        X86CodeGenerator codeGen = new X86CodeGenerator();
        AssemblyProgram assembly = codeGen.generate(program);
        
        // 4. Generate ELF binary
        ELFWriter elfWriter = new ELFWriter();
        elfWriter.writeELF(assembly, outputFile);
        
        System.out.println("Compilation completed successfully!");
    }
}