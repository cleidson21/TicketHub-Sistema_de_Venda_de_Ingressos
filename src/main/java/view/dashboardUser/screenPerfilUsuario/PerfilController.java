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

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import model.tickethub.Controller;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import model.tickethub.Usuario;
import model.tickethub.Cartao;
import java.util.Objects;

/**
 * Controlador da tela de perfil do usuário no sistema TicketHub.
 * É responsável por exibir e gerenciar os dados do perfil do usuário,
 * incluindo informações pessoais e gerenciamento de cartões.
 *
 * @author Cleidson
 * @version 1.3
 */
public class PerfilController {
    private Controller controller;
    private Usuario usuario;

    // Elementos da interface relacionados ao perfil
    private TextField nomeField;
    private TextField emailField;
    private TextField loginField;
    private TextField cpfField;
    private TextField senhaField;
    private Label userName;
    private TextField senhaNovamenteField;
    private ChoiceBox<String> cartoesList;
    private ImageView userImage;
    private Button rmvCartaoButton;
    private Stage novoCartaoStage;

    /**
     * Configura o controlador principal do sistema.
     *
     * @param controller instância do controlador principal.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Define o usuário cujos dados serão exibidos no perfil.
     *
     * @param usuario instância do usuário.
     */
    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
        carregarDadosPerfil();
    }

    /**
     * Inicializa os elementos da interface que serão manipulados pelo controlador.
     *
     * @param nomeField Campo de texto para o nome do usuário.
     * @param emailField Campo de texto para o e-mail do usuário.
     * @param loginField Campo de texto para o login do usuário.
     * @param cpfField Campo de texto para o CPF do usuário.
     * @param senhaField Campo de texto para a senha do usuário.
     * @param senhaNovamenteField Campo de texto para confirmação da senha.
     * @param cartoesList Lista de cartões associados ao usuário.
     * @param rmvCartaoButton Botão para remover cartões.
     * @param userImage Imagem do perfil do usuário.
     * @param userName Label que exibe o nome do usuário.
     */
    public void setElements(TextField nomeField, TextField emailField, TextField loginField, TextField cpfField,
                            TextField senhaField, TextField senhaNovamenteField, ChoiceBox<String> cartoesList,
                            Button rmvCartaoButton, ImageView userImage, Label userName) {
        this.nomeField = nomeField;
        this.emailField = emailField;
        this.loginField = loginField;
        this.cpfField = cpfField;
        this.senhaField = senhaField;
        this.senhaNovamenteField = senhaNovamenteField;
        this.cartoesList = cartoesList;
        this.userImage = userImage;
        this.rmvCartaoButton = rmvCartaoButton;
        this.userName = userName;
    }

    /**
     * Carrega os dados do perfil do usuário na interface gráfica.
     * Atualiza os campos de texto e a lista de cartões, e exibe a imagem de perfil padrão.
     */
    public void carregarDadosPerfil() {
        if (usuario != null) {
            // Carrega a imagem padrão do perfil
            Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/images/userIcon.png")));
            userImage.setImage(image);

            // Define os valores nos campos da interface
            nomeField.setText(usuario.getNome());
            emailField.setText(usuario.getEmail());
            loginField.setText(usuario.getLogin());
            cpfField.setText(usuario.getCpf());

            // Atualiza a lista de cartões
            carregarListaCartoes();
        }
    }

    /**
     * Trata o evento de alteração dos dados do perfil.
     * Valida os campos, atualiza os dados do usuário e exibe mensagens de sucesso ou erro.
     */
    @FXML public void handleAlterarPerfil() {
        // Obtém os dados inseridos pelo usuário
        String nome = nomeField.getText();
        String email = emailField.getText();
        String senha1 = senhaField.getText();
        String senha2 = senhaNovamenteField.getText();

        try {
            // Tenta alterar os atributos do usuário no controlador
            controller.alterarAtributoUsuario(usuario, nome, email, senha1, senha2);

            // Atualiza o nome exibido no perfil
            String primeiroNome = usuario.getNome().split(" ")[0];
            userName.setText(primeiroNome);

            // Exibe mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Alteração efetuada com sucesso.");
            alert.showAndWait();

            // Limpa os campos de senha
            senhaField.clear();
            senhaNovamenteField.clear();
        } catch (IllegalArgumentException e) {
            showErrorAlert("Alteração dos dados falhou: " + e.getMessage());
        } catch (RuntimeException e) {
            showErrorAlert("Alteração dos dados falhou: " + e.getMessage());
            senhaField.clear();
            senhaNovamenteField.clear();
        }
    }

    /**
     * Controla a ação de adicionar um novo cartão ao perfil do usuário.
     * Exibe uma nova janela para cadastro de um cartão e, ao adicionar, atualiza a lista de cartões.
     */
    @FXML
    public void handleAddCartao() {
        try {
            // Verifica se a janela de novo cartão já está aberta
            if (novoCartaoStage != null && novoCartaoStage.isShowing()) {
                novoCartaoStage.toFront();  // Coloca a janela em primeiro plano
            } else {
                // Carrega o formulário de novo cartão
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboardUser/screenPerfilUsuario/novoCartaoForm.fxml"));
                AnchorPane novoCartaoForm = loader.load();

                // Obtém o controlador da nova janela e configura seus parâmetros
                NovoCartaoController novoCartaoController = loader.getController();
                novoCartaoController.setController(controller);
                novoCartaoController.setUsuario(usuario);

                // Define o callback para atualizar a lista de cartões ao adicionar um novo cartão
                novoCartaoController.setOnCartaoAdicionado(this::carregarListaCartoes);

                // Exibe a nova janela para criação do cartão
                novoCartaoStage = new Stage();
                novoCartaoForm.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/css/dashboard.css")).toExternalForm());
                novoCartaoStage.setScene(new Scene(novoCartaoForm));
                novoCartaoStage.setTitle("Adicionar Novo Cartão");
                novoCartaoController.setStage(novoCartaoStage);
                novoCartaoStage.setResizable(false);
                novoCartaoStage.show();
            }
        } catch (Exception e) {
            // Em caso de erro, exibe uma mensagem de erro
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR, "Alerta: " + e.getMessage());
            alert.setTitle("Alerta");
            alert.setHeaderText(null);
            alert.showAndWait();
        }
    }

    /**
     * Controla a ação de remover um cartão da lista de cartões do usuário.
     * Exibe uma janela de confirmação e, caso confirmado, remove o cartão selecionado.
     */
    @FXML
    public void handleRmvCartao() {
        // Obtém o cartão selecionado na lista
        String cartaoSelecionado = cartoesList.getValue();

        // Verifica se um cartão foi selecionado
        if (cartaoSelecionado == null) {
            showWarningAlert();
            return;
        }

        // Exibe uma confirmação para remover o cartão
        Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja remover o cartão selecionado?");
        confirmAlert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    // Obtém os últimos 4 dígitos do cartão selecionado
                    String ultimosDigitos = cartaoSelecionado.substring(8);
                    // Remove o cartão do usuário
                    controller.removerCartao(usuario, ultimosDigitos);
                    // Atualiza a lista de cartões após remoção
                    carregarListaCartoes();
                    showInfoAlert();
                } catch (Exception e) {
                    showErrorAlert("Erro ao remover o cartão: " + e.getMessage());
                }
            }
        });
    }

    /**
     * Atualiza a lista de cartões exibida na interface.
     * Habilita ou desabilita a lista e o botão de remoção, dependendo da quantidade de cartões.
     */
    @FXML
    private void carregarListaCartoes() {
        // Limpa a lista atual de cartões
        cartoesList.getItems().clear();

        // Verifica se o usuário possui cartões
        if (usuario.getCartoes().isEmpty()) {
            // Desabilita a lista e o botão de remoção caso não haja cartões
            cartoesList.setDisable(true);
            rmvCartaoButton.setDisable(true);
        } else {
            // Habilita a lista e o botão de remoção se houver cartões
            cartoesList.setDisable(false);
            rmvCartaoButton.setDisable(false);

            // Adiciona os cartões na lista exibida
            for (Cartao cartao : usuario.getCartoes()) {
                String ultimosDigitos = cartao.getNumero().substring(cartao.getNumero().length() - 4);
                cartoesList.getItems().add("Cartão: " + ultimosDigitos);
            }
        }
    }

    /**
     * Exibe uma mensagem de informação em uma janela de alerta.
     */
    private void showInfoAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, "Cartão removido com sucesso!");
        alert.showAndWait();
    }

    /**
     * Exibe uma mensagem de aviso em uma janela de alerta.
     */
    private void showWarningAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING, "Nenhum cartão selecionado!");
        alert.showAndWait();
    }

    /**
     * Exibe uma mensagem de erro em uma janela de alerta.
     *
     * @param message A mensagem de erro a ser exibida.
     */
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message);
        alert.showAndWait();
    }

}
