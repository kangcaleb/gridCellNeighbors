package org.ga;

public class ManhattanDistanceNeighborFinder extends DistanceNeighborFinder {
    @Override
    public double getDistance(GridCoordinate coordinate) {
        return Math.abs(coordinate.getDeltaY()) + Math.abs(coordinate.getDeltaX());
    }
}
