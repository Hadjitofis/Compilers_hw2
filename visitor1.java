import syntaxtree.*;
import visitor.*;
import java.util.*;


//We got 2 visitors
//This one walks the AST and collects the info
public class visitor1 extends GJDepthFirst<String,Void>{
    public SymbolTable symbolTable;

    //To keep track of our position
    private String currentClass;
    private String currentMethod;
    private MethodInfo currentMethodInfo;

    public visitor1(SymbolTable t){
        this.symbolTable=t;
    }
    @Override
    public String visit(MainClass n,Void argu) throws Exception{
        //get class name from f1
        String classname=n.f1.accept(this,null);
        //register in symbol table
        symbolTable.addClass(classname,null);
        //set current class for vardeclaration
        currentClass=classname;
        return null;
    }
    @Override
    public String visit(ClassDeclaration n,Void argu) throws Exception{
        
        String classname=n.f1.accept(this,null);
        symbolTable.addClass(classname,null);
        currentClass=classname;

        //visit fields
        n.f3.accept(this,null);
        n.f4.accept(this,null);

        return null;
    }
    @Override
    public String visit(ClassExtendsDeclaration n,Void argu) throws Exception{
        String classname=n.f1.accept(this,null);
        //parent is now in f3
        symbolTable.addClass(classname,n.f3.accept(this,null));
        currentClass=classname;

        //fields are in f5,6
        n.f5.accept(this,null);
        n.f6.accept(this,null);

        return null;
    }
    @Override
    public String visit(VarDeclaration n,Void argu) throws Exception{
        //get type and name
        String type=n.f0.accept(this,null);
        String name = n.f1.accept(this,null);
        //we are at class
        if(currentMethod==null)
        {
            symbolTable.getClass(currentClass).newField(name,type);
        }
        else
        {
            //we are at a method
            currentMethodInfo.addlocal(name,type);
        }
        return null;
    }
    @Override
    public String visit(MethodDeclaration n,Void argu) throws Exception{
        String type=n.f1.accept(this,null);
        String name = n.f2.accept(this,null);
        //method we're inside of
        MethodInfo MI= new MethodInfo(name,type);
        currentMethod=name;
        currentMethodInfo=MI;
        //visit parameters if they exists
        if(n.f4.present())
        n.f4.accept(this,null);

        //add method to class
        symbolTable.getClass(currentClass).addMethod(name,MI);
        //visit variables
        n.f7.accept(this,null);
        //reset
        currentMethod=null;
        currentMethodInfo=null;
        return null;
    }
    @Override
    public String visit(FormalParameter n,Void argu) throws Exception{
        String type=n.f0.accept(this,null);
        String name=n.f1.accept(this,null);
        currentMethodInfo.addParam(name,type);
        return null;
    }
    @Override
    public String visit(ArrayType n,Void argu) {
        return "int[]";
    }
    @Override
    public String visit(BooleanType n,Void argu) {
        return "boolean";
    }
    @Override
    public String visit(IntegerType n,Void argu) {
        return "int";
    }
    @Override
    public String visit(Identifier n,Void argu) {
        return n.f0.toString();
    }
    
}