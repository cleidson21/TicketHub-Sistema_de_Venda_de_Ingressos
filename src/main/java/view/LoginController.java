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
package view;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import model.tickethub.Controller;
import model.tickethub.Usuario;

import java.util.Locale;

/**
 * Controlador responsável pela lógica da tela de login e registro no sistema TicketHub.
 * Permite que usuários se autentiquem e novos usuários se registrem no sistema.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */
public class LoginController extends BaseController {

    // Entrada de seleção de linguagem
    @FXML private ChoiceBox<String> languageSelectionLogin;
    @FXML private Label cabecalhoLoginLabel;
    @FXML private Label subcabecalhoLoginLabel;
    @FXML private Label cabecalhoRegisterLabel;
    @FXML private Label subcabecalhoRegisterLabel;


    // Campos de entrada para login
    @FXML private TextField loginField;
    @FXML private PasswordField passwordField;

    // Campos de entrada para registro
    @FXML private TextField nameRgField;
    @FXML private TextField cpfRgField;
    @FXML private TextField emailRgField;
    @FXML private TextField loginRgField;
    @FXML private TextField senhaRgField;

    // Botões de ação
    @FXML private Button loginButton;
    @FXML private Button registerButton;

    // Instância do controlador principal
    private Controller controller;

    /**
     * Sincroniza o `ChoiceBox` local com o global.
     * @param languageSelection Instância global do `ChoiceBox`.
     */
    public void setLanguageSelection(ChoiceBox<String> languageSelection) {
        if (languageSelection != null) {
            languageSelectionLogin.getItems().setAll(languageSelection.getItems());
            // Define o idioma selecionado no ChoiceBox com base no idioma atual
            switch (Main.getCurrentLanguage().getLanguage()) {
                case "en":
                    languageSelectionLogin.setValue("English");
                    break;
                case "es":
                    languageSelectionLogin.setValue("Español");
                    break;
                default:
                    languageSelectionLogin.setValue("Português");
                    break;
            }
        }
    }

    /**
     * Define o controlador principal que gerencia as operações do sistema.
     * @param controller Instância do controlador principal.
     */
    public void setController(Controller controller) {
        this.controller = controller;
    }

    /**
     * Inicializa os componentes e configura os eventos de ação dos botões e campos.
     */
    @FXML private void initialize() {
        // Carrega o idioma inicial
        updateLanguage();

        // Sincronizar a seleção de idioma
        languageSelectionLogin.setOnAction(event -> {
            String selectedLanguage = languageSelectionLogin.getValue();
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

        // Configurações de ação para os botões de login e registro
        loginButton.setOnAction(event -> handleLogin());
        registerButton.setOnAction(event -> handleRegister());

        // Configuração de eventos de tecla para login e registro
        loginField.setOnKeyPressed(this::handleLoginKeyPress);
        passwordField.setOnKeyPressed(this::handleLoginKeyPress);
        nameRgField.setOnKeyPressed(this::handleRegisterKeyPress);
        cpfRgField.setOnKeyPressed(this::handleRegisterKeyPress);
        emailRgField.setOnKeyPressed(this::handleRegisterKeyPress);
        loginRgField.setOnKeyPressed(this::handleRegisterKeyPress);
        senhaRgField.setOnKeyPressed(this::handleRegisterKeyPress);
    }

    /**
     * Atualiza os textos da interface de login e registro com base no idioma atual.
     * Utiliza o {@link LanguageManager} para obter as strings traduzidas conforme o idioma configurado.
     * Este metodo é chamado automaticamente sempre que houver uma mudança de idioma na aplicação.
     */
    @Override public void updateLanguage() {
        // Configuração dos textos na seção de Login
        cabecalhoLoginLabel.setText(LanguageManager.getString("label.cabecalhoLogin")); // Atualiza o cabeçalho de login
        subcabecalhoLoginLabel.setText(LanguageManager.getString("label.subcabecalhoLogin")); // Atualiza o subcabeçalho de login
        loginField.setPromptText(LanguageManager.getString("label.nomeLogin")); // Placeholder do campo de login
        passwordField.setPromptText(LanguageManager.getString("label.senhaLogin")); // Placeholder do campo de senha
        loginButton.setText(LanguageManager.getString("button.logar")); // Texto do botão de login

        // Configuração dos textos na seção de Registro
        cabecalhoRegisterLabel.setText(LanguageManager.getString("label.cabecalhoRegister")); // Atualiza o cabeçalho de registro
        subcabecalhoRegisterLabel.setText(LanguageManager.getString("label.subcabecalhoRegister")); // Atualiza o subcabeçalho de registro
        nameRgField.setPromptText(LanguageManager.getString("textField.nomeRegister")); // Placeholder do campo nome
        cpfRgField.setPromptText(LanguageManager.getString("textField.cpfRegister")); // Placeholder do campo CPF
        emailRgField.setPromptText(LanguageManager.getString("textField.emailRegister")); // Placeholder do campo e-mail
        senhaRgField.setPromptText(LanguageManager.getString("textField.senhaRegister")); // Placeholder do campo senha
        registerButton.setText(LanguageManager.getString("button.registrar")); // Texto do botão de registrar
    }

    /**
     * Trata o evento de pressionar tecla no campo de login.
     * Executa o login se a tecla ENTER for pressionada.
     * @param event Evento de tecla capturado.
     */
    private void handleLoginKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleLogin();
        }
    }

    /**
     * Trata o evento de pressionar tecla nos campos de registro.
     * Executa o registro se a tecla ENTER for pressionada.
     * @param event Evento de tecla capturado.
     */
    private void handleRegisterKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleRegister();
        }
    }

    /**
     * Lida com o processo de login de um usuário.
     * Verifica as credenciais, redireciona para o dashboard correspondente e exibe alertas em caso de falha.
     */
    @FXML private void handleLogin() {
        String login = loginField.getText().toLowerCase().replaceAll("\\s", "");
        String senha = passwordField.getText();

        // Valida as credenciais do usuário
        Usuario encontrado = controller.logar(login, senha);
        if (encontrado != null) {
            if (login.equals("admin") && senha.equals("senha123")) {
                // Acesso ao painel do administrador
                Main.showDashboardAdminScreen();
            } else {
                // Acesso ao painel do usuário comum
                Main.showDashboardScreen(encontrado);
            }
        } else {
            // Exibe mensagem de erro caso as credenciais estejam incorretas
            Alert alert = new Alert(Alert.AlertType.ERROR, "Login falhou. Verifique suas credenciais.");
            alert.showAndWait();
            passwordField.clear();
        }
    }

    /**
     * Lida com o processo de registro de novos usuários.
     * Valida os campos e exibe mensagens de erro ou sucesso com base no resultado.
     */
    @FXML private void handleRegister() {
        String nome = nameRgField.getText();
        String cpf = cpfRgField.getText();
        String email = emailRgField.getText();
        String loginRg = loginRgField.getText().toLowerCase().replaceAll("\\s", "");
        String senha = senhaRgField.getText();

        try {
            // Realiza o registro do novo usuário
            controller.cadastrarUsuario(loginRg, senha, nome, cpf, email, Boolean.FALSE);

            // Exibe mensagem de sucesso
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Registro efetuado com sucesso.");
            alert.showAndWait();

            // Limpa os campos de entrada após o registro
            nameRgField.clear();
            cpfRgField.clear();
            emailRgField.clear();
            loginRgField.clear();
            senhaRgField.clear();

        } catch (NullPointerException e) {
            // Mensagem de erro caso algum campo esteja vazio
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registro falhou. Todos os campos devem ser preenchidos.");
            alert.showAndWait();
        } catch (IllegalArgumentException e) {
            // Mensagem de erro caso o CPF seja inválido
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registro falhou. Digite um CPF válido.");
            alert.showAndWait();
            cpfRgField.clear();
        } catch (RuntimeException e) {
            // Mensagem de erro caso o usuário ou login já exista
            Alert alert = new Alert(Alert.AlertType.ERROR, "Registro falhou. Usuário ou Login já cadastrado.");
            alert.showAndWait();
            nameRgField.clear();
            cpfRgField.clear();
            emailRgField.clear();
            loginRgField.clear();
            senhaRgField.clear();
        }
    }
}
