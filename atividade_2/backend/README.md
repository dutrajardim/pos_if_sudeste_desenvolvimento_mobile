# Docs

* https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/best-practices.html
* https://docs.aws.amazon.com/cli/latest/reference/dynamodb/

# Banco de dados DynamoDB

A convenção utilizada aqui para nomear as tabelas no DynamoDB foi a composição do nome da aplicação em caixa baixa (ex. ifbot) com o nome da tabela em CamelCase (ex. TableName), separados um underline (ex. \<appname\>_\<TableName\>) e para os atributos da tabela foi utilizado o CamelCase (ex. \<AttributeName\>).

## Ambiente local

No diretório `__local__/dynamodb` foi disponibilizados arquivos para configurar um ambiente de teste para o DynamoDB utilizando conteiner docker.

O conteiner docker do DynamoDB pode ser inicializado com o seguinte comando:

```bash
docker run -p 8000:8000 amazon/dynamodb-local
```

Em seguida, a criação das tabelas com os arquivos disponibilizados pode ser executada com o seguinte comando, que pode ser repetido para cara arquivo.

```bash
aws dynamodb create-table \
    --cli-input-json file://./__local__/dynamodb/create-ifbot-training-table.json \
    --endpoint-url http://localhost:8000
```

Segue alguns comandos úteis para manipular a base no ambiente local.

* listar dados de tabela:

```bash
aws dynamodb scan --table-name ifbot_Training \
    --endpoint-url http://localhost:8000
```

* deletar tabela:

```bash
aws dynamodb delete-table --table-name ifbot_Training \
    --endpoint-url http://localhost:8000
```

* descrição de demais comandos:

```bash
aws dynamodb help
```

# Serverless API/Lambda

## Preparação

A preparação da aplicação serverles, tanto para o ambiente de local de testes como para o ambidente de produção, é realicada com o seguinte comando:

```bash
sam build --use-container
```

Os arquivo gerados pelo comando acima são armazenados no diretório oculto `.aws-sam`. Qualquer alteração nos códigos da aplicação ou nos arquivos de configuração (ex. template.yaml) implica a necessida de executar este comando novamente para atualizar os arquivos de preparação.

### Ambiente local

```bash
sam local invoke botTrain --event events/botTrain.json --env-vars env.json 
```