package com;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Rectangle2D;
import javafx.geometry.VPos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Slider;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class App extends Application {
	// UI elements
    Stage stage, settings, about;
    static Scene mainScene, settingsScene, aboutScene;
    static Label backgroundLabel, animationLabel, volumeLabel;
    static MenuBar menuBar;
    static Menu menu1, menu2, menu3, menu4;
    static MenuItem menuItem1, menuItem2, menuItem3, menuItem4, menuItem5;
    static Text aboutText;
    static ColorPicker colorPicker1, colorPicker2;
    static Slider volumeSlider;
    static FileChooser fileChooser;
    
    Group group;
    Player player;
    VBox vbox;
    VPos vpos;
    ObservableList<Node> list, settingsList, aboutList;
    List<Line> lines = Collections.synchronizedList(new LinkedList<Line>());	
    Optional<File> song; 									// for choosing songs
    AnimationTimer animationTimer;
    Integer num;
    Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();	// for animation position
    
    @Override
    public void start(Stage stage) throws IOException {
        this.num = 100;
        this.stage = stage;
        player = new Player();
        colorPicker1 = new ColorPicker();
    	colorPicker2 = new ColorPicker();
    	volumeSlider = new Slider(0,1,0.5);
        mainScene = new Scene(initUI());
        stage.setMinWidth(600);
        stage.setMinHeight(600);
        stage.setScene(mainScene);
        stage.setTitle("Java Musicplayer");
        stage.setMaximized(true);
        stage.show();
        initAnimation(primaryScreenBounds.getHeight() / 2.0);
    }
    private Parent initUI() {
        group = new Group();
        list = group.getChildren();
        menuBar = new MenuBar();
        menuBar.prefWidthProperty().bind(stage.widthProperty());
        vbox = new VBox(menuBar);
        menu1 = new Menu("File");
        menu2 = new Menu("Settings");
        menu3 = new Menu("Play");
        menu4 = new Menu("About");
        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        menuBar.getMenus().add(menu3);
        menuBar.getMenus().add(menu4);
        menuItem1 = new MenuItem("Load");
        menuItem2 = new MenuItem("Exit");
        menuItem3 = new MenuItem("Settings");
        menuItem4 = new MenuItem("Play");
        menuItem5 = new MenuItem("About");
        menuItem1.setOnAction(Event -> {
            fileChooser = new FileChooser();
            song = Optional.ofNullable(fileChooser.showOpenDialog(stage));
            if (song.isPresent()) {
            	try {
                    player.stop();
                } catch (Exception e) {
                	// first call or cancel lead not to player stopping
                } finally {
                    player.set(song.get());
                    menuItem4.setText("Pause");
                }
            }
        });
        menuItem2.setOnAction(Event -> {
        });
        menuItem3.setOnAction(Event -> {
        	this.showSettings();
        });
        menuItem4.setOnAction(Event -> {
        	if (player.ready()) {
            	if (player.playing()) {
            		player.pause();
            		menuItem4.setText("Play");
            	} else {
                	player.play();
                	menuItem4.setText("Pause");
            	}
        	}
        });
        menuItem5.setOnAction(Event -> {
        	showAbout();
        });
        menu1.getItems().add(menuItem1);
        menu1.getItems().add(menuItem2);
        menu2.getItems().add(menuItem3);
        menu3.getItems().add(menuItem4);
        menu4.getItems().add(menuItem5);
        list.add(vbox);
        return group;
    }
    private void showAbout() {
    	Group layout = new Group();
    	about = new Stage();
    	aboutScene = new Scene(layout, 400, 400);
    	layout.minWidth(400);
    	layout.minHeight(400);
    	aboutList = layout.getChildren();
    	aboutText = new Text("About Java Musicplayer\n\nApplication for playing music");
    	aboutText.layoutXProperty().bind(aboutScene.widthProperty().subtract(aboutText.prefWidth(-1)).divide(2));
    	aboutText.layoutYProperty().bind(aboutScene.heightProperty().subtract(aboutText.prefHeight(-1)).divide(2));
    	aboutList.add(aboutText);
    	about.setScene(aboutScene);
    	about.setMinWidth(400);
    	about.setMinHeight(400);
    	about.setTitle("About");
    	about.show();	
    }
    private void showSettings() {
    	Group layout = new Group();
    	settingsList = layout.getChildren();
    	volumeLabel = new Label("Volume");
    	volumeLabel.setLayoutY(70);
    	volumeSlider.setShowTickMarks(true);
        volumeSlider.setShowTickLabels(true);
        volumeSlider.setMajorTickUnit(0.5f);
    	volumeSlider.setLayoutX(0);
    	volumeSlider.setLayoutY(100);
    	volumeSlider.setPrefWidth(400);
    	player.volume(volumeSlider);
    	backgroundLabel = new Label("Background");
    	colorPicker1.setLayoutY(30);
    	colorPicker1.setPrefWidth(200);
    	colorPicker1.setOnAction((event) -> {
        	mainScene.setFill(colorPicker1.getValue());               
        });
    	animationLabel = new Label("Animation");
    	animationLabel.setLayoutX(200);
    	animationLabel.setLayoutY(0);
    	colorPicker2.setLayoutX(200);
    	colorPicker2.setLayoutY(30);
    	colorPicker2.setPrefWidth(200);
    	settingsList.add(animationLabel);
    	settingsList.add(backgroundLabel);
    	settingsList.add(volumeSlider);
    	settingsList.add(colorPicker1);
    	settingsList.add(colorPicker2);
    	settingsList.add(volumeLabel);
    	settingsScene = new Scene(layout, 400, 400);
    	settings = new Stage();
		settings.setTitle("Settings");
		settings.setScene(settingsScene);
		settings.setX(stage.getX() + 200);
		settings.setY(stage.getY() + 100);
		settings.setMinWidth(400);
		settings.setMinHeight(400);
		settings.show();
    }
    private void initAnimation(double h) {
        for (int i = 0; i < num; i++) {
            lines.add(new Line());
            lines.get(i).setStartX(10.0f + 20.0f * i);
            lines.get(i).setStartY(h);
            lines.get(i).setEndX(10.0f + 20.0f * i);
            lines.get(i).setEndY(h);
        }
        for (int i = 0; i < num; i++) {
        	int k = i + num;
            lines.add(new Line());
            lines.get(k).setStartX(20.0f + 20.0f * i);
            lines.get(k).setStartY(h);
            lines.get(k).setEndX(20.0f + 20.0f * i);
            lines.get(k).setEndY(h);
        }
    	colorPicker2.setOnAction((event) -> {
    		for (int i = 0; i < 2 * num; i++) {
            	lines.get(i).setStroke(colorPicker2.getValue());
    		}
        });
    	// update animation data
    	animationTimer = new AnimationTimer() {
            @Override public void handle(final long NOW) {
            	Optional<float[]> mag = Optional.ofNullable(player.getMagnitudes());
            	Optional<float[]> pha = Optional.ofNullable(player.getPhases());
            	if (mag.isPresent() && pha.isPresent()) { 
            		for (int i = 0; i < num; i++) {
            			// the magnitude data gets updated
            			// a offset comes with the data
                        lines.get(i).setStartY(h - 300.0f - 5.0f * mag.get()[i] + player.getCorrection());     
                	}
            		for (int i = 0; i < num; i++) {
            			// the phase data gets updated
                        	lines.get(i + num).setStartY(h + 100.0f * pha.get()[i]);     
			}
                	}
        	}
        };
	animationTimer.start();
	for (int i = 0; i < 2 * num; i++) {
        	lines.get(i).setStrokeWidth(8);
        	list.add(lines.get(i));
	}
        return;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
