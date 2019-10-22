import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.ArrayList;
import java.util.HashMap;


public class BaccaratGame extends Application {
	private ArrayList<Card> playerHand;
	private ArrayList<Card> bankerHand;
	private BaccaratDealer theDealer;
	private BaccaratGameLogic gameLogic;
	double currentBet;
	double totalWinnings;
	private Card pDraw; // the third card drawn for the player (if drawn)
	private String bet; // get who the player bet on

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		launch(args);
	}

	public double evaluateWinnings() {
		if(gameLogic.whoWon(playerHand, bankerHand).equals("Draw") && bet == "Draw") {
			totalWinnings -= 8 * currentBet;
		}
		if (gameLogic.whoWon(playerHand, bankerHand).equals(bet)) {
			totalWinnings += currentBet;
		}
		else {
			totalWinnings -= currentBet;
		}
		return totalWinnings;
	}

	private static Double stod(String str) {     // validates user input
		try {
			return Double.parseDouble(str);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	public ImageView showCard(Card card) {     // generates and returns ImageView of Card passed
		Image whichCard = new Image(card.getValue() + card.getSuite() + ".png");
		ImageView theCard = new ImageView(whichCard);
		theCard.setFitHeight(100);
		theCard.setFitWidth(60);
		return theCard;
	}

	//feel free to remove the starter code from this method
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		primaryStage.setTitle("Let's Play Baccarat!!!");
        BorderPane game = new BorderPane();
        theDealer = new BaccaratDealer();
        gameLogic = new BaccaratGameLogic();
        theDealer.generateDeck();

        Image decks = new Image("deck.png");
        ImageView deck = new ImageView(decks);
		deck.setFitWidth(90);
		deck.setFitHeight(110);

		VBox vBottom = new VBox();
		Rectangle r = new Rectangle(); // rectangle to fill in bottom layout of borderpane
		r.setWidth(1000);
		r.setHeight(100);
		r.setFill(Color.GREEN);
		vBottom.getChildren().addAll(r);
		game.setBottom(vBottom);
		game.setAlignment(vBottom, Pos.CENTER);

        // Setting up menu
        Menu options = new Menu("Options");
        MenuItem exit = new MenuItem("Exit");
        MenuItem reset = new MenuItem("Fresh Start");
        options.getItems().addAll(reset, exit);
        MenuBar menu = new MenuBar();
        menu.getMenus().addAll(options);
		game.setTop(menu);

		// Display cards drawn
		VBox vCenter = new VBox(100);
		HBox playerCards = new HBox(10);
		HBox bankerCards = new HBox(10);
		vCenter.getChildren().addAll(bankerCards, deck, playerCards);
		vCenter.setAlignment(Pos.CENTER);
		game.setCenter(vCenter);
		game.setMargin(vCenter, new Insets(20, 20, 20, 40));

		// On screen messages and updates about game
		VBox vRight = new VBox();
		ListView message = new ListView();
		message.getItems().addAll("Welcome to Baccarat!", "Please enter a bid to start.", " ");
		message.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		message.setPrefWidth(200);
		message.setPrefHeight(500);
		vRight.getChildren().addAll(message);
		vRight.setMargin(message, new Insets(40, 100, 30, 0));
		game.setRight(vRight);

		// Winnings and Bidding display
		VBox vLeft = new VBox(10);
		VBox bidBox = new VBox(5);  // vbox to organize bidding section
		HBox hButtons = new HBox(5); // hbox for side-by-side bid buttons
		Text askBid = new Text("Enter bid: ");
		askBid.setFill(Color.WHITE);
		TextField bid = new TextField();
		TextField pWin = new TextField();    // for displaying player winnings
		pWin.setMaxWidth(100);
		pWin.setDisable(true);
		TextField bWin = new TextField();   // for displaying banker winnings
		bWin.setMaxWidth(100);
		bWin.setDisable(true);
		Button player = new Button("Player");
		Button banker = new Button("Banker");
		Button draw = new Button("Draw");
		Button start = new Button("Start");  // post game button

		// displaying winnings, bid buttons, bid input on left side
		Text pWinnings = new Text("Player winnings: ");
		Text bWinnings = new Text("Banker winnings: ");
		pWinnings.setFill(Color.WHITE);
		bWinnings.setFill(Color.WHITE);
		hButtons.getChildren().addAll(player, banker, draw);  // combine buttons
		bid.setAlignment(Pos.CENTER);
		bid.setPrefWidth(100);
		bidBox.getChildren().addAll(askBid, bid, hButtons); // setting up bid section
		vLeft.getChildren().addAll(bWinnings, bWin, bidBox, pWinnings, pWin);
		vLeft.setAlignment(Pos.CENTER);
		vLeft.setMargin(bidBox, new Insets(100, 20, 20, 20));
		vLeft.setMargin(pWinnings, new Insets(100, 20, 0, 20));
		game.setLeft(vLeft);
		game.setStyle("-fx-background-color: green");

		player.setOnAction(e -> {
			if(stod(bid.getText()) != null) {
				currentBet = stod(bid.getText());
				bet = "Player";
				player.setDisable(true);
				banker.setDisable(true);
				draw.setDisable(true);
				bid.setDisable(true);
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();
				playerCards.getChildren().addAll(showCard(playerHand.get(0)), showCard(playerHand.get(1)));  // add generated imageviews of cards to player Hbox
				bankerCards.getChildren().addAll(showCard(bankerHand.get(0)), showCard(bankerHand.get(1)));  // add generated imageviews of cards to banker Hbox
				if (gameLogic.evaluatePlayerDraw(playerHand)) {
					pDraw = theDealer.drawOne();
					playerHand.add(pDraw);
					playerCards.getChildren().addAll(showCard(playerHand.get(2)));
				} else {
					pDraw = null;    // player did not draw
				}
				if (gameLogic.evaluateBankerDraw(bankerHand, pDraw)) {
					bankerHand.add(theDealer.drawOne());
					bankerCards.getChildren().addAll(showCard(bankerHand.get(2)));

				}
				totalWinnings = evaluateWinnings();
				pWin.setText(String.valueOf(totalWinnings));
				message.getItems().addAll("Banker Total: " + gameLogic.handTotal(bankerHand));
				message.getItems().addAll("Player Total: " + gameLogic.handTotal(playerHand), "");
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Draw")) {     // check who won and compare to who user bet on
					if (bet.equals("Draw")) {
						message.getItems().addAll("Congrats, you bet Draw! ");
						message.getItems().addAll("You win!");

					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Player")) {
					if (bet.equals("Player")) {
						message.getItems().addAll("Congrats, you bet Player!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Banker")) {
					if (bet.equals("Banker")) {
						message.getItems().addAll("Congrats, you bet Banker!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Player")) {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				message.getItems().addAll("");
				message.getItems().addAll("Press Start to start new game");
				message.getItems().addAll(start);   // allow user to start a new game
			}

			else {
				message.getItems().add("Invalid bid, try again.");
				bid.clear();
			}
		});

		banker.setOnAction(e -> {
			if(stod(bid.getText()) != null) {
				currentBet = stod(bid.getText());
				bet = "Banker";
				player.setDisable(true);
				banker.setDisable(true);
				draw.setDisable(true);
				bid.setDisable(true);
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();
				playerCards.getChildren().addAll(showCard(playerHand.get(0)), showCard(playerHand.get(1)));
				bankerCards.getChildren().addAll(showCard(bankerHand.get(0)), showCard(bankerHand.get(1)));
				if (gameLogic.evaluatePlayerDraw(playerHand)) {
					pDraw = theDealer.drawOne();
					playerHand.add(pDraw);
					playerCards.getChildren().addAll(showCard(playerHand.get(2)));
				} else {
					pDraw = null;
				}
				if (gameLogic.evaluateBankerDraw(bankerHand, pDraw)) {
					bankerHand.add(theDealer.drawOne());
					bankerCards.getChildren().addAll(showCard(bankerHand.get(2)));

				}
				totalWinnings = evaluateWinnings();
				pWin.setText(String.valueOf(totalWinnings));
				message.getItems().addAll("Player Total: " + gameLogic.handTotal(playerHand));
				message.getItems().addAll("Banker Total: " + gameLogic.handTotal(bankerHand), "");
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Draw")) {
					if (bet.equals("Draw")) {
						message.getItems().addAll("Congrats, you bet Draw! ");
						message.getItems().addAll("You win!");

					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Player")) {
					if (bet.equals("Player")) {
						message.getItems().addAll("Congrats, you bet Player!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Banker")) {
					if (bet.equals("Banker")) {
						message.getItems().addAll("Congrats, you bet Banker!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Player")) {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				message.getItems().addAll("");
				message.getItems().addAll("Press Start to start new game");
				message.getItems().addAll(start);
			}

			else {
				message.getItems().add("Invalid bid, try again.");
				bid.clear();
			}
		});

		draw.setOnAction(e -> {
			if(stod(bid.getText()) != null) {
				currentBet = stod(bid.getText());
				bet = "Draw";
				player.setDisable(true);
				banker.setDisable(true);
				draw.setDisable(true);
				bid.setDisable(true);
				playerHand = theDealer.dealHand();
				bankerHand = theDealer.dealHand();
				playerCards.getChildren().addAll(showCard(playerHand.get(0)), showCard(playerHand.get(1)));
				bankerCards.getChildren().addAll(showCard(bankerHand.get(0)), showCard(bankerHand.get(1)));
				if (gameLogic.evaluatePlayerDraw(playerHand)) {
					pDraw = theDealer.drawOne();
					playerHand.add(pDraw);
					playerCards.getChildren().addAll(showCard(playerHand.get(2)));
				} else {
					pDraw = null;
				}
				if (gameLogic.evaluateBankerDraw(bankerHand, pDraw)) {
					bankerHand.add(theDealer.drawOne());
					bankerCards.getChildren().addAll(showCard(bankerHand.get(2)));

				}
				totalWinnings = evaluateWinnings();
				pWin.setText(String.valueOf(totalWinnings));
				message.getItems().addAll("Player Total: " + gameLogic.handTotal(playerHand));
				message.getItems().addAll("Banker Total: " + gameLogic.handTotal(bankerHand), "");
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Draw")) {
					if (bet.equals("Draw")) {
						message.getItems().addAll("Congrats, you bet Draw! ");
						message.getItems().addAll("You win!");

					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Player")) {
					if (bet.equals("Player")) {
						message.getItems().addAll("Congrats, you bet Player!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Banker")) {
						message.getItems().addAll("Sorry you bet Banker!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				if (gameLogic.whoWon(playerHand, bankerHand).equals("Banker")) {
					if (bet.equals("Banker")) {
						message.getItems().addAll("Congrats, you bet Banker!");
						message.getItems().addAll("You win!");
					}
					else if(bet.equals("Player")) {
						message.getItems().addAll("Sorry you bet Player!", "You lost your bet!");
						bWin.setText(String.valueOf(currentBet));
					}
					else {
						message.getItems().addAll("Sorry you bet Draw!", "You lost your bet");
						bWin.setText(String.valueOf(currentBet));
					}
				}
				message.getItems().addAll("");
				message.getItems().addAll("Press Start to start new game");
				message.getItems().addAll(start);
			}

			else {
				message.getItems().add("Invalid bid, try again.");
				bid.clear();
			}
		});

		start.setOnAction(new EventHandler<ActionEvent>() {      // reset screen and elements to begin new game
			@Override
			public void handle(ActionEvent event) {
				message.getItems().clear();
				message.getItems().addAll("Welcome to Baccarat!", "Please enter a bid to start.", " ");
				player.setDisable(false);
				banker.setDisable(false);
				draw.setDisable(false);
				bid.clear();
				bid.setDisable(false);
				pWin.clear();
				playerCards.getChildren().clear();
				bankerCards.getChildren().clear();
				theDealer.generateDeck();
			}
		});

		reset.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				totalWinnings = 0;
				message.getItems().clear();
				message.getItems().addAll("Welcome to Baccarat!", "Please enter a bid to start.", " ");
				player.setDisable(false);
				banker.setDisable(false);
				draw.setDisable(false);
				bid.clear();
				bid.setDisable(false);
				pWin.clear();
				playerCards.getChildren().clear();
				bankerCards.getChildren().clear();
				theDealer.generateDeck();
			}
		});

		exit.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				Platform.exit();
				System.exit(0);
			}
		});

		Scene scene = new Scene(game, 900, 800);
		primaryStage.setScene(scene);
		primaryStage.show();
	}


}
