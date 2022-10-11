package assignment1;

import javafx.animation.FillTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.ScaleTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class EvaluationBarContainer extends BorderPane implements Observer {
	Slider slider;
	Label m;
	Label g;

	public EvaluationBarContainer() {
		this.slider = new Slider();
		this.slider.setDisable(true);
		this.slider.setMin(0);
		this.slider.setMax(60);
		this.slider.setPrefWidth(500);
		this.m = new Label("Musketeer");
		this.m.setFont(new Font(18));
		this.m.setStyle("-fx-text-fill: white;");
		this.g = new Label("Guard");
		this.g.setFont(new Font(18));
		this.g.setStyle("-fx-text-fill: white;");

		slider.styleProperty().bind(Bindings.createStringBinding(() -> {
	        double percentage = (slider.getValue() - slider.getMin()) / (slider.getMax() - slider.getMin()) * 100.0 ;
	        return String.format("-slider-track-color: linear-gradient(to right, -slider-filled-track-color 0%%, "
	                + "-slider-filled-track-color %f%%, -fx-base %f%%, -fx-base 100%%);", 
	                percentage, percentage);
	    }, slider.valueProperty(), slider.minProperty(), slider.maxProperty()));
	
		StackPane s = new StackPane(slider);
		HBox hbox = new HBox(m, s, g);
		hbox.setAlignment(Pos.CENTER);
		this.setCenter(hbox);
	}

	public void update(double state) {
		Timeline timeline = new Timeline();
		timeline.setCycleCount(1);
		KeyValue a = new KeyValue(this.slider.valueProperty(), state, Interpolator.EASE_BOTH);
		KeyFrame c = new KeyFrame(Duration.millis(200), a);
		timeline.getKeyFrames().add(c);
		timeline.play();

	}
}
