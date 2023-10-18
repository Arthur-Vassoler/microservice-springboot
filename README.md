# Projeto de Microservices para Cadastro de Usuário e Envio de E-mail

## Projeto em Constrição 🛠️

Este é um projeto de microservices que consiste em dois serviços principais: Cadastro de Usuário e Envio de E-mail para Novos Cadastros.

## Visão Geral

Este projeto visa criar uma arquitetura de microservices para facilitar o registro de usuários e o envio de e-mails de boas-vindas para novos registros. A separação em serviços distintos permite escalabilidade, manutenção e implantação independentes.

## Componentes

### 1. Serviço de Cadastro de Usuário

- O serviço de cadastro de usuário fornece endpoints para criar e gerenciar contas de usuário.
- Ele inclui validação de entrada, armazenamento de dados do usuário e segurança.
- Tecnologias utilizadas: Java 17 - Spring Boot 3.1.4 - PostgreSQL

### 2. Serviço de Envio de E-mail

- O serviço de envio de e-mail é responsável por enviar mensagens de boas-vindas aos novos usuários cadastrados.
- Ele é acionado pelo serviço de cadastro de usuário quando um novo usuário é registrado.
- Tecnologias utilizadas: Java 17 - Spring Boot 3.1.4 - PostgreSQL

## Contribuição

- Se você deseja contribuir para este projeto, siga as diretrizes de contribuição no arquivo CONTRIBUTING.md.

## Contato

- Autor: Arthur Vassoler
- Email: a.h.vassoler@gmail.com