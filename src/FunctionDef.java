import java.util.HashMap;

public class FunctionDef {

    String functionName;
    HashMap<String, String> paramTypeMapByParam = new  HashMap<String, String>();
    String returnType;


    void print()
    {
        System.out.print("def "+ functionName +" ( " );
        paramTypeMapByParam.entrySet().forEach(entry->{
           System.out.print(entry.getKey() + ": " + entry.getValue()+",");
        });
        System.out.print(" ) ->"+ returnType+":\n");


    }

}
