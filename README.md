# Hoodlum

Hoodlum is a tiny DSL for stock trading.

## Build

- Requires Java 17+ and Maven 3.9+
- To build and run tests:

```
mvn -q -DskipTests=false clean test package
```

## Run

### Interpreter Mode (default)

Run with an input file (see `examples/quickstart.hlm`):

```
java -jar target/hoodlum-*-SNAPSHOT.jar examples/quickstart.hlm
```

Or run with the built-in demo program (no args):

```
java -jar target/hoodlum-*-SNAPSHOT.jar
```

### Bytecode Mode

Compile Hoodlum programs to JVM bytecode and execute them:

```bash
# Compile and run with an input file
mvn compile exec:java -Dexec.mainClass="com.hoodlum.BytecodeRunner" -Dexec.args="examples/simple.hlm"

# Compile and run the built-in demo
mvn compile exec:java -Dexec.mainClass="com.hoodlum.BytecodeRunner"
```

The BytecodeRunner will:
1. Parse your Hoodlum program
2. Generate JVM bytecode using ASM
3. Write a `.class` file (e.g., `HoodlumProgram.class`)
4. Load and execute the bytecode

**Note**: Currently only `PRINT` statements with numbers and the `cash` variable are supported in bytecode mode.

## Language sketch

```
DEPOSIT 2000;
LET risk = 0.02;
BUY 100 AAPL @ 175.5;
SELL 50 AAPL @ 185.0 if cash > 1000;
IF cash < 1000 THEN DEPOSIT 5000;
PRINT portfolio;
```

- Arithmetic and comparisons: `+ - * / > < >= <= == !=`
- Variables: `LET name = expr;`
- Cash variable: `cash`
- Orders: `BUY|SELL <INT> <SYMBOL> [@ <NUMBER>] [if <expr>]`
- Print: `PRINT portfolio;` or `PRINT <expr>;`

## Notes

- This is a teaching project, not investment advice.



