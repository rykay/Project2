import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class Trie {
    private Node root;

    public Trie() {
        root = new Node(' ');
    }
    /* This function is used to insert a word in trie*/
    public void insert(String word) { //inserts a node with count and type passed
        if (search(word) == true) //check if word has been inserted
            return;
        Spelling spelling = new Spelling();
        String token = spelling.getToken(word);
        long nodeCount = spelling.getTokenCount(word);
        Node current = root; //start at root node
        for (char ch : token.toCharArray()) { //loop through chars
            Node child = current.getChild(ch);
            if (child != null)
                current = child;
            else {
                // If child not present, adding it to the list
                current.childList.add(new Node(ch));
                current = current.getChild(ch);
            }
            long count = current.getCount();
            count++;
            current.setCount(count);
        }
        current.setWord(true);//node is a word
        current.setCount(nodeCount);
    }
    /* This function is used to search a word in trie*/
    public boolean search(String word) {
        Node current = root;
        for (char ch : word.toCharArray() ) {
            if (current.getChild(ch) == null)
                return false;
            else
                current = current.getChild(ch);
        }
        if (current.isWord() == true)
            return true;
        return false;
    }
    /* This function is used to remove function from trie*/
    public void remove(String word) {
        if (search(word) == false) {
            System.out.println(word + " is not present in the trie!");
            return;
        }
        Node current = root;
        for (char ch : word.toCharArray()) {
            Node child = current.getChild(ch);
            if (child.getCount() == 1) {
                current.childList.remove(child);
                return;
            }
            else {
                long count = current.getCount();
                count--;
                child.setCount(count);
                current = child;
            }
        }
        current.setWord(false);
    }

    public void printAllWordsInTrie(Node root, String s) {
        Node current = root;
        if(root.childList==null || root.childList.size()==0)
            return;
        Iterator iter = current.childList.iterator();
        while(iter.hasNext()) {
            Node node = (Node) iter.next();
            s += node.getType();
            printAllWordsInTrie(node,s);
            if(node.isWord() == true) {
                System.out.print(" | Type: " + s + " Count: " +  node.getCount() + " | ");
                s=s.substring(0,s.length()-1);
            } else {
                s=s.substring(0,s.length()-1);
            }
        }

    }

    public void loadTrie()throws IOException {
        String fileName = "C:\\Users\\Ryan Kennedy\\Desktop\\unigram_freq.csv";
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new java.io.FileReader(file));
        String line = null;
        int iteration = 0;
        while((line = br.readLine()) != null){
            if(iteration == 0){ //skip first line
                iteration++;
                continue;
            }
            insert(line);
        }

    }

    public void loadImprovementTrie()throws IOException {
        String fileName = "C:\\Users\\Ryan Kennedy\\Desktop\\misspelling.csv";
        File file = new File(fileName);
        BufferedReader br = new BufferedReader(new java.io.FileReader(file));
        String line = null;
        int iteration = 0;
        while((line = br.readLine()) != null){
            if(iteration == 0){ //skip first line
                iteration++;
                continue;
            }
            insert(line);
        }

    }

    public List<String> findAllWordsForPrefix(String prefix, Node root) {
        List<String> words = new ArrayList<>();
        Node current = root;
        for(Character c: prefix.toCharArray()) {
            Node nextNode = current.getChild(c);
            if(nextNode == null) return words;
            current = nextNode;
        }
        if(!current.getChildList().isEmpty()) {
            findAllWordsForPrefixRecursively(prefix, current, words);
        } else {
            if(current.isWord()) words.add(prefix);
        }
        return words;
    }

    public void findAllWordsForPrefixRecursively(String prefix, Node node, List<String> words) {
        if(node.isWord()) words.add(prefix);
        if(node.getChildList().isEmpty()) {
            return;
        }
        int length = node.getChildList().size();
        char[] charArray = new char[length];
        for(int i = 0; i < length; i++) {
            charArray[i] = node.getChildList().get(i).getType();
        }
        for(Character c: charArray) {
            findAllWordsForPrefixRecursively(prefix + c, node.getChild(c), words);
        }
    }

    public List<Long> findAllCountsForPrefix(String prefix, Node root) {
        List<Long> words = new ArrayList<>();
        Node current = root;
        for(Character c: prefix.toCharArray()) {
            Node nextNode = current.getChild(c);
            if(nextNode == null) return words;
            current = nextNode;
        }
        if(!current.getChildList().isEmpty()) {
            findAllCountsRecursively(prefix, current, words);
        } else {
            if(current.isWord()) words.add(current.getCount());
        }
        return words;
    }

    public void findAllCountsRecursively(String prefix, Node node, List<Long> words) {
        if(node.isWord()) words.add(node.getCount());
        if(node.getChildList().isEmpty()) {
            return;
        }
        int length = node.getChildList().size();
        char[] charArray = new char[length];
        for(int i = 0; i < length; i++) {
            charArray[i] = node.getChildList().get(i).getType();
        }
        for(Character c: charArray) {
            findAllCountsRecursively(prefix + c, node.getChild(c), words);
        }
    }

    public List<List<String>> generatePrefixLists(int count, Node root, String prefix){
            //given list of all strings with common prefix, find count most common up to count of prefix.
            List<List<String>> mostCommonList = new ArrayList<>();
            List<String> prefixList;
            for(int i = 0; i < count; i++){
                String prefixGrow = prefix.substring(0, i+1);
                prefixList = findAllWordsForPrefix(prefixGrow, root);
                mostCommonList.add(prefixList);
            }

            return mostCommonList;
    }


    public Node getRoot() {
        return root;
    }

    public void setRoot(Node root) {
        this.root = root;
    }

    public static void main(String[] args) throws IOException {
        Trie trie = new Trie();
        Node root = trie.root;
        trie.loadTrie();
        //trie.printAllWordsInTrie(root, "");
        List<List<String>> stringList = trie.generatePrefixLists(5, root, "onoma"); //prints the prefix lists
        List<Long> maxCountList = new ArrayList<>();
        maxCountList = trie.findAllCountsForPrefix("o", root);
        maxCountList.sort(Collections.reverseOrder());
        System.out.println(maxCountList); //prints sorted count values in decreasing order
        int count = 5;
        List<Long> countMostCommonList = new ArrayList<>(); //List that stores the highest counts
        for(int i = 0; i < count; i++){
            countMostCommonList.set(i, maxCountList.get(i));
        }
        List<Long> list = trie.findAllCountsForPrefix("o", root);
        for (int i = 0; i < list.size(); i++) {
            if(countMostCommonList.contains(i)){

            }

        }



    }
}
