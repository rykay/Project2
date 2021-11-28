import java.util.LinkedList;

public class Node {
    private long count;
    private char type;
    private boolean isWord;
    LinkedList<Node> childList;

    public Node(){}
    public Node(char type){
        childList = new LinkedList<>();
        isWord = false;
        this.type = type;
        count = 0;
    }

    public long getCount() {
        return count;
    }


    public void setCount(long count) {
        this.count = count;
    }

    public Node getChild(char type) {
        if (childList != null) {
            for (Node child : childList) {
                if (child.type == type) {
                    return child;
                }
            }
        }
        return null;
    }

    public LinkedList<Node> getChildList() {
        return childList;
    }

    public void setChildList(LinkedList<Node> childList) {
        this.childList = childList;
    }

    public char getType() {
        return type;
    }

    public void setType(char type) {
        this.type = type;
    }

    public boolean isWord() {
        return isWord;
    }

    public void setWord(boolean word) {
        isWord = word;
    }


}
