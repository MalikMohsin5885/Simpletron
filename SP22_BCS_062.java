import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.paint.Color; 
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.TextInputControl;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.FileChooser;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.*;
import java.util.*;
import java.nio.file.*;
import java.io.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import java.io.IOException;
import java.lang.IllegalStateException;

public class SP22_BCS_062 extends Application
{
	int[] Memory = new int[100];
	int tInst = 0;
	@Override
	public void start(Stage stage)
	{
		BorderPane layout = new BorderPane();
		HBox topHorBox = new HBox(10);
		HBox bottomHorBox = new HBox(30);
		ToggleGroup group = new ToggleGroup();
		MethodsClass myMethods = new MethodsClass(Memory);
		
		TextArea areaTxt = new TextArea();
		areaTxt.setPadding(new Insets(7,5,7,5));
		areaTxt.setStyle("-fx-background-color: #D6C5BD");
		areaTxt.setEditable(false);
		layout.setCenter(areaTxt);		//sets text area to center
		
		//-----------------------------header txt fields-------------------------->
		TextField accumulatorTxt = new TextField("0");
		accumulatorTxt.setPrefWidth(50);
		accumulatorTxt.setEditable(false);
		
		TextField instCountTxt = new TextField("0");
		instCountTxt.setPrefWidth(50);
		instCountTxt.setEditable(false);
		
		TextField instRegTxt = new TextField("0");
		instRegTxt.setPrefWidth(50);
		instRegTxt.setEditable(false);
		
		TextField opcodeTxt = new TextField("0");
		opcodeTxt.setPrefWidth(50);
		opcodeTxt.setEditable(false);
		
		TextField operandTxt = new TextField("0");
		operandTxt.setPrefWidth(50);
		operandTxt.setEditable(false);

		topHorBox.getChildren().addAll(new Label("Accumulator "),accumulatorTxt,new Label("InstCounter"),
										instCountTxt,new Label("InstReg"),instRegTxt,new Label("OpCode"),
												opcodeTxt,new Label("Operand"),operandTxt);
		topHorBox.setStyle("-fx-background-color: #DFDDDC");
		topHorBox.setAlignment(Pos.CENTER);
		topHorBox.setPadding(new Insets(5,5,5,5));
		layout.setTop(topHorBox);		//sets text fields to top
		//----------------------------------------------------------------->
		
		//---------------------------fotter Buttons-------------------------->
		Button loadProgram = new Button("  Load Program  ");
		Button nextInstruction = new Button(" Execute Next Instruction ");
		
		RadioButton oneInst = new RadioButton("Exe one Inst");
			oneInst.setToggleGroup(group);
		RadioButton exeProgram = new RadioButton("Exe program");
			exeProgram.setToggleGroup(group);
		
		Button appCloser = new Button("CLOSE");
		bottomHorBox.getChildren().addAll(appCloser,loadProgram,nextInstruction,oneInst,exeProgram);
		bottomHorBox.setAlignment(Pos.CENTER);
		bottomHorBox.setStyle("-fx-background-color: #DFDDDC");
		bottomHorBox.setPadding(new Insets(5,5,5,5));
		layout.setBottom(bottomHorBox);		//sets buttons at bottom
		//----------------------------------------------------------------->
		
	//--------------buttons functionality--------------------->
		//-------------------app closer---------------->
		appCloser.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) {
				System.out.println("Application Closes successfully...!");
				System.exit(0);
			}
		});
		
		//---------------------load Program----------------------->
		loadProgram.setOnAction(new EventHandler<ActionEvent>() 
		{
			@Override
			public void handle(ActionEvent event) {
				FileChooser fileChooser = new FileChooser();
				File file= fileChooser.showOpenDialog(stage);
				if(file == null)
				{
					Alert Walert = new Alert(AlertType.WARNING);
					Walert.setTitle("Warning Dialog");
					Walert.setHeaderText(null);
					Walert.setContentText("File not Found");
					Walert.showAndWait();
					file = fileChooser.showOpenDialog(stage);
					if(file == null) 
						System.exit(0);
				}
				//passing file to scanner class
				try (Scanner input = new Scanner(file))
				{
					int i = 0;
					while(input.hasNext())
					{
						Memory[i] = input.nextInt();
						i++;
					}
					tInst = i;
					for(int o = i ; o<Memory.length ; o++)
					{
						Memory[o] = 0;
					}
				}
				catch(IOException|InputMismatchException | SecurityException | ArrayIndexOutOfBoundsException e)
				{
					e.printStackTrace();
				}
				
				if(myMethods.buttonState)
					nextInstruction.setDisable(false);
				
				accumulatorTxt.setText("0");
				instCountTxt.setText("0");
				instRegTxt.setText("0");
				opcodeTxt.setText("0");
				operandTxt.setText("0");
				myMethods.defaultValue();
				areaTxt.setText(myMethods.getText());
			}
		});
		
		//---------------------exe button ------------------->
		nextInstruction.setOnAction(new EventHandler<ActionEvent>()
		{
			@Override
			public void handle(ActionEvent event)
			{
				switch (oneInst.isSelected() ? "One at a time" : 
								exeProgram.isSelected() ?  "All inst" : "None")
				{
					case "One at a time" :		//for one instructions at a time
					{
						myMethods.decode(Memory[myMethods.getCurrentIndex()]);
						opcodeTxt.setText(myMethods.getOpcode());
						operandTxt.setText(myMethods.getOperand());
						instCountTxt.setText(Integer.toString(myMethods.getCurrentIndex()));
						instRegTxt.setText(Integer.toString(Memory[myMethods.getCurrentIndex()]));
						myMethods.execution(nextInstruction);
						accumulatorTxt.setText(myMethods.getAccumulator());
						
						areaTxt.setText(myMethods.getText());
					}
						break;
					case "All inst" :		//for whole set of instructions
					{
						int krt = tInst;
						while(krt != 0)
						{
							myMethods.decode(Memory[myMethods.getCurrentIndex()]);
							opcodeTxt.setText(myMethods.getOpcode());
							operandTxt.setText(myMethods.getOperand());
							instCountTxt.setText(Integer.toString(myMethods.getCurrentIndex()));
							instRegTxt.setText(Integer.toString(Memory[myMethods.getCurrentIndex()]));
							myMethods.execution(nextInstruction);
							accumulatorTxt.setText(myMethods.getAccumulator());
						
							areaTxt.setText(myMethods.getText());
							
							if(myMethods.buttonState)
								krt = 1;
							krt--;
						}
						krt = tInst;
					}
						break;
					case "None" :
					{
						System.out.println("None");
					}
						break;
				}
			}
		});
	//----------------------------------------------------------->
		
		//create scene and set that to stage
		Scene myScene = new Scene(layout,750,450);
		stage.setTitle("Simpletron Simulator");
		stage.setScene(myScene);
		stage.show();
	}//---------start method ends--------------------------------->
}

class MethodsClass
{
	private int[] refMemory;
	private int opcode = 0;
	private int operand = 0;
	private int accumulator = 0;
	private int currentIndex = 0;
	public boolean buttonState = false;
	
	TextInputDialog inputdialog = new TextInputDialog("0");
	Alert Walert = new Alert(AlertType.WARNING);
	Alert WalertNumber = new Alert(AlertType.WARNING);
	Alert Ialert = new Alert(AlertType.INFORMATION);
	Alert Calert = new Alert(AlertType.CONFIRMATION);
	
	public MethodsClass(int[] refMemory){this.refMemory = refMemory;}

	//-----------------------getters and setters-------------------------->
	public void defaultValue() {opcode = 0 ; operand = 0 ; accumulator = 0 ; currentIndex = 0;
									buttonState = false;}
	public int getCurrentIndex() { return currentIndex; }
	public String getOpcode() { return String.format("%s",opcode); }
	public String getOperand() { return String.format("%s",operand); }
	public String getAccumulator() { return String.format("%s",accumulator); }
	//--------------------------------------------------------------------------------->
	
	public void decode(int member)//decode
	{
		opcode = member / 100;
		operand = member % 100;
	}
	
	public void execution(Button exe)//execution method
	{
		caseOperation(exe);
		currentIndex++;
	}
	
	public void caseOperation(Button exe)//checks which case to execute
	{
		switch(opcode)
		{
			case 10 ://read
			{
				inputdialog.setTitle("Text Input Dialog");
				inputdialog.setHeaderText("Taking Number");
				inputdialog.setContentText("Please enter a number:");
				inputdialog.showAndWait();
				int num = 0;
				try{
					num = Integer.parseInt(inputdialog.getEditor().getText());
					if(num>9999){
						Walert.setTitle("Warning Dialog");
						Walert.setHeaderText("Look, a Warning Dialog");
						Walert.setContentText("Attempts to enter value greater than 9999 , Please try again");
						Walert.showAndWait();
						inputdialog.showAndWait();
						num = Integer.parseInt(inputdialog.getEditor().getText());
						if(num>9999)
							num = 0;
					}
				}
				catch (NumberFormatException e)  
				{ 
					WalertNumber.setTitle("Warning Dialog");
					WalertNumber.setHeaderText("Look, a Warning Dialog");
					WalertNumber.setContentText("No number found");
					WalertNumber.showAndWait();
					
					inputdialog.showAndWait();
					num = Integer.parseInt(inputdialog.getEditor().getText());
					if(num>9999)
							num = 0;
				} 
				refMemory[operand] = num;
			}
			break;
			case 11 ://write
			{
				Ialert.setTitle("Output Dialog");
				Ialert.setHeaderText("OUTPUT......!");
				Ialert.setContentText("Data store at location "+ operand+" is : "+refMemory[operand] );
				Ialert.showAndWait();
			}
			break;
			case 20 ://load
			{
				accumulator = refMemory[operand];
			}
			break;
			case 21 ://store
			{
				refMemory[operand] = accumulator;
			}
			break;
			case 30 ://add and leave result in accumulator;
			{
				accumulator += refMemory[operand];
			}
			break;
			case 31 ://subtract and leave result in accumulator;
			{
				accumulator -= refMemory[operand];
			}
			break;
			case 32 ://divide and leave result in accumulator;
			{
				accumulator /= refMemory[operand];
			}
			break;
			case 33 ://multiply and leave result in accumulator;
			{
				accumulator *= refMemory[operand];
			}
			break;
			case 40 ://branch to given location
			{
				currentIndex = operand - 1;
			}
			break;
			case 41 ://branch if accumulator is neg
			{
				if (accumulator<0)
				{
					currentIndex = operand -1;
				}
			}
			break;
			case 42 ://branch if accumulator is 0
			{
				if (accumulator == 0)
					currentIndex = operand - 1;
			}
			break;
			case 43 ://halt
			{
				buttonState = true;	//sets to true if intruction set contain 43 to disable exe button
				Ialert.setTitle("Output Dialog");
				Ialert.setHeaderText(null);
				Ialert.setContentText("The instruction execution is completed.........! ");
				Ialert.showAndWait();
				
				Ialert.setContentText("For further execution press Load Program Again ⏰");
				Ialert.showAndWait();
				
				exe.setDisable(true);
			}
			break;
		}
	}
	
//----------------------------------printing array-----------------------------------------------
	public String fLine(){
		String line = "";
		for (int u=0;u<=9;u++)
			line += ("            "+u+"   ");
		return line;
	}
	public String sequence(){
		String ME = "";
		int j = 0;
		int i = 0;
		for(i=0;i<100;i++){
			if(i == 0){
				ME += "00 ";
				j+=10;
			}
			if(i != 0 && i%10 == 0){
				ME += (j+" ");
				j+=10;
			}
			
			ME += ("    "+String.format("+%04d  ",refMemory[i]));
			
			if((i+1)%10 == 0){
				ME += "\n";
			}
		}
		return ME;
	}
	public String getText(){return String.format("%s%n%s",fLine(),sequence());}
//-------------------------------------------------------------------------------------

}//end of methods class