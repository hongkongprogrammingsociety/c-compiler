package hk.ccompiler;

import org.junit.jupiter.api.Test;

import org.hkprog.CCompiler;


import java.io.File;

public class TestCompileSimpleProgram {

	@Test
	void testCompileSimpleProgram() throws Exception {
		// Create a test C file in current directory
		File inputFile = new File("examples/test_function.c");
		File outputFile = new File("test_function.out");

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
