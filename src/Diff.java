import java.util.HashMap;

public class Diff {

    String diff;
    HashMap<String, FunctionDef> functionDefHashMap = new  HashMap<String, FunctionDef>();
    boolean typeHintAdded=false;




    public void getAllFunctionDef()
    {

        /*
        -    def addTransceiver(self, trackOrKind, direction="sendrecv"):
+    def addTransceiver(
+        self, trackOrKind: Union[str, MediaStreamTrack], direction: str = "sendrecv"
+    ) -> RTCRtpTransceiver:


        */
        String[] lines = diff.split(System.getProperty("line.separator"));
        for(int i=0;i<lines.length;i++)
        {
            String line=lines[i];

            if(line.startsWith("-    def") ||line.startsWith("-    async"))
            {
                //System.out.println(line);


                while(!line.contains(")"))
                {
                    i++;
                    String next_line=lines[i];
                   // System.out.println("next_line=="+next_line);

                    next_line=next_line.substring(1).trim();
                   // System.out.println("next_line=="+next_line);

                    line=line.replace("\n","");
                  //  System.out.println("line=="+line);

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
                            next_line=lines[i];
                            index1=next_line.indexOf(":",index);
                            if(index1!=-1)
                                next_line=next_line.substring(1,index1+1).trim();
                        }
                    }

                    line=line+next_line;
                    //System.out.println("heree_bef=="+line);

                }


                FunctionDef fundef= new FunctionDef();
                String function_name= line.substring(line.indexOf("def")+3, line.indexOf("(")).trim();
                // System.out.println(function_name);
                fundef.functionName=function_name;
                //now get all the params

                String parameters=line.substring(line.indexOf("(")+1, line.indexOf(")")).trim();

                //System.out.println(parameters);
                String[] parameters_ = parameters.split(",");
                for(String param: parameters_)
                {
                    String[] slipType = param.split(":");
                    if(slipType.length>1)
                       fundef.paramTypeMapByParam.put(slipType[0].trim(),slipType[1]);
                    else
                        fundef.paramTypeMapByParam.put(slipType[0].trim(),"");
                }
                String returnType="";
                if(line.indexOf("->")!=-1) {

                    returnType = line.substring(line.indexOf("->") + 2, line.length()).trim();
                }
                fundef.returnType = returnType;
                functionDefHashMap.put(function_name+"_bef",fundef);


            }

            if(line.startsWith("+    def") || line.startsWith("+    async"))
            {
                //System.out.println(line);



                while(!line.contains(")"))
                {
                    i++;
                    String next_line=lines[i];
                   // System.out.println("next_line=="+next_line);

                    next_line=next_line.substring(1).trim();
                   // System.out.println("next_line=="+next_line);

                    line=line.replace("\n","");
                   // System.out.println("line=="+line);

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
                            next_line=lines[i];
                            index1=next_line.indexOf(":",index);
                            if(index1!=-1)
                                next_line=next_line.substring(1,index1+1).trim();
                        }
                    }

                    line=line+next_line;
                   // System.out.println("heree=="+line);

                }


                FunctionDef fundef_added= new FunctionDef();
                String function_name= line.substring(line.indexOf("def")+3, line.indexOf("(")).trim();
               // System.out.println(function_name);
                fundef_added.functionName=function_name;
                //now get all the params

                String parameters=line.substring(line.indexOf("(")+1, line.indexOf(")")).trim();
                FunctionDef fundef_bef= functionDefHashMap.get(function_name+"_bef");
                if(fundef_bef==null)
                {
                    //System.out.println("nulll...........");
                }

                //System.out.println(parameters);
                String[] parameters_ = parameters.split(",");
                for(String param: parameters_)
                {
                    String[] slipType = param.split(":");
                    if(slipType.length>1) {

                        //means type hint added
                        fundef_added.paramTypeMapByParam.put(slipType[0].trim(), slipType[1]);



                        if( fundef_bef!=null ) {
                            //System.out.println("not null ..........."+slipType[0]);

                            fundef_bef.print();
                            if (fundef_bef.paramTypeMapByParam.get(slipType[0].trim())!=null && fundef_bef.paramTypeMapByParam.get(slipType[0].trim()).equals(""))

                                //System.out.println("hint added");
                                typeHintAdded = true;
                        }


                    }
                    else {


                        fundef_added.paramTypeMapByParam.put(slipType[0].trim(), "");

                    }
                }
                String returnType="";
                if(line.indexOf("->")!=-1) {

                    returnType = line.substring(line.indexOf("->") + 2, line.length()).trim();
                    if( fundef_bef!=null && fundef_bef.returnType.equals(""))
                    {
                        //System.out.println("hint added");
                        typeHintAdded=true;
                    }
                }
                fundef_added.returnType = returnType;
                functionDefHashMap.put(function_name+"_aft",fundef_added);
            }
        }


    }

}
