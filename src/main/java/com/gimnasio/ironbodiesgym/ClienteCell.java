package com.gimnasio.ironbodiesgym;

import javafx.animation.ScaleTransition;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

public class ClienteCell extends ListCell<ClaseClientes> {

    private FXMLLoader fxmlLoader;
    private GridPane fondoItem;
    private Label labelNombre, labelCorreo, labelPlan, labelFecha;
    private Button botonEditar, botonEliminar, botonSuscripcion, botonBloqueo;
    private ImageView imagenBloqueo;
    private EventHandler<ActionEvent> onItemSelected;

    ControladorBD controladorBD = new ControladorBD();
    ControladorTransiciones transiciones = new ControladorTransiciones();

    int BLOQUEAR = 1, DESBLOQUEAR = 2, ELIMINAR = 3;

    public EventHandler<ActionEvent> getOnItemSelected() {
        return onItemSelected;
    }

    public void setOnItemSelected(EventHandler<ActionEvent> onItemSelected) {
        this.onItemSelected = onItemSelected;
    }

    public ClienteCell() {
        super();
        fxmlLoader = new FXMLLoader(getClass().getResource("ListViewItem.fxml"));
        try {
            fondoItem = fxmlLoader.load();
            labelNombre = (Label) fxmlLoader.getNamespace().get("LabelNombre");
            labelCorreo = (Label) fxmlLoader.getNamespace().get("LabelCorreo");
            labelPlan = (Label) fxmlLoader.getNamespace().get("LabelPlan");
            labelFecha = (Label) fxmlLoader.getNamespace().get("LabelFecha");
            botonEditar = (Button) fxmlLoader.getNamespace().get("BotonEditar");
            botonEliminar = (Button) fxmlLoader.getNamespace().get("BotonEliminar");
            botonSuscripcion = (Button) fxmlLoader.getNamespace().get("BotonSuscripcion");
            botonBloqueo = (Button) fxmlLoader.getNamespace().get("BotonBloqueo");
            imagenBloqueo = (ImageView) fxmlLoader.getNamespace().get("ImagenBotonBloqueo");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected void updateItem(ClaseClientes cliente, boolean empty) {
        super.updateItem(cliente, empty);

        if (empty || cliente == null) {
            setGraphic(null);
        } else {
            labelNombre.setText(cliente.getNombre());
            labelCorreo.setText(cliente.getCorreo());
            labelPlan.setText("Suscripción: " + cliente.getTipo_suscripcion());
            if (cliente.getFecha_termino() == null) {
                labelFecha.setVisible(false);
            } else {
                labelFecha.setText("Vence: " + String.valueOf(cliente.getFecha_termino()));
                botonSuscripcion.setVisible(false);
            }

            if (cliente.bloqueado) {
                imagenBloqueo.setImage(new Image(Objects.requireNonNull(ClienteCell.class.getResourceAsStream("/assets/ic_unlock.png"))));
            } else {
                imagenBloqueo.setImage(new Image(Objects.requireNonNull(ClienteCell.class.getResourceAsStream("/assets/ic_lock.png"))));
            }
            setGraphic(fondoItem);
        }

        botonEditar.setOnAction(event -> {
            if (getOnItemSelected() != null) {
                ActionEvent actionEvent = new ActionEvent(cliente, null);
                getOnItemSelected().handle(actionEvent);
            }
        });

        botonBloqueo.setOnAction(actionEvent -> {
            if (!cliente.bloqueado) {
                AlertaPersonalizada("Confirmar bloqueo", "¿Deseas bloquear a " + cliente.getNombre()
                        + " con correo " + cliente.getCorreo() + "?", BLOQUEAR, cliente.id_usuario);
                transiciones.CrearAnimacionFade(500, getParent(), View.MENU_ADMINISTRADOR);
            } else {
                AlertaPersonalizada("Confirmar desbloqueo", "¿Deseas desbloquear a " + cliente.getNombre()
                        + " con correo " + cliente.getCorreo() + "?", DESBLOQUEAR, cliente.id_usuario);
                transiciones.CrearAnimacionFade(500, getParent(), View.MENU_ADMINISTRADOR);
            }
        });

        botonEliminar.setOnAction(actionEvent -> {
            AlertaPersonalizada("Eliminación", "¿Estás seguro de eliminar a " + cliente.getNombre()
                    + " con correo " + cliente.getCorreo() + "?", ELIMINAR, cliente.id_usuario);
            transiciones.CrearAnimacionFade(500, getParent(), View.MENU_ADMINISTRADOR);
        });

        botonSuscripcion.setOnAction(actionEvent -> {
            ControladorRenovarSuscripciones.ADMINISTRADOR = true;
            ControladorRenovarSuscripciones.Correo = cliente.getCorreo();
            transiciones.CrearAnimacionFade(500, getParent(), View.RENOVAR_SUSCRIPCIONES);
        });

        setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY && !empty) {
                assert cliente != null;
                ControladorConsultaUsuario.correo = cliente.getCorreo();
                Stage stage = (Stage) fondoItem.getScene().getWindow();
                mostrarVentanaModal(stage, cliente.getNombre());
            }
        });
    }

    private void mostrarVentanaModal(Stage ownerStage, String nombre) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("VistaConsultaUsuario.fxml"));
            Parent root = fxmlLoader.load();

            Stage modalStage = new Stage();
            modalStage.initOwner(ownerStage);
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Información de " + nombre);
            modalStage.setResizable(false);
            modalStage.getIcons().add(new Image(Objects.requireNonNull(ClienteCell.class.getResourceAsStream("/assets/IconGym.png"))));
            Scene modalScene = new Scene(root);
            if (IndexApp.Tema == 0){
                modalScene.getStylesheets().clear();
                modalScene.getStylesheets().add(ClienteCell.class.getResource("/css/ModoClaro.css").toExternalForm());
            }else if (IndexApp.Tema == 1){
                modalScene.getStylesheets().clear();
                modalScene.getStylesheets().add(ClienteCell.class.getResource("/css/ModoOscuro.css").toExternalForm());
            }
            modalStage.setScene(modalScene);

            // Crear la animación de escala para iniciar el modal
            ScaleTransition scaleIn = new ScaleTransition(Duration.seconds(0.3), root);
            scaleIn.setFromX(0);
            scaleIn.setFromY(0);
            scaleIn.setToX(1);
            scaleIn.setToY(1);
            // Crear la animación de escala para cerrar el modal
            ScaleTransition scaleOut = new ScaleTransition(Duration.seconds(0.3), root);
            scaleOut.setFromX(1);
            scaleOut.setFromY(1);
            scaleOut.setToX(0);
            scaleOut.setToY(0);
            scaleOut.setOnFinished(e -> modalStage.close());

            modalStage.setOnShowing(e -> scaleIn.play());
            modalStage.setOnCloseRequest(e -> {
                e.consume();
                scaleOut.play();
            });

            modalStage.showAndWait();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void AlertaPersonalizada(String titulo, String contenido, int operacion, int id_usuario) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        DialogPane dialogPane = alert.getDialogPane();
        Stage stage = (Stage) dialogPane.getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(ClienteCell.class.getResourceAsStream("/assets/IconGym.png"))));
        Label content = new Label(alert.getContentText());
        alert.setHeaderText(null);
        alert.setTitle(titulo);
        content.setText(contenido);
        Button button = (Button) alert.getDialogPane().lookupButton(ButtonType.OK);
        if (IndexApp.Tema == 0) {
            dialogPane.setStyle("-fx-background-color: white;");
            content.setTextFill(Color.BLACK);
            alert.getDialogPane().setContent(content);
            button.setStyle("-fx-background-color: gray; -fx-text-fill: black; -fx-border-color: black");
        } else {
            dialogPane.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white");
            content.setTextFill(Color.WHITESMOKE);
            alert.getDialogPane().setContent(content);
            button.setStyle("-fx-background-color: #2b2b2b; -fx-text-fill: white; -fx-border-color: white;");
        }

        switch (operacion) {
            case 1 -> {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    Task<Boolean> bloquear_usuario = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            return controladorBD.bloquearUsuario(id_usuario);
                        }
                    };
                    new Thread(bloquear_usuario).start();
                    bloquear_usuario.setOnSucceeded(event -> {
                        alert.close();
                    });
                } else {
                    alert.close();
                }
            }

            case 2 -> {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    Task<Boolean> desbloquear_usuario = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            return controladorBD.desbloquearUsuario(id_usuario);
                        }
                    };
                    new Thread(desbloquear_usuario).start();
                    desbloquear_usuario.setOnSucceeded(event -> {
                        alert.close();
                    });
                } else {
                    alert.close();
                }
            }

            case 3 -> {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {

                    Task<Boolean> eliminar_usuario = new Task<Boolean>() {
                        @Override
                        protected Boolean call() throws Exception {
                            return controladorBD.eliminarUsuario(id_usuario);
                        }
                    };
                    new Thread(eliminar_usuario).start();
                    eliminar_usuario.setOnSucceeded(event -> {
                        alert.close();
                    });
                } else {
                    alert.close();
                }
            }
        }

    }
}
