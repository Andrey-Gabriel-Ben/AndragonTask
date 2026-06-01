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
                System.out.println("É a sua primeira vez aqui, né? As suas tarefas ficarão salvas em: " + CAMINHO_PASTA
                        + ", separadas por usuários.");
            } else {
                System.out.println(
                        "Ops, houve uma falha na criação do arquivo que irá armazenar as tarefas, isso resultará em um erro de execução, por favor, consulte o suporte para resolver.");
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
                        System.out.println("Direcionando para a sua lista de tarefas...");
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

    // gets
    public static String getCaminho() {
        return CAMINHO_PASTA;
    }

    public static String getArquivoUsers() {
        return ARQUIVO_USUARIOS;
    }

    public static void main(String[] args) {
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



/*

pra dps

public void encerrarSessao(List<Tarefa> listaDeTarefas, String emailLogado) {
    System.out.println("\n==================================================");
    System.out.println("🎉 ANDRAGON TASK - RESUMO DA SESSÃO 🎉");
    System.out.println("==================================================");
    
    List<Tarefa> concluidas = new ArrayList<>();
    List<Tarefa> pendentes = new ArrayList<>();
    
    // 1. Separa as tarefas para saber o que apagar e o que manter
    for (Tarefa t : listaDeTarefas) {
        if (t.isConcluida()) {
            concluidas.add(t);
        } else {
            pendentes.add(t); // Essas vão sobreviver no arquivo
        }
    }
    
    // 2. Mostra o relatório motivacional se ele tiver concluído algo
    if (!concluidas.isEmpty()) {
        System.out.println("Essas foram as tarefas que você concluiu nessa sessão, muito bem:");
        for (Tarefa t : concluidas) {
            System.out.println("✔️ " + t.getDescricao());
        }
        System.out.println("\nAgora elas serão apagadas para dar espaço para novas realizações! 🚀");
    } else {
        System.out.println("Nenhuma tarefa foi concluída hoje, mas amanhã é um novo dia para progredir!");
    }
    
    // 3. Salva no arquivo JSON APENAS as tarefas que continuam pendentes
    // Assim, na próxima vez que ele logar, as concluídas sumiram do arquivo automaticamente!
    salvarTarefasNoArquivo(pendentes, emailLogado);
    
    System.out.println("==================================================");
}

*/


}



