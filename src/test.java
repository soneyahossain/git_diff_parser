
import java.io.*;
import java.util.ArrayList;
import java.util.stream.StreamSupport;

public class test {



    static  ArrayList<CommitStructure> parseCommit() throws IOException
    {
        ArrayList<CommitStructure> commitStructureList= new ArrayList<CommitStructure>();


        CommitStructure commitStructure = new CommitStructure();
        commitStructureList.add(commitStructure);
        File file = new File("log.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));

        String st;
        while ((st = br.readLine()) != null)
        {

            if( st.contains("commit"))
            {
                commitStructure.commit_id=st.substring("commit".length(), st.length()).trim();
            }
            if(st.contains("Author"))
            {
                commitStructure.Author=st.substring("Author".length(), st.length()).trim();
            }
            if( st.contains("Date"))
            {
                commitStructure.Date=st.substring("Date".length(), st.length()).trim();
            }
            if( st.startsWith("diff --"))
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
                commitStructure.diffList.add(diffObject);
                //System.out.println("diff=============="+diff);


                if(st_diff!=null  && st_diff.startsWith("commit"))
                {
                    commitStructure = new CommitStructure();
                    commitStructureList.add(commitStructure);
                    commitStructure.commit_id=st_diff.substring("commit".length(), st_diff.length()).trim();
                }
            }
        }
       return commitStructureList;
    }


    public static void main(String args[]) throws IOException {


        // first parse this log file
        ArrayList<CommitStructure> cs= parseCommit();

        for(CommitStructure e: cs)
             e.toString_();

    }
}
