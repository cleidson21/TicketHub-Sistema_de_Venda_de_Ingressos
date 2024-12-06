/**
 * Sistema Operacional: Windows 10 - 64 Bits
 * Versão Da Linguagem: Java JDK version "21.0.4" 2024-07-16
 * Autor: Cleidson Ramos de Carvalho
 * Componente Curricular: EXA 863 - MI Programação
 * Concluído em: 05/12/2024
 * Declaro que este código foi elaborado por mim de forma individual e não contém nenhum trecho de código de outro
 * colega ou de outro autor, tais como provindos de livros e apostilas, e páginas ou documentos eletrônicos da Internet.
 * Qualquer trecho de código de outra autoria que não a minha está destacado com uma citação para o autor e a fonte do código,
 * e estou ciente que estes trechos não serão considerados para fins de avaliação.
 */

package view.dashboardUser.screenIngressosUsuario;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import model.tickethub.*;
import java.text.SimpleDateFormat;

/**
 * Controlador da tela de envio de feedback no sistema TicketHub.
 * Este controlador gerencia a interação do usuário ao avaliar um evento relacionado a um ingresso já comprado.
 * Permite que o usuário insira comentários e selecione uma avaliação por meio de RadioButtons.
 * Além disso, lida com o envio do feedback ao sistema e o encerramento da janela.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class NovoFeedbackController {

    @FXML private TextField nomeEvento;
    @FXML private TextField dataCompra;
    @FXML private TextArea comentario;
    @FXML private ToggleGroup avaliacaoGroup;
    @FXML private HBox avaliacaoContainer;

    private Controller controller;
    private Compra compra;
    private Stage stage;
    private Runnable onFeedbackEnviado;

    /**
     * Inicializa os componentes da interface.
     * Configura o grupo de avaliação (ToggleGroup) e define os atalhos de teclado para interagir com a janela.
     * O botão ENTER finaliza o feedback, enquanto o ESC cancela a operação.
     */
    @FXML
    public void initialize() {
        avaliacaoGroup = new ToggleGroup();

        // Associa os RadioButtons ao ToggleGroup para garantir exclusividade
        for (Node node : avaliacaoContainer.getChildren()) {
            if (node instanceof RadioButton) {
                ((RadioButton) node).setToggleGroup(avaliacaoGroup);
            }
        }

        // Configura atalhos de teclado após a janela ser exibida
        Platform.runLater(() -> stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleFinalizarFeedback();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                handleCancelar();
            }
        }));
    }

    /**
     * Configura o controlador principal do sistema.
     *
     * @param controller Objeto do tipo Controller para gerenciar a lógica de negócios.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Configura o palco (Stage) da janela atual.
     *
     * @param stage Objeto Stage que representa a janela do feedback.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Configura os dados do ingresso relacionados ao feedback.
     * Preenche os campos de nome do evento e data de compra baseados nas informações do ingresso.
     *
     * @param compra Objeto Compra que contém os detalhes do ingresso.
     */
    public void setDadosCompra(Compra compra) {
        this.compra = compra;
        nomeEvento.setText("Evento: " + compra.getIngresso().getEvento().getNome());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        dataCompra.setText("Data: " + sdf.format(compra.getIngresso().getEvento().getData()));
    }

    /**
     * Define o callback a ser executado após o envio do feedback.
     *
     * @param onFeedbackEnviado Callback executado quando o feedback for enviado com sucesso.
     */
    public void setOnFeedbackEnviado(Runnable onFeedbackEnviado) {
        this.onFeedbackEnviado = onFeedbackEnviado;
    }

    /**
     * Finaliza o feedback e envia os dados para o sistema.
     * Obtém as informações do formulário, como avaliação e comentário, e envia via Controller.
     * Caso o envio seja bem-sucedido, executa o callback definido e fecha a janela.
     */
    @FXML
    private void handleFinalizarFeedback() {
        try {
            // Obtém o texto do RadioButton selecionado
            RadioButton selectedRadio = (RadioButton) avaliacaoGroup.getSelectedToggle();
            String avaliacao = selectedRadio.getText();
            String comentarioTexto = comentario.getText();

            // Envia o feedback para o sistema
            controller.solicitarFeedback(compra, avaliacao + " - " + comentarioTexto);

            // Executa o callback após sucesso
            if (onFeedbackEnviado != null) {
                onFeedbackEnviado.run();
            }

            // Fecha a janela
            fecharJanela();
        } catch (Exception e) {
            // Exibe alerta em caso de erro
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao enviar feedback: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Cancela o envio do feedback e fecha a janela.
     */
    @FXML
    private void handleCancelar() {
        fecharJanela();
    }

    /**
     * Fecha a janela atual com confirmação do usuário.
     */
    private void fecharJanela() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancelar envio de feedback?");
        alert.setTitle("Confirmação");
        alert.setHeaderText(null);

        // Exibe o alerta e aguarda a resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.close();
            }
        });
    }
}
