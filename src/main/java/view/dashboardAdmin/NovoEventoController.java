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
import view.Main;
import java.sql.Date;

/**
 * Controlador para a tela de criação de um novo evento no dashboard do administrador.
 * Permite adicionar os detalhes de um evento, como nome, descrição, data, e configuração de assentos,
 * além de gerenciar interações com a interface gráfica do usuário.
 * Funcionalidades principais:
 * - Criar um novo evento com os detalhes fornecidos pelo administrador.
 * - Cancelar a operação de criação e retornar ao dashboard principal.
 * - Gerenciar eventos de teclado para facilitar a navegação.
 *
 * @author Cleidson Ramos
 * @version 1.3
 */
public class NovoEventoController {

    // Campos de entrada para os dados do evento
    @FXML private TextField nomeEventoField; // Nome do evento
    @FXML private TextField descricaoEventoField; // Descrição do evento
    @FXML private DatePicker dataEventoField; // Data do evento
    @FXML private TextField colunasField; // Número de colunas de assentos
    @FXML private TextField fileirasField; // Número de fileiras de assentos

    // Botões para criar ou cancelar a criação de um novo evento
    @FXML private Button criarNovoEventoButton;
    @FXML private Button cancelarNovoEventoButton;

    private Controller controller; // Referência ao controlador principal
    private Stage stage; // Referência à janela atual

    /**
     * Inicializa o controlador e configura os eventos de botão e teclado.
     */
    @FXML private void initialize() {
        criarNovoEventoButton.setOnAction(event -> handleCriarEvento()); // Configura o botão de criação
        cancelarNovoEventoButton.setOnAction(event -> handleCancelar()); // Configura o botão de cancelamento

        // Configura eventos de teclado para "Enter" e "Esc"
        Platform.runLater(() -> stage.getScene().setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                handleCriarEvento();
            } else if (event.getCode() == KeyCode.ESCAPE) {
                handleCancelar();
            }
        }));
    }

    /**
     * Define o controlador principal do sistema.
     * @param controller o controlador principal
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define o estágio (janela) atual.
     * @param stage o estágio atual
     */
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    /**
     * Trata a criação de um novo evento com base nos dados fornecidos pelo administrador.
     * Realiza validação de campos e chama métodos do controlador para persistir o evento.
     */
    @FXML
    private void handleCriarEvento() {
        try {
            // Captura os valores dos campos de entrada
            String nomeEvento = nomeEventoField.getText();
            String descricaoEvento = descricaoEventoField.getText();
            Date data = Date.valueOf(dataEventoField.getValue());
            String colunasStr = colunasField.getText();
            String fileirasStr = fileirasField.getText();

            // Valida os campos obrigatórios
            if (nomeEvento.isEmpty() || descricaoEvento.isEmpty() || colunasStr.isEmpty() || fileirasStr.isEmpty()) {
                exibirAlerta("Todos os campos devem ser preenchidos!");
            } else {
                // Chama o metodo para cadastrar o evento
                controller.cadastrarEvento(nomeEvento, descricaoEvento, data, colunasStr, fileirasStr);

                // Fecha a janela de criação
                stage.close();

                // Retorna à tela do dashboard do administrador
                Main.showDashboardAdminScreen();
            }
        } catch (NullPointerException e) {
            exibirAlerta("Selecione uma data!");
        } catch (IllegalArgumentException e) {
            exibirAlerta("Erro: " + e.getMessage());
        }
    }

    /**
     * Trata o cancelamento da criação de um novo evento.
     * Exibe uma confirmação para o usuário e retorna ao dashboard se confirmado.
     */
    @FXML
    private void handleCancelar() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja cancelar a criação do evento?");
        alert.setTitle("Cancelamento de novo evento");
        alert.setHeaderText(null);

        // Mostra o alerta e espera pela resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                stage.close();
                Main.showDashboardAdminScreen();
            }
        });
    }

    /**
     * Exibe um alerta na interface gráfica.
     *
     * @param mensagem a mensagem a ser exibida
     */
    private void exibirAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem);
        alert.setTitle("Alerta");
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
