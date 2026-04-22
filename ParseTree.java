import java.util.ArrayList;

class ParseTree {
    String name; 
    ArrayList<ParseTree> children;

    public ParseTree(String name, ArrayList<ParseTree> children) {
        this.name = name;
        this.children = children;
    }

    public ParseTree(String name) {
        this.name = name;
        this.children = new ArrayList<>();
    }

}
