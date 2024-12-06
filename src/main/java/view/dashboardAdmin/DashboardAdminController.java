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
package view.dashboardAdmin;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import model.tickethub.Controller;
import model.tickethub.Evento;
import view.BaseController;
import view.LanguageManager;
import view.Main;

/**
 * Controlador para o Dashboard do Administrador no sistema TicketHub.
 * Gerencia a exibição, criação, edição e remoção de eventos, além de interações da interface com o usuário.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class DashboardAdminController extends BaseController {

    @FXML private VBox eventList;  // Lista de eventos exibida no painel.
    @FXML private Button eventosButton; // Botão para visualizar eventos do dashboard.
    @FXML private Button sairButton;  // Botão para sair do dashboard.
    @FXML private ImageView adminImage;  // Imagem do perfil do administrador.
    @FXML private Button novoEventoButton;  // Botão para criar um novo evento.
    @FXML private Button editarEventoButton;  // Botão para editar o evento selecionado.
    @FXML private Button removerEventoButton;  // Botão para remover o evento selecionado.
    @FXML private ChoiceBox<String> languageSelectionAdmin; // ChoiceBox para selecionar o idioma.

    private Controller controller;  // Controlador principal que gerencia as operações do sistema.
    private ToggleGroup toggleGroup;  // Grupo de seleção único para associar RadioButtons dos eventos.
    private Stage novoEventoStage;  // Janela para criar um novo evento.
    private Stage editarEventoStage;  // Janela para editar um evento existente.

    /**
     * Define o controlador principal do sistema.
     * @param controller instância do controlador principal.
     */
    public void setController(Controller controller) {
        this.controller = controller;
        carregarEventos(); // Atualiza a lista de eventos ao configurar o controlador.
    }

    /**
     * Sincroniza o `ChoiceBox` local com o global.
     * @param languageSelection Instância global do `ChoiceBox`.
     */
    public void setLanguageSelection(ChoiceBox<String> languageSelection) {
        if (languageSelection != null) {
            languageSelectionAdmin.getItems().setAll(languageSelection.getItems());
            switch (Main.getCurrentLanguage().getLanguage()) {
                case "en":
                    languageSelectionAdmin.setValue("English");
                    break;
                case "es":
                    languageSelectionAdmin.setValue("Español");
                    break;
                default:
                    languageSelectionAdmin.setValue("Português");
                    break;
            }
        }
    }

    /**
     * Inicializa os componentes da interface gráfica e configura os eventos de ação.
     */
    public void initialize() {
        // Sincronizar a seleção de idioma
        languageSelectionAdmin.setOnAction(event -> {
            String selectedLanguage = languageSelectionAdmin.getValue();
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

        toggleGroup = new ToggleGroup(); // Inicializa o ToggleGroup para os RadioButtons.

        // Configura os botões com seus respectivos métodos de ação.
        sairButton.setOnAction(event -> handleSair());
        novoEventoButton.setOnAction(event -> handleNovoEvento());
        editarEventoButton.setOnAction(event -> handleEditarEvento(toggleGroup));
        removerEventoButton.setOnAction(event -> handleRemoverEvento(toggleGroup));

        // Configura tecla ESC para realizar logout.
        Platform.runLater(() -> sairButton.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ESCAPE) {
                handleSair();
            }
        }));

        // Carrega a imagem do administrador.
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/adminIcon.png")));
        adminImage.setImage(image);
    }

    /**
     * Atualiza os textos da interface de Dashboard do administrador com base no idioma atual.
     * Utiliza o {@link LanguageManager} para obter as strings traduzidas conforme o idioma configurado.
     * Este metodo é chamado automaticamente sempre que houver uma mudança de idioma na aplicação.
     */
    @Override public void updateLanguage() {
        eventosButton.setText(LanguageManager.getString("button.eventos"));
        novoEventoButton.setText(LanguageManager.getString("button.novoEventoAdmin"));
        editarEventoButton.setText(LanguageManager.getString("button.editarEventoAdmin"));
        removerEventoButton.setText(LanguageManager.getString("button.removerEventoAdmin"));
        sairButton.setText(LanguageManager.getString("button.sair"));
    }

    /**
     * Carrega os eventos cadastrados no sistema e exibe na interface gráfica.
     */
    private void carregarEventos() {
        eventList.getChildren().clear(); // Limpa a lista antes de carregar os eventos.
        List<Evento> eventos = controller.getEventos(); // Obtém a lista de eventos do controlador.

        // Cria um RadioButton e Label para cada evento.
        for (Evento evento : eventos) {
            RadioButton radioButton = new RadioButton(); // Botão de seleção para o evento.
            radioButton.setToggleGroup(toggleGroup); // Adiciona ao grupo para garantir seleção única.

            // Configura o Label do evento.
            Label eventoLabel = new Label(formatarEvento(evento));
            eventoLabel.setMinWidth(780);
            eventoLabel.setMaxWidth(780);
            eventoLabel.setWrapText(true); // Permite quebra de linha.
            eventoLabel.setStyle("-fx-padding: 10px; -fx-border-color: lightgray;");

            // Agrupa o RadioButton e o Label em um HBox.
            HBox eventoItem = new HBox(10, radioButton, eventoLabel);
            eventoItem.setAlignment(Pos.CENTER_LEFT); // Alinha os itens à esquerda.

            eventList.getChildren().add(eventoItem); // Adiciona o HBox à lista de eventos.
        }
    }

    /**
     * Formata as informações de um evento para exibição no painel.
     * @param evento objeto Evento a ser formatado.
     * @return uma String formatada com os detalhes do evento.
     */
    private String formatarEvento(Evento evento) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("Nome do Evento: %s\nData: %s\nIngressos: %d disponíveis\nDescrição: %s",
                evento.getNome(),
                sdf.format(evento.getData()),
                evento.getQuantidadeAssentosDisponiveis(),
                evento.getDescricao());
    }

    /**
     * Ação para abrir a janela de criação de novo evento.
     */
    @FXML private void handleNovoEvento() {
        try {
            // Evita abrir múltiplas janelas da criação de evento.
            if (novoEventoStage != null && novoEventoStage.isShowing()) {
                novoEventoStage.toFront();
            } else {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboardAdmin/novoEventoForm.fxml"));
                AnchorPane novoEventoForm = loader.load();
                NovoEventoController novoEventoController = loader.getController();
                novoEventoController.setController(controller);

                // Configura a janela do formulário.
                novoEventoStage = new Stage();
                novoEventoStage.setScene(new Scene(novoEventoForm));
                novoEventoStage.setTitle("Criar Novo Evento");
                // Adiciona o arquivo CSS para estilização
                novoEventoForm.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/dashboardAdmin.css")).toExternalForm());
                novoEventoController.setStage(novoEventoStage);
                novoEventoStage.setResizable(false);
                novoEventoStage.show();
            }
        } catch (IOException e) {
            mostrarAlertaErro("Erro ao abrir o formulário de novo evento: " + e.getMessage());
        }
    }

    /**
     * Ação para abrir a janela de edição do evento selecionado.
     * @param toggleGroup grupo de seleção de eventos.
     */
    @FXML private void handleEditarEvento(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            int selectedIndex = eventList.getChildren().indexOf(selectedRadioButton.getParent());
            Evento eventoSelecionado = controller.getEventos().get(selectedIndex);

            try {
                if (editarEventoStage != null && editarEventoStage.isShowing()) {
                    editarEventoStage.toFront();
                } else {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboardAdmin/editarEventoForm.fxml"));
                    AnchorPane editarEventoForm = loader.load();
                    EditarEventoController editarEventoController = loader.getController();
                    editarEventoController.setController(controller);
                    editarEventoController.preencherCampos(eventoSelecionado);

                    editarEventoStage = new Stage();
                    editarEventoStage.setScene(new Scene(editarEventoForm));
                    // Adiciona o arquivo CSS para estilização
                    editarEventoForm.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/dashboardAdmin.css")).toExternalForm());
                    editarEventoStage.setTitle("Editar Evento: " + eventoSelecionado.getNome());
                    editarEventoController.setStage(editarEventoStage);
                    editarEventoStage.setResizable(false);
                    editarEventoStage.show();
                }
            } catch (Exception e) {
                mostrarAlertaErro("Erro ao editar o evento: " + e.getMessage());
            }
        } else {
            mostrarAlertaErro("Selecione um evento para editar.");
        }
    }

    /**
     * Ação para remover o evento selecionado.
     * @param toggleGroup grupo de seleção de eventos.
     */
    @FXML private void handleRemoverEvento(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            int selectedIndex = eventList.getChildren().indexOf(selectedRadioButton.getParent());
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja apagar o evento selecionado?");
            alert.setTitle("Confirmação de Remoção");

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    controller.removerEvento(selectedIndex);
                    carregarEventos();
                }
            });
        } else {
            mostrarAlertaErro("Selecione um evento para remover.");
        }
    }

    /**
     * Ação para sair do sistema e retornar à tela de login.
     */
    @FXML private void handleSair() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente sair?");
        alert.setTitle("Confirmação de Saída");

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Main.showLoginScreen();
            }
        });
    }

    /**
     * Mostra uma mensagem de erro em um diálogo de alerta.
     * @param mensagem texto da mensagem de erro.
     */
    private void mostrarAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
