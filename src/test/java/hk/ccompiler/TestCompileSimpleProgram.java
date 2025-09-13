package hk.ccompiler;

import org.junit.jupiter.api.Test;

import org.hkprog.CCompiler;


import java.io.File;
import java.io.FileWriter;

public class TestCompileSimpleProgram {

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
		compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
//		assertDoesNotThrow(() -> {
//			compiler.compile(inputFile.getAbsolutePath(), outputFile.getAbsolutePath());
//		}, "Compilation should not throw exceptions");
//
//		// Verify output file was created
//		assertTrue(outputFile.exists(), "Output file should be created");
//		assertTrue(outputFile.length() > 0, "Output file should not be empty");

		// Clean up
		// inputFile.delete();
		// outputFile.delete();
	}

}
