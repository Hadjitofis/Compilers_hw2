import java.util.*;
//Stores all the information we could need about a program like a database
public class SymbolTable{
    //to keep track of every class by its name
    public LinkedHashMap<String,ClassInfo> classes = new LinkedHashMap<>();

    //for new classes declaration
    public void addClass(String name, String parent) throws Exception {
        if (classes.containsKey(name))
            throw new Exception("Class '"+name+"'declared twice.");
        classes.put(name,new ClassInfo(name,parent));
    }
    // gets a class by its name
    public ClassInfo getClass(String name){
        return classes.get(name);
    }

    //checks if the class exists
    public boolean classExists(String name){
        return classes.containsKey(name);
    }

    //returns true if child and parent are the same or inerit from it
    public boolean inheritsFrom(String child, String parent){
        if(child.equals(parent)) return true;
        ClassInfo ci=classes.get(child);
        while (ci != null && ci.parent!=null){
            if(ci.parent.equals(parent))return true;
            ci=classes.get(ci.parent);
        }
        return false;
    }
}
//To store all the information about a method
class MethodInfo{
    public String name;
    public String returnType;
    
    //parameters needed
    public List<String> paramTypes=new ArrayList<>();
    public List<String> paramNames=new ArrayList<>();

    public LinkedHashMap<String, String> local = new LinkedHashMap<>();
    public MethodInfo(String name, String returnType){
        this.name = name;
        this.returnType=returnType;
    }
    //add a parameter or throw if dublicated
    public void addParam(String pname, String ptype)throws Exception{
        if(paramNames.contains(pname))
            throw new Exception("Parameter '"+pname+"'already exists in method");
        paramNames.add(pname);
        paramTypes.add(ptype);

    }
    //add a parameter or throw if dublicated variable name
    public void addlocal(String vname,String vtype) throws Exception{
        if(local.containsKey(vname))
            throw new Exception("Local variable '"+vname+"'already exists in method");
        local.put(vname,vtype);
    }

    public String searchvar(String vname){
        if (local.containsKey(vname))
            return local.get(vname);
        
        int i=paramNames.indexOf(vname);
        if(i>=0)return paramTypes.get(i);
        return null;
    }

}
//To store all the information about classes
class ClassInfo{
    public String name;
    public String parent;
    //Maps name
    public LinkedHashMap<String, String> fields = new LinkedHashMap<>();
    //Maps the Method information
    public LinkedHashMap<String, MethodInfo> methods = new LinkedHashMap<>();
    //parent could be null
    public ClassInfo(String name, String parent){
        this.name=name;
        this.parent=parent;
    }
    //Creates new field or throw exception if it already exists
    public void newField(String nname,String ntype) throws Exception{
        if(fields.containsKey(nname))
        throw new Exception("Field '"+nname+ "'already exists in class'"+name);
        fields.put(nname,ntype);
    }
    //add a new method
    public void addMethod(String nname,MethodInfo info){
        methods.put(nname+"/"+info.paramTypes.size(),info);
    }
    //find fieldtype  by name
    public String getFieldType(String fname){
        return fields.get(fname);
    }
    //get methods info by name and arguments
    public MethodInfo getMethod(String mname,int argCount){
        return methods.get(mname+"/"+argCount);
    }
}