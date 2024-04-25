package com.project;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteTrie {
    static final int CHAR_TO_INDEX = 97;
    static TrieNode root;

    public static void insert(String key) {
        TrieNode pCrawl = root;
        for (int i = 0; i < key.length(); i++) {
            char c = Character.toLowerCase(key.charAt(i));
            if (c == ' ') {
                pCrawl.isWordEnd = true;
                pCrawl = root;
            } else {
                int index = c - 'a';
                if (pCrawl.children[index] == null)
                    pCrawl.children[index] = new TrieNode();
                pCrawl = pCrawl.children[index];
            }
        }
        pCrawl.isWordEnd = true;
    }

    public static boolean isLastNode(TrieNode root) {
        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++)
            if (root.children[i] != null)
                return false;
        return true;
    }

    public static void suggestionsRec(TrieNode root, String currPrefix, List<String> suggestions) {
        if (root.isWordEnd) {
            suggestions.add(currPrefix);
        }

        if (isLastNode(root)) return;

        for (int i = 0; i < TrieNode.ALPHABET_SIZE; i++) {
            if (root.children[i] != null) {
                suggestionsRec(root.children[i], currPrefix + (char) (i + CHAR_TO_INDEX), suggestions);
            }
        }
    }

    public static List<String> printAutoSuggestions(String query) {
        List<String> suggestions = new ArrayList<>();
        TrieNode pCrawl = root;

        for (int level = 0; level < query.length(); level++) {
            char c = Character.toLowerCase(query.charAt(level));
            if (c == ' ') {
                return suggestions;
            }
            int index = c - 'a';
            if (index < 0 || index >= TrieNode.ALPHABET_SIZE || pCrawl.children[index] == null)
                return suggestions;
            pCrawl = pCrawl.children[index];
        }

        boolean isWord = pCrawl.isWordEnd;
        boolean isLast = isLastNode(pCrawl);

        if (isWord && isLast) {
            suggestions.add(query);
            return suggestions;
        }

        if (!isLast) {
            suggestionsRec(pCrawl, query, suggestions);
        }
        return suggestions;
    }
    
    public static void addWordIfNotExist(String key) {
        
        String word = "";
        for (int i = 0; i < key.length(); i++) {
            char c = key.charAt(i);
            if (c == ' ') {
                if (!word.isEmpty()) {
                    insert(word);
                    word = "";
                }
            } else {
                word += c;
            }
        }
        if (!word.isEmpty()) {
            insert(word);
        }
    }
}
