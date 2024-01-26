# POSTUBE
Aplicação web de gerenciamento e streaming de vídeos.

## Vídeos
Este microsserviço permite adicionar, atualizar e deletar vídeos, assim como exibir um vídeo específico ou listar todos os vídeos existentes. Ele também oferece um endpoint para exibição do vídeo, com carregamento de forma assíncrona.

## Usuários
A aplicação também permite adicionar usuários ao sistema, assim como buscar usuários específicos. É possível também favoritar vídeos para um usuário e listar seus vídeos favoritos.

## Relatório
A aplicação oferece um endpoint com um relatório geral do sistema, exibindo o número total de vídeos, o total de vídeos favoritados e a média de visualizações.


## Desenvolvimento
Para subir o banco de dados localmente, é necessário o docker instalado. Para subir, usamos o seguinte comando no terminal

`docker run -it -p 27017:27017 mongo:4.4`

Em seguida basta executar o comando `mvn spring-boot:run` para rodar o seu projeto.

## Endereços da API
No link abaixo são listados os endpoints disponíveis na aplicação:

[PosTube](https://documenter.getpostman.com/view/15692045/2s9YypDNHL#f148061f-115f-48a2-a1cf-9751b699b977)
