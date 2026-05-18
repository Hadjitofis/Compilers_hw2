import syntaxtree.*;
import visitor.*;
import java.util.*;
import java.io.*;

public class Main {
    public static void main(String[] args) throws Exception {
        if(args.length<1){
            System.err.println("Wrong input");
            System.exit(1);            
        }
        for(String filename:args){
            FileInputStream fis=null;
            try{
                fis=new FileInputStream(filename);
                MiniJavaParser parser = new MiniJavaParser(fis);

                //parse file into tree
                Goal root = parser.Goal();

                //create symbol table
                SymbolTable st= new SymbolTable();
                //first pass with visitor1
                visitor1 v1=new visitor1(st);
                root.accept(v1,null);
                //secondpass with visitor2
                visitor2 v2=new visitor2(st);
                root.accept(v2,null);

                printOffsets(st);
            
            }catch(ParseException e){
                System.err.println("parse error in "+filename+":"+e.getMessage());
            }catch(Exception e){
                System.err.println("error in " + filename + ": " + e.getMessage());
            }finally {
                if(fis!=null)
                    fis.close();
            }
        }
    }
    static void printOffsets(SymbolTable st){
        for(ClassInfo ci: st.classes.values()){
            int fOffset=0;
            int mOffset=0;
            
            //if we have a parent start were we left
            if(ci.parent!=null){
                fOffset=getFieldSize(st,ci.parent);
                mOffset=getMethodSize(st,ci.parent);
            }
            //print field offsets
            for(Map.Entry<String,String>field : ci.fields.entrySet()){
                System.out.println(ci.name+"."+field.getKey()+" : " +fOffset);
                fOffset+=sizeOf(field.getValue());
            }
            //print Method offsets
            for(MethodInfo mi:ci.methods.values()){
                if(!isOverride(st,ci,mi)){
                    System.out.println(ci.name+"."+mi.name+" : "+mOffset);
                    mOffset+=8;
                }
            }
        }
    }
    //size of a type in bytes
    static int sizeOf(String type){
        if(type.equals("int"))
            return 4;
        if(type.equals("boolean"))
            return 1;
        return 8;
    }
    //gets the size of a field in bytes
    static int getFieldSize(SymbolTable st,String classname){
        ClassInfo ci=st.getClass(classname);
        if(ci==null)
            return 0;
        int size=getFieldSize(st,ci.parent);
        for(String type : ci.fields.values())
            size+= sizeOf(type);
        return size;
    }
    //total method slots used
    static int getMethodSize(SymbolTable st,String classname){
        ClassInfo ci = st.getClass(classname);
        if (ci == null) return 0;
        int size = getMethodSize(st, ci.parent);
        for (MethodInfo mi : ci.methods.values())
            if (!isOverride(st, ci, mi))
                size += 8;
        return size;
    }
    //true if it overrides from parent class
    static boolean isOverride(SymbolTable st,ClassInfo ci, MethodInfo mi){
        ClassInfo parent = ci.parent!=null?st.getClass(ci.parent):null;
        while(parent!=null){
            if(parent.getMethod(mi.name,mi.paramTypes.size())!=null)
                return true;
            parent=parent.parent!=null? st.getClass(parent.parent):null;
        }
        return false;
    }
}