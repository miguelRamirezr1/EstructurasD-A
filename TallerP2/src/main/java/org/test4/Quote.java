package org.test4;

public class Quote {
    private String quote;  // quote text
    private String author;  // author name

    public String getQuote() {
        return quote;
    }

    public String getAuthor() {
        return author;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "text='" + quote + '\'' +
                ", author='" + author + '\'' +
                '}';
    }
}
