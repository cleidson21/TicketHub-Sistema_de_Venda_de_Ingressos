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
package view.dashboardUser.screenIngressosUsuario;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.tickethub.Compra;
import model.tickethub.Controller;
import model.tickethub.Usuario;
import view.Main;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * Controller responsável pela exibição e gerenciamento da lista de ingressos do usuário.
 * Permite visualizar ingressos válidos e vencidos, cancelar ingressos válidos,
 * e enviar feedback para ingressos vencidos.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class IngressosController {

    private Controller controller; // Controlador principal do sistema
    private Usuario usuario; // Usuário autenticado
    private Stage novoFeedbackStage; // Janela para enviar feedback

    // Elementos da interface relacionados aos ingressos
    private VBox ingressosCompradosList; // Lista de ingressos válidos
    private VBox ingressosVencidosList; // Lista de ingressos vencidos

    /**
     * Define o controlador principal do sistema.
     *
     * @param controller Instância do controlador.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define o usuário autenticado no sistema.
     *
     * @param usuario Instância do usuário.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Configura os elementos visuais utilizados na interface.
     *
     * @param ingressosCompradosList Lista de ingressos válidos.
     * @param ingressosVencidosList Lista de ingressos vencidos.
     */
    public void setElements(VBox ingressosCompradosList, VBox ingressosVencidosList) {
        this.ingressosCompradosList = ingressosCompradosList;
        this.ingressosVencidosList = ingressosVencidosList;
    }

    /**
     * Carrega os ingressos do usuário e organiza-os em válidos e vencidos.
     * Adiciona mensagens de aviso caso nenhuma categoria tenha itens.
     */
    public void carregarIngressos() {
        // Limpa as listas antes de carregar novos dados
        ingressosCompradosList.getChildren().clear();
        ingressosVencidosList.getChildren().clear();

        List<Compra> ingressosComprados = usuario.getIngressos();
        Date dataAtual = new Date();

        if (ingressosComprados != null && !ingressosComprados.isEmpty()) {
            boolean possuiIngressosValidos = false;
            boolean possuiIngressosVencidos = false;

            // Itera sobre os ingressos para classificá-los
            for (Compra compra : ingressosComprados) {
                Label eventoLabel = new Label(formatarIngresso(compra));
                eventoLabel.setMinWidth(210);
                eventoLabel.setMaxWidth(210);
                eventoLabel.setWrapText(true);

                if (compra.getIngresso().getEvento().getData().after(dataAtual)) {
                    // Ingressos válidos
                    Button cancelarIngressoButton = new Button("Cancelar");
                    cancelarIngressoButton.getStyleClass().add("menu-button-sair");
                    cancelarIngressoButton.setOnAction(e -> handleCancelarIngresso(compra));

                    HBox eventoItem = new HBox(10, eventoLabel, cancelarIngressoButton);
                    eventoItem.setStyle("-fx-padding: 10px; -fx-border-color: lightgray; -fx-border-radius: 7px;");
                    eventoItem.setAlignment(Pos.CENTER_LEFT);

                    ingressosCompradosList.getChildren().add(eventoItem);
                    possuiIngressosValidos = true;
                } else {
                    // Ingressos vencidos
                    Button feedbackButton = new Button("Avaliar");
                    feedbackButton.setOnAction(e -> handleEnviarFeedback(compra));
                    if (compra.getFeedBack()) {
                        feedbackButton.setDisable(true);
                    }

                    HBox eventoItem = new HBox(10, eventoLabel, feedbackButton);
                    eventoItem.setStyle("-fx-padding: 10px; -fx-border-color: lightgray; -fx-border-radius: 7px;");
                    eventoItem.setAlignment(Pos.CENTER_LEFT);

                    ingressosVencidosList.getChildren().add(eventoItem);
                    possuiIngressosVencidos = true;
                }
            }

            // Mensagens se não houver ingressos
            if (!possuiIngressosValidos) {
                Label nenhumIngressoValido = new Label("Nenhum ingresso válido encontrado.");
                nenhumIngressoValido.setMinWidth(350);
                nenhumIngressoValido.setWrapText(true);
                ingressosCompradosList.getChildren().add(nenhumIngressoValido);
            }

            if (!possuiIngressosVencidos) {
                Label nenhumIngressoVencido = new Label("Nenhum ingresso vencido encontrado.");
                nenhumIngressoVencido.setMinWidth(350);
                nenhumIngressoVencido.setWrapText(true);
                ingressosVencidosList.getChildren().add(nenhumIngressoVencido);
            }
        } else {
            // Mensagens padrão para ambas as listas
            Label nenhumIngressoComprado = new Label("Você ainda não comprou ingressos.");
            nenhumIngressoComprado.setMinWidth(350);
            nenhumIngressoComprado.setWrapText(true);
            ingressosCompradosList.getChildren().add(nenhumIngressoComprado);

            Label nenhumIngressoVencido = new Label("Não há ingressos vencidos.");
            nenhumIngressoVencido.setMinWidth(350);
            nenhumIngressoVencido.setWrapText(true);
            ingressosVencidosList.getChildren().add(nenhumIngressoVencido);
        }
    }

    /**
     * Formata os dados de um ingresso para exibição.
     *
     * @param compra Objeto de compra contendo informações do ingresso.
     * @return String formatada com detalhes do ingresso.
     */
    private String formatarIngresso(Compra compra) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        return String.format("Nome do Evento: %s\nData do Evento: %s\nAssento: %s",
                compra.getIngresso().getEvento().getNome(),
                sdf.format(compra.getIngresso().getEvento().getData()),
                compra.getIngresso().getAssento());
    }

    /**
     * Manipula o cancelamento de um ingresso comprado pelo usuário.
     * Este metodo exibe uma janela de confirmação para o usuário antes de proceder com o cancelamento.
     * Caso o cancelamento seja bem-sucedido, exibe uma mensagem de sucesso e atualiza a lista de ingressos.
     * Em caso de falha, exibe uma mensagem informando o motivo.
     *
     * @param compra Objeto de compra representando o ingresso a ser cancelado.
     */
    @FXML
    private void handleCancelarIngresso(Compra compra) {
        try {
            // Cria um alerta para confirmar o cancelamento
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                    "Deseja realmente cancelar este ingresso: " + compra.getIngresso().getAssento() + "?");
            alert.setTitle("Confirmação de Cancelamento");
            alert.setHeaderText(null);

            // Aguarda a resposta do usuário
            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    // Tenta cancelar a compra no sistema
                    boolean cancelado = controller.cancelarCompra(usuario, compra);
                    if (cancelado) {
                        // Sucesso: exibe mensagem e atualiza a lista
                        Alert sucesso = new Alert(Alert.AlertType.INFORMATION,
                                "Ingresso cancelado com sucesso.\nNotificação enviada para o email: " + usuario.getEmail());
                        sucesso.setTitle("Cancelamento Realizado");
                        sucesso.setHeaderText(null);
                        sucesso.showAndWait();
                        carregarIngressos(); // Atualiza a interface
                    } else {
                        // Falha: informa o problema
                        Alert falha = new Alert(Alert.AlertType.WARNING,
                                "Não foi possível cancelar o ingresso. Verifique as condições de cancelamento.");
                        falha.setTitle("Falha no Cancelamento");
                        falha.setHeaderText(null);
                        falha.showAndWait();
                    }
                }
            });
        } catch (Exception e) {
            // Erro inesperado: exibe mensagem detalhada
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao cancelar o ingresso: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Abre o formulário para o usuário enviar feedback sobre um ingresso vencido.
     * Este metodo carrega uma nova janela contendo o formulário de feedback.
     * Após o envio do feedback, a lista de ingressos é atualizada automaticamente.
     *
     * @param compra Objeto de compra relacionado ao ingresso avaliado.
     */
    @FXML
    private void handleEnviarFeedback(Compra compra) {
        try {
            // Verifica se a janela de feedback já está aberta
            if (novoFeedbackStage != null && novoFeedbackStage.isShowing()) {
                novoFeedbackStage.toFront();
                return;
            }

            // Carrega o formulário de feedback a partir do arquivo FXML
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/screenIngressosUsuario/novoFeedbackForm.fxml"));
            AnchorPane feedbackForm = loader.load();

            // Configura o controller do formulário de feedback
            NovoFeedbackController feedbackController = loader.getController();
            feedbackController.setController(controller);
            feedbackController.setDadosCompra(compra);

            // Define um callback para atualizar a lista de ingressos após o envio do feedback
            feedbackController.setOnFeedbackEnviado(() -> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Avaliação enviada com sucesso!");
                alert.setTitle("Sucesso");
                alert.setHeaderText(null);
                alert.showAndWait();
                carregarIngressos(); // Atualiza a interface
            });

            // Configura a janela do formulário de feedback
            feedbackForm.getStylesheets().add(Objects.requireNonNull(
                    Main.class.getResource("/css/dashboard.css")).toExternalForm());
            novoFeedbackStage = new Stage();
            novoFeedbackStage.setScene(new Scene(feedbackForm));
            novoFeedbackStage.setTitle("Enviar Avaliação");
            novoFeedbackStage.setResizable(false);

            // Define o estágio no controller e exibe a janela
            feedbackController.setStage(novoFeedbackStage);
            novoFeedbackStage.show();

        } catch (IOException e) {
            // Erro ao carregar ou exibir o formulário
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir a janela de avaliação: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

}
