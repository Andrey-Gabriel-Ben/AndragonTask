package com.dragonet;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Utils {
    private static final int LIMITE_TITULO = 50;
    private static final int LIMITE_DESCRICAO = 50;

    public static final String RESET = "\u001B[0m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARELO = "\u001B[33m";

    public static boolean isTituloValido(String titulo) {
        // Valida se não está vazio E se não passou do limite máximo
        boolean verify = titulo != null && !titulo.trim().isEmpty() && titulo.length() <= LIMITE_TITULO;

        if (!verify)  {
        System.err.println("O título inserido é inválido, verifique e tente novamente.\n");
        return false;
        }

        return true;
    }

    public static boolean isDescricaoValida(String descricao) {
        System.setOut(new java.io.PrintStream(System.out, true, java.nio.charset.StandardCharsets.UTF_8));
        // Valida se não está vazio E se não passou do limite máximo

        boolean verify = descricao != null && !descricao.trim().isEmpty() && descricao.length() <= LIMITE_DESCRICAO;

        if (!verify)  {
        System.err.println("A descrição inserida é inválida, verifique e tente novamente.\n");
        return false;
        }

        return true;

    }

    public static boolean isStringVazia(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("Erro: O campo não pode ser vazio!");
            return true;
        }
        return false;
    }

    public static boolean aStringEhVazia(String texto) {
        return texto == null || texto.trim().isEmpty();
    }

    public static boolean isSenhaValida(String senha) {
        if (isStringVazia(senha)){return false;}

        return senha.length() >= 8;
    }

    public static boolean isSenhaIgual(String senha, String confirmacao){
        return senha.equals(confirmacao);
    }

    public static int getLIMITE_TITULO() {
        return LIMITE_TITULO;
    }

    public static int getLIMITE_DESCRICAO() {
        return LIMITE_DESCRICAO;
    }



    public static String captarDiaEmString(){
        // 1. Pega a data atual do sistema
        LocalDate hoje = LocalDate.now();
        
        // 2. Cria o molde de formatação (Dia/Mês/Ano com 4 dígitos)
        DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        
        // 3. Transforma a data em texto usando o molde
        String dataFormatada = hoje.format(formatador);
        
        return dataFormatada;
    }    
}
