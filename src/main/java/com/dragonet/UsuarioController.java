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

public class UsuarioController {

    // Instancia o Gson para ser usado nos métodos abaixo
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // cadastro de usuarios:
    public boolean CadastrarUsuario(Scanner scanner) {
        System.out.println("Bora cadastrar um usuário novo! \n");

        System.out.println("Digite seu nome:");
        String nomeimput = scanner.nextLine();
        if (Utils.isStringVazia(nomeimput)) {
            return false;
        }

        System.out.println("\nDigite seu email:");
        String emailimput = scanner.nextLine();
        if (Utils.isStringVazia(emailimput)) {
            return false;
        }

        System.out.println("\nDigite uma senha com mais de 8 digitos:");
        String senhaimput = scanner.nextLine();
        if (!Utils.isSenhaValida(senhaimput)) {
            return false;
        }

        System.out.println("\nConfirme sua senha:");
        String confirmacaoimput = scanner.nextLine();
        if (!Utils.isSenhaIgual(senhaimput, confirmacaoimput)) {
            return false;
        }

        Usuario cadastrado = new Usuario(nomeimput, emailimput, senhaimput);

        // Salva na lista de usuários cadastrados
        if (!salvarNovoUsuarioNaLista(cadastrado)) {
            return false;
        }

        // Se salvou na lista com sucesso, cria o arquivo individual de tarefas dele
        if (!CriarArquivo(cadastrado)) {
            return false;
        }

        // fim
        System.out.println("Usuario criado com sucesso\n");
        return true;
    }

    // Cria o arquivo do usuario para armazenar as tarefas
    private static boolean CriarArquivo(Usuario usuario) {
        // Usando o email para dar nome ao arquivo de tarefas
        String caminho = ATM.getCaminho() + File.separator + usuario.getEmail() + ".json";

        File arquivo = new File(caminho);

        if (arquivo.exists()) {
            System.out.println("Ops! Já existe um usuário com esse email cadastrado.\n");
            return false;
        }

        try {
            boolean criadoComSucesso = arquivo.createNewFile();

            if (criadoComSucesso) {
                System.out.println("Arquivo de tarefas criado para o usuário: " + usuario.getNome() + "\n");
                return true;
            } else {
                return false;
            }

        } catch (IOException e) {
            System.out.println("Erro técnico ao tentar criar o arquivo do usuário: " + e.getMessage());
            return false;
        }
    }

    // Método para amarrar o cadastro à sua lista JSON central
    private boolean salvarNovoUsuarioNaLista(Usuario novoUsuario) {
        List<Usuario> usuariosAtuais = lerTodosUsuarios();

        for (Usuario u : usuariosAtuais) {
            if (u.getEmail().equalsIgnoreCase(novoUsuario.getEmail())) {
                System.out.println("Erro: Este e-mail já está cadastrado!");
                return false;
            }
        }

        usuariosAtuais.add(novoUsuario);
        salvarLista(usuariosAtuais);
        return true;
    }

    // fazer login do usuario
    public String realizarLoggin(Scanner scanner) {
        System.out.println("Bora fazer login\n");

        // Inserção de dados
        System.out.println("Digite seu email: ");
        String emailimput = scanner.nextLine();
        if (Utils.isStringVazia(emailimput)) {
            return null;
        }

        System.out.println("\nDigite sua senha: ");
        String senhaimput = scanner.nextLine();
        if (Utils.isStringVazia(senhaimput)) {
            return null;
        }

        // realiza a verificação:
        if (!verificarLogin(emailimput, senhaimput)) {
            return null;
        }

        return emailimput;

    }

    private boolean verificarLogin(String email, String senha) {
        // 1. Carrega todos os usuários cadastrados
        List<Usuario> usuariosAtuais = lerTodosUsuarios();

        // 2. Percorre a lista procurando a combinação de email e senha
        for (Usuario u : usuariosAtuais) {
            if (u.getEmail().equalsIgnoreCase(email) && u.getSenha().equals(senha)) {
                System.out.println("Login realizado com sucesso! Bem-vindo, " + u.getNome() + ".\n");
                return true;
            }
        }

        System.out.println("Usuário ou senha incorretos.\n");
        return false;
    }

    // Método auxiliar para ler TODOS os usuários do arquivo de uma vez só
    public List<Usuario> lerTodosUsuarios() {
        File arquivo = new File(ATM.getArquivoUsers());

        if (!arquivo.exists() || arquivo.length() == 0) {
            return new ArrayList<>();
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            Type tipoLista = new TypeToken<ArrayList<Usuario>>() {
            }.getType();

            List<Usuario> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo de usuários: " + e.getMessage());
            System.out.println("\n \nRetornando uma lista vazia");
            return new ArrayList<>();
        }
    }

    // Método auxiliar para salvar a LISTA COMPLETA de usuários no arquivo
    private void salvarLista(List<Usuario> lista) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(ATM.getArquivoUsers()))) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            System.out.println("Erro técnico ao salvar dados: " + e.getMessage());
        }
    }
    
}