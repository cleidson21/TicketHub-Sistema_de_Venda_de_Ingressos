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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import model.tickethub.Controller;
import model.tickethub.Usuario;
import view.dashboardUser.DashboardUserController;
import view.dashboardAdmin.DashboardAdminController;
import java.util.Locale;
import java.util.Objects;

/**
 * Classe principal da aplicação TicketHub.
 * É responsável por iniciar a aplicação, exibir a tela de login e gerenciar a transição entre as telas.
 *
 * @author Cleidson Ramos de Carvalho
 * @version 1.3
 */

public class Main extends Application {

    // Janela principal da aplicação
    private static Stage primaryStage;
    // Instância do Controller principal para gerenciar lógica do sistema
    private static Controller controller;
    // Seletor de linguagem para ser unico para todas as telas
    private static ChoiceBox<String> languageSelection;
    // Idioma atual selecionado
    public static Locale currentLanguage= new Locale("pt", "BR");


    /**
     * Metodo principal de entrada da aplicação.
     * @param stage Janela principal do JavaFX.
     */
    @Override public void start(Stage stage) {
        primaryStage = stage;
        controller = new Controller();
        // Configuração inicial do idioma
        LanguageManager.setLanguage(currentLanguage);
        // Configura o ChoiceBox de idiomas
        languageSelection = new ChoiceBox<>();
        languageSelection.getItems().addAll("Português", "English", "Español");
        // Define o idioma selecionado no ChoiceBox com base no idioma atual
        switch (currentLanguage.getLanguage()) {
            case "en":
                languageSelection.setValue("English");
                break;
            case "es":
                languageSelection.setValue("Español");
                break;
            default:
                languageSelection.setValue("Português");
                break;
        }
        // Exibe a tela de login ao iniciar o aplicativo
        showLoginScreen();
        primaryStage.setTitle(LanguageManager.getString("titulo.janela"));
        // Configura o ícone da janela
        primaryStage.getIcons().add(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("/images/logo.JPG"))));
        // Impede que a janela seja redimensionada
        primaryStage.setResizable(false);
        // Exibe a janela principal
        primaryStage.show();
    }

    /**
     * Exibe a tela de login do sistema.
     * Carrega o arquivo FXML correspondente e associa o controlador da tela.
     */
    public static void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("LoginRegister.fxml"));
            Scene scene = new Scene(loader.load());
            // Obtem o controlador da tela de login e injeta a instância do Controller principal
            LoginController loginController = loader.getController();
            loginController.setController(controller);
            loginController.setLanguageSelection(languageSelection);

            primaryStage.setScene(scene);
            // Adiciona o arquivo CSS para estilização
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/login.css")).toExternalForm());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exibe o dashboard do usuário após um login bem-sucedido.
     * @param usuarioEncontrado Usuário autenticado cujos dados serão exibidos.
     */
    public static void showDashboardScreen(Usuario usuarioEncontrado) {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/dashboardUser/dashboard.fxml"));
            Scene scene = new Scene(loader.load());

            // Configura o controlador da tela de dashboard com os dados do usuário
            DashboardUserController dashboardUserController = loader.getController();
            dashboardUserController.setController(controller);
            dashboardUserController.setUsuario(usuarioEncontrado);
            dashboardUserController.setLanguageSelection(languageSelection);
            dashboardUserController.inicializar();

            primaryStage.setScene(scene);

            // Adiciona o arquivo CSS para estilização
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/dashboard.css")).toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Exibe o dashboard do administrador.
     * Esta tela é destinada para a criação e edição de eventos.
     */
    public static void showDashboardAdminScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(Main.class.getResource("/view/dashboardAdmin/dashboardAdmin.fxml"));
            Scene scene = new Scene(loader.load());

            // Configura o controlador da tela de dashboard do administrador
            DashboardAdminController dashboardAdminController = loader.getController();
            dashboardAdminController.setController(controller);
            dashboardAdminController.setLanguageSelection(languageSelection);

            primaryStage.setScene(scene);

            // Adiciona o arquivo CSS para estilização
            scene.getStylesheets().add(Objects.requireNonNull(Main.class.getResource("/css/dashboardAdmin.css")).toExternalForm());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Atualiza todas as cenas exibidas na aplicação para refletir mudanças no idioma.
     * Este metodo verifica se a cena atual possui um controlador que implementa a interface {@link BaseController}.
     * Caso tenha, chama o metodo {@code updateLanguage()} do controlador para aplicar as atualizações
     * de idioma. Além disso, atualiza o título da janela principal com base no idioma configurado.
     */
    public static void updateAllScenes() {
        // Obtém a cena atual da aplicação
        Scene currentScene = primaryStage.getScene();

        if (currentScene != null) {
            // Obtém o controlador associado à cena atual
            Object controller = currentScene.getUserData();

            // Verifica se o controlador implementa BaseController
            if (controller instanceof BaseController) {
                // Chama o método updateLanguage para atualizar o idioma da interface
                ((BaseController) controller).updateLanguage();
            }
        }

        // Atualiza o título da janela principal da aplicação
        primaryStage.setTitle(LanguageManager.getString("titulo.janela"));
    }

    /**
     * Retorna a linguagem atual selecionada
     * @return A linguagem atual selecionada
     */
    public static Locale getCurrentLanguage() {
        return currentLanguage;
    }

    /**
     * Metodo principal para iniciar a aplicação.
     * @param args Argumentos da linha de comando.
     */
    public static void main(String[] args) {
        launch(args);
    }
}
