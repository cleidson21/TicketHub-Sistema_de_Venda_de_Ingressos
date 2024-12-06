module view {
    requires javafx.controls;
    requires javafx.fxml;
    requires junit;
    requires java.sql;


    opens view to javafx.fxml;
    exports view;
    exports view.dashboardUser.screenPerfilUsuario;
    opens view.dashboardUser.screenPerfilUsuario to javafx.fxml;
    exports view.dashboardAdmin;
    opens view.dashboardAdmin to javafx.fxml;
    exports view.dashboardUser.screenIngressosUsuario;
    opens view.dashboardUser.screenIngressosUsuario to javafx.fxml;
    exports view.dashboardUser.screenComprasUsuario;
    opens view.dashboardUser.screenComprasUsuario to javafx.fxml;
    exports view.dashboardUser;
    opens view.dashboardUser to javafx.fxml;

}