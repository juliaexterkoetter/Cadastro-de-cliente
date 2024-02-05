API de Gerenciamento de Clientes

A API de Gerenciamento de Clientes é uma aplicação Spring Boot que permite o registro, atualização, pesquisa e exclusão de clientes em um banco de dados MySQL. Esta API é projetada para ser utilizada em aplicações de gerenciamento de clientes.

Tecnologias Utilizadas:
- Spring Boot
- Spring Data JPA
- Spring Web
- MySQL
- Swagger (para documentação da API)
- Java 17
- Maven

Configuração:
Certifique-se de ter o Java 17, o Maven e o MySQL instalados em sua máquina. O arquivo `application.properties` já contém as configurações para o MySQL. Você pode ajustar essas configurações conforme necessário.

1. Clone este repositório:
git clone URL_DO_REPOSITORIO
2. Navegue até o diretório do projeto:
cd cliente-api
3. Execute a aplicação Spring Boot:
mvn spring-boot:run
A aplicação estará acessível em http://localhost:8080.

Documentação da API:
A documentação da API está disponível usando o Swagger. Você pode acessá-la em http://localhost:8080/swagger-ui.html.

Endpoints:
- POST /customers: Registra um novo cliente.
- PUT /customers/{id}: Atualiza os dados de um cliente existente.
- GET /customers/{id}: Obtém informações de um cliente pelo ID.
- GET /customers: Obtém a lista de todos os clientes.
- DELETE /customers/{id}: Exclui um cliente pelo ID.

Estrutura do Projeto:
- com.example.cliente.controller: Contém os controladores REST da API.
- com.example.cliente.exception: Contém as classes de exceção personalizadas.
- com.example.cliente.model: Contém a definição da entidade Customer.
- com.example.cliente.repository: Contém o repositório JPA para a entidade Customer.
- com.example.cliente.service: Contém a lógica de negócios da aplicação.
- com.example.cliente.swagger: Contém a configuração do Swagger.
- com.example.cliente.ClientApplication: Classe principal de inicialização da aplicação.