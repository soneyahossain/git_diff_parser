import java.util.ArrayList;

public class CommitStructure {

    String commit_id;
    String Author;
    String Date;
    String commit_title;
    ArrayList<Diff> diffList=new  ArrayList<Diff>() ;

    void toString_()
    {
        System.out.println("commit_id=============="+commit_id);
        System.out.println("Author=============="+Author);
        System.out.println("Date=============="+Date);
        System.out.println("diffList=============="+diffList.size());

    }
}
