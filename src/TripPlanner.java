import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

public class TripPlanner extends Application {
    private Stage primaryStage;
    private Scene scene;
    private Button loadFile;
    private Button saveFile;
    private Button newFile;
    private Button plus_trip;
    private Button minus_trip;
    private Button plus_stop;
    private Button minus_stop;
    private Button update;
    private String layout;
    private Label tripStops;
    private Label city;
    private Label state;
    private Label latitude_deg;
    private Label latitude_min;
    private Label longitude_deg;
    private Label longitude_min;
    private Label possiblestops;
    private Label totalMilage;
    private static TextField tf_city;
    private static TextField tf_state;
    private static TextField tf_latdeg;
    private static TextField tf_latmin;
    private static TextField tf_longdeg;
    private static TextField tf_longmin;
    private static TextField displaymialge;
    private BorderPane b_pane = new BorderPane();
    private BorderPane bot = new BorderPane();
    private BorderPane bot_center = new BorderPane();
    private BorderPane border_Right = new BorderPane();
    private StackPane mapPane;
    private Pane emptyPane = new BorderPane();
    private GridPane gp_bot = new GridPane();
    private HBox mf_top = new HBox();
    private HBox hbox_right = new HBox();
    private HBox hbox_bot = new HBox();
    private HBox hbox_bot_top = new HBox();
    private HBox hbox_right_bot = new HBox();
    private static ArrayList<City> theList = new ArrayList<>();
    private static ArrayList<City> tripPlanner = new ArrayList<>();
    private static ObservableList<City> possible_stops;
    private static ObservableList<City> trip_stops;
    private ListView<City> list_right;
    private ListView<City> list_bot;
    private static boolean isCityValid = false;
    private static boolean isStateValid = false;
    private static boolean isLatDegValid = false;
    private static boolean isLatMinValid = false;
    private static boolean isLongDegValid = false;
    private static boolean isLongMinValid = false;
    private static int updateDecider = 0;
    private Alert delete = new Alert(Alert.AlertType.CONFIRMATION);
    private TextInputDialog newTrip = new TextInputDialog("");
    private static int clickcount = 0;
    private static double totaldistance;
    private static double distance;
    private static double lat1;
    private static double lat2;
    private static double long1;
    private static double long2;
    private static ImageView imageView;
    private static GraphicsContext gc;
    private static Canvas canvas;

    @Override
    public void start(Stage initprimaryStage) {

        primaryStage = initprimaryStage;
        initProgram();
        layoutGUI();
        initHandlers();
    }
    public static void main(String[] args) {
        launch(args);
    }

    //  LAYOUT
    private void layoutGUI() {
        //  Arc properties
        layout = "-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #53b8ff;";

        //  Buttons & Labels & TextFields
        mapPane = new StackPane();
        canvas = new Canvas(650, 339.726567821);
        gc = canvas.getGraphicsContext2D();
        newFile = new Button("New");
        saveFile = new Button("Save");
        loadFile = new Button("Load");
        plus_trip = new Button("+");
        minus_trip = new Button("-");
        plus_stop = new Button("+");
        minus_stop = new Button("-");
        update = new Button("Update");
        newFile.setMinWidth(100);
        saveFile.setMinWidth(100);
        loadFile.setMinWidth(100);
        tripStops = new Label("Trip Stops");
        city = new Label("City:");
        state = new Label("State:");
        latitude_min = new Label("Latitude Minutes:");
        latitude_deg = new Label("Latitude Degrees:");
        longitude_deg = new Label("Longitude Degrees:");
        longitude_min = new Label("Longitude Minutes:");
        possiblestops = new Label("Possible Stops");
        totalMilage = new Label("Total : ");
        tf_city = new TextField();
        tf_latdeg = new TextField();
        tf_latmin = new TextField();
        tf_longdeg = new TextField();
        tf_longmin = new TextField();
        tf_state = new TextField();
        displaymialge = new TextField();

        //  ListView
        possible_stops = FXCollections.observableArrayList(theList);
        trip_stops = FXCollections.observableArrayList(tripPlanner);
        list_right = new ListView<>(trip_stops);
        list_bot = new ListView<>(possible_stops);
        list_right.setItems(trip_stops);
        list_bot.setItems(possible_stops);
        //  Alert


        //  Map
        Image img = new Image(getClass().getResourceAsStream("usa_map.jpg"));
        imageView = new ImageView(img);
        imageView.setOpacity(0.6);
        imageView.setFitWidth(650);
        imageView.setPreserveRatio(true);


        //

        hbox_bot_top.getChildren().add(possiblestops);
        hbox_bot_top.getChildren().add(plus_stop);
        hbox_bot_top.getChildren().add(minus_stop);
        plus_stop.setMaxWidth(30);
        minus_stop.setMaxWidth(30);
        plus_stop.setMinHeight(8);
        minus_stop.setMinHeight(8);
        hbox_bot_top.setAlignment(Pos.CENTER_LEFT);
        hbox_bot_top.setSpacing(8);
        bot_center.setTop(hbox_bot_top);
        bot_center.setRight(emptyPane);
        bot_center.setCenter(hbox_bot);
        bot_center.setLeft(list_bot);
        border_Right.setBottom(bot);
        border_Right.setTop(hbox_right);
        hbox_bot.getChildren().add(gp_bot);
        hbox_right.getChildren().add(tripStops);
        hbox_right.getChildren().add(plus_trip);
        hbox_right.getChildren().add(minus_trip);
        hbox_right_bot.getChildren().add(totalMilage);
        hbox_right_bot.getChildren().add(displaymialge);
        gp_bot.add(city, 0, 0);
        gp_bot.add(tf_city, 1, 0);
        gp_bot.add(state, 0, 1);
        gp_bot.add(tf_state, 1, 1);
        gp_bot.add(latitude_deg, 0, 2);
        gp_bot.add(tf_latdeg, 1, 2);
        gp_bot.add(latitude_min, 0, 3);
        gp_bot.add(tf_latmin, 1, 3);
        gp_bot.add(longitude_deg, 0, 4);
        gp_bot.add(tf_longdeg, 1, 4);
        gp_bot.add(longitude_min, 0, 5);
        gp_bot.add(tf_longmin, 1, 5);
        gp_bot.add(update, 0, 6);
        border_Right.setCenter(list_right);
        border_Right.setBottom(hbox_right_bot);
        b_pane.setTop(mf_top);
        b_pane.setLeft(mapPane);
        b_pane.setCenter(border_Right);
        b_pane.setBottom(bot);
        mapPane.getChildren().addAll(canvas,imageView);

        mf_top.setSpacing(20);
        mf_top.setStyle(layout);
        hbox_bot.setStyle(layout);
        border_Right.setStyle(layout);
        bot.setStyle(layout);
        hbox_right_bot.setAlignment(Pos.CENTER);
        displaymialge.setDisable(true);
        displaymialge.setStyle( "-fx-border-color: grey;" +
        "-fx-border-width: 0 0 1 0;" // top, right, bottom, left
        + "-fx-background-color: transparent;");
        displaymialge.setMinSize(10, 10);
        mf_top.getChildren().add(newFile);
        mf_top.getChildren().add(saveFile);
        mf_top.getChildren().add(loadFile);
        bot.setCenter(bot_center);
        scene = new Scene(b_pane, 900, 865);
        primaryStage.setTitle("Trip Planner");
        primaryStage.setScene(scene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    //  EVENT HANDLER
    private void initHandlers() {
        //  When Possible Stops item selected
        list_bot.setOnMouseClicked(event -> {
            updateDecider = 1;
            tf_city.setText(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getName());
            tf_state.setText(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getState());
            tf_latdeg.setText(Integer.toString(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getLatDeg()));
            tf_latmin.setText(Integer.toString(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getLatMin()));
            tf_longdeg.setText(Integer.toString(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getLongDeg()));
            tf_longmin.setText(Integer.toString(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).getLongMin()));
            update.setDisable(true);
            tf_latdeg.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_latdeg.getText()) > -90 && Integer.parseInt(tf_latdeg.getText()) < 90) {
                        isLatDegValid = true;
                    } else {
                        update.setDisable(true);
                        isLatDegValid = false;
                    }
                    if (isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLatDegValid = false;
                }

            });
            tf_latmin.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_latmin.getText()) > 0 || Integer.parseInt(tf_latmin.getText()) < 60) {

                        isLatMinValid = true;
                    } else {
                        update.setDisable(true);
                        isLatMinValid = false;
                    }
                    if (isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLatMinValid = true;
                }
            });
            tf_longdeg.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_longdeg.getText()) > -180 || Integer.parseInt(tf_longdeg.getText()) < 180) {
                        isLongDegValid = true;
                    } else {
                        update.setDisable(true);
                        isLongDegValid = false;
                    }
                    if (isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLongDegValid = false;
                }

            });
            tf_longmin.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_longmin.getText()) > 0 || Integer.parseInt(tf_longmin.getText()) < 60) {
                        isLongMinValid = true;
                    } else {
                        update.setDisable(true);
                    }
                    if (isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    isLongMinValid = false;
                    update.setDisable(true);
                }
            });
        });
        list_right.setOnMouseClicked(event -> {
            clickcount++;
            if (clickcount == 2) {
                list_right.getSelectionModel().clearSelection();
                clickcount = 0;
            }
        });
        //  When Possible Stops +- buttons clicked
        plus_stop.setOnMouseClicked(event -> {
            updateDecider = 0;
            setTextFieldEmpty();
            update.setDisable(true);
            tf_city.textProperty().addListener(((observable, oldValue, newValue) -> {
                if (contains(possible_stops, tf_city.getText())) {
                    isCityValid = false;
                } else {
                    isCityValid = true;
                }

                if (isCityValid && isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                    update.setDisable(false);
                } else {
                    update.setDisable(true);
                }
            }));
            tf_latdeg.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_latdeg.getText()) > -90 && Integer.parseInt(tf_latdeg.getText()) < 90) {
                        isLatDegValid = true;
                    } else {
                        update.setDisable(true);
                        isLatDegValid = false;
                    }
                    if (isCityValid && isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLatDegValid = false;
                }

            });
            tf_latmin.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_latmin.getText()) > 0 || Integer.parseInt(tf_latmin.getText()) < 60) {

                        isLatMinValid = true;
                    } else {
                        update.setDisable(true);
                        isLatMinValid = false;
                    }
                    if (isCityValid && isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLatMinValid = true;
                }
            });
            tf_longdeg.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_longdeg.getText()) > -180 || Integer.parseInt(tf_longdeg.getText()) < 180) {
                        isLongDegValid = true;
                    } else {
                        update.setDisable(true);
                        isLongDegValid = false;
                    }
                    if (isCityValid && isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    update.setDisable(true);
                    isLongDegValid = false;
                }

            });
            tf_longmin.textProperty().addListener((observable, oldValue, newValue) -> {
                try {
                    if (Integer.parseInt(tf_longmin.getText()) > 0 || Integer.parseInt(tf_longmin.getText()) < 60) {
                        isLongMinValid = true;
                    } else {
                        update.setDisable(true);
                    }
                    if (isCityValid && isLongMinValid && isLongDegValid && isLatMinValid && isLatDegValid) {
                        update.setDisable(false);
                    }
                } catch (NumberFormatException nfe) {
                    isLongMinValid = false;
                    update.setDisable(true);
                }
            });
        });
        minus_stop.setOnMouseClicked(event -> {
            delete.setTitle("Warning!");
            delete.setHeaderText("Do you want to delete selected stop?");
            delete.setContentText("Choose your Option");
            ButtonType buttonTypeOne = new ButtonType("Yes");
            ButtonType buttonTypeTwo = new ButtonType("No");
            delete.getButtonTypes().setAll(buttonTypeOne, buttonTypeTwo);
            Optional<ButtonType> result = delete.showAndWait();
            if (result.get() == buttonTypeOne) {
                possible_stops.remove(possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()));
                Collections.sort(possible_stops);
                writeArrayListToFile(possible_stops, "ListOfPossibleStops.txt");
            }
        });
        //  When Update Button clicked
        update.setOnMouseClicked(event -> {
            if (updateDecider == 0) {
                possible_stops.add(new City(tf_city.getText(), tf_state.getText(), Integer.parseInt(tf_latdeg.getText()),
                        Integer.parseInt(tf_latmin.getText()), Integer.parseInt(tf_longdeg.getText()),
                        Integer.parseInt(tf_longmin.getText())));
            } else if (updateDecider == 1) {
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setName(tf_city.getText());
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setState(tf_state.getText());
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setLatDeg(Integer.parseInt(tf_latdeg.getText()));
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setLatMin(Integer.parseInt(tf_latmin.getText()));
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setLongDeg(Integer.parseInt(tf_longdeg.getText()));
                possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()).setLongMin(Integer.parseInt(tf_longmin.getText()));
            }
            Collections.sort(possible_stops);
            writeArrayListToFile(possible_stops, "ListOfPossibleStops.txt");
        });
        // When New Button Clicked
        newFile.setOnMouseClicked(event -> {
            trip_stops.removeAll();
            newTrip.setTitle("New Trip Plan");
            newTrip.setHeaderText("Enter the name of the Plan!");
            newTrip.setContentText("Name of the Plan: ");
            Optional<String> tripName = newTrip.showAndWait();
            primaryStage.setTitle("Trip Planner" + " - " + tripName.get());
        });
        //  When Save Button Clicked
        saveFile.setOnMouseClicked(event -> {
            FileChooser savefilechooser = new FileChooser();
            savefilechooser.setTitle("Saving Current Trip");
            savefilechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin Files", "*.bin"));
            File selectedFile = savefilechooser.showSaveDialog(primaryStage);
            DataOutputStream dataOut;
            try {
                FileOutputStream fileOut = new FileOutputStream(selectedFile);
                dataOut = new DataOutputStream(fileOut);
                for (int i = 0; i < trip_stops.size(); i++) {
                    dataOut.writeUTF(trip_stops.get(i).getName());
                    dataOut.writeUTF(trip_stops.get(i).getState());
                    dataOut.writeInt(trip_stops.get(i).getLatDeg());
                    dataOut.writeInt(trip_stops.get(i).getLatMin());
                    dataOut.writeInt(trip_stops.get(i).getLongDeg());
                    dataOut.writeInt(trip_stops.get(i).getLongMin());
                }
                dataOut.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        //  When Load Button Clicked
        loadFile.setOnMouseClicked(event -> {
            FileChooser loadfilechooser = new FileChooser();
            loadfilechooser.setTitle("Loading Trip");
            loadfilechooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Bin Files", "*.bin"));
            File loadedFile = loadfilechooser.showOpenDialog(primaryStage);
            try {
                FileInputStream fis = new FileInputStream(loadedFile);
                DataInputStream dis = new DataInputStream(fis);
                if (trip_stops.size() > 0) {
                    trip_stops.clear();
                }
                while (dis.available() > 0) {
                    String name = dis.readUTF();
                    String state = dis.readUTF();
                    int LatDeg = dis.readInt();
                    int LatMin = dis.readInt();
                    int LongDeg = dis.readInt();
                    int LongMin = dis.readInt();
                    trip_stops.add(new City(name, state, LatDeg, LatMin, LongDeg, LongMin));
                }
                for (int i = 0; i < trip_stops.size(); i++) {
                    drawCircle(x_coordinate(trip_stops.get(i).getLongDeg(),
                            trip_stops.get(i).getLongMin()),
                            y_coordinate(trip_stops.get(i).getLatDeg(),
                                    trip_stops.get(i).getLatMin()));
                }
                for (int i = 0; i < trip_stops.size() - 1; i++) {
                    if (trip_stops.size() > 1) {
                        drawLine(x_coordinate(trip_stops.get(i).getLongDeg(), trip_stops.get(i).getLongMin())+3,
                                y_coordinate(trip_stops.get(i).getLatDeg(), trip_stops.get(i).getLatMin())+4,
                                x_coordinate(trip_stops.get(i+1).getLongDeg(), trip_stops.get(i+1).getLongMin())+3,
                                y_coordinate(trip_stops.get(i+1).getLatDeg(), trip_stops.get(i+1).getLatMin())+4);
                    }
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        //  When Trip Stops +- buttons clicked
        plus_trip.setOnMouseClicked(event -> {
            if (list_right.getSelectionModel().getSelectedItem() == null) {
                trip_stops.add(0, possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()));
            } else {
                trip_stops.add(list_right.getSelectionModel().getSelectedIndex() + 1, possible_stops.get(list_bot.getSelectionModel().getSelectedIndex()));
            }
            for (int i = 0; i < trip_stops.size() - 1; i++) {
                lat1 = trip_stops.get(i).getLatDeg() + ((trip_stops.get(i).getLatMin()) / 60);
                long1 = trip_stops.get(i).getLongDeg() + ((trip_stops.get(i).getLongMin()) / 60);
                lat2 = trip_stops.get(i + 1).getLatDeg() + ((trip_stops.get(i + 1).getLatMin()) / 60);
                long2 = trip_stops.get(i + 1).getLongDeg() + ((trip_stops.get(i + 1).getLongMin()) / 60);
                totaldistance += calMilage(lat1, long1, lat2, long2);
            }
            displaymialge.setText(Double.toString((int)totaldistance));

            for (int i = 0; i < trip_stops.size(); i++) {
                drawCircle(x_coordinate(trip_stops.get(i).getLongDeg(),
                        trip_stops.get(i).getLongMin()),
                        y_coordinate(trip_stops.get(i).getLatDeg(),
                                trip_stops.get(i).getLatMin()));
            }
            for (int i = 0; i < trip_stops.size() - 1; i++) {
                if (trip_stops.size() > 1) {
                    drawLine(x_coordinate(trip_stops.get(i).getLongDeg(), trip_stops.get(i).getLongMin())+3,
                            y_coordinate(trip_stops.get(i).getLatDeg(), trip_stops.get(i).getLatMin())+4,
                            x_coordinate(trip_stops.get(i+1).getLongDeg(), trip_stops.get(i+1).getLongMin())+3,
                            y_coordinate(trip_stops.get(i+1).getLatDeg(), trip_stops.get(i+1).getLatMin())+4);
                }
            }
        });

        minus_trip.setOnMouseClicked(event -> {
            totaldistance = 0;
            City temp = trip_stops.get(list_right.getSelectionModel().getSelectedIndex());
            trip_stops.remove(trip_stops.get(list_right.getSelectionModel().getSelectedIndex()));
            gc.clearRect(0,0,650,340);
            for (int i = 0; i < trip_stops.size(); i++) {
                drawCircle(x_coordinate(trip_stops.get(i).getLongDeg(),
                        trip_stops.get(i).getLongMin()),
                        y_coordinate(trip_stops.get(i).getLatDeg(),
                                trip_stops.get(i).getLatMin()));
            }
            for (int i = 0; i < trip_stops.size() - 1; i++) {
                if (trip_stops.size() > 1) {
                    drawLine(x_coordinate(trip_stops.get(i).getLongDeg(), trip_stops.get(i).getLongMin())+3,
                            y_coordinate(trip_stops.get(i).getLatDeg(), trip_stops.get(i).getLatMin())+4,
                            x_coordinate(trip_stops.get(i+1).getLongDeg(), trip_stops.get(i+1).getLongMin())+3,
                            y_coordinate(trip_stops.get(i+1).getLatDeg(), trip_stops.get(i+1).getLatMin())+4);
                }
            }
            //gc.clearRect(x_coordinate(temp.getLongDeg(),temp.getLongMin()),
            //        y_coordinate(temp.getLatDeg(),temp.getLatMin()),20,20);

            for (int i = 0; i < trip_stops.size() - 1; i++) {
                lat1 = trip_stops.get(i).getLatDeg() + ((trip_stops.get(i).getLatMin()) / 60);
                long1 = trip_stops.get(i).getLongDeg() + ((trip_stops.get(i).getLongMin()) / 60);
                lat2 = trip_stops.get(i + 1).getLatDeg() + ((trip_stops.get(i + 1).getLatMin()) / 60);
                long2 = trip_stops.get(i + 1).getLongDeg() + ((trip_stops.get(i + 1).getLongMin()) / 60);
                totaldistance += calMilage(lat1, long1, lat2, long2);
            }
            displaymialge.setText(Double.toString((int)totaldistance));

        });
    }

    private static void setTextFieldEmpty() {
        tf_city.setText("");
        tf_state.setText("");
        tf_latdeg.setText("");
        tf_latmin.setText("");
        tf_longdeg.setText("");
        tf_longmin.setText("");
    }

    private static void initProgram() {
        File possibleStops = new File("ListOfPossibleStops.txt");
        if (!possibleStops.exists()) {
            initPossibleStops();
            writeArrayListToFile(possible_stops, "ListOfPossibleStops.txt");
        } else {
            readArrayListFromFile(theList, "ListOfPossibleStops.txt");
        }
    }

    private static void initPossibleStops() {
        theList.add(new City("Chicago", "IL", 41, 53, 87, 38));
        theList.add(new City("Dallas", "TX", 32, 51, 96, 51));
        theList.add(new City("Detroit", "MI", 42, 25, 83, 1));
        theList.add(new City("Houston", "TX", 29, 59, 95, 22));
        theList.add(new City("Los Angeles", "CA", 33, 56, 118, 24));
        theList.add(new City("New York City", "NY", 40, 47, 73, 58));
        theList.add(new City("Philadelphia", "NJ", 39, 53, 75, 15));
        theList.add(new City("Pheonix", "AZ", 33, 26, 112, 1));
        theList.add(new City("San Antonio", "TX", 29, 32, 98, 28));
        theList.add(new City("San Diego", "CA", 32, 44, 117, 10));
    }

    private static void writeArrayListToFile(ObservableList<City> arrayList, String filename) {
        DataOutputStream dataOut;
        try {
            File theFile = new File(filename);
            FileOutputStream fileOut = new FileOutputStream(theFile);
            dataOut = new DataOutputStream(fileOut);
            for (int i = 0; i < arrayList.size(); i++) {
                dataOut.writeUTF(arrayList.get(i).getName());
                dataOut.writeUTF(arrayList.get(i).getState());
                dataOut.writeInt(arrayList.get(i).getLatDeg());
                dataOut.writeInt(arrayList.get(i).getLatMin());
                dataOut.writeInt(arrayList.get(i).getLongDeg());
                dataOut.writeInt(arrayList.get(i).getLongMin());
            }
            dataOut.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static void readArrayListFromFile(ArrayList<City> arrayList, String filename) {
        try {
            File f = new File(filename);
            FileInputStream fis = new FileInputStream(f);
            DataInputStream dis = new DataInputStream(fis);
            if (arrayList.size() > 0) {
                arrayList.clear();
            }
            while (dis.available() > 0) {
                String name = dis.readUTF();
                String state = dis.readUTF();
                int LatDeg = dis.readInt();
                int LatMin = dis.readInt();
                int LongDeg = dis.readInt();
                int LongMin = dis.readInt();
                arrayList.add(new City(name, state, LatDeg, LatMin, LongDeg, LongMin));
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    private static boolean contains(ObservableList<City> arrayList, String str) {
        boolean returnMe = false;
        for (int i = 0; i < arrayList.size(); i++) {
            if (arrayList.get(i).getName().equals(str)) {
                returnMe = true;
            }
        }
        return returnMe;
    }

    private static double calMilage(double lat1, double long1, double lat2, double long2) {
        if (trip_stops != null) {
            final int EARTH_RADIUS = 6371;
            double RADIAN_FACTOR = 180 / Math.PI;
            double x = (Math.sin(lat1 / RADIAN_FACTOR) * Math.sin(lat2 / RADIAN_FACTOR))
                    + (Math.cos(lat1 / RADIAN_FACTOR)
                    * Math.cos(lat2 / RADIAN_FACTOR)
                    * Math.cos((long2 / RADIAN_FACTOR) - (long1 / RADIAN_FACTOR)));
            distance = EARTH_RADIUS * Math.atan((Math.sqrt(1 - Math.pow(x, 2)) / x));
        }
        return distance;
    }
    private static double x_coordinate(double long_deg, double long_min) {
        //  lat: 25-50, long: 65-125
        //  width: 650 height: 339.726567821
        //  pixels per lat
        double longitude = long_deg + (long_min / 60);
        double pixel_per_long = (650.0 / (125 - 65));
        double x = (650 - ((longitude - 65) * pixel_per_long));
        return x;
    }
    private static double y_coordinate(double lat_deg, double lat_min) {
        double lat = lat_deg + (lat_min / 60);
        double pixel_per_lat = (339.726567821 / (50 - 25));
        double y = (339.726567821 - ((lat - 25) * pixel_per_lat));
        return y;
    }
    private static void drawCircle(double x, double y) {
        gc.setStroke(Color.RED);
        gc.setFill(Color.RED);
        gc.strokeOval(x,y,10,10);
        gc.fillOval(x,y,10,10);
    }
    private static void drawLine(double x1, double y1, double x2, double y2) {
        gc.setStroke(Color.RED);
        gc.setFill(Color.RED);
        gc.strokeLine(x1, y1, x2, y2);
    }
}

