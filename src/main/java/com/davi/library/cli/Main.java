package com.davi.library.cli;

import com.davi.library.connection.ConnectionFactory;
import com.davi.library.repository.JdbcBookRepository;
import com.davi.library.repository.JdbcLoanRepository;
import com.davi.library.repository.JdbcReaderRepository;
import com.davi.library.service.BookService;
import com.davi.library.service.LoanService;
import com.davi.library.service.ReaderService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = new ConnectionFactory();

        ReaderService readerService = new ReaderService(new JdbcReaderRepository(connectionFactory));
        BookService bookService = new BookService(new JdbcBookRepository(connectionFactory));
        LoanService loanService = new LoanService(new JdbcLoanRepository(connectionFactory), bookService, readerService);

        ReaderMenu readerMenu = new ReaderMenu(readerService);
        BookMenu bookMenu = new BookMenu(bookService);
        LoanMenu loanMenu = new LoanMenu(loanService);

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while (running) {
            System.out.println("\n=== Sistema de Biblioteca ===");
            System.out.println("1. Menu Leitores");
            System.out.println("2. Menu Livros");
            System.out.println("3. Menu Empréstimos");
            System.out.println("0. Sair");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();
            int option;

            try {
                option = Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
                continue;
            }

            switch (option) {
                case 1 -> readerMenu.show();
                case 2 -> bookMenu.show();
                case 3 -> loanMenu.show();
                case 0 -> {
                    running = false;
                    System.out.println("Encerrando...");
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }
}