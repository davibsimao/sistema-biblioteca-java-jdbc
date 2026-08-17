package com.davi.library.cli;

import com.davi.library.domain.Reader;
import com.davi.library.service.ReaderService;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class ReaderMenu {

    private final ReaderService readerService;
    private final Scanner scanner = new Scanner(System.in);

    public ReaderMenu(ReaderService readerService) {
        this.readerService = readerService;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Menu Leitores ---");
            System.out.println("1. Cadastrar leitor");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Buscar por email");
            System.out.println("4. Listar todos");
            System.out.println("5. Atualizar leitor");
            System.out.println("6. Deletar leitor");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();

            try {
                int option = Integer.parseInt(input);

                switch (option) {
                    case 1 -> create();
                    case 2 -> findById();
                    case 3 -> findByEmail();
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
        System.out.print("Nome: ");
        String name = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Reader reader = readerService.create(name, email);
        System.out.println("Leitor criado! ID: " + reader.getId());
    }

    private void findById() {
        System.out.print("ID: ");
        Long id = Long.parseLong(scanner.nextLine());

        Reader reader = readerService.findById(id);
        System.out.println(reader.getId() + " | " + reader.getName() + " | " + reader.getEmail());
    }

    private void findByEmail() {
        System.out.print("Email: ");
        String email = scanner.nextLine();

        Optional<Reader> result = readerService.findByEmail(email);

        if (result.isPresent()) {
            Reader reader = result.get();
            System.out.println(reader.getId() + " | " + reader.getName() + " | " + reader.getEmail());
        } else {
            System.out.println("Nenhum leitor encontrado com esse email.");
        }
    }

    private void findAll() {
        List<Reader> readers = readerService.findAll();

        if (readers.isEmpty()) {
            System.out.println("Nenhum leitor cadastrado.");
            return;
        }

        for (Reader reader : readers) {
            System.out.println(reader.getId() + " | " + reader.getName() + " | " + reader.getEmail());
        }
    }

    private void update() {
        System.out.print("ID do leitor: ");
        Long id = Long.parseLong(scanner.nextLine());
        System.out.print("Novo nome: ");
        String name = scanner.nextLine();
        System.out.print("Novo email: ");
        String email = scanner.nextLine();

        Reader updated = readerService.update(id, name, email);
        System.out.println("Leitor atualizado: " + updated.getName() + " | " + updated.getEmail());
    }

    private void delete() {
        System.out.print("ID do leitor: ");
        Long id = Long.parseLong(scanner.nextLine());

        Reader deleted = readerService.delete(id);
        System.out.println("Leitor deletado: " + deleted.getName());
    }
}