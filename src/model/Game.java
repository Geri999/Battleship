package model;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;
import java.util.logging.*;

public class Game {

    private Board player1Board;
    private Board player1BoardWithMarkedFireOfP1;
    private Board player2Board;
    private Board player2BoardWithMarkedFireOfP2;
    static Logger logger = Logger.getLogger("");
    Scanner sc = new Scanner(System.in);

    public Game() throws IOException {
        initLogger();
    }

    public void initTheGame() {
        this.player1Board = new Board().createEmptyBoard();
        this.player1BoardWithMarkedFireOfP1 = new Board().createEmptyBoard();

        this.player2Board = new Board().createEmptyBoard();
        this.player2BoardWithMarkedFireOfP2 = new Board().createEmptyBoard();

        System.out.println("Player 1, place your ships on the game field\n");
        System.out.println(player1Board);
        player1Board.putAllShipsOnTheBoard();
        switchPlayer();
        System.out.println("Player 2, place your ships on the game field\n");
        System.out.println(player2Board);
        player2Board.putAllShipsOnTheBoard();
        switchPlayer();
    }

    public void courseOfTheGame2() {
        int player1AmountOfShips = 5;
        int player2AmountOfShips = 5;
        String result;

        do {
            showTwoBoards(player1BoardWithMarkedFireOfP1, player1Board);
            System.out.println("Player 1, it's your turn:");
            result = fireService(player1BoardWithMarkedFireOfP1, player2Board);
            if (result.equals("hit&sank")) player2AmountOfShips--;
            if (player2AmountOfShips == 0) continue;
            slogans(result);
            switchPlayer();

            showTwoBoards(player2BoardWithMarkedFireOfP2, player2Board);
            System.out.println("Player 2, it's your turn:");
            result = fireService(player2BoardWithMarkedFireOfP2, player1Board);
            if (result.equals("hit&sank")) player1AmountOfShips--;
            if (player1AmountOfShips == 0) continue;
            slogans(result);
            switchPlayer();
        }
        while (player1AmountOfShips > 0 && player2AmountOfShips > 0);
        System.out.println("You sank the last ship. You won. Congratulations!");
    }

    public void slogans(String shortMessage) {
        String message = switch (shortMessage) {
            case "miss" -> "You missed. Try again:";
            case "hit" -> "You hit a ship! Try again:";
            case "hit&sank" -> "You sank a ship! Specify a new target:";
            case "hitAgain" -> "You hit a ship AGAIN! Try again:";
            default -> "";
        };
        System.out.println(message);
    }

    public String fireService(Board boardWithMarkedFireOfPX, Board opponentBoardWithShips) {
        String hit = "X";
        String miss = "M";
        int[] shotCoordinates = takeFireCoordinatesFromPlayer();

        if (opponentBoardWithShips.getBoard()[shotCoordinates[0]][shotCoordinates[1]].equals(hit)) return "hitAgain";

        Optional<Ship> shootingResult = opponentBoardWithShips.fireAndReturnResultIfShipWasHit(shotCoordinates);
        if (shootingResult.isPresent()) {
            boardWithMarkedFireOfPX.putSignOnBoard(shotCoordinates, hit);
            opponentBoardWithShips.putSignOnBoard(shotCoordinates, hit);
            return shootingResult.get().ifShipSank() ? "hit&sank" : "hit";
        } else {
            boardWithMarkedFireOfPX.putSignOnBoard(shotCoordinates, miss);
            opponentBoardWithShips.putSignOnBoard(shotCoordinates, miss);
            return "miss";
        }
    }

    private int[] takeFireCoordinatesFromPlayer() {
        int[] shotCoordinates;
        boolean tryAgain;

        do {
            tryAgain = false;
            String shotCoordinatesGivenByUser = sc.nextLine().toUpperCase();
            shotCoordinates = Service.convertShootCoordinatesGivenByUserToArray(shotCoordinatesGivenByUser);
            if (shotCoordinates[0] < 1 || shotCoordinates[1] < 1 || shotCoordinates[0] > 10 || shotCoordinates[1] > 10) {
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                tryAgain = true;
            }
        } while (tryAgain);
        return shotCoordinates;
    }

    private void initLogger() throws IOException {
        Handler fileHandler = new FileHandler("default.log");

        fileHandler.setLevel(Level.ALL);
        fileHandler.setFormatter(new SimpleFormatter());
        for (Handler handler : logger.getHandlers()) {
            logger.removeHandler(handler);
        }
        logger.addHandler(fileHandler);
        logger.setLevel(Level.ALL);
        logger.setUseParentHandlers(false);
    }

    public void showTwoBoards(Board boardWithHitsMarked, Board boardWithPlayerShips) {
        System.out.print(boardWithHitsMarked);
        System.out.println(("-").repeat(21));
        System.out.println(boardWithPlayerShips);
    }

    private void switchPlayer() {
        System.out.println("Press Enter and pass the move to another player");
        sc.nextLine();
    }
}