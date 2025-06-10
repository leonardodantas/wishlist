# Wishlist API

Microserviço responsavel por gerenciar uma Wishlist(Lista de desejos do cliente). Implementa padrões modernos de arquitetura, código limpo e testes automatizados.

### Desafio

O objetivo é o desenvolvimento de serviço HTTP resolvendo a funcionalidade de Wishlist do cliente. Esse serviço deve atender os seguintes requisitos:
- Adicionar um produto na Wishlist do cliente;
- Remover um produto da Wishlist do cliente;
- Consultar todos os produtos da Wishlist do cliente;
- Consultar se um determinado produto está na Wishlist docliente;

### Detalhes Tecnicos

- Java 21 com Spring Boot
- MongoDB como banco de dados
- Arquitetura em camadas com Clean Architecture
- Regras de negócio encapsuladas em domínio rico
- Auditoria automática (data de criação/atualização)
- Esteira CI com GitHub Actions
- Testes unitários, de integração e cobertura com Jacoco


# Documentação

Para facilitar a execução do projeto, foi adicionado um arquivo docker-compose.yml, que cria dois contêineres:

MongoDB: banco de dados utilizado pela aplicação

Mongo Express: interface web para visualização dos dados

Após iniciar os contêineres, a interface do Mongo Express pode ser acessada em http://localhost:8081, utilizando:

**Usuário: admin**

**Senha: pass**

###  Iniciando o projeto

```bash
# Clone este repositório
git clone https://github.com/leonardodantas/wishlist.git

# Tenha o docker compose instalando, acesse a pasta raiz do projeto e execute o seguinte comando
docker-compose up --build

# O comando acima ira criar as seguintes instâncias
- Mongo
- Mongo Express
 
# Inicie a aplicação com uma IDE

#Acesse o seguinte endereço no navegador
http://localhost:8080/swagger-ui/index.html
```
---

### Endpoint: Adicionar produto na wishlist

#### Método
`POST`

#### URL
`/api/v1/wishlist/{customerId}/products`

#### Descrição

Adicionar um produto a wishlist do cliente.

- O campo `customerId` é fornecido pelo cliente e será utilizado para associar o produto ao cliente.

#### Request Body

```json
{
  "id": "a1b2c3d4-e5f6-7890-abcd-ef1234567890"
}
```

#### Campos

| Campo             | Tipo     | Obrigatório | Validação                      | Descrição                                                                 |
|-------------------|----------|-------------|--------------------------------|---------------------------------------------------------------------------|
| `id`            | `string` | Sim         | `@NotBlank`                    | Id do produto.                                                 |

#### Regras de Negócio

Antes de salvar, o sistema realiza as seguintes validações:
- Se o produto ja existe na wishlist do cliente.
- Se a lista de produtos da wishlist do cliente é menor que 20.
- Caso o cliente não possua nenhuma wishlist, então uma nova é criada.


#### Resposta

- **Status:** `201 Created`
- **Body:** Nenhum conteúdo retornado.


#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                           | Causa                                                                 |
|-------------|-------------------------------------------------------------|-----------------------------------------------------------------------|
| `400`       | `"Erro de validação nos campos"`                           | Algum campo obrigatório está ausente ou inválido.                     |
| `409`       | `"Produto com ID {productId} já está na lista de desejos."`         | Já existe um produto na wishlist do cliente com o mesmo id. |
| `409`       | `"Wishlist atingida para o cliente com ID {customerId}"` | Limite de produtos excedidos na wishlist.          |

#### Exemplo de curl

```
curl -X 'POST' \
  'http://localhost:8080/api/v1/wishlist/1s584dd1d1/products' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json' \
  -d '{
  "id": "1"
}'
```


#### Observações

- Embora esteja sendo feita a atualização da wishlist e pareça correto utilizar um http metodo PUT ou PATCH, entendo que sempre estamos criando um novo recurso, e por isso utilizei o POST.

---

### Endpoint: Remover produto na wishlist

#### Método
`DELETE`

#### URL
`/api/v1/wishlist/{customerId}/products/{productId}`

#### Descrição

Remover um produto a wishlist do cliente.

- O campo `customerId` é fornecido pelo cliente e será utilizado para recuperar a wishlist do cliente.
- O campo `productId` é fornecido pelo cliente e será utilizado para remover o produto da wishlist.

#### Regras de Negócio

Antes de remover o produto, o sistema realiza as seguintes validações:
- Se o cliente não possuir wishlist a exceção WishlistNotFoundException é lançada.
- Se o produto não existe na wishlist a exceção ProductNotInWishlistException é lançada.
- Caso a lista de produtos do cliente fique vazia após a remoção, então a wishlist é removida.
- Caso a lista de produtos do cliente não fique vazia após a remoção, apenas o produto é removido.

#### Resposta

- **Status:** `204 No Content`
- **Body:** Nenhum conteúdo retornado.

#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                           | Causa                                                                 |
|-------------|-------------------------------------------------------------|-----------------------------------------------------------------------|
| `400`       | `"Erro de validação nos campos"`                           | Algum campo obrigatório está ausente ou inválido.                     |
| `404`       | `"Lista de desejos não encontrada para o cliente com ID {customerId}"`         | Cliente não possui wishlist. |
| `404`       | `"Produto com ID {productId} não está na lista de desejos"` | Produto não existe na na wishlist.          |


#### Exemplo de curl

```
curl -X 'DELETE' \
  'http://localhost:8080/api/v1/wishlist/1s584dd1d1/products/{productId}' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'\'
```

#### Observações

- Para operações de remoção como delete, normalmente é utilizado o status code 204.

---

### Endpoint: Verificar se produto existe na wishlist

#### Método
`GET`

#### URL
`/api/v1/wishlist/{customerId}/products/{productId}`

#### Descrição

Retornar se um produto existe ou não.

- O campo `customerId` é fornecido pelo cliente e será utilizado para recuperar a wishlist do cliente.
- O campo `productId` é fornecido pelo cliente e será utilizado para buscar o produto da wishlist.

#### Regras de Negócio

Antes de retornar se o produto existe, o sistema realiza as seguintes validações:
- Se o cliente não possuir wishlist a exceção WishlistNotFoundException é lançada.
- Se o produto não existe na wishlist retorna false, senão true.

#### Resposta

- **Status:** `200 Ok`
- **Body:**
```json
{"productExist": true}
```

#### Erros Possíveis

| Código HTTP | Mensagem de Erro                                           | Causa                                                                 |
|-------------|-------------------------------------------------------------|-----------------------------------------------------------------------|
| `400`       | `"Erro de validação nos campos"`                           | Algum campo obrigatório está ausente ou inválido.                     |
| `404`       | `"Lista de desejos não encontrada para o cliente com ID {customerId}"`         | Cliente não possui wishlist. |


#### Exemplo de curl
```
curl -X 'GET' \
  'http://localhost:8080/api/v1/wishlist/1s584dd1d1/products/{productId}' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'\'
```

#### Observações

- Nesse endpoint preferi devolver um objeto com o valor booleano para informar se o produto existe ou não. Uma abordagem normal e utilizada no mercado é retornar um 204 para quando o produto não existe e um 200 para quando existe.

---

### Endpoint: Recuperar wishlist do cliente

#### Método
`GET`

#### URL
`/api/v1/wishlist/{customerId}/products`


#### Descrição

Recupear a wishlist de um cliente com todos os produtos.

- O campo `customerId` é fornecido pelo cliente e será utilizado para recuperar a wishlist do cliente.

#### Resposta

- **Status:** `200 Ok`
- **Body:**
```json
{
  "id": "",
  "customerId": "",
  "products": [],
  "totalProducts": ""
}
```

#### Exemplo de curl

```
curl -X 'GET' \
  'http://localhost:8080/api/v1/wishlist/1s584dd1d1/products/' \
  -H 'accept: */*' \
  -H 'Content-Type: application/json'\'
```


#### Observações

- Aqui sempre vamos devolver um 200 para o cliente, seja com uma lista vazia de produtos(caso ela nao possuir nenhum produto) ou com a lista populada.
- Devido ao limite de 20 items eu não utilizei paginação.



# Boas praticas de programação.

### Arquitetura e Camadas

Adotei alguns principios de arquitetura limpa para a solução do desafio. As camadas foram organizadas da seguinte forma:
- **API**: Responsavel pelos endpoints da aplicação e seus jsons, tanto de entrada quanto de saida. Para esse dominio utilizei Records no lugar de classes, visto que Records são recomendados para estruturas imutaveis e orientadas a dados.
- **APP**: Camada responsavel por conter os casos de usos da aplicação e interfaces de conexão de banco de dados. Aqui os casos de usos possuem as regras de negocios da aplicação, ou seja, buscam alguma informação em um banco de dados, fazem alguma validação mais simples e controlam o fluxo. Tambem respeitando principipos de SOLID e OOP fiz a utilização de interfaces promovendo dessa forma o baixo acomplamento entre casos de usos e banco de dados.
- **DOMAIN**: Os dominios aqui possuem as regras cruciais de negocio da aplicação, representando o comportamento da vida real. Optei por dominios inteligentes no lugar de dominios anemicos.
- **INFRA**: Se na camada de APP temos as abstrações dos repositories, aqui temos as classes concretas. Na camada de infra realizamos a implementação de classes que acessam nosso banco de dados, seja com MongoRepository ou MongoTemplate.
- **CONFIG**: Camada existente para armazenar todas as classes de configurações, como swagger, auditoria e afins.

A seguir uma imagem para representar a arquitetura adotada: <br>
![Untitled Diagram-Page-2](https://github.com/user-attachments/assets/ad444a7c-239d-4ea7-a4c7-c2a9fbbf6383)


Apesar da adoção da Clean Architecture, uma estrutura mais simples também seria válida, especialmente em projetos onde a rapidez na entrega e a facilidade de manutenção são mais relevantes que a arquitetura.

### Orientação a Objetos e Programação Funcional
Durante o desenvolvimento, utilizei conceitos de ambos os paradigmas (orientação a objetos e programação funcional) de forma complementar.
Alguns pilares da OOP estão presentes no código, como o Polimorfismo por sobrecarga de métodos, como no caso abaixo:
```java
    public boolean containsProduct(final Product product) {
        return products.stream()
                .anyMatch(p -> p.getId().equals(product.getId()));
    }

    public boolean containsProduct(final String productId) {
        return products.stream()
                .anyMatch(p -> productId.equals(p.getId()));
    }

```
Encapsulamento, ao proteger o estado interno das entidades e expor apenas métodos que representam comportamentos do domínio, e abstração, com o uso de interfaces como IWishlistRepository para desacoplar regras de negócio dos detalhes de persistência.

Já no paradigma funcional, adotei o uso de objetos imutáveis sempre que possível, priorizando a criação de novas instâncias em vez da modificação do estado de objetos existentes.
Além disso, utilizei recursos como streams e expressões lambda, que tornam o código mais declarativo e expressivo.

O exemplo abaixo demonstra a aplicação desse conceito, onde um novo objeto Wishlist é criado com os dados atualizados, mantendo a imutabilidade da instância original:

```java
public Wishlist addProduct(final Product product) {

        if (this.containsProduct(product)) {
            throw new ProductAlreadyInWishlistException(product.getId());
        }

        if (Boolean.FALSE.equals(this.canAddMoreProducts())) {
            throw new WishlistLimitReachedException(this.customerId);
        }

        final List<Product> productUpdate = new ArrayList<>(this.products);
        productUpdate.add(product);

        return new Wishlist(
                this.id,
                this.customerId,
                productUpdate,
                this.createdAt,
                this.updatedAt
        );
    }
```

### Dominios Anemicos e Dominios Inteligentes
Tambem optei pela utilização de dominios inteligentes, diferente de dominios anemicos que existem apenas para transportar dados, os inteligentes conseguem transportar dados e encapsular comportamentos. Quando utilizamos modelos inteligentes estamos fazendo uso de principios do DDD. Em um projeto com modelos anemicos costumamos concentrar todas a logica em nosso use cases ou services, ja em dominios inteligentes usamos a camada de use case ou service apenas para orquestrar as chamadas. Ambas abordagens podem ser utilizadas, não sendo considerada uma certa ou a outra errada. A seguir um pequeno exemplo de dominio inteligente:

```java
public Wishlist removeProduct(final String productId) {
        final boolean productRemoved = this.getProducts().removeIf(product -> product.getId().equals(productId));

        if (productRemoved) {
            return new Wishlist(
                    this.id,
                    this.customerId,
                    products,
                    this.createdAt,
                    this.updatedAt
            );
        }

        throw new ProductNotInWishlistException(productId);
    }
``` 

### Padrões de Projetos e SOLID
Alguns padrões de projetos tambem foram utilizados no desenvolvimento da solução, como por exemplo o factory method:

```java
    public static Wishlist of(final String customerId, final Product product) {
        return new Wishlist(null, customerId, List.of(product), null, null);
    }
```
E buildes com a anotação @Builder so lombok. Um outro padrão utilizado junto com spring é o Strategy, pois definimos a interface e o container do spring injeta via injeção de dependencia a classe concreta.

Durante o desenvolvimento, também busquei seguir os princípios do SOLID para manter o código limpo e desacoplado.

Um exemplo claro é a aplicação do SRP (Single Responsibility Principle), que diz que uma classe deve ter apenas um motivo para existir. No projeto, os use cases foram organizados de forma que cada um trata exclusivamente de uma funcionalidade específica. Já os controllers ficaram responsáveis exclusivamente pelo tratamento das requisições HTTP.

O ISP (Interface Segregation Principle) também está presente, com interfaces específicas e bem definidas, evitando métodos genéricos ou desnecessários.

Por fim, o DIP (Dependency Inversion Principle) foi aplicado ao fazer com que os use cases dependam de abstrações, como a interface IWishlistRepository, e não de implementações concretas, promovendo baixo acoplamento e facilitando testes e substituições.

### Outros Detalhes
- Foi necessário definir um tamanho máximo para a lista de produtos da wishlist. Considerando o contexto do desafio, o valor fixo de 20 itens está diretamente ligado à regra de negócio e não deve ser alterado dinamicamente.
Porem, caso essa limitação precise ser flexibilizada, o valor pode ser adicionado em um arquivo de configuração e injetado no use case via @Value e repassado ao domínio.
- Dado que o customerId é sempre utilizado para busca das wishlists, defini esse campo como um índice no MongoDB, tendo em mente preparar o serviço para futuras necessidades de escalabilidade e melhora significativa na performance de leitura.
```java
@Document(collection = "wishlists")
public class WishlistDocument {

    @Id
    private String id;
    @Indexed(unique = true)
    private String customerId;
    private List<ProductDocument> products;
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;

}
```
- Configurei o Logback para geração de arquivos de log separados por nível: um para mensagens de INFO e outro para ERROR.
Os arquivos são rotacionados diariamente, com retenção máxima de 2 dias e limite de tamanho total de 10MB.
- Apliquei uma regra de proteção na branch main via GitHub.
Com isso, apenas merges via Pull Requests são permitidos, e é obrigatório que o workflow valide os testes automatizados com sucesso antes de permitir a mesclagem.

### Testes
Com Junit5 e Mockito criei testes para cobrir 100% meus casos de uso, dominios, controllers e repositorios, garantindo dessa forma que a funcionalidade está sendo executada da forma correta. Tambem criei testes integrados com testcontainer, onde validei todos os fluxos esperados de forma integrada, desde a entrada da informação via http, até a persistencia da informação no banco de dados. Para garantir que a cobertura sempre vai ser alta, adicionei o jacoco e configurei como requisito 100% de cobertura nas camadas principais. 

## Licença

Este projeto está sob a licença MIT.
