# Editor Gráfico com MongoDB

Este é um editor gráfico desenvolvido em Java que permite criar figuras simples como pontos, linhas, círculos e elipses. O aplicativo oferece funcionalidades para salvar os desenhos localmente no computador ou enviá-los para um servidor MongoDB. O sistema também permite abrir desenhos salvos previamente.

## Funcionalidades

- **Desenho de Figuras**: 
  - Ponto
  - Linha
  - Círculo
  - Elipse
- **Cores**: Escolha da cor para desenhar as figuras.
- **Salvar e Abrir Desenhos**: 
  - Salve desenhos localmente em formato de texto.
  - Abra desenhos salvos anteriormente.
- **Enviar para MongoDB**: Envie os desenhos para um banco de dados MongoDB como imagens em formato base64.

## Requisitos

- **Java**: O projeto foi desenvolvido utilizando a versão 8 ou superior do Java.
- **MongoDB**: Uma instância do MongoDB precisa estar disponível para enviar os desenhos para o banco de dados.

## Como Executar

1. **Clone o repositório**:

   ```bash
   git clone https://github.com/seu-usuario/editor-grafico.git
   cd editor-grafico
   Compilar o Projeto:
   javac Janela.java
   java Janela
   Conectar ao MongoDB:
   MongoClient mongoClient = MongoClients.create("mongodb+srv://<usuario>:<senha>@cluster0.vtnlrbv.mongodb.net/");
