# Primeira atividade

Para esta atividade foi desenvolvido um robô que a partir de um texto de entrada, é definida uma
resposta a ser entregue o utilizador com base nas regras definidas nas instruções da atividade. A linguagem utilizada para construção do robô foi a Kotlin com a plataforma JVM.

Para escolha da resposta pelo robô foi utilizado uma função de predição com base em parametros extraídos do texto de entrada. A partir do texto de entrada é gerado um vetor com valores booleanos que representam as regras (instruções da atividade) que foram atendidas. Esse vetor então é passado para a função de predição que utiliza um modelo de classificação por rede neural persistido em um [arquivo json](app/data/model.json) gerado com o script [training.py](training.py) que implementa a biblioteca [sklearn](https://scikit-learn.org).     

## Instruções

Crie uma classe que representa um robô chamado Marciano que é capaz de responder a
frases ao ser dirigido usando a função responda().
Marciano é um robô ultrapassado e possui respostas limitadas:
- Ele deverá responder "Certamente" se alguém fizer uma pergunta a ele.
- Se alguém gritar com ele (usando alguma palavra com todas suas letras em maiúsculas),
ele deverá responder "Opa! Calma aí!".
- Ele responde "Relaxa, eu sei o que estou fazendo!" se alguém gritar com ele em uma
pergunta.
- Caso alguém fale alguma coisa que use a palavra "eu" (com ou sem maiúsculas), ele
responde "A responsabilidade é sua".
- Se alguém se dirigir a ele sem dizer nenhuma palavra ele deverá responder "Não me
incomode".
- Ele diz "Tudo bem, como quiser" caso alguém fale qualquer outra coisa com ele.
Crie também uma versão mais avançada do robô Marciano que possui as mesmas
funcionalidades da versão anterior mas também é capaz de realizar operações matemáticas
básicas.
- O robô poderá receber as seguintes operações como parâmetro de responda: "some",
"subtraia", "multiplique", "divida"
- Os operandos das operações deverão ser passados como parâmetros extras da função
responda()
- O robô deverá responder: "Essa eu sei", seguida do resultado
A versão premium do robô Marciano permite que o próprio usuário defina uma ação que ele
vai realizar.
- Essa versão tem todas as funcionalidades anteriores, mas a palavra "agir", ele deverá
responder "É pra já!" e realize a ação do usuário
- A ação do usuário deverá ser passada como parâmetro do construtor da classe usando
uma interface definida na versão premium do robô.
Crie um programa que implemente a versão premium do robô Marciano, com uma
funcionalidade qualquer que você quiser. Deixe que o usuário interaja com o robô até que
ele digite "FIM".
Sua entrega deverá conter:
- Classe do robô com comandos básicos
- Classe de extensão do robô com operações matemáticas
- Classe de extensão com comandos personalizados
- Implementação da classe premium do robô
- Uma funcionalidade personalizada de sua escolha
- capriche na sua função personalizada
- tente usar alguns dos conceitos vistos durante a revisão das
linguagens
- Um programa que deixe o usuário digitar comandos ao robô indefinidamente
ou digite a palavra FIM para encerrar


Exercício adaptado da atividade do link: [Bob](https://exercism.org/tracks/java/exercises/bob)


## Inicialização do projeto

O projeto utilizou o [Gradle](https://gradle.org/) para gerenciamento da compilação do projeto, assim a aplicação pode ser inicializada conforme abaixo:

```console
git clone https://github.com/dutrajardim/pos_if_sudeste_desenvolvimento_mobile.git
cd pos_if_sudeste_desenvolvimento_mobile/atividade_1
gradle run
```

Para executar os testes criados, na pasta raiz da atividade execute o comando de inicialização dos testes do Gradle:

```console
gradle test
```

Conforme descrito anteriormente, é utilizado um modelo de aprendizagem de máquina para tomada de decisão do robô, e para atualizar ele é utilizado um script python. Contudo, o script python utiliza um arquivo de dados de perguntas mapeado para vetores de valores booleanos. O arquivo de dados de perguntas utilizado esta disponível neste repositório em [app/data/perguntas.csv](app/data/perguntas.csv). Para gerar o arquivo mapeado de vetores é necessário inicializar a aplicação com o argumento map conforme mostrado abaixo.

```console
gradle run --args="map"
```

Um arquivo gerado por este script também foi disponibilizado neste repositório em [app/data/mapped.csv](app/data/mapped.csv). Este script vai ler o arquivo em app/data/perguntas.csv, mapear as entradas e salvar o resultado em app/data/mapped.csv.

Para gerar um novo modelo de aprendizagem, utilize o script **training.py**. Segue comando para instalação das dependências do script e inicialização dele:

```console
pip install -r py-requirements.txt
python training.py
```

O modelo configurado é inicializado com valores aleatórios e não foi configurado testes neste script para verificar se os valores resultantes da aprendizagem convergiram para uma solução adequada, desta forma é possível que ao rodar o algorítimo o modelo gerado não atenda os requisitos da instrução da atividade. Os valores passados como argumento para a MLPClassifier geraram resultados adequados e validados pelos testes realizados com o Gradle na maioria das vezes (com o arquivo perguntas.csv). Um modelo que atendeu aos requisitos esta disponível em [app/data/model.json](app/data/model.json).

A função de predição utiliza a função de ativação [relu](https://en.wikipedia.org/wiki/Rectifier_(neural_networks)) e a função [softmax](https://en.wikipedia.org/wiki/Softmax_function) para classificação, e foi configurado para aceitar mais de uma camada (hidden layer).