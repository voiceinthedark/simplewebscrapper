package application;
	
import java.util.List;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.Stage;


public class Main extends Application {
	Label lblWebPage;
	TextField tfWebAddress;
	Button btnScrape;
	HBox hbAddress;
	TextArea taLinks;
	ScrollPane spTextArea;
	TextFlow tflowLinks;
	ScrollPane spTextFlow;
	@Override
	public void start(Stage primaryStage) {
		try {
			BorderPane root = new BorderPane();
			Scene scene = new Scene(root,1280,800);
			
			lblWebPage = new Label("Page Address");
			tfWebAddress = new TextField("");
			tfWebAddress.setPrefWidth(1000);
			
			btnScrape = new Button("Scrape");
			hbAddress = new HBox(5.0, lblWebPage, tfWebAddress, btnScrape);
			hbAddress.prefWidthProperty().bind(root.widthProperty());
			root.setTop(hbAddress);
			
			taLinks = new TextArea();			
			spTextArea = new ScrollPane(taLinks);
			/*spTextArea.prefHeightProperty().bind(taLinks.heightProperty());
			spTextArea.prefWidthProperty().bind(taLinks.widthProperty());*/
			spTextArea.setFitToWidth(true);
			spTextArea.setFitToHeight(true);
			taLinks.prefHeightProperty().bind(root.heightProperty());
			taLinks.prefWidthProperty().bind(root.widthProperty());
			taLinks.setFont(Font.font("Arial", 15));
			
			tflowLinks = new TextFlow();
			tflowLinks.prefHeightProperty().bind(root.heightProperty());
			tflowLinks.prefWidthProperty().bind(root.widthProperty());
			spTextFlow = new ScrollPane(tflowLinks);
			spTextFlow.setFitToWidth(true);
			//spTextFlow.setFitToHeight(true);
			//tflowLinks.setLineSpacing(1.5);		
			
			//root.setCenter(spTextArea);
			root.setCenter(spTextFlow);
			
			btnScrape.setOnAction(e -> {
				//getLinksToTextArea();
				getLinksAsHyperLinks();
			});
			
			tfWebAddress.setOnKeyPressed(k -> {
				if(k.getCode() == KeyCode.ENTER) {
					getLinksAsHyperLinks();
				}
			});
			
			
			
			
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.setTitle("Web Scrapper 0.1");
			primaryStage.show();
		} catch(Exception e) {
			System.err.println(e.getMessage());
		}
	}

	private void getLinksAsHyperLinks() {
		if(tfWebAddress.getText().equals("")) {					
		}
		else {		
			tflowLinks.getChildren().clear();
			WebScrapper webScrapper = new WebScrapper(tfWebAddress.getText());	
			webScrapper.setHostService(getHostServices());					
			List<Hyperlink> list = webScrapper.collectLinksAsHL();
			//tflowLinks.getChildren().addAll(list);
			for(Hyperlink h : list) {
				Text t = new Text("\n");
				tflowLinks.getChildren().addAll(h, t);
			}
		}
	}

	private void getLinksToTextArea() {
		if(tfWebAddress.getText().equals("")) {
			taLinks.setText("Please enter a link in the address bar");
		}
		else {					
			taLinks.clear();
			//System.out.println(tfWebAddress.getText());
			WebScrapper webScrapper = new WebScrapper(tfWebAddress.getText());
			//String str = webScrapper.collectLinks();
			//System.out.println(str);
			taLinks.setText(webScrapper.collectLinks());
			
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
