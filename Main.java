package com.project;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        AutoCompleteTrie.root = new TrieNode();
        AutoCompleteTrie.insert("hello");
        AutoCompleteTrie.insert("dog");
        AutoCompleteTrie.insert("hell");
        AutoCompleteTrie.insert("cat");
        AutoCompleteTrie.insert("a");
        AutoCompleteTrie.insert("ayush");
        AutoCompleteTrie.insert("ayushi");
        AutoCompleteTrie.insert("arushi");
        AutoCompleteTrie.insert("aditi");
        AutoCompleteTrie.insert("help");
        AutoCompleteTrie.insert("helps");
        AutoCompleteTrie.insert("prasanna");

        JFrame frame = new JFrame("AutoComplete Trie");
        frame.setSize(700, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField textField = new JTextField(20);
        JPanel suggestionPanel = new JPanel(new GridLayout(4, 3));
        JPanel keyboardPanel = createKeyboardPanel(textField);
        inputPanel.setBackground(Color.gray);
        suggestionPanel.setBackground(Color.DARK_GRAY);
        keyboardPanel.setBackground(Color.DARK_GRAY);
        List<JButton> suggestionButtons = new ArrayList<>();

        inputPanel.add(new JLabel("Enter the string: "), BorderLayout.NORTH);
        inputPanel.add(textField, BorderLayout.CENTER);

        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(suggestionPanel, BorderLayout.CENTER);
        frame.add(keyboardPanel, BorderLayout.SOUTH);

        textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                updateSuggestions();
            }

            private void updateSuggestions() {
                final String query = textField.getText().toLowerCase();
                suggestionPanel.removeAll();
                final String[] queries = query.split(" ");
                for (String word : queries) {
                    if (!word.isEmpty()) {
                        List<String> suggestions = AutoCompleteTrie.printAutoSuggestions(word);
                        for (String suggestion : suggestions) {
                            JButton button = new JButton(suggestion);
                            button.addActionListener(new SuggestionActionListener(textField, suggestion));
                            suggestionButtons.add(button);
                            suggestionPanel.add(button);
                        }
                        if (!suggestions.contains(word)) {
                            AutoCompleteTrie.addWordIfNotExist(word);
                            JButton button = new JButton(word);
                            button.addActionListener(new SuggestionActionListener(textField, word));
                            suggestionButtons.add(button);
                            suggestionPanel.add(button);
                        }
                    }
                }
                frame.revalidate();
                frame.repaint();
            }

        });
        frame.setVisible(true);
    }

    private static JPanel createKeyboardPanel(JTextField textField) {
        JPanel keyboardPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridwidth = 1;
        String[][] keyValues = {
                {"1", "2", "3", "4", "5", "6", "7", "8", "9", "0"},
                {"q", "w", "e", "r", "t", "y", "u", "i", "o", "p"},
                {"a", "s", "d", "f", "g", "h", "j", "k", "l", "Clear"},
                {"z", "x", "c", "v", "b", "n", "m", ".", "Space", "Delete"},
        };
        Color buttonColor = Color.gray;

        for (int i = 0; i < keyValues.length; i++) {
            for (int j = 0; j < keyValues[i].length; j++) {
                JButton button = new JButton(keyValues[i][j]);
                button.setBackground(buttonColor);
                if (keyValues[i][j].equals("Space") || keyValues[i][j].equals("Delete") || keyValues[i][j].equals("Clear")) {
                    button.addActionListener(new KeyboardActionListener(textField, keyValues[i][j]));
                } else {
                    button.addActionListener(new KeyboardActionListener(textField, keyValues[i][j]));
                }
                constraints.gridx = j;
                constraints.gridy = i;
                keyboardPanel.add(button, constraints);
            }
        }
        return keyboardPanel;
    }
}

class KeyboardActionListener implements ActionListener {
    private final JTextField textField;
    private final String keyValue;

    public KeyboardActionListener(JTextField textField, String keyValue) {
        this.textField = textField;
        this.keyValue = keyValue;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        switch (keyValue) {
            case "Space":
                textField.setText(textField.getText() + " ");
                break;
            case "Delete":
                String text = textField.getText();
                if (!text.isEmpty()) {
                    textField.setText(text.substring(0, text.length() - 1));
                }
                break;
            case "Clear":
                textField.setText("");
                break;
            default:
                textField.setText(textField.getText() + keyValue);
                break;
        }
    }
}

class SuggestionActionListener implements ActionListener {
    private final JTextField textField;
    private final String suggestedWord;

    public SuggestionActionListener(JTextField textField, String suggestedWord) {
        this.textField = textField;
        this.suggestedWord = suggestedWord;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        String[] words = textField.getText().split("\\s+");
        if (words.length > 0) {
            words[words.length - 1] = suggestedWord;
            textField.setText(String.join(" ", words));
        }
    }
}

