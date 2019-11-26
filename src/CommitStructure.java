import java.util.ArrayList;

public class CommitStructure {

    String commit_id;
    String Author;
    String Date;
    String commit_title;
    ArrayList<Diff> diffList=new  ArrayList<Diff>() ;
    boolean typeHintAdded=false;

    void isTypeHintAdded()
    {
        for(Diff e: diffList){
            e.getAllFunctionDef();
            if(e.typeHintAdded)
                typeHintAdded=true;
        }
    }
    void clearMap()
    {
        for(Diff e: diffList){
            e.functionDefHashMap.clear();
        }
    }


    boolean getTypeHintInfo()
    {
        this.isTypeHintAdded();
        return typeHintAdded;
    }

    void toString_()
    {
        System.out.println("commit "+commit_id);
        System.out.println("Author=============="+Author);
        System.out.println("Date=============="+Date);
        System.out.println("diffList=============="+diffList.size());

    }
}
