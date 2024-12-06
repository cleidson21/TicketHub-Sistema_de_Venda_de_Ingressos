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
package view.dashboardUser.screenPerfilUsuario;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.tickethub.Controller;
import model.tickethub.Usuario;

/**
 * Controlador responsável pela interface de adição de um novo cartão ao sistema.
 * Permite que o usuário insira as informações do cartão e realize o cadastro no sistema TicketHub.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class NovoCartaoController {

    // Campos de entrada do formulário de cadastro de cartão
    @FXML private TextField nomeTitularField;
    @FXML private ComboBox<String> tipoCartaoComboBox;
    @FXML private TextField mesValidadeField;
    @FXML private TextField anoValidadeField;
    @FXML private TextField numeroField;
    @FXML private TextField cvcField;

    private Controller controller;
    private Stage stage;
    private Usuario usuario;
    private Runnable onCartaoAdicionado;

    /**
     * Define o callback a ser executado após o cartão ser adicionado.
     *
     * @param onCartaoAdicionado Runnable com a ação a ser executada.
     */
    public void setOnCartaoAdicionado(Runnable onCartaoAdicionado) {
        this.onCartaoAdicionado = onCartaoAdicionado;
    }

    /**
     * Define o controlador principal para comunicação com o sistema.
     *
     * @param controller instância do controlador principal.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define o usuário associado ao cartão que será adicionado.
     *
     * @param usuario instância do usuário atual.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Define o palco (Stage) atual para manipulação da janela.
     *
     * @param stage instância do Stage associado a esta interface.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Inicializa os eventos de teclado para a janela.
     * Configura atalhos para confirmar ou cancelar a operação.
     */
    @FXML
    private void initialize() {
        Platform.runLater(() -> stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleAddCartao();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                handleCancelar();
            }
        }));
    }

    /**
     * Lida com a adição de um novo cartão.
     * Valida os dados inseridos e os envia ao sistema via controller.
     * Exibe mensagens de erro em caso de falha na validação.
     */
    @FXML
    private void handleAddCartao() {
        try {
            String nomeTitular = nomeTitularField.getText();
            String tipoCartao = tipoCartaoComboBox.getSelectionModel().getSelectedItem();
            String validade = mesValidadeField.getText() + "/" + anoValidadeField.getText();
            String numero = numeroField.getText();
            String cvc = cvcField.getText();

            // Validação de campos obrigatórios
            if (isCampoVazio(nomeTitular, tipoCartao, numero, cvc)) {
                exibirAlerta(Alert.AlertType.ERROR, "Erro", "Todos os campos devem ser preenchidos!");
                return;
            }

            // Adiciona o cartão ao sistema
            controller.adicionarCartaoUsuario(usuario, nomeTitular, tipoCartao, numero, validade, cvc);
            exibirAlerta(Alert.AlertType.INFORMATION, "Confirmação", "Cartão cadastrado com sucesso!");

            // Executa o callback, se definido
            if (onCartaoAdicionado != null) {
                onCartaoAdicionado.run();
            }

            stage.close();
        } catch (NullPointerException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Selecione um tipo de cartão!");
        } catch (IllegalArgumentException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Erro: " + e.getMessage());
        }
    }

    /**
     * Lida com a ação de cancelar a operação de adição do cartão.
     * Exibe um alerta de confirmação antes de fechar a janela.
     */
    @FXML
    private void handleCancelar() {
        if (exibirConfirmacao()) {
            stage.close();
        }
    }

    /**
     * Verifica se algum dos campos obrigatórios está vazio.
     *
     * @param valores Lista de valores a serem verificados.
     * @return true se algum valor estiver vazio ou nulo.
     */
    private boolean isCampoVazio(String... valores) {
        for (String valor : valores) {
            if (valor == null || valor.isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Exibe uma mensagem de alerta genérica.
     *
     * @param tipo     Tipo de alerta.
     * @param titulo   Título da janela de alerta.
     * @param mensagem Mensagem principal do alerta.
     */
    private void exibirAlerta(Alert.AlertType tipo, String titulo, String mensagem) {
        Alert alert = new Alert(tipo, mensagem);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.showAndWait();
    }

    /**
     * Exibe um alerta de confirmação e retorna a resposta do usuário.
     *
     * @return true se o usuário confirmar a operação.
     */
    private boolean exibirConfirmacao() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancelar adição de novo cartão?");
        alert.setTitle("Cancelamento de novo cartão");
        alert.setHeaderText(null);
        return alert.showAndWait().filter(response -> response == ButtonType.OK).isPresent();
    }
}
