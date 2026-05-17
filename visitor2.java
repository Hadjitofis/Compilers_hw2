import syntaxtree.*;
import visitor.*;
import java.util.*;

//this is visitor 2
//Type checks everything
public class visitor2 extends GJDepthFirst<String, Void>{
    public SymbolTable symbolTable;

    //our position
    private String currentClass;
    private String CurrentMethod;

    public visitor2(SymbolTable t){
        this.symbolTable=t;
    }
    //almost identical to visitor1
    @Override
    public String visit(MainClass n,Void argu)throws Exception{
        String classname=n.f1.accept(this,null);
        currentClass=classname;

        return null;
    }
    @Override
    public String visit(ClassDeclaration n,Void argu) throws Exception{
        
        String classname=n.f1.accept(this,null);
        currentClass=classname;
        n.f4.accept(this,null);

        return null;
    }
    @Override
    public String visit(ClassExtendsDeclaration n,Void argu) throws Exception{
        String classname=n.f1.accept(this,null);
        currentClass=classname;
        String parentName=n.f3.accept(this,null);
        if (!symbolTable.classExists(parentName))
            throw new Exception("Class '" + parentName+"'doesn't exist");
        n.f6.accept(this,null);

        return null;
    }
    @Override
    public String visit(MethodDeclaration n,Void argu) throws Exception{
        String type=n.f1.accept(this,null);
        String name = n.f2.accept(this,null);
        //we're inside this method
        currentMethod=name;
        //visit statement in method
        n.f8.accept(this,null);
        //check if expression matches the type 
        String Return = n.f10.accept(this,null);
        if(!symbolTable.inheritsFrom(Return,type))
            throw new Exception("Method '"+name+"'doesn't match");
        //reset
        currentMethod=null;
        return null;
    }
    @Override
    public String visit(IntegerLiteral n,Void argu) {
        return "int";
    }
    @Override
    public String visit(TrueLiteral n,Void argu) {
        return "boolean";
    }
        @Override
    public String visit(FalseLiteral n,Void argu) {
        return "boolean";
    }
    @Override
    public String visit(ThisExpression n,Void argu) {
        return currentClass;
    }
    @Override
    public String visit(NotExpression n,Void argu) throws Exception{
        if(!n.f1.accept(this,null).equals("boolean"))
            throw new Exception("must be boolean");
        return "boolean";
    }
    @Override
    public String visit(PlusExpression n,Void argu)throws Exception{
        String left=n.f0.accept(this,null);
        String right=n.f2.accept(this,null);
        if(!left.equals("int")||!right.equals("int"))
            throw new Exception("must be ints");
        return "int";
    }
    @Override
    public String visit(MinusExpression n,Void argu)throws Exception{
        String left=n.f0.accept(this,null);
        String right=n.f2.accept(this,null);
        if(!left.equals("int")||!right.equals("int"))
            throw new Exception("must be ints");
        return "int";
    }
    @Override
    public String visit(TimesExpression n,Void argu)throws Exception{
        String left=n.f0.accept(this,null);
        String right=n.f2.accept(this,null);
        if(!left.equals("int")||!right.equals("int"))
            throw new Exception("must be ints");
        return "int";
    }
    @Override
    public String visit(AndExpression n,Void argu)throws Exception{
        String left=n.f0.accept(this,null);
        String right=n.f2.accept(this,null);
        if(!left.equals("boolean")||!right.equals("boolean"))
            throw new Exception("must be booleans");
        return "boolean";
    }
    @Override
    public String visit(CompareExpression n,Void argu)throws Exception{
        String left=n.f0.accept(this,null);
        String right=n.f2.accept(this,null);
        if(!left.equals("int")||!right.equals("int"))
            throw new Exception("must be ints");
        return "boolean";
    }
    @Override
    public String visit(ArrayLookup n,Void argu)throws Exception{
        String array = n.f0.accept(this,null);
        String index = n.f2.accept(this,null);
        if(!array.equals("int[]"))
            throw new Exception("should be on int[]");
        if(!index.equals("int"))
            throw new Exception("should be int");
        return "int";
    }
        @Override
    public String visit(ArrayLength n,Void argu)throws Exception{
        String array = n.f0.accept(this,null);
        if(!array.equals(|"int[]"))
            throw new Exception("should be on int[]");
        return "int";
    }
    
}