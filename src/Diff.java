import java.util.HashMap;

public class Diff {

    String diff; // diff content
    HashMap<String, FunctionDef> functionDefHashMap = new  HashMap<String, FunctionDef>();  // for keeping all function def inside a diff
    boolean typeHintAdded=false;
    //boolean localtypeHintAdded =false;

    public void getAllFunctionDef()
    {
        String[] diff_lines = diff.split(System.getProperty("line.separator")); // getting all the lines inside a diff

        for(int i=0; i< diff_lines.length; i++)
        {
            String line = diff_lines[i];

            if(line.startsWith("-") )
            {


                String subLine= line.substring(1,line.length()).trim();
                //System.out.println(subLine);

                if(!subLine.startsWith("def ") || subLine.startsWith("async def ")) continue;


                while(!line.contains(")"))
                {
                    i++;
                    if(i== diff_lines.length) break;;
                    String next_line = diff_lines[i];
                    if(next_line.startsWith("+") ) break; // invalid function def
                    next_line=next_line.substring(1).trim();  // as the line may start with +/-
                    line=line.replace("\n","");

                    if(next_line.indexOf(")")!=-1)
                    {

                        int index= next_line.indexOf(")");
                        int index1=next_line.indexOf(":",index);
                        if(index1!=-1)
                            next_line=next_line.substring(0,index1+1);
                        else {
                            next_line=next_line.substring(0,index+1);
                            line=line+next_line;
                            i++;
                            next_line=diff_lines[i];
                            index1=next_line.indexOf(":",index);
                            if(index1!=-1)
                                next_line=next_line.substring(1,index1+1).trim();
                        }
                    }

                    line=line+next_line;
                }

                // check here if full def is found // else

                if( line.contains(")")) {

                   // System.out.println("got full function declaration:    " + line); // got full function definition


                    FunctionDef fun_def_before = new FunctionDef();
                    String function_name = line.substring(line.indexOf("def") + 3, line.indexOf("(")).trim();  // fetch the function name
                    fun_def_before.functionName = function_name;
                    String all_parameters = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim(); // fetching all parameters
                    String[] parameters_list = all_parameters.split(",");
                    for (String param : parameters_list) {
                        String[] split = param.split(":");
                        if (split.length > 1)
                            fun_def_before.paramTypeMapByParam.put(split[0].trim(), split[1]);
                        else
                            fun_def_before.paramTypeMapByParam.put(split[0].trim(), "");
                    }
                    String returnType = ""; // getting return type

                    if (line.indexOf("->") != -1) {

                        returnType = line.substring(line.indexOf("->") + 2, line.length()).trim();
                    }
                    fun_def_before.returnType = returnType;
                    functionDefHashMap.put(function_name + "_bef", fun_def_before);
                }
            }

//-----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------
            if(line.startsWith("+"))
            {

                String subLine= line.substring(1,line.length()).trim();
               // System.out.println(subLine);


                if(!subLine.startsWith("def ") || subLine.startsWith("async def ")) continue;


                while(!line.contains(")"))
                {
                    i++;
                    if(i== diff_lines.length) break;;
                    String next_line = diff_lines[i];
                    if(next_line.startsWith("-") ) break; // invalid function def

                    next_line=next_line.substring(1).trim();  // as the line may start with +/-
                    line=line.replace("\n","");


                    if(next_line.indexOf(")")!=-1)
                    {

                        int index= next_line.indexOf(")");
                        int index1=next_line.indexOf(":",index);
                        if(index1!=-1)
                            next_line=next_line.substring(0,index1+1);
                        else
                        {
                            next_line=next_line.substring(0,index+1);
                            line=line+next_line;
                            i++;
                            next_line=diff_lines[i];
                            index1=next_line.indexOf(":",index);
                            if(index1!=-1)
                                next_line=next_line.substring(1,index1+1).trim();
                        }
                    }

                    line=line+next_line;
                }

                //System.out.println("got full function declaration:    "+line); // got full function definition
                if( line.contains(")")) {

                    FunctionDef fun_def_after = new FunctionDef();
                    String function_name_after = line.substring(line.indexOf("def") + 3, line.indexOf("(")).trim();
                    fun_def_after.functionName = function_name_after;

                    String all_parameters = line.substring(line.indexOf("(") + 1, line.indexOf(")")).trim(); //now get all the params
                    String[] parameters_list = all_parameters.split(",");
                    for (String param : parameters_list) {
                        String[] split = param.split(":");
                        if (split.length > 1)
                            fun_def_after.paramTypeMapByParam.put(split[0].trim(), split[1]);
                        else
                            fun_def_after.paramTypeMapByParam.put(split[0].trim(), "");
                    }
                    String returnType = ""; // getting return type

                    if (line.indexOf("->") != -1) {
                        returnType = line.substring(line.indexOf("->") + 2, line.length()).trim();
                    }
                    fun_def_after.returnType = returnType;

                    //functionDefHashMap.put(function_name+"_bef",fun_def_after);  // commented as no need to add this definition in map, as we only need to store the prev def

                    FunctionDef fun_def_before = functionDefHashMap.get(function_name_after + "_bef");
                    if (fun_def_before != null) {
                        // now check for type hinting
                        fun_def_after.paramTypeMapByParam.entrySet().forEach(entry -> {
                            String paramName = entry.getKey();   // after param
                            String paramType = entry.getValue();  // after type
                            //System.out.println("paramName="+paramName);
                            //System.out.println("paramType="+paramType);

                            if (fun_def_before.paramTypeMapByParam.get(paramName.trim()) != null) {

                                String pervParam = fun_def_before.paramTypeMapByParam.get(paramName.trim()).trim();
                                if ((pervParam.equals("Any") || pervParam.equals("")) && (!paramType.equals("Any") && !paramType.equals(""))) {
                                    //System.out.println("pervParam="+pervParam);
                                    //System.out.println("currentParam="+paramType);
                                    //System.out.println("gotcha");
                                    typeHintAdded = true;
                                    //localtypeHintAdded = true;
                                }
                            }
                        });
                        if (fun_def_before.returnType.equals("") && !fun_def_after.returnType.equals("")) {
                            //System.out.println("hint added");
                            typeHintAdded = true;
                            //localtypeHintAdded = true;
                        }
                    }
                    if (typeHintAdded) //&& localtypeHintAdded)
                    {
                        System.out.println("type hint info------------------------------------------------------------------------------------------------start");
                        fun_def_before.print();
                        fun_def_after.print();
                        System.out.println("type hint info------------------------------------------------------------------------------------------------end");

                        //localtypeHintAdded =false;
                        break;  // as we don't wanna process if we already got type hinting
                    }
                }
            }
        }
    }
}
