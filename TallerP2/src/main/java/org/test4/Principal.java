package org.test4;

import java.util.List;

public class Principal {

    public static void main(String[] args) throws Exception {

        llamadoApi quoteService = new llamadoApi();
        List<Quote> quotes = quoteService.fetchQuotes();

        Quote firstQuote = quotes.get(0);
        System.out.println("Autor: " + firstQuote.getAuthor());
        Words words = new Words();
        words.encryptMessage(firstQuote.getQuote());
    }
}