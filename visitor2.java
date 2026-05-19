import syntaxtree.*;
import visitor.*;
import java.util.*;

//this is visitor 2
//Type checks everything
public class visitor2 extends GJDepthFirst<String, Void>{
    public SymbolTable symbolTable;

    //our position
    private String currentClass;
    private String currentMethod;

    public visitor2(SymbolTable t){
        this.symbolTable=t;
    }
    //almost identical to visitor1
    @Override
    public String visit(MainClass n,Void argu)throws Exception{
        String classname = n.f1.f0.toString();
        currentClass=classname;

        return null;
    }
    @Override
    public String visit(ClassDeclaration n,Void argu) throws Exception{
        
        String classname = n.f1.f0.toString();
        currentClass=classname;
        n.f4.accept(this,null);

        return null;
    }
    @Override
    public String visit(ClassExtendsDeclaration n,Void argu) throws Exception{
        String classname = n.f1.f0.toString();
        String parentName = n.f3.f0.toString();
        currentClass=classname;
        if (!symbolTable.classExists(parentName))
            throw new Exception("Class '" + parentName+"'doesn't exist");
        n.f6.accept(this,null);

        return null;
    }
    @Override
    public String visit(MethodDeclaration n,Void argu) throws Exception{
        String type = n.f1.accept(this, null);
        String name = n.f2.f0.toString();
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
    public String visit(ArrayType n, Void argu) {
        return "int[]";
    }
    @Override
    public String visit(BooleanType n, Void argu) {
        return "boolean";
    }
    @Override
    public String visit(IntegerType n, Void argu) {
        return "int";
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
        if(!array.equals("int[]"))
            throw new Exception("should be on int[]");
        return "int";
    }
    @Override
    public String visit(AllocationExpression n,Void argu)throws Exception{
        String classname = n.f1.f0.toString();
        if(!symbolTable.classExists(classname))
            throw new Exception(classname+"doesn't exist");
        return classname;
    }
    
    @Override
    public String visit(ArrayAllocationExpression n,Void argu)throws Exception{
        return n.f0.accept(this,argu);
    }
    @Override
    public String visit(IntegerArrayAllocationExpression n,Void argu)throws Exception{
        String size=n.f3.accept(this,null);
        if(!size.equals("int"))
            throw new Exception("size must be int");
        return "int[]";
    }
    @Override
    public String visit(IfStatement n,Void argu) throws Exception{
        String condition =n.f2.accept(this,null);
        if(!condition.equals("boolean"))
            throw new Exception("if must be bool");
            n.f4.accept(this,null);
            n.f6.accept(this,null);
            return null;
    }
    @Override
    public String visit(WhileStatement n,Void argu) throws Exception{
        String condition =n.f2.accept(this,null);
        if(!condition.equals("boolean"))
            throw new Exception("while must be bool");
            n.f4.accept(this,null);
            return null;
    }
    @Override
    public String visit(PrintStatement n,Void argu)throws Exception{
        String expression=n.f2.accept(this,null);
        if(!expression.equals("int"))
            throw new Exception("expression must be int");
        return null;
    }
    @Override
    public String visit(AssignmentStatement n,Void argu)throws Exception{
        String var=n.f0.accept(this,null);
        String expr=n.f2.accept(this,null);
        if(!symbolTable.inheritsFrom(expr,var))
            throw new Exception("wrong assignment");
        return null;
    }
    @Override
    public String visit(ArrayAssignmentStatement n,Void argu)throws Exception{
        String var=n.f0.accept(this,null);
        String index=n.f2.accept(this,null);
        String value=n.f5.accept(this,null);
        if(!var.equals("int[]")||!index.equals("int")||!value.equals("int"))
            throw new Exception("all should be int");
        return null;
    }

    @Override
    public String visit(Identifier n,Void argu)throws Exception{
        String name = n.f0.toString();

        if(symbolTable.classExists(name))
            return name;
        //look in current method vars and parameters
        if(currentMethod!=null){
            ClassInfo ci=symbolTable.getClass(currentClass);
            MethodInfo mi=ci.MethodName(currentMethod);
            //search for variables and parameters
            String type=mi.searchvar(name);
            if(type!=null)
                return type;
        }
        //now look in fields
        ClassInfo classi=symbolTable.getClass(currentClass);
        while(classi!=null){
            String type=classi.getFieldType(name);
            if(type!=null)
                return type;
            //move to parent
            if (classi.parent != null)
                classi = symbolTable.getClass(classi.parent);
            else 
                classi = null;
            
            }
        throw new Exception("Variable not declared");
    }
    @Override
    public String visit(MessageSend n, Void argu) throws Exception{
        //get object type
        String objectType=n.f0.accept(this,null);
        //get method name
        String methodName=n.f2.f0.toString();
        List<String> argTypes=new ArrayList<>();
            if(n.f4.present()){
                syntaxtree.ExpressionList EL=(syntaxtree.ExpressionList) n.f4.node;
                argTypes.add(EL.f0.accept(this,null));
                for(syntaxtree.Node node:EL.f1.f0.nodes){
                    syntaxtree.ExpressionTerm et=(syntaxtree.ExpressionTerm) node;
                    argTypes.add(et.f1.accept(this, null));
                }
            }
        
        //find the method from the inheritance list
        ClassInfo ci=symbolTable.getClass(objectType);
        MethodInfo mi=null;
        while(ci!=null){
            mi=ci.getMethod(methodName,argTypes.size());
            if(mi!=null)
                break;
            if(ci.parent!=null)
                ci=symbolTable.getClass(ci.parent);
            else
                ci=null;
        }
            if(mi==null)
                throw new Exception("method '"+methodName+"'not found");
            //check if arguments type match
            for(int i=0;i<argTypes.size();i++)
            {
                if(!symbolTable.inheritsFrom(argTypes.get(i),mi.paramTypes.get(i)))
                    throw new Exception("Arg type doesnt match");
            }
            return mi.returnType;
    }
}