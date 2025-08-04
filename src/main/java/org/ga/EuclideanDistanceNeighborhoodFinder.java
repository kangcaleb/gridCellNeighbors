package org.ga;

public class EuclideanDistanceNeighborhoodFinder extends DistanceNeighborFinder {

    @Override
    public double getDistance(GridCoordinate coordinate) {
        double sumOfSquaredDifferences = Math.pow(coordinate.getDeltaY(), 2) + Math.pow(coordinate.getDeltaX(), 2);
        return Math.sqrt(sumOfSquaredDifferences);
    }
}
