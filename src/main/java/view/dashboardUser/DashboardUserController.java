/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão Da Linguagem: Java JDK version "21.0.4" 2024-07-16
 * Autor: Cleidson Ramos de Carvalho
 * Componente Curricular: EXA 863 - MI Programação
 * Concluído em: 05/12/2024
 * Declaro que este código foi elaborado por mim de forma
 * individual e não contém nenhum trecho de código de outro
 * colega ou de outro autor, tais como provindos de livros e
 * apostilas, e páginas ou documentos eletrônicos da Internet.
 * Qualquer trecho de código de outra autoria que não a minha
 * está destacado com uma citação para o autor e a fonte do código,
 * e estou ciente que estes trechos não serão considerados para fins de avaliação.
 */
package view.dashboardUser;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import model.tickethub.*;
import view.BaseController;
import view.LanguageManager;
import view.Main;
import view.dashboardUser.screenComprasUsuario.ComprasController;
import view.dashboardUser.screenIngressosUsuario.IngressosController;
import view.dashboardUser.screenPerfilUsuario.PerfilController;

/**
 * Classe principal da aplicação TicketHub.
 * Responsável por iniciar a aplicação, gerenciar as interações do usuário e
 * controlar a navegação entre as diferentes telas da aplicação.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class DashboardUserController extends BaseController {

    @FXML private VBox eventList;
    @FXML private VBox dadosEventoCompra;
    @FXML private VBox ingressosCompradosList;
    @FXML private VBox ingressosVencidosList;
    @FXML private ImageView userImage;
    @FXML private Label userName;
    @FXML private Button sairButton;
    @FXML private Button perfilButton;
    @FXML private Button eventosButton;
    @FXML private Button ingressosButton;
    @FXML private Button comprasButton;
    @FXML private Button alterarDadosButton;
    @FXML private Button addCartaoButton;
    @FXML private Button rmvCartaoButton;
    @FXML private Button comprarIngressoButton;
    @FXML private Button cancelarCompraButton;
    @FXML private AnchorPane eventosPane;
    @FXML private AnchorPane perfilPane;
    @FXML private AnchorPane ingressosPane;
    @FXML private AnchorPane comprasPane;
    @FXML private TextField nomeField;
    @FXML private TextField emailField;
    @FXML private TextField loginField;
    @FXML private TextField cpfField;
    @FXML private TextField senhaField;
    @FXML private TextField senhaNovamenteField;
    @FXML private ChoiceBox<String> cartoesList;
    @FXML private ChoiceBox<String> eventCompraList;
    @FXML private ChoiceBox<String> assentoCompraList;
    @FXML private ChoiceBox<String> metodoCompraList;
    @FXML private ChoiceBox<String> languageSelectionUser;


    private Controller controller;
    private Usuario usuario;
    private PerfilController perfilController;
    private IngressosController ingressosController;
    private ComprasController comprasController;

    /**
     * Define o controlador da aplicação e carrega a lista de eventos.
     * @param controller O controlador principal da aplicação.
     */
    public void setController(Controller controller) {
        this.controller = controller;
        carregarEventos();
    }

    /**
     * Sincroniza o `ChoiceBox` local com o global.
     * @param languageSelection Instância global do `ChoiceBox`.
     */
    public void setLanguageSelection(ChoiceBox<String> languageSelection) {
        if (languageSelection != null) {
            languageSelectionUser.getItems().setAll(languageSelection.getItems());
            switch (Main.getCurrentLanguage().getLanguage()) {
                case "en":
                    languageSelectionUser.setValue("English");
                    break;
                case "es":
                    languageSelectionUser.setValue("Español");
                    break;
                default:
                    languageSelectionUser.setValue("Português");
                    break;
            }
        }
    }

    /**
     * Define o usuário atual e atualiza a imagem e nome do usuário na interface.
     * @param user O objeto que representa o usuário.
     */
    public void setUsuario(Usuario user) {
        this.usuario = user;

        // Atualiza a imagem e o nome do usuário
        if (usuario != null) {
            // Carrega a imagem do administrador dinamicamente
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/userIcon.png")));
            userImage.setImage(image);

            // Carrega o nome do usuario abaixo da imagem
            String primeiroNome = usuario.getNome().split(" ")[0];
            userName.setText(primeiroNome);
        }
    }

    /**
     * Inicializa o componente de linguagem e o configura.
     */
    @FXML private void initialize() {
        // Sincronizar a seleção de idioma
        languageSelectionUser.setOnAction(event -> {
            String selectedLanguage = languageSelectionUser.getValue();
            if (selectedLanguage.equals("Português")) {
                Main.currentLanguage = new Locale("pt", "BR");
            } else if (selectedLanguage.equals("English")) {
                Main.currentLanguage = new Locale("en", "US");
            } else if (selectedLanguage.equals("Español")) {
                Main.currentLanguage = new Locale("es", "ES");
            }
            LanguageManager.setLanguage(Main.getCurrentLanguage());
            updateLanguage();
            Main.updateAllScenes();
        });
    }

    /**
     * Inicializa os controladores de perfil, ingressos e compras,
     * configura os botões e atalhos da interface e carrega os dados iniciais.
     */
    public void inicializar() {
        // Inicializa o PerfilController
        perfilController = new PerfilController();
        perfilController.setElements(nomeField, emailField, loginField, cpfField, senhaField, senhaNovamenteField,
                cartoesList, rmvCartaoButton, userImage, userName);
        perfilController.setController(controller);
        perfilController.setUsuario(usuario);
        perfilButton.setOnAction(event -> {
            showPerfilPane();
            perfilController.carregarDadosPerfil(); // Atualiza os dados ao abrir o painel de perfil
        });
        // Configura os botões de perfilController
        alterarDadosButton.setOnAction(event -> perfilController.handleAlterarPerfil());
        addCartaoButton.setOnAction(event -> perfilController.handleAddCartao());
        rmvCartaoButton.setOnAction(event -> perfilController.handleRmvCartao());

        eventosButton.setOnAction(event -> {
            showEventosPane();
            carregarEventos(); // Atualiza a lista de eventos todas as vezes que o painel é acessado
        });

        // Inicializa o IngressosController
        ingressosController = new IngressosController();
        ingressosController.setElements(ingressosCompradosList, ingressosVencidosList);
        ingressosController.setController(controller);
        ingressosController.setUsuario(usuario);
        ingressosButton.setOnAction(event -> {
            showIngressosPane();
            ingressosController.carregarIngressos();
        });

        // Inicializa o ComprasController
        comprasController = new ComprasController();
        comprasController.setElements(eventCompraList, assentoCompraList, metodoCompraList, dadosEventoCompra,
                cancelarCompraButton);
        comprasController.setController(controller);
        comprasController.setUsuario(usuario);
        comprasController.setDashboardController(this);
        comprasController.setPerfilController(perfilController);
        // Configura os botões de ComprarController
        comprarIngressoButton.setOnAction(event -> comprasController.handleComprarIngresso());
        cancelarCompraButton.setOnAction(event -> comprasController.handleCancelarCompraIngresso());
        comprasButton.setOnAction(event -> showComprasPane());
        cancelarCompraButton.setDisable(true); // Desativar inicialmente

        sairButton.setOnAction(event -> handleSair());
        // Inicializa teclas de Atalhos de saída
        teclasAtalhoSair();
        // Inicializa teclas de Atalhos gerais
        teclasAtalhoEnter();
        // Inicializa teclas de Atalhos para os menus
        teclasAtalhoMenu();
        // Adiciona legendas nos botões do Menu
        adicionarLegendaBotoes();
    }

    /**
     * Atualiza os textos da interface de Dashboard do usuario com base no idioma atual.
     * Utiliza o {@link LanguageManager} para obter as strings traduzidas conforme o idioma configurado.
     * Este metodo é chamado automaticamente sempre que houver uma mudança de idioma na aplicação.
     */
    @Override public void updateLanguage() {
        perfilButton.setText(LanguageManager.getString("button.perfilUser"));
        eventosButton.setText(LanguageManager.getString("button.eventos"));
        ingressosButton.setText(LanguageManager.getString("button.ingressosUser"));
        comprasButton.setText(LanguageManager.getString("button.comprasUser"));
        sairButton.setText(LanguageManager.getString("button.sair"));
    }

    /**
     * Configura o atalho de tecla ESC para sair ou cancelar uma compra.
     */
    private void teclasAtalhoSair() {
        Platform.runLater(() -> {
            Scene scene = sairButton.getScene(); // Obtém a cena
            scene.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ESCAPE) {
                    if (comprasPane.isVisible() && !cancelarCompraButton.isDisable()) {
                        comprasController.handleCancelarCompraIngresso();
                    } else {
                        handleSair();
                    }
                    event.consume();
                }
            });
        });
    }

    /**
     * Configura atalhos de teclado para navegação e ações dentro dos formulários.
     */
    private void teclasAtalhoEnter() {
        // Teclas de atalho para o Perfil
        nomeField.setOnKeyPressed(this::handleSalvarDadosKeyPress);
        emailField.setOnKeyPressed(this::handleSalvarDadosKeyPress);
        senhaField.setOnKeyPressed(this::handleSalvarDadosKeyPress);
        senhaNovamenteField.setOnKeyPressed(this::handleSalvarDadosKeyPress);

        // Teclas de atalho para Compra
        eventCompraList.setOnKeyPressed(this::handleComprarIngressoKeyPress);
        assentoCompraList.setOnKeyPressed(this::handleComprarIngressoKeyPress);
        metodoCompraList.setOnKeyPressed(this::handleComprarIngressoKeyPress);
    }

    /**
     * Configura os atalhos de teclado para acessar os menus de forma rápida.
     */
    private void teclasAtalhoMenu() {
        Scene scene = perfilButton.getScene();
        // Atalho para o Perfil
        KeyCombination altP = new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (altP.match(event)) {
                showPerfilPane();
            }
        });
        // Atalho para os Eventos
        KeyCombination altE = new KeyCodeCombination(KeyCode.E, KeyCombination.ALT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (altE.match(event)) {
                showEventosPane();
            }
        });
        // Atalho para Ingressos
        KeyCombination altI = new KeyCodeCombination(KeyCode.I, KeyCombination.ALT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (altI.match(event)) {
                showIngressosPane();
            }
        });
        // Atalho para Compras
        KeyCombination altC = new KeyCodeCombination(KeyCode.C, KeyCombination.ALT_DOWN);
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event -> {
            if (altC.match(event)) {
                showComprasPane();
            }
        });
    }

    /**
     * Adiciona as legendas (tooltips) aos botões de navegação.
     * As tooltips mostram os atalhos de teclado para cada botão.
     */
    private void adicionarLegendaBotoes() {
        // Tooltip para o botão de perfil, com o atalho "Alt + P"
        Tooltip tooltip = new Tooltip("Alt + P");
        Tooltip.install(perfilButton, tooltip);

        // Tooltip para o botão de eventos, com o atalho "Alt + E"
        tooltip = new Tooltip("Alt + E");
        Tooltip.install(eventosButton, tooltip);

        // Tooltip para o botão de ingressos, com o atalho "Alt + I"
        tooltip = new Tooltip("Alt + I");
        Tooltip.install(ingressosButton, tooltip);

        // Tooltip para o botão de compras, com o atalho "Alt + C"
        tooltip = new Tooltip("Alt + C");
        Tooltip.install(comprasButton, tooltip);

        // Tooltip para o botão de sair, com o atalho "ESCAPE"
        tooltip = new Tooltip("ESC");
        Tooltip.install(sairButton, tooltip);
    }

    /**
     * Atualiza os dados do usuário ao pressionar enter.
     */
    private void handleSalvarDadosKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            perfilController.handleAlterarPerfil();
        }
    }

    /**
     * Compra um ingresso quando pressionado enter.
     */
    private void handleComprarIngressoKeyPress(KeyEvent keyEvent) {
        if (keyEvent.getCode() == KeyCode.ENTER) {
            comprasController.handleComprarIngresso();
        }
    }

    /**
     * Carrega os eventos na interface do usuário, exibindo apenas os eventos ativos.
     * Para cada evento, é criado um Label com as informações formatadas, e um HBox que contém o Label.
     * Os eventos são adicionados a uma lista de eventos na interface.
     */
    public void carregarEventos() {
        // Limpa a lista de eventos na interface antes de adicionar novos eventos
        eventList.getChildren().clear();

        // Recupera a lista de eventos ativos
        List<Evento> eventos = controller.getEventos();

        // Itera sobre a lista de eventos
        for (Evento evento : eventos) {

            // Verifica se o evento está ativo
            if (evento.isAtivo()) {
                // Cria um Label para o evento com as informações formatadas
                Label eventoLabel = new Label(formatarEvento(evento));
                eventoLabel.setMinWidth(760); // Define o tamanho fixo para o Label
                eventoLabel.setMaxWidth(760);
                eventoLabel.setWrapText(true); // Permite que o texto quebre linha
                eventoLabel.setStyle("-fx-padding: 10px; -fx-border-color: lightgray; -fx-border-radius: 7px;");

                // Cria um HBox para agrupar o Label com um possível RadioButton
                HBox eventoItem = new HBox(10, eventoLabel); // Espaçamento de 10 entre o Label e o RadioButton
                eventoItem.setAlignment(Pos.CENTER_LEFT); // Alinhamento à esquerda

                // Adiciona o HBox à lista de eventos
                eventList.getChildren().add(eventoItem);
            }
        }
    }

    /**
     * Formata as informações de um evento em uma string legível.
     * A data do evento é formatada no formato "dd/MM/yyyy", e as informações do evento são formatadas em um texto.
     *
     * @param evento O evento cujas informações serão formatadas.
     * @return Uma string com as informações formatadas do evento.
     */
    private String formatarEvento(Evento evento) {
        // Formata a data do evento
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        // Retorna as informações formatadas do evento
        return String.format("Nome do Evento: %s\nData: %s\nIngressos: %d disponíveis\nDescrição: %s",
                evento.getNome(),
                sdf.format(evento.getData()),
                evento.getQuantidadeAssentosDisponiveis(),
                evento.getDescricao());
    }

    /**
     * Exibe o painel de perfil, ocultando os outros painéis.
     * Reseta a visibilidade de todos os painéis antes de exibir o painel de perfil.
     * Além disso, carrega os dados do perfil do usuário.
     */
    private void showPerfilPane() {
        // Reseta a visibilidade de todos os painéis
        resetarVisibilidade();

        // Exibe o painel de perfil e o coloca em primeiro plano
        perfilPane.setVisible(true);
        perfilPane.toFront();

        // Carrega os dados do perfil
        perfilController.carregarDadosPerfil();
    }

    /**
     * Exibe o painel de eventos, ocultando os outros painéis.
     * Reseta a visibilidade de todos os painéis antes de exibir o painel de eventos.
     * Carrega a lista de eventos na interface.
     */
    public void showEventosPane() {
        // Reseta a visibilidade de todos os painéis
        resetarVisibilidade();

        // Exibe o painel de eventos e o coloca em primeiro plano
        eventosPane.setVisible(true);
        eventosPane.toFront();

        // Carrega os eventos na interface
        carregarEventos();
    }

    /**
     * Exibe o painel de compras, ocultando os outros painéis.
     * Reseta a visibilidade de todos os painéis antes de exibir o painel de compras.
     * Limpa as seleções e recarrega os elementos necessários para o painel de compras.
     */
    private void showComprasPane() {
        // Reseta a visibilidade de todos os painéis
        resetarVisibilidade();

        // Exibe o painel de compras e o coloca em primeiro plano
        comprasPane.setVisible(true);
        comprasPane.toFront();

        // Limpa as seleções de listas no painel de compras
        eventCompraList.getSelectionModel().clearSelection();
        assentoCompraList.getSelectionModel().clearSelection();
        metodoCompraList.getSelectionModel().clearSelection();

        // Recarrega a lista de eventos para a compra
        comprasController.carregarListaEventoCompra();

        // Limpa os elementos relacionados à compra de ingressos
        assentoCompraList.getItems().clear();
        dadosEventoCompra.getChildren().clear();
    }

    /**
     * Exibe o painel de ingressos, ocultando os outros painéis.
     * Reseta a visibilidade de todos os painéis antes de exibir o painel de ingressos.
     * Carrega os ingressos disponíveis na interface.
     */
    private void showIngressosPane() {
        // Reseta a visibilidade de todos os painéis
        resetarVisibilidade();

        // Exibe o painel de ingressos e o coloca em primeiro plano
        ingressosPane.setVisible(true);
        ingressosPane.toFront();

        // Carrega os ingressos disponíveis na interface
        ingressosController.carregarIngressos();
    }

    /**
     * Reseta a visibilidade de todos os painéis, ocultando todos os painéis.
     */
    private void resetarVisibilidade() {
        perfilPane.setVisible(false);
        eventosPane.setVisible(false);
        ingressosPane.setVisible(false);
        comprasPane.setVisible(false);
    }

    /**
     * Metodo para finalizar a aplicação. Exibe um alerta de confirmação de saída.
     * Se o usuário confirmar a saída, a aplicação é encerrada e retorna para a tela de login.
     */
    @FXML private void handleSair() {
        // Cria um alerta de confirmação para a saída
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente sair?");
        alert.setTitle("Confirmação de Saída");
        alert.setHeaderText(null);

        // Mostra o alerta e espera pela resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Se o usuário clicar em "OK", volta para a tela de login
                Main.showLoginScreen();
            }
        });
    }
}