package hk.hkprog.ir;

/**
 * Represents types in the intermediate representation
 */
public enum IRType {
    VOID("void", 0),
    INT("int", 4),
    CHAR("char", 1),
    LONG("long", 8),
    POINTER("pointer", 8);
    
    private final String name;
    private final int size;
    
    IRType(String name, int size) {
        this.name = name;
        this.size = size;
    }
    
    public String getName() {
        return name;
    }
    
    public int getSize() {
        return size;
    }
    
    public static IRType fromString(String typeString) {
        switch (typeString.toLowerCase()) {
            case "void": return VOID;
            case "int": return INT;
            case "char": return CHAR;
            case "long": return LONG;
            default: return INT; // Default to int
        }
    }
}