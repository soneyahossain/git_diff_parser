
import java.io.*;
import java.util.ArrayList;
public class test {

    static  ArrayList<CommitStructure> parseCommit() throws IOException
    {
        ArrayList<CommitStructure> commitStructureList = new ArrayList<CommitStructure>();
        CommitStructure commitStructure = new CommitStructure();
        File file = new File("/u/sh7hv/IdeaProjects/git_diff_parser/log1.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        boolean isFirstCommit=true;

        String lineFromLog;
        while ((lineFromLog = br.readLine()) != null)
        {
            if( lineFromLog.startsWith("commit"))
            {
                if(isFirstCommit)
                    isFirstCommit=false;
                else commitStructure = new CommitStructure();
                commitStructureList.add(commitStructure);
                commitStructure.commit_id=lineFromLog.substring("commit".length(), lineFromLog.length()).trim();
            }
            else if(lineFromLog.startsWith("Author"))
            {
                commitStructure.Author=lineFromLog.substring("Author".length(), lineFromLog.length()).trim();
            }
            else if( lineFromLog.startsWith("Date"))
            {
                commitStructure.Date=lineFromLog.substring("Date".length(), lineFromLog.length()).trim();
            }
            else if( lineFromLog.startsWith("diff --"))
            {
                //loop until the next diff
                String diff = lineFromLog+"\n";
                String st_diff;
                while((st_diff = br.readLine()) != null &&  !st_diff.startsWith("commit"))
                {
                    if(st_diff.startsWith("diff --"))
                    {
                        Diff diffObject= new Diff();
                        diffObject.diff=diff;
                        commitStructure.diffList.add(diffObject);
                        diff = st_diff+"\n";
                    }else
                    {
                        diff=diff+st_diff+"\n";
                    }
                }
                Diff diffObject= new Diff();
                diffObject.diff=diff;
                commitStructure.diffList.add(diffObject);
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
        ArrayList<String> typeHintedCommitInfo= new ArrayList<String>();
        int total_diff=0;
        System.out.println("total commits=============="+cs.size());
        for(CommitStructure e: cs){
            boolean typeHintAdded= e.getTypeHintInfo();
            if(typeHintAdded)
            {
                typeHintedCommitInfo.add(e.Date+" ,commit id: "+e.commit_id+"\n");
            }
        }
        for(CommitStructure e: cs){
            e.clearMap();
        }
        System.out.println("type hint info==============\n"+typeHintedCommitInfo.toString());

    }
}
