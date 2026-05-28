# Compilers_hw2 - Andreas Hadjitofis sdi2300236

## MiniJava Static Checking (Semantic Analysis)

## Implementation

A semantic analyzer for MiniJava built on top of a JavaCC-generated parser and JTB-generated AST visitors.

### Files

* `SymbolTable.java`: Acts as a database for the program. Contains three classes:
   * `SymbolTable`: maps class names to their info
   * `ClassInfo`: stores a class's name, parent, fields, and methods
   * `MethodInfo`: stores a method's name, return type, parameters, and local variables

* `visitor1.java`: First pass over the AST. Collects all class names, field types, method signatures, and local variables into the symbol table. No type checking happens here.

* `visitor2.java`: Second pass over the AST. Use the symbol table to type check everything:
   * Variable declarations and usage
   * Assignment type compatibility
   * Method call argument types
   * Return type correctness
   * Inheritance and subtyping rules

* `Main.java`: Entry point. For each input file, runs the parser, then visitor1, then visitor2. If no errors, prints the memory offsets for every class.

## Offset Calculation

For every class, the program prints the memory offset of each field and method:
* int : 4 bytes
* boolean : 1 byte
* everything else : 8 bytes
* methods : 8 bytes each
* subclasses continue from where the parent left off
* overriding methods arent printed

## Compile

javac -cp . syntaxtree/*.java visitor/*.java SymbolTable.java visitor1.java visitor2.java Main.java

## Run

java Main <file1> <file2> ...

## Example

Input:
class A {
    int i;
    A a;

    public int foo(int i, int j) {
        int k;
        k = i+j;

        return k; 
    }
    public int bar(){ return 1; }
}

class B extends A {
    int i;

    public int foo(int i, int j) { return i+j; }
    public int foobar(boolean k){ return 1; }
}

Output:
A.i : 0
A.a : 4
A.foo : 0
A.bar : 8
B.i : 12
B.foobar : 16