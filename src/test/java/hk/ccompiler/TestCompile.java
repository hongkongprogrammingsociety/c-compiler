package hk.ccompiler;

import org.junit.jupiter.api.Test;

import org.hkprog.CCompiler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;

public class TestCompile {
    
    @Test
    void testCompileSimpleProgram() throws Exception {
        // Create a test C file in current directory
        File inputFile = new File("test_simple.c");
        File outputFile = new File("test_simple.out");
        
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
        CCompiler compiler = new CCompiler();
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation should not throw exceptions");
        
        // Verify output file was created
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
        
        // Clean up
        // inputFile.delete();
        // outputFile.delete();
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
        
        File outputFile = new File("helloworld_test.out");
        
        CCompiler compiler = new CCompiler();
        assertDoesNotThrow(() -> {
            compiler.compile(exampleFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation of hello world should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
        
        // Clean up
        // outputFile.delete();
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
        
        File outputFile = new File("funccall_test.out");
        
        CCompiler compiler = new CCompiler();
        assertDoesNotThrow(() -> {
            compiler.compile(exampleFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation of function call example should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
        
        // Clean up
        // outputFile.delete();
    }
    
    @Test
    void testCompileWithVariables() throws Exception {
        // Create a test with variable declarations
        File inputFile = new File("test_variables.c");
        File outputFile = new File("test_variables.out");
        
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
        
        CCompiler compiler = new CCompiler();
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation with variables should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
        
        // Clean up
        // inputFile.delete();
        // outputFile.delete();
    }
    
    @Test
    void testCompileWithFunctionDefinition() throws Exception {
        // Create a test with function definition
        File inputFile = new File("test_function.c");
        File outputFile = new File("test_function.out");
        
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
        
        CCompiler compiler = new CCompiler();
        assertDoesNotThrow(() -> {
            compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
        }, "Compilation with function definition should not throw exceptions");
        
        assertTrue(outputFile.exists(), "Output file should be created");
        assertTrue(outputFile.length() > 0, "Output file should not be empty");
        
        // Clean up
        // inputFile.delete();
        // outputFile.delete();
    }
}
