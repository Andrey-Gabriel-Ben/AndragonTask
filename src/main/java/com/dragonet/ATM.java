package com.dragonet;

import java.io.File;
import java.io.IOException;

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

    private void telaLogin() {
        System.out.println("Seja bem-vindo ao AndragonTask.");

    }

    // gets
    public static String getCaminho() {
        return CAMINHO_PASTA;
    }

    public static String getArquivoUsers() {
        return ARQUIVO_USUARIOS;
    }

    public static void main(String[] args) {

        criaArquivosSeNecessario();
        
        boolean executa = true;
         
        do {
        telaLogin()
         
        exibeAsOpçoes(atual);
         
        executa = acessarOutroPerfil();
        } while (executa == true);
         
        
    }

}
