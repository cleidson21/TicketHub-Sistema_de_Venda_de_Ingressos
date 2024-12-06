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
package view.dashboardUser.screenComprasUsuario;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.tickethub.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.function.Consumer;

/**
 * Classe controladora da tela de nova compra no sistema TicketHub.
 * Gerencia a interação do usuário com a interface, permite configurar os dados da compra e
 * realizar a finalização ou cancelamento da mesma.
 *
 * @author Cleidson Ramoos de Carvalho
 * @version 1.3
 */
public class NovaCompraController {

    @FXML private TextField nomeEvento;
    @FXML private ComboBox<String> selecionarCartaoComboBox;
    @FXML private TextField assentoSelecionado;
    @FXML private TextField dataCompra;

    private Controller controller;
    private Usuario usuario;
    private Evento eventoSelecionado;
    private String metodoSelecionado;
    private Stage stage;
    private Consumer<Compra> onCompraFinalizada;

    /**
     * Define o controller principal e o usuário para a tela de compra.
     *
     * @param controller O controller principal do sistema.
     * @param usuario O usuário que está realizando a compra.
     */
    public void setDadosControle(Controller controller, Usuario usuario) {
        this.controller = controller;
        this.usuario = usuario;
    }

    /**
     * Define o estágio atual da tela.
     *
     * @param stage O estágio da tela atual.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Define a ação a ser executada após a compra ser finalizada.
     *
     * @param callback Callback que será executado ao finalizar ou cancelar a compra.
     */
    public void setOnCompraFinalizada(Consumer<Compra> callback) {
        this.onCompraFinalizada = callback;
    }

    /**
     * Inicializa a tela com ações para teclas ENTER (finalizar) e ESCAPE (cancelar).
     * Executa após a interface ser carregada.
     */
    @FXML
    private void initialize() {
        // Configura atalhos de teclado após o carregamento da tela
        Platform.runLater(() -> stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleFinalizarCompra();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                handleCancelar();
            }
        }));
    }

    /**
     * Configura os dados de compra, incluindo evento, assento e metodo de pagamento.
     *
     * @param evento O evento selecionado.
     * @param assento O assento selecionado para a compra.
     * @param metodoCompra O metodo de pagamento escolhido (Boleto ou Cartão).
     */
    public void setDadosCompra(Evento evento, String assento, String metodoCompra) {
        this.eventoSelecionado = evento;
        nomeEvento.setText("Evento: " + evento.getNome());
        assentoSelecionado.setText("Assento: " + assento);

        // Define a data atual no campo de data da compra
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        dataCompra.setText("Data: " + sdf.format(date));

        selecionarCartaoComboBox.setDisable(false); // Ativa a seleção por padrão
        selecionarCartaoComboBox.getSelectionModel().clearSelection();

        if (metodoCompra.equals("Boleto")) {
            // Configura para boleto, desativando a seleção de cartão
            metodoSelecionado = metodoCompra;
            selecionarCartaoComboBox.setDisable(true);
            selecionarCartaoComboBox.getItems().clear();
            selecionarCartaoComboBox.setPromptText("Boleto");
        } else {
            // Carrega os cartões do usuário para seleção
            carregarCartoesUsuario();
        }
    }

    /**
     * Carrega os cartões cadastrados pelo usuário na ComboBox.
     */
    private void carregarCartoesUsuario() {
        List<Cartao> cartoes = usuario.getCartoes();
        selecionarCartaoComboBox.getItems().clear();

        // Adiciona os últimos dígitos dos cartões para identificação
        for (Cartao cartao : cartoes) {
            String ultimosDigitos = cartao.getNumero().substring(cartao.getNumero().length() - 4);
            selecionarCartaoComboBox.getItems().add("Cartao: " + ultimosDigitos);
        }
    }

    /**
     * Finaliza a compra de um ingresso. Valida os dados e executa a ação definida no callback.
     */
    @FXML private void handleFinalizarCompra() {
        try {
            if (!selecionarCartaoComboBox.isDisable()) {
                // Valida se um cartão foi selecionado, caso necessário
                metodoSelecionado = selecionarCartaoComboBox.getSelectionModel().getSelectedItem();
                if (metodoSelecionado == null) {
                    throw new IllegalArgumentException("Selecione um cartão para finalizar a compra.");
                }
            }

            // Obtém o assento selecionado e realiza a compra
            String assento = assentoSelecionado.getText().split(" ")[1];
            Compra compra = controller.comprarIngresso(usuario, eventoSelecionado, assento, metodoSelecionado);

            // Executa o callback, se definido
            if (onCompraFinalizada != null) {
                onCompraFinalizada.accept(compra);
            }

        } catch (IllegalArgumentException e) {
            // Exibe um alerta caso ocorra erro na validação
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao finalizar compra: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Cancela a compra atual após confirmação do usuário.
     */
    @FXML private void handleCancelar() {
        // Alerta de confirmação de cancelamento
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancelar Compra?");
        alert.setTitle("Cancelamento de Compra");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK && onCompraFinalizada != null) {
                onCompraFinalizada.accept(null); // Sinaliza o cancelamento
            }
        });
    }
}
