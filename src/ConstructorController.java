import graph.Graph;
import graph.Node;
import javafx.animation.AnimationTimer;
import javafx.animation.Interpolator;
import javafx.animation.PathTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.shape.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import modeling.Car;
import modeling.Distribution;
import parking.Parking;
import parking.Verificator;
import parking.VerificatorError;
import parking.template.InfoTable;
import parking.template.Template;

import java.io.*;

import java.util.ArrayList;

public class ConstructorController {

    private Stage stage;
    private Template template;
    private Parking constructorParking;
    private Parking modelingParking;
    private int size;

    private GraphicsContext graphicsContextConstructor;
    private GraphicsContext graphicsContextModeling;

    private void clearConstructorContext() {
        graphicsContextConstructor.clearRect(0, 0, graphicsContextConstructor.getCanvas().getWidth(), graphicsContextConstructor.getCanvas().getHeight());
    }

    private void clearModelingContext() {
        graphicsContextModeling.clearRect(0, 0, graphicsContextModeling.getCanvas().getWidth(), graphicsContextModeling.getCanvas().getHeight());
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    Pane constructor;
    @FXML
    Pane modeling;
    @FXML
    TabPane tabPane;

    @FXML
    public void initialize() {
        //Инициализация вкладки конструирования
        template = Template.Null;
        size = 50;
        Canvas canvas = new Canvas(815, 575);
        graphicsContextConstructor = canvas.getGraphicsContext2D();
        constructorParking = new Parking(4, 4, graphicsContextConstructor, size);
        canvas.setOnMouseClicked(event -> {
                    if (template != Template.Null) {
                        int x = (int) event.getX();
                        int y = (int) event.getY();
                        if (constructorParking.isInParking(x, y)) {
                            constructorParking.createFunctionalBlock(x, y, template);
                            constructorParking.drawFunctionalBlock(x, y);
                        }
                    }
                }
        );
        constructor.getChildren().add(canvas);
        constructorParking.drawBackground();
        constructorParking.drawMarkup();
        constructorParking.drawHighway();
        constructorParking.drawFunctionalBlocks();
        //Инициализация вкладки моделирования
        canvas = new Canvas(900, 575);
        graphicsContextModeling = canvas.getGraphicsContext2D();
        modeling.getChildren().add(canvas);
    }

    @FXML
    public void onChooseCashBox() {
        template = Template.CashBox;
    }

    @FXML
    public void onChooseInfoTable() {
        template = Template.InfoTable;
    }

    @FXML
    public void onChooseDeparture() {
        template = Template.Departure;
    }

    @FXML
    public void onChooseEntry() {
        template = Template.Entry;
    }

    @FXML
    public void onChooseParkingPlace() {
        template = Template.ParkingPlace;
    }

    @FXML
    public void onChooseLawn() {
        template = Template.Lawn;
    }

    @FXML
    public void onChooseRoad() {
        template = Template.Road;
    }

    //Обработчики для вкладки конструирования
    @FXML
    public void onConstructorSettingsClick() {
        try {
            // Загружаем fxml-файл и создаём новую сцену
            // для всплывающего диалогового окна.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("constructor_settings.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            // Создаём диалоговое окно Stage.
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Размер парковки");
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);
            // Передаём адресата в контроллер.
            ConstructorSettingsController controller = loader.getController();
            controller.setInitialWidth(constructorParking.getFunctionalBlockH());
            controller.setInitialHeight(constructorParking.getFunctionalBlockV());
            controller.setDialogStage(dialogStage);
            // Отображаем диалоговое окно и ждём, пока пользователь его не закроет
            dialogStage.showAndWait();

            if (controller.isSubmitClicked()) {
                clearConstructorContext();
                constructorParking = new Parking(controller.getSelectedWidth(), controller.getSelectedHeight(), graphicsContextConstructor, size, constructorParking);
                constructorParking.drawMarkup();
                constructorParking.drawHighway();
                constructorParking.drawFunctionalBlocks();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onConstructorLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки", "*.top"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream in = new FileInputStream(file.getPath())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                constructorParking = (Parking) objectInputStream.readObject();
                constructorParking.setGraphicsContext(graphicsContextConstructor);
                for (int i = 0; i < constructorParking.getFunctionalBlockH(); i++) {
                    for (int j = 0; j < constructorParking.getFunctionalBlockV(); j++) {
                        if (constructorParking.getFunctionalBlock(i, j) != null) {
                            constructorParking.getFunctionalBlock(i, j).setGraphicsContext(graphicsContextConstructor);
                        }
                    }
                }
                clearConstructorContext();
                //constructorParking.drawBackground();
                constructorParking.drawHighway();
                constructorParking.drawMarkup();
                constructorParking.drawFunctionalBlocks();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void onSave() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранение топологию:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки", "*.top"));
        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try (FileOutputStream out = new FileOutputStream(file.getPath())) {
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(out);
                objectOutputStream.writeObject(constructorParking);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getErrorMessage(Parking parking) {
        ArrayList<VerificatorError> errorList = Verificator.checkAll(parking);
        if (errorList.size() == 0)
            return null;
        else {
            StringBuilder message = new StringBuilder();
            if (errorList.contains(VerificatorError.hasNoCashBox)) {
                message.append("\n - На парковке должна быть касса.");
            }
            if (errorList.contains(VerificatorError.hasNoInfoTable)) {
                message.append("\n - На парковке должно быть информационное табло.");
            }
            if (errorList.contains(VerificatorError.IncorrectEntryDeparturePlacement)) {
                message.append("\n - Въезд и выезд должны прилегать к шоссе. Выезд должен находиться дальше по ходу движения автомобилей по шоссе.");
            }
            if (errorList.contains(VerificatorError.UnrelatedGraph)) {
                message.append("\n - Требуется наличие пути от въезда до всех парковочных мест и дорог.");
            }
            return message.toString();
        }
    }

    @FXML
    public void onCheck() {
        String message = getErrorMessage(constructorParking);
        if (message == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология соответствует правилам организации парковки!");
            alert.showAndWait();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации парковки: " + message);
            alert.showAndWait();
        }
    }

    @FXML
    public void onGoToModeling() {
        String message = getErrorMessage(constructorParking);
        if (message == null) {
            modelingParking = new Parking(constructorParking.getFunctionalBlockH(), constructorParking.getFunctionalBlockV(), graphicsContextModeling, size, constructorParking);
            clearModelingContext();
            modelingParking.drawBackground();
            modelingParking.drawFunctionalBlocksInModeling();
            modelingParking.drawHighwayInModeling();
            SingleSelectionModel<Tab> singleSelectionModel = tabPane.getSelectionModel();
            singleSelectionModel.select(1);
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("");
            alert.setHeaderText("");
            alert.setContentText("Топология не соответствует правилам организации парковки: " + message);
            alert.showAndWait();
        }

    }

    //Обработчики для вкладки моделирования

    @FXML
    public void onModelingLoad() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузка топологии:");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Файл топологии парковки", "*.top"));
        File file = fileChooser.showOpenDialog(stage);
        if (file != null) {
            try (FileInputStream in = new FileInputStream(file.getPath())) {
                ObjectInputStream objectInputStream = new ObjectInputStream(in);
                Parking parking = (Parking) objectInputStream.readObject();
                modelingParking = new Parking(parking.getFunctionalBlockH(), parking.getFunctionalBlockV(), graphicsContextModeling, size, parking);
                for (int i = 0; i < modelingParking.getFunctionalBlockH(); i++) {
                    for (int j = 0; j < modelingParking.getFunctionalBlockV(); j++) {
                        if (modelingParking.getFunctionalBlock(i, j) != null) {
                            modelingParking.getFunctionalBlock(i, j).setGraphicsContext(graphicsContextModeling);
                        }
                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            String message = getErrorMessage(modelingParking);
            if (message == null) {
                clearModelingContext();
                modelingParking.drawHighwayInModeling();
                modelingParking.drawFunctionalBlocksInModeling();

            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("");
                alert.setHeaderText("");
                alert.setContentText("Топология сконструирована неверно, попробуйте изменить топологию в конструировании.");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void onStartModeling() {
        AnimationTimer animationTimer = new AnimationTimer() {
            private long lastHighwayReverse = 0;
            private long lastHighway = 7_000_000_00L;
            private Graph graph = new Graph(modelingParking);

            {
                graph.fillFreeParkingPlaces();
                modelingParking.drawInfoTableInModeling(graph.getFreeParkingPlaces().size());
            }

            ArrayList<Car> cars = new ArrayList<>();

            @Override
            public void handle(long now) {
                if (now - lastHighwayReverse > 12_000_000_00L) {
                    Car car = new Car(graphicsContextModeling.getCanvas().getWidth() + 50, modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 75);
                    cars.add(car);
                    modeling.getChildren().add(car);


                    Path path = new Path();
                    path.getElements().add(new MoveTo(car.getX(),
                            car.getY()));
                    path.getElements().add(new LineTo(-50, modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 75));

                    PathTransition pathTransition = new PathTransition();
                    pathTransition.setDuration(Duration.millis(8000));
                    pathTransition.setNode(car);
                    pathTransition.setPath(path);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setInterpolator(Interpolator.LINEAR);
                    pathTransition.setOnFinished((event -> {
                        PathTransition pathTransition1 = (PathTransition) event.getSource();
                        Car car_car = (Car) pathTransition1.getNode();
                        cars.remove(car_car);
                        modeling.getChildren().remove(car_car);
                    }));
                    pathTransition.play();

                    System.out.println(cars.size());
                    System.out.println(modeling.getChildren().size());
                    lastHighwayReverse = now;
                }
                if (now - lastHighway > 14_000_000_00L) {
                    Car car = new Car(-50, modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25);
                    cars.add(car);
                    modeling.getChildren().add(car);

                    Path path = new Path();
                    path.getElements().add(new MoveTo(car.getX(),
                            car.getY()));
                    PathTransition pathTransition = new PathTransition();

                    //Путь от начала шоссе до въезда
                    path.getElements().add(new LineTo(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() - 25,
                            modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25));

                    pathTransition.setOnFinished(event -> {
                        PathTransition pathTransitionToEntry = (PathTransition)event.getSource();
                        Car car2 = (Car)pathTransitionToEntry.getNode();
                        PathTransition pathNextTransition = new PathTransition();
                        Path pathNext = new Path();
                        pathNext.getElements().add(new MoveTo(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() - 25,
                                modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25));

                        if (graph.hasFreeParkingPlaces()) {


                            //Поворот на парковку

                            CubicCurveTo cubicTo = new CubicCurveTo();
                            cubicTo.setControlX1(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN());
                            cubicTo.setControlY1(modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25);
                            cubicTo.setControlX2(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setControlY2(modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 15);
                            cubicTo.setX(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25);
                            cubicTo.setY(modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size - 25);
                            pathNext.getElements().add(cubicTo);
                            /*pathNext.getElements().add(new LineTo(graph.getEntry().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                    modelingParking.getVERTICAL_MARGIN() + (modelingParking.getFunctionalBlockV()-1) * size + 25));*/


                            // Путь от въезда до парковочного места

                            car2.setParkingPlace(graph.getFreeParkingPlace());
                            modelingParking.drawInfoTableInModeling(graph.getFreeParkingPlaces().size());
                            for (Node step : car2.getPathToEntry()
                                    ) {
                                pathNext.getElements().add(new LineTo(step.getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                        step.getJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));
                            }

                            //Анимация по приезде на парковочное место
                            pathNextTransition.setOnFinished((event1) -> {
                                PathTransition pathTransitionToParkingPlace = (PathTransition) event1.getSource();
                                PathTransition pathTransition1 = new PathTransition();
                                Car car_car = (Car) pathTransitionToParkingPlace.getNode();
                                Path emptyPath = new Path();
                                emptyPath.getElements().add(new MoveTo(car_car.getParkingPlace().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getParkingPlace().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));
                                emptyPath.getElements().add(new LineTo(car_car.getParkingPlace().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                        car_car.getParkingPlace().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));
                                pathTransition1.setNode(car_car);
                                pathTransition1.setPath(emptyPath);
                                pathTransition1.setInterpolator(Interpolator.LINEAR);
                                pathTransition1.setDuration(Duration.millis(0.1));
                                pathTransition1.setDelay(Duration.millis(Distribution.getUniformDistribution(20000, 100000)));

                                pathTransition1.setOnFinished(event2 -> {
                                    PathTransition delay = (PathTransition) event2.getSource();
                                    PathTransition pathTransitionFromParkingPlace = new PathTransition();
                                    Car car_car_car = (Car) delay.getNode();
                                    Path pathToDeparture = new Path();

                                    pathToDeparture.getElements().add(new MoveTo(car_car_car.getParkingPlace().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                            car_car_car.getParkingPlace().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));

                                    //Путь от парковочного места до выезда

                                    for (Node step : car_car_car.getPathToDeparture()
                                            ) {
                                        pathToDeparture.getElements().add(new LineTo(step.getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25,
                                                step.getJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));
                                    }

                                    pathTransitionFromParkingPlace.setOnFinished(event3 -> {
                                        graph.freeParkingPlace(car_car_car.getParkingPlace());
                                        modelingParking.drawInfoTableInModeling(graph.getFreeParkingPlaces().size());
                                        PathTransition turn = (PathTransition) event3.getSource();
                                        Car car1 = (Car) turn.getNode();
                                        PathTransition pathTransitionToEnd = new PathTransition();
                                        Path pathToEnd = new Path();
                                        //Поворот на шоссе

                                        pathToEnd.getElements().add(new MoveTo(modelingParking.getDepartureI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25, modelingParking.getDepartureJ() * size + modelingParking.getVERTICAL_MARGIN() + 25));
                                        /*CubicCurveTo cubicTo1 = new CubicCurveTo();
                                        cubicTo1.setControlX1(graph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25);
                                        cubicTo1.setControlY1(graph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 65);
                                        cubicTo1.setControlX2(graph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 35);
                                        cubicTo1.setControlY2(graph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);
                                        cubicTo1.setX(graph.getDeparture().getI() * size + modelingParking.getHORIZONTAL_MARGIN() + 75);
                                        cubicTo1.setY(graph.getDeparture().getJ() * size + modelingParking.getVERTICAL_MARGIN() + 75);*/
                                        pathToEnd.getElements().add(new LineTo(modelingParking.getDepartureI() * size + modelingParking.getHORIZONTAL_MARGIN() + 25, (modelingParking.getDepartureJ()+1) * size + modelingParking.getVERTICAL_MARGIN() + 25));

                                        //Путь до конца

                                        pathToEnd.getElements().add(new LineTo(modeling.getWidth() + 50, modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25));

                                        pathTransitionToEnd.setDuration(Duration.millis((modelingParking.getHORIZONTAL_MARGIN() / size * 500) + (modelingParking.getFunctionalBlockH() - graph.getDeparture().getI()) * 500 + 500));
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setNode(car1);
                                        pathTransitionToEnd.setPath(pathToEnd);
                                        pathTransitionToEnd.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                        pathTransitionToEnd.setInterpolator(Interpolator.LINEAR);
                                        pathTransitionToEnd.setOnFinished(event4 -> {
                                            PathTransition pathTransition2 = (PathTransition) event4.getSource();
                                            Car car_car_car_car = (Car) pathTransition2.getNode();
                                            cars.remove(car_car_car_car);
                                            modeling.getChildren().remove(car_car_car_car);
                                        });
                                        pathTransitionToEnd.play();
                                    });

                                    pathTransitionFromParkingPlace.setDuration(Duration.millis(car_car_car.getPathToDeparture().size() * 500));
                                    pathTransitionFromParkingPlace.setPath(pathToDeparture);
                                    pathTransitionFromParkingPlace.setNode(car_car_car);
                                    pathTransitionFromParkingPlace.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                                    pathTransitionFromParkingPlace.setInterpolator(Interpolator.LINEAR);
                                    pathTransitionFromParkingPlace.play();
                                });

                                pathTransition1.play();
                            });
                            pathNextTransition.setDuration(Duration.millis(car2.getPathToEntry().size()*500+500));
                        } else {
                            pathNext.getElements().add(new LineTo(modeling.getWidth() + 50, modelingParking.getVERTICAL_MARGIN() + modelingParking.getFunctionalBlockV() * size + 25));
                            pathNextTransition.setOnFinished((event5 -> {
                                PathTransition pathTransition1 = (PathTransition) event5.getSource();
                                Car car_car = (Car) pathTransition1.getNode();
                                cars.remove(car_car);
                                modeling.getChildren().remove(car_car);
                            }));
                            pathNextTransition.setDuration(Duration.millis(modelingParking.getHORIZONTAL_MARGIN()/size*500+(modelingParking.getFunctionalBlockH()+1-modelingParking.getEntryI())*500));
                        }
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setNode(car2);
                        pathNextTransition.setPath(pathNext);
                        pathNextTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                        pathNextTransition.setInterpolator(Interpolator.LINEAR);
                        pathNextTransition.play();
                    });


                    pathTransition.setDuration(Duration.millis(graph.getEntry().getI() * 500
                            + (modelingParking.getHORIZONTAL_MARGIN() / size * 500)));
                    pathTransition.setNode(car);
                    pathTransition.setPath(path);
                    pathTransition.setOrientation(PathTransition.OrientationType.ORTHOGONAL_TO_TANGENT);
                    pathTransition.setInterpolator(Interpolator.LINEAR);
                    pathTransition.play();
                    lastHighway = now;
                }
            }
        };
        animationTimer.start();
    }
}
