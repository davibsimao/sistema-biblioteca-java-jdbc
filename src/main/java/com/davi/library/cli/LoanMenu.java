package com.davi.library.cli;

import com.davi.library.domain.Loan;
import com.davi.library.domain.LoanStatus;
import com.davi.library.service.LoanService;

import java.util.List;
import java.util.Scanner;

public class LoanMenu {

    private final LoanService loanService;
    private final Scanner scanner = new Scanner(System.in);

    public LoanMenu(LoanService loanService) {
        this.loanService = loanService;
    }

    public void show() {
        boolean back = false;

        while (!back) {
            System.out.println("\n--- Menu Empréstimos ---");
            System.out.println("1. Criar empréstimo");
            System.out.println("2. Buscar por ID");
            System.out.println("3. Listar todos");
            System.out.println("4. Listar por status");
            System.out.println("5. Devolver livro");
            System.out.println("0. Voltar");
            System.out.print("Escolha: ");

            String input = scanner.nextLine();

            try {
                int option = Integer.parseInt(input);

                switch (option) {
                    case 1 -> create();
                    case 2 -> findById();
                    case 3 -> findAll();
                    case 4 -> findByStatus();
                    case 5 -> returnLoan();
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
        System.out.print("ID do livro: ");
        Long bookId = Long.parseLong(scanner.nextLine());
        System.out.print("ID do leitor: ");
        Long readerId = Long.parseLong(scanner.nextLine());

        Loan loan = loanService.create(bookId, readerId);
        System.out.println("Empréstimo criado! ID: " + loan.getId());
    }

    private void findById() {
        System.out.print("ID do empréstimo: ");
        Long id = Long.parseLong(scanner.nextLine());

        Loan loan = loanService.findById(id);
        print(loan);
    }

    private void findAll() {
        List<Loan> loans = loanService.findAll();

        if (loans.isEmpty()) {
            System.out.println("Nenhum empréstimo cadastrado.");
            return;
        }

        loans.forEach(this::print);
    }

    private void findByStatus() {
        System.out.print("Status (ACTIVE/RETURNED): ");
        String input = scanner.nextLine();

        LoanStatus status = LoanStatus.valueOf(input.toUpperCase());
        List<Loan> loans = loanService.findByStatus(status);

        if (loans.isEmpty()) {
            System.out.println("Nenhum empréstimo com esse status.");
            return;
        }

        loans.forEach(this::print);
    }

    private void returnLoan() {
        System.out.print("ID do empréstimo: ");
        Long id = Long.parseLong(scanner.nextLine());

        Loan loan = loanService.updateStatus(id, LoanStatus.RETURNED);
        System.out.println("Empréstimo devolvido! ID: " + loan.getId());
    }

    private void print(Loan loan) {
        System.out.println(loan.getId() + " | Livro: " + loan.getBookId()
                + " | Leitor: " + loan.getReaderId() + " | Status: " + loan.getStatus());
    }
}