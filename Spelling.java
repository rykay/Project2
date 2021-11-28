import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Spelling {

    public String getToken(String token) {
        char breaker = ',';
        StringBuilder newString = new StringBuilder();
        int i = 0;
        while (token.charAt(i) != breaker) {
            newString.append(token.charAt(i));
            i++;
        }
        return String.valueOf(newString);
    }

    public long getTokenCount(String token) {
        String newString = "";
        for (int i = 0; i < token.length(); i++) {
            Boolean flag = Character.isDigit(token.charAt(i));
            if (flag) {
                newString += token.charAt(i);
            }
        }
        long tokenCount = Long.parseLong(newString);
        return tokenCount;
    }


    public List<List<String>> suggest(String token, int count) throws IOException {
        Trie trie = new Trie();
        Node root = trie.getRoot();
        trie.loadTrie();
        List<List<String>> suggestionList = new ArrayList<>();
        if (trie.search(token) == false) {
            System.out.println("The token you've entered does not exist");
        }
        if (count > token.length()) {
            System.out.println("Please enter a valid count.");
        }
        List<List<String>> stringList = trie.generatePrefixLists(count, root, token);//stores the lists of all words with count parts of prefix o, on, ono, onom, onoma...


        List<List<Long>> countsList = new ArrayList<>();
        for(int i = 0; i < count; i++){
            List<Long> tempList = trie.findAllCountsForPrefix(token.substring(0, i +1), root);
            tempList.sort(Collections.reverseOrder());
            countsList.add(tempList);
        }
        //prints sorted count values in decreasing order


        long[][] highestCountList = new long[count][count]; //store the (count) greatest counts of sorted count arrays for each prefix of token
        for (int i = 0; i < count; i++) {
            for(int j = 0; j < count; j++){
                try{
                    highestCountList[i][j] = countsList.get(i).get(j);
                }
                catch (IndexOutOfBoundsException e){
                    highestCountList[i][j] = countsList.get(i-1).get(j-1);
                }

            }

        }

        //search the unsorted array for each greatest count, get index, and add same index of token from mostCommonList
        List<Long> list = trie.findAllCountsForPrefix(token.substring(0, 1), root); //all counts of o (index matches with all token of o)
        for (int i = 0; i < highestCountList.length; i++) {
            List<String> tempList = new ArrayList<>();
            for(int j = 0; j < highestCountList[i].length; j++){
                if(list.contains(highestCountList[i][j])){
                    int index = list.indexOf(highestCountList[i][j]);
                    tempList.add(trie.findAllWordsForPrefix(token.substring(0, 1), root).get(index));
                }
            }
            suggestionList.add(tempList);
        }
        return suggestionList;
    }



    public static void main (String[]args) throws IOException {
            Spelling spelling = new Spelling();
            Trie trie = new Trie();
            Node root = trie.getRoot();
            trie.loadTrie();
            System.out.println(spelling.suggest("onoma", 5));
            //Trie improvementTrie = new Trie();
            //improvementTrie.loadImprovementTrie();
            //Node improvementRoot = new Node();
            //improvementTrie.setRoot(improvementRoot);
            //System.out.println(spelling.suggest("hello", 2));


        }
    }
