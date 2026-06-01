package com.dragonet;

public class Utils {
    public static boolean isStringVazia(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            System.err.println("Erro: O campo não pode ser vazio!");
            return true;
        }
        return false;
    }

    public static boolean isSenhaValida(String senha) {
        if (isStringVazia(senha)){return false;}

        return senha.length() >= 8;
    }

    public static boolean isSenhaIgual(String senha, String confirmacao){
        return senha.equals(confirmacao);
    }
}
