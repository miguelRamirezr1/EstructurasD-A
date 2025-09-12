package org.test7;

public class Quote {
    private String q;  // quote text
    private String a;  // author name

    public String getQuote() {
        return q;
    }

    public String getAuthor() {
        return a;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "text='" + q + '\'' +
                ", author='" + a + '\'' +
                '}';
    }
}
