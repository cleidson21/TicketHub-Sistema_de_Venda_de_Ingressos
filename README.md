# TicketHub - Sistema de Venda de Ingressos
## Descrição do projeto:
TicketHub é uma aplicação desenvolvida em Java com o framework JavaFX, projetada para gerenciar a compra, venda e administração de ingressos para eventos. O sistema é robusto e modular, atendendo tanto os administradores dos eventos quanto os usuários que desejam adquirir ingressos.

A aplicação possui uma interface moderna e interativa, estruturada para facilitar a navegação e a usabilidade. Todos os dados são manipulados e armazenados utilizando boas práticas de programação orientada a objetos e persistência em JSON, garantindo confiabilidade e organização.

## 📋 Funcionalidades Principais

### 1. Módulo de Administração
- Cadastro e Edição de Eventos: Criação de novos eventos com informações detalhadas como nome, descrição, data, local e quantidade de ingressos disponíveis.
- Gerenciamento de Eventos: Visualização, edição ou exclusão de eventos existentes.
- Painel de Controle: Dashboard dedicado para administradores, oferecendo uma visão consolidada dos eventos cadastrados.

### 2. Módulo de Usuário
- Navegação por Eventos: Lista completa de eventos disponíveis com filtros e detalhes relevantes.
- Compra de Ingressos: Seleção de assentos e métodos de pagamento para concluir a aquisição.
- Histórico de Compras: Visualização de ingressos adquiridos e status das compras.
- Feedback: Avaliação e comentários sobre eventos já concluídos.

### 3. Suporte a Múltiplos Idiomas
- O sistema suporta diferentes idiomas, ajustando a interface e as mensagens com base na preferência do usuário.

### 4. Segurança e Acessibilidade
- Login e Registro: Sistema de autenticação para usuários e administradores, com validações de dados.
- Teclas de Atalho: Facilita a navegação para usuários avançados.
- Responsividade: Layout adaptado para diferentes resoluções de tela.

## 🛠️ Tecnologias Utilizadas
- Java 21: Backend e lógica de negócio.
- JavaFX: Interface gráfica rica e responsiva.
- Maven: Gerenciamento de dependências e build do projeto.
- Gson: Persistência de dados em arquivos JSON.
- FXML: Definição de layouts modulares e personalizáveis.

## 📁 Estrutura do Projeto
```
TicketHub/
├── src/main/java/
│   ├── view/                            # Pacote principal das interfaces e controladores
│   │   ├── dashboardAdmin/              # Pacote das interfaces e controlador da tela do Admin
│   │   ├── dashboardUser/               # Pacote das interfaces e controladores das telas do Usuario
│   │   │   ├── screenComprasUsuario/    # Telas para compra de ingressos
│   │   │   ├── screenEventosUsuario/    # Navegação por eventos
│   │   │   ├── screenPerfilUsuario/     # Gerenciamento de perfil do usuário
│   ├── model/                           # Classes de modelo para persistência e lógica de negócio
├── src/main/resources/                  # Arquivos FXML, CSS e de internacionalização
└── README.md                            # Documentação do projeto
```

##⚡ Como Executar o Projeto
1. Clone o repositório:
   - git clone https://github.com/cleidson21/TicketHub-Sistema_de_Venda_de_Ingressos.java
3. Abra o projeto em sua IDE preferida (recomenda-se IntelliJ IDEA).
4. Certifique-se de ter o Java JDK 21 e o Maven instalados.
5. Compile o projeto:
  - mvn clean install
6. Execute a aplicação:
  - mvn javafx:run

## 🤝 Contribuições
Contribuições são bem-vindas! Para melhorar o projeto, você pode:
- Abrir uma Issue para discutir novas funcionalidades ou correções.
- Enviar um Pull Request com suas melhorias.

## 📜 Licença
Este projeto está licenciado sob a Licença MIT. Sinta-se à vontade para usá-lo, modificá-lo e distribuí-lo, desde que a devida atribuição seja mantida.

TicketHub é a solução ideal para quem busca gerenciar eventos e ingressos de maneira fácil, segura e eficiente. 🎉

