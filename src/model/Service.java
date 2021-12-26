package model;

public class Service {

    public static int[][] convertCoordinatesGivenByUserTo2DArray(String alphaCoordinatesGivenByUser) {
        String firstCoordinate = alphaCoordinatesGivenByUser.split(" ")[0];
        String secondCoordinate = alphaCoordinatesGivenByUser.split(" ")[1];

        int[] frontArrayCoordinates = convertCoordinatesFromLettersToDigital(firstCoordinate);
        int[] backArrayCoordinates = convertCoordinatesFromLettersToDigital(secondCoordinate);

        return new int[][]{frontArrayCoordinates, backArrayCoordinates};
    }

    public static int[] convertShootCoordinatesGivenByUserToArray(String alphaShootCoordinatesGivenByUser) {
        return convertCoordinatesFromLettersToDigital(alphaShootCoordinatesGivenByUser);
    }


    private static int[] convertCoordinatesFromLettersToDigital(String letterCoordinate) {
        int row = letterCoordinate.charAt(0) - 64;
        int column = Integer.parseInt(letterCoordinate.replaceFirst(".", ""));
        return new int[]{row, column};
    }
}