package com.davi.library.cli;

import com.davi.library.domain.Book;
import com.davi.library.service.BookService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class BookMenu {

    private final BookService bookService;
    private final Scanner scanner = new Scanner(System.in);

    public BookMenu(BookService bookService) {
        this.bookService = bookService;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Menu Livros ---");
            System.out.println("1. Cadastrar livro");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por ISBN");
            System.out.println("4. Listar todos");
            System.out.println("5. Atualizar livro");
            System.out.println("6. Deletar livro");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();

            try {
                int option = Integer.parseInt(input);

                switch (option) {
                    case 1 -> create();
                    case 2 -> findById();
                    case 3 -> findByIsbn();
                    case 4 -> findAll();
                    case 5 -> update();
                    case 6 -> delete();
                    case 0 -> back = true;
                    default -> System.out.println("Opção inválida.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Digite um número válido.");
            } catch (Exception e) {
                System.out.println("Erro: " + e.getMessage());
            }
        }
    }

    private void create() {
        System.out.print("Título: ");
        String title = scanner.nextLine();
        System.out.print("Autor: ");
        String author = scanner.nextLine();
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Book book = bookService.create(title, author, isbn);
        System.out.println("Livro cadastrado! ID: " + book.getId());
    }

    private void findById() {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        Book book = bookService.findById(id);
        System.out.println(book.getId() + " | " + book.getTitle() + " | " + book.getAuthor() + " | " + book.getIsbn());
    }

    private void findByIsbn() {
        System.out.print("ISBN: ");
        String isbn = scanner.nextLine();

        Optional<Book> result = bookService.findByIsbn(isbn);

        if (result.isPresent()) {
            Book book = result.get();
            System.out.println(book.getId() + " | " + book.getTitle() + " | " + book.getAuthor());
        } else {
            System.out.println("Nenhum livro encontrado com esse ISBN.");
        }
    }

    private void findAll() {
        List<Book> books = bookService.findAll();

        if (books.isEmpty()) {
            System.out.println("Nenhum livro cadastrado.");
            return;
        }

        for (Book book : books) {
            System.out.println(book.getId() + " | " + book.getTitle() + " | " + book.getAuthor() + " | " + book.getIsbn());
        }
    }

    private void update() {
        System.out.print("ID do livro: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Novo título: ");
        String title = scanner.nextLine();
        System.out.print("Novo autor: ");
        String author = scanner.nextLine();
        System.out.print("Novo ISBN: ");
        String isbn = scanner.nextLine();

        Book updated = bookService.update(id, title, author, isbn);
        System.out.println("Livro atualizado: " + updated.getTitle());
    }

    private void delete() {
        System.out.print("ID do livro: ");
        Long id = Long.parseLong(scanner.nextLine());

        Book deleted = bookService.delete(id);
        System.out.println("Livro deletado: " + deleted.getTitle());
    }
}