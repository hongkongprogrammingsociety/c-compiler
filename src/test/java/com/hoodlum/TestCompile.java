package com.hoodlum;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.io.TempDir;

import org.hkprog.CCompiler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Path;

public class TestCompile {
    
    @TempDir
    Path tempDir;
    
    private CCompiler compiler;
    
    @BeforeEach
    void setUp() {
        compiler = new CCompiler();
    }
    
    @Test
    void testCompileSimpleProgram() throws Exception {
        // Create a temporary C file
        File inputFile = tempDir.resolve("test.c").toFile();
        File outputFile = tempDir.resolve("test.out").toFile();
        
        String cCode = """
            int main()
            {
                return 42;
            }
            """;
        
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(cCode);
        }
        
        // Test the full compilation pipeline
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation should not throw exceptions");
        
        // Verify output file was created
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }
    
    @Test
    void testCompileHelloWorldExample() throws Exception {
        // Use the actual hello world example from the examples directory
        String examplePath = "examples/helloworld.c";
        File exampleFile = new File(examplePath);
        
        // Skip test if example file doesn't exist (in case running in different environment)
        if (!exampleFile.exists()) {
            System.out.println("Skipping test - examples/helloworld.c not found");
            return;
        }
        
        File outputFile = tempDir.resolve("helloworld.out").toFile();
        
        assertDoesNotThrow(() -> {
            compiler.compile(exampleFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation of hello world should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }
    
    @Test
    void testCompileFunctionCallExample() throws Exception {
        // Use the function call example
        String examplePath = "examples/FuncCallAsFuncArgument.c";
        File exampleFile = new File(examplePath);
        
        // Skip test if example file doesn't exist
        if (!exampleFile.exists()) {
            System.out.println("Skipping test - examples/FuncCallAsFuncArgument.c not found");
            return;
        }
        
        File outputFile = tempDir.resolve("funccall.out").toFile();
        
        assertDoesNotThrow(() -> {
            compiler.compile(exampleFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation of function call example should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }
    
    @Test
    void testCompileWithVariables() throws Exception {
        // Create a test with variable declarations
        File inputFile = tempDir.resolve("variables.c").toFile();
        File outputFile = tempDir.resolve("variables.out").toFile();
        
        String cCode = """
            int main()
            {
                int x = 10;
                int y = 20;
                int z = x + y;
                return z;
            }
            """;
        
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(cCode);
        }
        
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation with variables should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }
    
    @Test
    void testCompileWithFunctionDefinition() throws Exception {
        // Create a test with function definition
        File inputFile = tempDir.resolve("function.c").toFile();
        File outputFile = tempDir.resolve("function.out").toFile();
        
        String cCode = """
            int add(int a, int b)
            {
                return a + b;
            }
            
            int main()
            {
                int result = add(5, 7);
                return result;
            }
            """;
        
        try (FileWriter writer = new FileWriter(inputFile)) {
            writer.write(cCode);
        }
        
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation with function definition should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
    }
}
