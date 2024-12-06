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
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import model.tickethub.Controller;
import model.tickethub.Evento;
import view.Main;
import java.sql.Date;

/**
 * Controlador para a tela de edição de eventos no dashboard do administrador.
 * Permite editar os detalhes de um evento selecionado, como nome, descrição, data,
 * e configuração de assentos, além de interagir com a interface gráfica do usuário.
 *
 * @author Cleidson Ramos
 * @version 1.3
 */
public class EditarEventoController {

    // Campos da interface FXML vinculados à edição do evento
    @FXML private TextField nomeEventoField;
    @FXML private TextField descricaoEventoField;
    @FXML private DatePicker dataEventoField;
    @FXML private TextField colunasField;
    @FXML private TextField fileirasField;
    @FXML private Button editarButton;
    @FXML private Button cancelarButton;

    private Controller controller; // Controlador principal do sistema
    private Stage stage; // Janela atual do JavaFX
    private Evento eventoSelecionado; // Evento que está sendo editado

    /**
     * Define o controlador principal do sistema.
     * @param controller Controlador principal.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define a janela associada a este controlador.
     * @param stage Janela atual.
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Metodo chamado automaticamente pelo JavaFX após a inicialização da interface.
     * Configura os eventos dos botões e ações do teclado.
     */
    @FXML private void initialize() {
        editarButton.setOnAction(event -> handleEditarEvento());
        cancelarButton.setOnAction(event -> handleCancelar());

        // Configura atalhos de teclado (ENTER para editar, ESC para cancelar)
        Platform.runLater(() -> stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleEditarEvento();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                handleCancelar();
            }
        }));
    }

    /**
     * Preenche os campos da interface com os dados do evento selecionado.
     * @param evento Evento selecionado para edição.
     */
    public void preencherCampos(Evento evento) {
        this.eventoSelecionado = evento;
        nomeEventoField.setText(evento.getNome());
        descricaoEventoField.setText(evento.getDescricao());

        // Habilita ou desabilita campos de assentos dependendo do estado da venda
        boolean vendaIniciada = evento.getIniciadoVenda();
        colunasField.setDisable(vendaIniciada);
        fileirasField.setDisable(vendaIniciada);
    }

    /**
     * Lida com a ação de editar o evento.
     * Valida os campos, atualiza o evento no sistema, e retorna ao dashboard.
     */
    @FXML private void handleEditarEvento() {
        try {
            // Captura os valores dos campos
            String nomeEvento = nomeEventoField.getText();
            String descricaoEvento = descricaoEventoField.getText();
            Date data = Date.valueOf(dataEventoField.getValue());
            String colunasStr = null;
            String fileirasStr = null;

            // Captura configurações de assentos caso a venda não tenha iniciado
            if (!eventoSelecionado.getIniciadoVenda()) {
                colunasStr = colunasField.getText();
                fileirasStr = fileirasField.getText();
            }

            // Valida campos obrigatórios
            if (!validarCampos(nomeEvento, descricaoEvento, data, colunasStr, fileirasStr)) {
                exibirAlerta("Todos os campos obrigatórios devem ser preenchidos!");
            } else {
                // Atualiza o evento com os novos dados
                controller.editarEvento(eventoSelecionado, nomeEvento, descricaoEvento, data);

                // Atualiza assentos, se aplicável
                if (!eventoSelecionado.getIniciadoVenda() && colunasStr != null && fileirasStr != null) {
                    eventoSelecionado.getAssentosDisponiveis().clear();
                    controller.adicionarAssentoEvento(eventoSelecionado, colunasStr, fileirasStr);
                }

                // Fecha a janela de edição e retorna ao dashboard
                stage.close();
                Main.showDashboardAdminScreen();
            }

        } catch (NullPointerException e) {
            exibirAlerta("Selecione uma data válida!");
        } catch (IllegalArgumentException e) {
            exibirAlerta("Erro: " + e.getMessage());
        }
    }

    /**
     * Lida com a ação de cancelar a edição.
     * Exibe uma confirmação antes de retornar ao dashboard.
     */
    @FXML private void handleCancelar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Cancelar Edição?");
        alert.setTitle("Cancelamento de Edição");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.close();
                Main.showDashboardAdminScreen();
            }
        });
    }

    /**
     * Valida os campos obrigatórios do formulário.
     *
     * @param nome Nome do evento.
     * @param descricao Descrição do evento.
     * @param data Data do evento.
     * @param colunas Número de colunas de assentos (opcional).
     * @param fileiras Número de fileiras de assentos (opcional).
     * @return true se os campos forem válidos; false caso contrário.
     */
    private boolean validarCampos(String nome, String descricao, Date data, String colunas, String fileiras) {
        if (nome == null || nome.isEmpty() || descricao == null || descricao.isEmpty() || data == null) {
            return false;
        }
        return eventoSelecionado.getIniciadoVenda() || (colunas != null && !colunas.isEmpty() && fileiras != null && !fileiras.isEmpty());
    }

    /**
     * Exibe uma mensagem de alerta para o usuário.
     * @param mensagem Mensagem a ser exibida no alerta.
     */
    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
