package impl;

import java.util.HashMap;
import java.util.Map;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class EventHandlers {
	private Commons common = new Commons();

	public void handlePublish(BorderPane pane) {
		VBox bottom = (VBox) pane.getBottom();
		TextArea result = (TextArea) bottom.getChildren().get(1);

		VBox mid = (VBox) pane.getCenter();
		TextArea area = (TextArea) mid.getChildren().get(0);

		VBox left = (VBox) pane.getLeft();
		TextField broker = (TextField) left.getChildren().get(1);
		TextField topic = (TextField) left.getChildren().get(3);
		TextField partition = (TextField) left.getChildren().get(5);
		if (!common.testURL(broker.getText())) {
			result.setText("Kafka Connection Failure");
		} else {
			try {
				KafkaProducerLocal p = new KafkaProducerLocal(Integer.valueOf(partition.getText()), topic.getText(),
						broker.getText());
				p.execute();
				String content = area.getText();

				p.publishMessage(Integer.valueOf(partition.getText()), null, content);
				p.closeProducer();
				result.setText("Message Published!");
			} catch (Exception e) {
				result.setText(e.toString());
			}
		}
	}

	public void handleSaveSettings(BorderPane pane) {
		VBox box = (VBox) pane.getLeft();
		TextField broker = (TextField) box.getChildren().get(1);
		TextField topic = (TextField) box.getChildren().get(3);
		TextField partition = (TextField) box.getChildren().get(5);

		String newBroker = broker.getText();
		String newTopic = topic.getText();
		String newPartition = partition.getText();

		VBox bottom = (VBox) pane.getBottom();
		TextArea result = (TextArea) bottom.getChildren().get(1);

		Map<String, String> map = new HashMap<String, String>();
		map.put("brokerURL", newBroker);
		map.put("topic", newTopic);
		map.put("partition", newPartition);
		Commons common = new Commons();
		try {
			common.saveSettings(map);
			result.setText("Settings Saved");
		} catch (Exception e) {
			result.setText(e.toString());
		}
	}

	public void handleClear(BorderPane pane) {
		VBox center = (VBox) pane.getCenter();
		TextArea content = (TextArea) center.getChildren().get(0);
		content.clear();

		VBox bottom = (VBox) pane.getBottom();
		TextArea result = (TextArea) bottom.getChildren().get(1);
		result.clear();
	}

}
