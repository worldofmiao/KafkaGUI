package impl;


import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;

import org.apache.log4j.PropertyConfigurator;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class Launcher extends Application {

	/**
	 * Logger Instance
	 */
	private static final Logger LOGGER = Logger.getLogger(Launcher.class);

	private final EventHandlers handlers = new EventHandlers();
	public BorderPane pane;
	private Commons common = new Commons();
	private Map<String, String> settings = new HashMap<String, String>();

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle("Simple Kafka GUI");
		// center
		final TextArea textArea = new TextArea();
		textArea.setMinHeight(400);
		Button btn = new Button("publish");
		// btn.setOnAction(new EventHandler<ActionEvent>() {
		//
		// public void handle(ActionEvent event) {
		// // TODO Auto-generated method stub
		// System.out.println("text published: " + textArea.getText());
		// textArea.setText("text published: " + textArea.getText());
		// }
		// });
		btn.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handlers.handlePublish(pane);
			}
		});
		// BorderPane
		pane = new BorderPane();
		pane.setPadding(new Insets(30));
		VBox paneCenter = new VBox();
		paneCenter.setSpacing(10);
		pane.setCenter(paneCenter);
		paneCenter.getChildren().add(textArea);
		paneCenter.getChildren().add(btn);

		// left
		VBox paneLeft = new VBox();
		Label brokerLabel = new Label("Broker URL");
		TextField brokerText = new TextField();

		Label topicLabel = new Label("Topic");
		TextField topicText = new TextField();

		Label partitionLabel = new Label("Partition");
		TextField partitionText = new TextField();

		Button setting = new Button("save settings");
		setting.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				handlers.handleSaveSettings(pane);
			}
		});

		paneLeft.setSpacing(10);
		pane.setLeft(paneLeft);
		paneLeft.getChildren().add(brokerLabel);
		paneLeft.getChildren().add(brokerText);
		paneLeft.getChildren().add(topicLabel);
		paneLeft.getChildren().add(topicText);
		paneLeft.getChildren().add(partitionLabel);
		paneLeft.getChildren().add(partitionText);
		paneLeft.getChildren().add(setting);

		// bottom
		VBox paneBottom = new VBox();
		Label result = new Label("Result");
		TextArea resultText = new TextArea();
		resultText.setMaxHeight(20);
		paneBottom.setSpacing(10);
		pane.setBottom(paneBottom);
		paneBottom.getChildren().add(result);
		paneBottom.getChildren().add(resultText);

		// load settings
		this.settings = common.loadSettings();
		brokerText.setText(settings.get("brokerURL"));
		topicText.setText(settings.get("topic"));
		partitionText.setText(settings.get("partition"));

		// Finally
		BorderPane.setMargin(paneCenter, new Insets(10));
		BorderPane.setMargin(paneLeft, new Insets(10));
		BorderPane.setMargin(paneBottom, new Insets(10));

		Scene scene = new Scene(pane, 800, 600);

		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		BasicConfigurator.configure();

		//PropertyConfigurator.configure("log4j.conf");
		launch(args);
	}

}
