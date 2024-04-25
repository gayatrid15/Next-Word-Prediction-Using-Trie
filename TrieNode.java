package com.project;

public class TrieNode {
    static final int ALPHABET_SIZE = 26;
    TrieNode[] children;
    boolean isWordEnd;

    TrieNode() {
        this.children = new TrieNode[ALPHABET_SIZE];
        this.isWordEnd = false;
    }
}