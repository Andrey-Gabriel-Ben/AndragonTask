@echo off
chcp 65001 > nul
title Andragon Task - Sistema de Gerenciamento de Metas

:: 1. Cria uma pasta interna no projeto para guardar as dependências se ela não existir
if not exist "lib" mkdir lib

:: 2. Procura e copia o JAR do Gson do seu repositório local do usuário para dentro do projeto
if not exist "lib\gson-2.11.0.jar" (
    echo Localizando a biblioteca Gson no sistema...
    copy "%USERPROFILE%\.m2\repository\com\google\code\gson\gson\2.11.0\gson-2.11.0.jar" "lib\gson-2.11.0.jar" > nul
)

:: 3. Executa o programa usando as classes compiladas e o JAR do Gson local da pasta lib
echo Iniciando o Andragon Task...
java -cp "target/classes;lib/gson-2.11.0.jar" com.dragonet.ATM

pause