# Teste Técnico Compasso Uol - Bruno Felix
Neste projeto será desenvolvido um pequeno sistema relacionado a clientes e suas cidades, onde será possível cadastrar, alterar, excluir e listar. Linguagem de programação Java 8 com Spring Boot e banco de dados MySQL.

## Pré-requisitos

Para buildar e executar a aplicação (API e Front-end) será necessário:
* 	[Maven](https://maven.apache.org/) - Dependency Management.
* 	[JDK](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html) - Java™ Platform, Standard Edition Development Kit.
* 	[Spring Boot](https://spring.io/projects/spring-boot) - Framework to ease the bootstrapping and development of new Spring Applications.
*   [Spring Tools 4 for Eclipse](https://spring.io/tools) - Spring Tool Suite 4 makes it easy to get started. A direct and easy-to-use integration of the Spring Initializr and the famous Spring Guides allows you to go from nothing to a running Spring Boot app in seconds.
* 	[MySQL](https://www.mysql.com/) - Open-Source Relational Database Management System.
* 	[git](https://git-scm.com/) - Free and Open-Source distributed version control system.
* 	[Swagger](https://swagger.io/) - Open-Source software framework backed by a large ecosystem of tools that helps developers design, build, document, and consume RESTful Web services.

## Documentação
* 	[Swagger](http://localhost:8088/swagger-ui.html) - Documentação dos serviços disponibilizado pela API.

## Arquivos e diretórios

O projeto foi criado e desenvolvido seguindo a estrutura descrita abaixo:

* 	[API]
```
.
├── Spring Elements
├── src
│   └── main
│       └── java
│           ├── br.com.compasso.uol.cliente.controller
│           ├── br.com.compasso.uol.cliente.model
│           ├── br.com.compasso.uol.cliente.repository
│           ├── br.com.compasso.uol.cliente.service
├── src
│   └── main
│       └── resources
│           ├── application.properties
├── src
│   └── test
│       └── java
|           ├── br.com.compasso.uol.cliente.controller
│           ├── br.com.compasso.uol.cliente.repository
│           ├── br.com.compasso.uol.cliente.service
├── JRE System Library
├── Maven Dependencies
├── src
├── target
├── mvnw
├── mvnw.cmd
├── pom.xml

```

## Pacotes

* 	[API]
- `controller` - Pasta responsável pelo mapeamento e direcionamento das ações recebidas (request) pela camada da apresentação para os respectivos serviços da aplicação.
- `model` - Pasta responsável pelo armazenamento de classes básicas e Enums;
- `repository` - Pasta responsável pelo armazenamento de arquivos que realizam consultas na base de dados;
- `service` - Pasta responsável pelo armazenamento dos arquivos que detalham os serviços da aplicação consumindo os arquivos as pasta repository;
- `resources/application.properties` - Arquivo que contem as propriedades do sistema, como os dados de conexão do banco de dados.
- `test/` - Pacote onde se encontram os testes unitários.
- `pom.xml` - Arquivo onde se encontram todas as dependências do projeto.


## Liçensa
* 	Apache License 2.0. Visualizar arquivo de [LICENSE](https://github.com/BrunoFelix/TesteTecnicoCompassoUol/blob/master/LICENSE).