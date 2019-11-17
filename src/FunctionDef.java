import java.util.ArrayList;
import java.util.HashMap;

public class FunctionDef {

    String functionName;

    HashMap<String, String> paramTypeMapByParam = new  HashMap<String, String>();
    String returnType;


    void print()
    {

        paramTypeMapByParam.entrySet().forEach(entry->{
           // System.out.println(entry.getKey() + " " + entry.getValue());
        });

    }

}
