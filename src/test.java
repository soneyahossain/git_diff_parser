
import java.io.*;
import java.util.ArrayList;
public class test {



    static  ArrayList<CommitStructure> parseCommit() throws IOException
    {
        ArrayList<CommitStructure> commitStructureList= new ArrayList<CommitStructure>();

        CommitStructure commitStructure = new CommitStructure();
        //commitStructureList.add(commitStructure);
        File file = new File("log.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        boolean isFirstCommit=true;

        String st;
        while ((st = br.readLine()) != null)
        {

            if( st.startsWith("commit"))
            {


                if(isFirstCommit)
                    isFirstCommit=false;
                else commitStructure = new CommitStructure();

                commitStructureList.add(commitStructure);

                commitStructure.commit_id=st.substring("commit".length(), st.length()).trim();
            }
            else if(st.startsWith("Author"))
            {
                commitStructure.Author=st.substring("Author".length(), st.length()).trim();
            }
            else if( st.startsWith("Date"))
            {
                commitStructure.Date=st.substring("Date".length(), st.length()).trim();
            }
            else if( st.startsWith("diff --"))
            {

                //loop until the next diff

                String diff = st+"\n";
                String st_diff;

                while((st_diff = br.readLine()) != null &&  !st_diff.startsWith("commit"))
                {

                    if(st_diff.startsWith("diff --"))
                    {
                        Diff diffObject= new Diff();
                        diffObject.diff=diff;
                       // diffObject.getAllFuntionDef();
                        commitStructure.diffList.add(diffObject);
                        //System.out.println("diff=============="+diff);
                        diff = st_diff+"\n";

                    }else
                    {
                        diff=diff+st_diff+"\n";
                    }
                }
                Diff diffObject= new Diff();
                diffObject.diff=diff;
                //diffObject.getAllFuntionDef();
                commitStructure.diffList.add(diffObject);
                // System.out.println("diffs=============="+st_diff);


                if(st_diff!=null  && st_diff.startsWith("commit"))
                {
                    //System.out.println("diffe=============="+st_diff);
                    commitStructure = new CommitStructure();
                    commitStructureList.add(commitStructure);
                    commitStructure.commit_id=st_diff.substring("commit".length(), st_diff.length()).trim();
                   // System.out.println("commitStructure.commit_id=============="+commitStructure.commit_id);
                }
            }
        }
       return commitStructureList;
    }


    public static void main(String args[]) throws IOException {
        // first parse this log file
        ArrayList<CommitStructure> cs= parseCommit();
        ArrayList<String> typeHintedCommitInfo= new ArrayList<String>();

        int total_diff=0;
        //System.out.println("total commits=============="+cs.size());
        for(CommitStructure e: cs){
            boolean typeHintAdded= e.getTypeHintInfo();
            if(typeHintAdded)
            {
                typeHintedCommitInfo.add(e.Date+","+e.commit_id+"\n");

            }

            //e.toString_();
            //total_diff+=e.diffList.size();
        }
        System.out.println("type hint info=============="+typeHintedCommitInfo.toString());

    }
}
