package com.dragonet;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class MetasControler {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public boolean adicionarNovaMeta(Scanner scanner, String emailLogado) {
        System.out.println("E lá vamos nós adicionar mais uma meta para a lista.\n");

        System.out
                .println("Para começar, adicione um título curto de até " + Utils.getLIMITE_TITULO() + " caracteres.");
        String tituloImput = scanner.nextLine();
        if (!Utils.isTituloValido(tituloImput)) {
            return false;
        }

        System.out.println("\nAgora descreva sua meta em até " + Utils.getLIMITE_DESCRICAO() + " caracteres.");
        String descricaoImput = scanner.nextLine();
        if (!Utils.isDescricaoValida(descricaoImput)) {
            return false;
        }

        String dataAtual = Utils.captarDiaEmString();

        Metas nova = new Metas(tituloImput, descricaoImput, dataAtual, false);

        List<Metas> metasAtuais = lerTodasMetas(emailLogado);

        metasAtuais.add(nova);

        salvarMetas(metasAtuais, emailLogado);

        System.out.println("Meta \"" + nova.getTitulo() + "\" registrada com sucesso! 🚀");
        return true;
    }

    // Método auxiliar para ler o arquivo JSON específico do usuário logado
    public List<Metas> lerTodasMetas(String emailLogado) {
        String caminhoArquivo = ATM.getCaminho() + File.separator + emailLogado + ".json";
        File arquivo = new File(caminhoArquivo);

        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            Type tipoLista = new TypeToken<ArrayList<Metas>>() {
            }.getType();
            List<Metas> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            System.out.println("Erro ao ler as metas do usuário: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    // Método auxiliar para salvar a lista completa de metas no arquivo do usuário
    public void salvarMetas(List<Metas> lista, String emailLogado) {
        String caminhoArquivo = ATM.getCaminho() + File.separator + emailLogado + ".json";

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(caminhoArquivo))) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.out.println("Erro técnico ao salvar as metas: " + e.getMessage());
        }
    }

    public void listarMetas(String emailLogado) {
        // 1. Busca todas as metas do arquivo JSON do usuário logado
        List<Metas> lista = lerTodasMetas(emailLogado);

        if (lista.isEmpty()) {
            System.out.println("Você ainda não registrou nenhuma meta.");
        } else {
            System.out.println("\n========= SUAS METAS =========");

            for (int i = 0; i < lista.size(); i++) {
                Metas m = lista.get(i);

                // 2. Define a cor e o ícone com base no status 'concluida'
                boolean status = m.getStatus();
                String cor = status ? Utils.VERDE : Utils.AMARELO;
                String statusIcone = status ? "✅" : "🔲";
                String statusTexto = status ? "Concluída" : "Pendente ";

                // 3. Exibe a linha formatada
                // Formato: ID - [V] Descrição (Data) - Status
                System.out.println(cor + i + " - " + statusIcone + " " + m.getTitulo() + " (" + m.getDataAdicao()
                        + ") - " + statusTexto);
                System.out.println("    " + m.getDescricao() + Utils.RESET + "\n");
            }
        }
        System.out.println("==============================\n");
    }

    // Método para marcar uma meta como concluída
    public boolean concluirMeta(Scanner scanner, String emailLogado) {
        // 1. Carrega as metas direto do arquivo do usuário
        List<Metas> lista = lerTodasMetas(emailLogado);

        if (lista.isEmpty()) {
            System.out.println("Você não possui metas registradas para concluir.");
            return false;
        }

        // 2. Mostra as metas atuais para o usuário saber qual número escolher
        listarMetas(emailLogado);

        System.out.println("Digite o número (ID) da meta que você cumpriu:");
        String entrada = scanner.nextLine().trim();

        try {
            int indice = Integer.parseInt(entrada);

            // 3. Valida se o número digitado existe na lista
            if (indice < 0 || indice >= lista.size()) {
                System.err.println("Erro: Esse ID de meta não existe, tente novamente");
                return false;
            }

            Metas metaEscolhida = lista.get(indice);

            // 4. Verifica se ela já estava concluída para não fazer trabalho duplo
            if (metaEscolhida.getStatus()) {
                System.err.println("Essa meta já foi concluída anteriormente!");
                return false;
            }

            // 5. Altera o status para true (concluída)
            metaEscolhida.setStatus(true);

            // 6. Salva a lista atualizada de volta no arquivo JSON
            salvarMetas(lista, emailLogado);

            System.out.println(Utils.VERDE + "Parabéns! Meta \"" + metaEscolhida.getTitulo()
                    + "\" marcada como cumprida! 🎉" + Utils.RESET);
            return true;

        } catch (NumberFormatException e) {
            System.out.println("Erro: Digite apenas o número de identificação da meta.");
            return false;
        }
    }

    public void encerrarSessao(List<Metas> listaDeMetas, String emailLogado) {
        System.out.println("\n==================================================");
        System.out.println("==== 🎉 ANDRAGON TASK - RESUMO DA SESSÃO 🎉  ====");
        System.out.println("==================================================");

        List<Metas> concluidas = new ArrayList<>();
        List<Metas> pendentes = new ArrayList<>();

        // 1. Separa as metas para saber o que apagar e o que manter
        for (Metas t : listaDeMetas) {
            if (t.getStatus()) {
                concluidas.add(t);
            } else {
                pendentes.add(t); 
            }
        }

        // 2. Mostra o relatório motivacional se ele tiver concluído algo
        if (!concluidas.isEmpty()) {
            System.out.println("Essas foram as metas que você concluiu nessa sessão, muito bem:");
            for (Metas t : concluidas) {
                System.out.println("✔️ " + t.getDescricao());
            }
            System.out.println("\nAgora elas serão apagadas para dar espaço para novas realizações! 🚀");
        } else {
            System.out.println("Nenhuma meta foi concluída hoje, mas amanhã é um novo dia para progredir!");
        }

        // 3. Salva no arquivo JSON APENAS as metas que continuam pendentes. Assim, na próxima vez que ele logar, as concluídas sumiram do arquivo automaticamente!
        salvarMetas(pendentes, emailLogado);

        System.out.println("==================================================");
    }

}