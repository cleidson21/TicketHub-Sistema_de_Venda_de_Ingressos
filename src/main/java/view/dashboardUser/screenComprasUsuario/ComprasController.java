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

import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.tickethub.Controller;
import model.tickethub.Evento;
import model.tickethub.Usuario;
import view.dashboardUser.DashboardUserController;
import view.Main;
import view.dashboardUser.screenPerfilUsuario.PerfilController;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Objects;

/**
 * Classe responsável por gerenciar as interações relacionadas à compra de ingressos.
 * Inclui funcionalidades como a seleção de eventos, assentos e métodos de pagamento,
 * bem como a finalização e o cancelamento de compras.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class ComprasController {
    private Controller controller;
    private Usuario usuario;
    private Stage novaCompraStage;
    private ChangeListener<String> eventoListener;
    private DashboardUserController dashboardUserController;
    private PerfilController perfilController;

    // Elementos da interface relacionados à compra
    private ChoiceBox<String> eventCompraList;
    private ChoiceBox<String> assentoCompraList;
    private ChoiceBox<String> metodoCompraList;
    private VBox dadosEventoCompra;
    private Button cancelarCompraButton;

    /**
     * Define o controlador principal do sistema.
     * @param controller o controlador principal
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define o usuário atual do sistema.
     * @param usuario o usuário logado
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Define o controlador do dashboard.
     * @param dashboardUserController o controlador do dashboard
     */
    public void setDashboardController(DashboardUserController dashboardUserController) {
        this.dashboardUserController = dashboardUserController;
    }

    /**
     * Define o controlador do perfil do usuário.
     * @param perfilController o controlador do perfil
     */
    public void setPerfilController(PerfilController perfilController) {
        this.perfilController = perfilController;
    }

    /**
     * Inicializa os elementos visuais relacionados à compra de ingressos.
     * Configura os listeners para as listas de eventos e assentos.
     *
     * @param eventCompraList lista de eventos
     * @param assentoCompraList lista de assentos
     * @param metodoCompraList lista de métodos de pagamento
     * @param dadosEventoCompra container para exibir detalhes do evento
     * @param cancelarCompraButton botão para cancelar a compra
     */
    public void setElements(ChoiceBox<String> eventCompraList, ChoiceBox<String> assentoCompraList, ChoiceBox<String> metodoCompraList,
                            VBox dadosEventoCompra, Button cancelarCompraButton) {
        this.eventCompraList = eventCompraList;
        this.assentoCompraList = assentoCompraList;
        this.dadosEventoCompra = dadosEventoCompra;
        this.metodoCompraList = metodoCompraList;
        this.cancelarCompraButton = cancelarCompraButton;

        // Inicializa o listener após configurar os elementos
        inicializarEventoListener();

        // Configura os listeners para habilitar ou desabilitar o botão de cancelar
        eventCompraList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                cancelarCompraButton.setDisable(newVal == null));

        assentoCompraList.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) ->
                cancelarCompraButton.setDisable(newVal == null || eventCompraList.getSelectionModel().getSelectedItem() == null));
    }

    /**
     * Carrega a lista de eventos disponíveis para compra.
     * Apenas eventos ativos e com assentos disponíveis são exibidos.
     */
    @FXML public void carregarListaEventoCompra() {
        eventCompraList.getItems().clear();

        // Remove listeners antigos para evitar problemas
        eventCompraList.getSelectionModel().selectedItemProperty().removeListener(eventoListener);

        // Adiciona eventos à lista
        for (Evento evento : controller.getEventos()) {
            if (evento.isAtivo() && !evento.getAssentosDisponiveis().isEmpty()) {
                eventCompraList.getItems().add(evento.getNome());
            }
        }

        // Reassocia o listener
        eventCompraList.getSelectionModel().selectedItemProperty().addListener(eventoListener);
    }

    /**
     * Carrega a lista de assentos disponíveis para o evento selecionado.
     */
    @FXML private void carregarListaAssentosCompra() {
        assentoCompraList.getItems().clear();

        // Verifica se um evento foi selecionado
        String nomeEventoSelecionado = eventCompraList.getSelectionModel().getSelectedItem();
        if (nomeEventoSelecionado == null) {
            assentoCompraList.setDisable(true);
            return;
        }

        assentoCompraList.setDisable(false);

        // Adiciona os assentos disponíveis do evento selecionado
        for (Evento evento : controller.getEventos()) {
            if (evento.getNome().equals(nomeEventoSelecionado)) {
                assentoCompraList.getItems().addAll(evento.getAssentosDisponiveis());
                break;
            }
        }
    }

    /**
     * Carrega os detalhes do evento selecionado na interface.
     */
    @FXML private void carregarEventoCompra() {
        dadosEventoCompra.getChildren().clear();

        String nomeEventoSelecionado = eventCompraList.getSelectionModel().getSelectedItem();
        if (nomeEventoSelecionado == null) {
            dadosEventoCompra.setDisable(true);
            return;
        }

        dadosEventoCompra.setDisable(false);

        Evento eventoSelecionado = controller.getEventos().stream()
                .filter(evento -> evento.getNome().equals(nomeEventoSelecionado))
                .findFirst()
                .orElse(null);

        if (eventoSelecionado != null) {
            Label eventoLabel = new Label(formatarEvento(eventoSelecionado));
            eventoLabel.setMinWidth(380);
            eventoLabel.setMaxWidth(380);
            eventoLabel.setMinHeight(300);
            eventoLabel.setMaxHeight(300);
            eventoLabel.setWrapText(true);
            eventoLabel.setStyle("-fx-padding: 10px; -fx-border-color: lightgray; -fx-border-radius: 7px;");
            dadosEventoCompra.getChildren().add(eventoLabel);
        }
    }

    /**
     * Manipula o processo de compra de ingressos.
     * Verifica as seleções feitas pelo usuário, valida os campos,
     * e abre a tela para finalizar a compra, se todos os requisitos forem atendidos.
     * <p>
     * Exibe mensagens de alerta em caso de erro ou falta de seleção.
     */
    @FXML public void handleComprarIngresso() {
        try {
            // Verifica se uma janela de compra já está aberta
            if (novaCompraStage != null && novaCompraStage.isShowing()) {
                novaCompraStage.toFront();
            }
            // Obtém os valores selecionados na interface
            String nomeEventoSelecionado = eventCompraList.getSelectionModel().getSelectedItem() != null
                    ? eventCompraList.getSelectionModel().getSelectedItem() : null;
            String assentoSelecionado = assentoCompraList.getSelectionModel().getSelectedItem() != null
                    ? assentoCompraList.getSelectionModel().getSelectedItem() : null;

            // Valida se todos os campos estão preenchidos
            if (nomeEventoSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um evento para a compra.");
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else if (assentoSelecionado == null) {
                Alert alert = new Alert(Alert.AlertType.WARNING, "Selecione um assento para a compra.");
                alert.setTitle("Aviso");
                alert.setHeaderText(null);
                alert.showAndWait();
            } else {
                // Lógica para metodo de pagamento
                String metodoSelecionado = handleMetodoCompraSelecionado();

                if (!(metodoSelecionado == null) && (metodoSelecionado.equalsIgnoreCase("Boleto") || !usuario.getCartoes().isEmpty())) {
                    // Busca o evento correspondente
                    Evento eventoSelecionado = controller.getEventos().stream()
                            .filter(evento -> evento.getNome().equals(nomeEventoSelecionado))
                            .findFirst()
                            .orElse(null);
                    if (eventoSelecionado != null) {
                        // Carrega o formulário de nova compra
                        FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/screenComprasUsuario/novaCompraForm.fxml"));
                        AnchorPane novaCompraForm = loader.load();

                        NovaCompraController novaCompraController = loader.getController();
                        novaCompraController.setDadosControle(controller, usuario);
                        // Passa o callback para atualizar o estado após a compra
                        novaCompraController.setOnCompraFinalizada(compra -> {
                            if (compra == null) {
                                // O usuário cancelou a compra
                                Alert cancelado = new Alert(Alert.AlertType.INFORMATION, "Compra cancelada pelo usuário.");
                                cancelado.setTitle("Cancelado");
                                cancelado.setHeaderText(null);
                                cancelado.showAndWait();
                                dashboardUserController.showEventosPane();
                            } else {
                                // Compra finalizada com sucesso
                                Alert alert = new Alert(Alert.AlertType.INFORMATION, "Compra realizada com sucesso!\n" +
                                        "Notificação enviada para o email: " + usuario.getEmail());
                                alert.setTitle("Sucesso");
                                alert.setHeaderText(null);
                                alert.showAndWait();
                                dashboardUserController.carregarEventos(); // Atualiza a lista após compra
                                dashboardUserController.showEventosPane();
                            }
                            if (novaCompraStage != null) {
                                novaCompraStage.close(); // Fecha a janela
                            }
                            novaCompraStage = null; // Reseta o controle da janela
                        });

                        novaCompraController.setDadosCompra(eventoSelecionado, assentoSelecionado, metodoSelecionado);

                        novaCompraForm.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/dashboard.css")).toExternalForm());
                        novaCompraStage = new Stage();
                        novaCompraStage.setScene(new Scene(novaCompraForm));
                        novaCompraStage.setTitle("Finalizar Compra");
                        novaCompraController.setStage(novaCompraStage);
                        novaCompraStage.setResizable(false);
                        novaCompraStage.show();
                    }
                }

            }

        } catch (RuntimeException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao finalizar compra: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Erro ao abrir a janela de compra: " + e.getMessage());
            alert.setTitle("Erro");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Manipula a seleção do metodo de pagamento pelo usuário.
     * Exibe alertas caso nenhum metodo seja selecionado ou, no caso de cartão, verifica se há cartões cadastrados.
     * Caso contrário, retorna o metodo selecionado.
     *
     * @return O metodo de pagamento selecionado ("Boleto" ou "Cartão"), ou {@code null} caso não seja selecionado.
     */
    @FXML public String handleMetodoCompraSelecionado() {
        // Obtém o metodo de pagamento selecionado na lista
        String metodoSelecionado = metodoCompraList.getSelectionModel().getSelectedItem() != null
                ? metodoCompraList.getSelectionModel().getSelectedItem() : null;

        if (metodoSelecionado == null) {
            // Exibe um alerta se nenhum metodo de pagamento for selecionado
            Alert alert = new Alert(Alert.AlertType.WARNING, "Por favor, selecione um método de pagamento.");
            alert.setTitle("Aviso");
            alert.setHeaderText(null);
            alert.showAndWait();
        } else {
            if (metodoSelecionado.equalsIgnoreCase("Boleto")) {
                // Retorna "Boleto" se este método for selecionado
                return metodoSelecionado;
            } else if (metodoSelecionado.equalsIgnoreCase("Cartão")) {
                // Verifica se há cartões cadastrados no perfil do usuário
                if (usuario.getCartoes().isEmpty()) {
                    // Alerta e solicita ao usuário o cadastro de um cartão
                    Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Nenhum cartão cadastrado. Deseja cadastrar um cartão agora?");
                    confirmAlert.setTitle("Cartão necessário");
                    confirmAlert.setHeaderText(null);
                    confirmAlert.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.OK) {
                            try {
                                perfilController.handleAddCartao(); // Abre a janela de cadastro de cartão
                            } catch (Exception e) {
                                // Exibe um alerta caso ocorra erro ao abrir a tela
                                Alert errorAlert = new Alert(Alert.AlertType.ERROR,
                                        "Erro ao abrir a tela de cadastro de cartão: " + e.getMessage());
                                errorAlert.setTitle("Erro");
                                errorAlert.setHeaderText(null);
                                errorAlert.showAndWait();
                            }
                        } else {
                            // Retorna para a tela de eventos se o usuário cancelar o cadastro
                            dashboardUserController.showEventosPane();
                            dashboardUserController.carregarEventos();
                        }
                    });
                } else {
                    // Verifica se um cartão específico foi selecionado na lista
                    if (metodoCompraList.getSelectionModel().getSelectedItem() == null) {
                        // Alerta o usuário para selecionar um cartão
                        Alert alert = new Alert(Alert.AlertType.WARNING,
                                "Por favor, selecione um cartão de pagamento.");
                        alert.setTitle("Cartão necessário");
                        alert.setHeaderText(null);
                        alert.showAndWait();
                    }
                    // Retorna "Cartão" se este método for válido
                    return metodoSelecionado;
                }
            }
        }
        // Retorna o método selecionado ou null caso nenhuma opção seja válida
        return metodoSelecionado;
    }

    /**
     * Cancela a compra de um ingresso após confirmação do usuário.
     * Retorna para a tela de eventos caso o usuário confirme o cancelamento.
     */
    @FXML public void handleCancelarCompraIngresso() {
        // Cria um alerta de confirmação para cancelar a compra
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente cancelar a compra?");
        alert.setTitle("Confirmação de Cancelamento!");
        alert.setHeaderText(null);

        // Mostra o alerta e espera pela resposta do usuário
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                // Se o usuário confirmar, retorna para a tela de eventos
                dashboardUserController.showEventosPane();
                dashboardUserController.carregarEventos();
            }
        });
    }

    /**
     * Inicializa o listener para monitorar a seleção de eventos.
     * Habilita o botão de cancelar compra e carrega as informações do evento e dos assentos quando um novo evento é selecionado.
     */
    private void inicializarEventoListener() {
        eventoListener = (obs, oldVal, newVal) -> {
            if (newVal != null) {
                // Habilita o botão de cancelar compra
                cancelarCompraButton.setDisable(false);
                // Carrega os dados do evento e lista de assentos
                carregarEventoCompra();
                carregarListaAssentosCompra();
            }
        };
    }

    /**
     * Formata as informações de um evento em uma string amigável para exibição.
     *
     * @param evento O evento a ser formatado.
     * @return Uma string contendo as informações do evento (nome, data, ingressos disponíveis e descrição).
     */
    private String formatarEvento(Evento evento) {
        // Formata a data do evento
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        // Retorna uma string formatada com os dados do evento
        return String.format("Nome do Evento: %s\nData: %s\nIngressos: %d disponíveis\nDescrição: %s",
                evento.getNome(),
                sdf.format(evento.getData()),
                evento.getQuantidadeAssentosDisponiveis(),
                evento.getDescricao());
    }

}
