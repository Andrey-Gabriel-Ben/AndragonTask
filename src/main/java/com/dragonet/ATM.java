package com.dragonet;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class ATM {
    // 1. Define o caminho para a pasta na raiz do usuário (ex
    // C:\Users\SeuNome\.AndragonTask)
    private static final String CAMINHO_PASTA = System.getProperty("user.home") + File.separator + ".AndragonTask";
    private static final String ARQUIVO_USUARIOS = CAMINHO_PASTA + File.separator + "users.json";

    private static void criaArquivosSeNecessario() {

        // 1. Cria o objeto File apontando para esse caminho
        File pastaDocs = new File(CAMINHO_PASTA);

        // 2. Verifica se a pasta já existe; se não existir, cria ela automaticamente
        if (!pastaDocs.exists()) {
            boolean criou = pastaDocs.mkdirs(); // Executa a criação da pasta

            if (criou) {
                System.out.println("É a sua primeira vez aqui, né? As suas metas ficarão salvas em: " + CAMINHO_PASTA
                        + ", separadas por usuários.");
            } else {
                System.out.println(
                        "Ops, houve uma falha na criação do arquivo que irá armazenar as metas, isso resultará em um erro de execução, por favor, consulte o suporte para resolver.");
            }
        }

        File arquivoUsuarios = new File(ARQUIVO_USUARIOS);
        if (!arquivoUsuarios.exists()) {
            try {
                // Cria o arquivo usuários.json em branco
                arquivoUsuarios.createNewFile();
            } catch (IOException e) {
                System.out.println("Ops, houve uma falha crítica ao criar o arquivo de usuários: " + e.getMessage());
            }
        }
    }

    private static String telaLogin(Scanner scanner) {
        int op = -1;
        UsuarioController uc = new UsuarioController();

        System.out.println("Seja bem-vindo ao AndragonTask. \n \n");

        while (op != 0) {

            System.out.println("Digite o número referente à opção desejada: \n");
            System.out.println("1 - Realizar login");
            System.out.println("2 - Cadastrar um novo usuário.");
            System.out.println("0 - Fechar sistema");

            String entradaOpcao = scanner.nextLine().trim();

            try {
                op = Integer.parseInt(entradaOpcao);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite apenas números.");
                op = -1;
                continue;
            }

            switch (op) {
                case 1 -> {
                    boolean sucesso = false;
                    String emailLogado = null;

                    while (!sucesso) {

                        emailLogado = uc.realizarLoggin(scanner);

                        if (emailLogado != null) {
                            sucesso = true;
                        } else {
                            System.out.println("Você Gostaria de tentar novamente? (s/n)");
                            String resposta = scanner.nextLine();

                            if (!resposta.equalsIgnoreCase("s")) {
                                System.out.println("Retornando ao menu principal...");
                                sucesso = true;
                            }
                        }
                    }

                    if (emailLogado != null) {
                        System.out.println("Direcionando para a sua lista de metas...");
                        return emailLogado;
                    }
                }

                case 2 -> {
                    boolean sucesso = false;

                    while (!sucesso) {

                        sucesso = uc.CadastrarUsuario(scanner);

                        if (!sucesso) {
                            System.out.println("Você Gostaria de tentar novamente? (s/n)");
                            String resposta = scanner.nextLine();

                            if (!resposta.equalsIgnoreCase("s")) {
                                System.out.println("Retornando ao menu principal...");
                                sucesso = true;
                            }
                        }
                    }

                }

                case 0 -> {
                    System.out.println("Até a próxima!");
                }
            }

        }
        return null;
    }


    private static void telaMetas(Scanner scanner, String emailLogado) {
        int op = -1;
        MetasControler mc = new MetasControler();
        UsuarioController uc = new UsuarioController();

        // 1. Busca o nome do usuário para a mensagem de boas-vindas personalizada
        String nomeUsuario = "Usuário";
        // Vamos ler a lista central de usuários para achar o nome dono deste email
        java.util.List<Usuario> usuarios = uc.lerTodosUsuarios();
        for (Usuario u : usuarios) {
            if (u.getEmail().equalsIgnoreCase(emailLogado)) {
                nomeUsuario = u.getNome();
                break;
            }
        }

        System.out.println("\n=======================================");
        System.out.println("Olá, " + nomeUsuario + "! Bem-vindo(a) ao seu painel.");
        System.out.println("=======================================");

        while (op != 0) {
            System.out.println("Digite o número referente à opção desejada:\n");
            System.out.println("1 - Adicionar nova meta");
            System.out.println("2 - Listar metas");
            System.out.println("3 - Completar meta");
            System.out.println("0 - Sair e limpar metas concluídas");

            String entradaOpcao = scanner.nextLine().trim();

            try {
                op = Integer.parseInt(entradaOpcao);
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Digite apenas números.");
                op = -1;
                continue;
            }

            switch (op) {
                case 1 -> {
                    // Chama o método de cadastrar que criamos
                    mc.cadastrarMeta(scanner, emailLogado);
                }
                case 2 -> {
                    // Lista as metas coloridas (Amarelo/Verde)
                    mc.listarMetas(emailLogado);
                }
                case 3 -> {
                    // Marca a meta como concluída com base no índice digitado
                    mc.concluirMeta(scanner, emailLogado);
                }
                case 0 -> {
                    // 2. O "Gran Finale": Antes de fechar, roda o seu relatório motivacional
                    // Busca a lista atual do arquivo antes de passar para a limpeza de despedida
                    java.util.List<Meta> metasAtuais = mc.lerTodasMetas(emailLogado);
                    
                    System.out.println("\n==================================================");
                    System.out.println("🎉 ANDRAGON TASK - RESUMO DA SESSÃO 🎉");
                    System.out.println("==================================================");
                    
                    java.util.List<Meta> concluidas = new java.util.ArrayList<>();
                    java.util.List<Meta> pendentes = new java.util.ArrayList<>();
                    
                    // Separa o que foi cumprido do que continua pendente
                    for (Meta m : metasAtuais) {
                        if (m.isConcluida()) {
                            concluidas.add(m);
                        } else {
                            pendentes.add(m);
                        }
                    }
                    
                    // Exibe o relatório que você planejou
                    if (!concluidas.isEmpty()) {
                        System.out.println("Essas foram as metas que você concluiu nessa sessão, muito bem:");
                        for (Meta m : concluidas) {
                            System.out.println("✔️ " + m.getDescricao());
                        }
                        System.out.println("\nAgora elas serão apagadas para dar espaço para novas realizações! 🚀");
                    } else {
                        System.out.println("Nenhuma meta foi concluída hoje, mas amanhã é um novo dia para progredir!");
                    }
                    
                    // Grava no arquivo JSON apenas as metas que continuam pendentes
                    mc.salvarMetas(pendentes, emailLogado);
                    System.out.println("==================================================");
                    System.out.println("Sessão encerrada com sucesso. Retornando ao menu de login...\n");
                }
                default -> System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    // gets
    public static String getCaminho() {
        return CAMINHO_PASTA;
    }

    public static String getArquivoUsers() {
        return ARQUIVO_USUARIOS;
    }

    public static void main(String[] args) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        
        boolean executa = true;
        String atual = null;

        criaArquivosSeNecessario();

        try (Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8)) {

            do {
                atual = telaLogin(scanner);
                if (Utils.aStringEhVazia(atual)) {return;}

                //tela do usuario0

            } while (executa == true);

        }
    }


}



