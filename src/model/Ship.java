package model;

import java.util.LinkedList;
import java.util.List;

public class Ship {

    private List<int[]> blocks;
    private final String shipName;
    private int shipSizeInCells;
    private boolean isActive;

    public Ship(String shipName, int shipSizeInCells) {
        this.shipName = shipName;
        this.shipSizeInCells = shipSizeInCells;
        this.isActive = false;
    }

    public static List<int[]> convertCoordinatesToListOfBLocks(int[][] shipCandidateCoordinates) {
        int[] front = shipCandidateCoordinates[0];
        int[] back = shipCandidateCoordinates[1];
        List<int[]> blocks = new LinkedList<>();

        for (int i = Math.min(front[0], back[0]); i <= Math.max(front[0], back[0]); i++) {
            for (int j = Math.min(front[1], back[1]); j <= Math.max(front[1], back[1]); j++) {
                blocks.add(new int[]{i, j});
            }
        }
        return blocks;
    }

    public boolean isShipHasCorrectLocation(int[][] coordinates) {
        return (coordinates[0][0] != coordinates[1][0] && coordinates[0][1] != coordinates[1][1]) ? false : true;
    }

    public boolean isShipHasCorrectLength(int[][] coordinates, int properSize) {
        int[] front = coordinates[0];
        int[] back = coordinates[1];
        int length = (Math.abs(back[0] - front[0]) + 1) * (Math.abs(back[1] - front[1]) + 1);
        return properSize == length ? true : false;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getShipName() {
        return shipName;
    }

    public int getShipSizeInCells() {
        return shipSizeInCells;
    }

    public List<int[]> getBlocks() {
        return blocks;
    }

    public void setBlocks(List<int[]> blocks) {
        this.blocks = blocks;
    }

    public void shipHit() {
        if (isActive== true) shipSizeInCells--;
        Game.logger.fine(String.valueOf(shipSizeInCells));
    }

    public boolean ifShipSank() {
        if (shipSizeInCells == 0) {
            isActive=false;
            return true;
        } else return false;
    }
}