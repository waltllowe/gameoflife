import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LoadSeedBox {

	static String output;

	public static String display() {
		Stage window = new Stage();

		window.initModality(Modality.APPLICATION_MODAL);
		window.setTitle("Select Seed");
		window.setWidth(250);
		window.setHeight(250);
		window.setResizable(false);

		Label label = new Label();
		label.setText("Select Seed");

		// Create ComboBox
		ChoiceBox<String> choiceBox = new ChoiceBox<>();

		// TODO: add different txt file names for different input seeds
		choiceBox.getItems().addAll("glidergun.txt", "spacerake.txt", "pufferfish.txt", "glidertrain.txt");

		// button
		Button button = new Button("Select seed");
		button.setOnAction(e -> {
			output = choiceBox.getValue();
			window.close();
		});

		VBox layout = new VBox(10);
		layout.getChildren().addAll(label, choiceBox, button);
		layout.setAlignment(Pos.CENTER);

		Scene scene = new Scene(layout);
		window.setScene(scene);
		window.showAndWait();
		return output;
	}

}
