package model;

import java.util.*;

public class Board {

    private String[][] board;
    private List<Ship> ships;

    public Board createEmptyBoard() {
        this.board = new String[11][11];
        Arrays.stream(this.board).forEach(a -> Arrays.fill(a, "~"));
        this.board[0] = new String[]{" ", "1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
        int i = 0;
        for (String firstColumn : " ABCDEFGHIJ".split("")) {
            this.board[i++][0] = firstColumn;
        }
        return this;
    }

    public void putAllShipsOnTheBoardTest() {
        createFleetOfShips();
        int i = 0;
        List<String> position = Arrays.asList("F3 F7", "A1 D1", "J10 J8", "B9 D9", "I2 J2");
        for (Ship ship : ships) {
            putTheShipOnTheBoard(ship, Service.convertCoordinatesGivenByUserTo2DArray(position.get(i++)));
            System.out.println(this);
        }
    }

    public void putAllShipsOnTheBoard() {
        createFleetOfShips();
        Scanner sc = new Scanner(System.in);

        for (Ship ship : ships) {

            int[][] coordinatesOfPotentialShip;
            System.out.printf("Enter the coordinates of %s (%d cells):\n", ship.getShipName(), ship.getShipSizeInCells());

            boolean isCorrect;
            do {
                String alphaCoordinatesGivenByUser = sc.nextLine().toUpperCase();
                coordinatesOfPotentialShip = Service.convertCoordinatesGivenByUserTo2DArray(alphaCoordinatesGivenByUser);

                isCorrect = ship.isShipHasCorrectLocation(coordinatesOfPotentialShip);
                if (!isCorrect) {
                    System.out.println(errorMessage("locationErr", ship.getShipName()));
                    continue;
                }

                isCorrect = ship.isShipHasCorrectLength(coordinatesOfPotentialShip, ship.getShipSizeInCells());
                if (!isCorrect) {
                    System.out.println(errorMessage("lengthErr", ship.getShipName()));
                    continue;
                }

                isCorrect = isCollisionDetectedOnBoard(coordinatesOfPotentialShip);
                if (!isCorrect) {
                    System.out.println(errorMessage("tooCloseErr", ship.getShipName()));
                    continue;
                }
            }
            while (!isCorrect);

            putTheShipOnTheBoard(ship, coordinatesOfPotentialShip);
            System.out.println(this);
        }
    }

    private void createFleetOfShips() {
        this.ships = new ArrayList<>();
        ships.add(new Ship("the Aircraft Carrier", 5));
        ships.add(new Ship("the Battleship", 4));
        ships.add(new Ship("the Submarine", 3));
        ships.add(new Ship("the Cruiser", 3));
        ships.add(new Ship("the Destroyer", 2));
    }

    public void putTheShipOnTheBoard(Ship ship, int[][] coordinatesOfPotentialShip) {
        String shipIsHere = "O";
        ship.setBlocks(Ship.convertCoordinatesToListOfBLocks(coordinatesOfPotentialShip));

        ship.getBlocks().forEach(o -> this.board[o[0]][o[1]] = shipIsHere);
        ship.setActive(true);
    }

    public boolean isCollisionDetectedOnBoard(int[][] shipCandidateCoordinates) {
        List<int[]> blocks = Ship.convertCoordinatesToListOfBLocks(shipCandidateCoordinates);

        for (Ship ship : ships) {
            if (ship.isActive()) {
                for (int[] block : ship.getBlocks()) {
                    for (int[] shipToCheckBlock : blocks) {
                        if (Math.abs(shipToCheckBlock[0] - block[0]) <= 1 && Math.abs(shipToCheckBlock[1] - block[1]) <= 1) return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();

        Arrays.stream(board)
                .forEach(a -> sb.append(Arrays.toString(a).replaceAll("[\\[,\\]]", "")).append("\n"));
        return sb.toString();
    }

    private String errorMessage(String errorCode, String additionalParameters) {
        return switch (errorCode) {
            case "lengthErr" -> "Error! Wrong length of the " + additionalParameters + "! Try again:\n";
            case "locationErr" -> "Error! Wrong ship location! Try again:\n";
            case "tooCloseErr" -> "Error! You placed it too close to another one. Try again:\n";
            default -> throw new IllegalArgumentException();
        };
    }

    public Optional<Ship> fireAndReturnResultIfShipWasHit(int[] coordinates) {
        for (Ship ship : ships) {
            for (int[] block : ship.getBlocks()) {
                if (Arrays.equals(block, coordinates)) {
                    ship.shipHit();
                    return Optional.of(ship);
                }
            }
        }
        return Optional.empty();
    }

    public void putSignOnBoard(int[] coordinates, String sign) {
        board[coordinates[0]][coordinates[1]] = sign;
    }

    public String[][] getBoard() {
        return board;
    }
}