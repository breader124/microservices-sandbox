package com.breader.cubeprovider.model;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Cube {
    private final List<List<List<String>>> space;
    private final List<Point> takenPoints;
    private final String emptyLetter = "*";

    public Cube(int cubeSize, String letter) {
        space = new ArrayList<>();
        takenPoints = new ArrayList<>();
        initSpace(cubeSize * 2, letter);
    }

    private void initSpace(int requiredSpaceAmount, String letter) {
        int cubeSize = requiredSpaceAmount / 2;
        for (int i = 0; i < requiredSpaceAmount; i++) {
            if (i >= cubeSize) {
                letter = nextAlphabetLetter(letter);
            }
            space.add(new ArrayList<>());
            for (int j = 0; j < requiredSpaceAmount; j++) {
                space.get(i).add(new ArrayList<>());
                for (int k = 0; k < requiredSpaceAmount; k++) {
                    List<String> row = space.get(i).get(j);
                    if (i >= cubeSize && j >= cubeSize && k >= cubeSize) {
                        row.add(letter);
                        takenPoints.add(new Point(String.valueOf(letter), i, j, k));
                    } else {
                        row.add(emptyLetter);
                    }
                }
            }
        }
    }

    private String nextAlphabetLetter(String currentOne) {
        if (currentOne.length() > 1) {
            throw new IllegalArgumentException("Provided String is not one letter length");
        }

        char[] codePoints = { (char) currentOne.codePointAt(0) };
        ++codePoints[0];
        return new String(codePoints);
    }

    public void rotateX(double angle) {
        NumericMatrix xRotationMatrix = RotationMatrixFactory.getInstance("x", angle);
        rotate(xRotationMatrix);
    }

    public void rotateY(double angle) {
        NumericMatrix xRotationMatrix = RotationMatrixFactory.getInstance("y", angle);
        rotate(xRotationMatrix);
    }

    public void rotateZ(double angle) {
        NumericMatrix xRotationMatrix = RotationMatrixFactory.getInstance("z", angle);
        rotate(xRotationMatrix);
    }

    private void rotate(NumericMatrix rotationMatrix) {
        ExecutorService service = Executors.newCachedThreadPool();
        List<PointPosChange> pointPosChangeList = startRotationComputing(service, rotationMatrix);
        try {
            service.shutdown();
            boolean result = service.awaitTermination(1, TimeUnit.SECONDS);
            if (result) {
                applyPosChanges(pointPosChangeList);
            } else {
                throw new RuntimeException("Rotation computation not completed in given time");
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            throw new RuntimeException("Interrupted while computing rotation");
        }
    }

    private List<PointPosChange> startRotationComputing(ExecutorService service, NumericMatrix rotationMatrix) {
        List<PointPosChange> pointPosChangeList = new ArrayList<>();
        takenPoints.forEach(oldPoint -> {
            Future<Point> newPoint = service.submit(() -> {
                List<Double> pos = List.of(oldPoint.getXPos(), oldPoint.getYPos(), oldPoint.getZPos());
                List<Double> newPos = rotationMatrix.multiplyByVector(pos);
                return new Point(oldPoint.getName(), newPos.get(0), newPos.get(1), newPos.get(2));
            });
            pointPosChangeList.add(new PointPosChange(oldPoint, newPoint));
        });

        return pointPosChangeList;
    }

    private void applyPosChanges(List<PointPosChange> posChangeList) throws ExecutionException, InterruptedException {
        for (PointPosChange posChange : posChangeList) {
            Point oldPoint = posChange.getOldPoint();
            Point newPoint = posChange.getNewPoint().get();
            removePoint(oldPoint);
            addPoint(newPoint);
        }
    }

    // TODO
    private void removePoint(Point p) {
        int spaceSize = space.size();
        int x = ((int) p.getXPos()) + spaceSize;
        int y = ((int) p.getYPos()) + spaceSize;
        int z = ((int) p.getZPos()) + spaceSize;

        if (x > spaceSize || y > spaceSize || z > spaceSize || x < 0 || y < 0 || z < 0) {
            return;
        }
        space.get(x).get(y).set(z, emptyLetter);
    }

    // TODO
    private void addPoint(Point p) {
        int spaceSize = space.size();
        int x = ((int) p.getXPos()) + spaceSize;
        int y = ((int) p.getYPos()) + spaceSize;
        int z = ((int) p.getZPos()) + spaceSize;

        if (x > spaceSize || y > spaceSize || z > spaceSize || x < 0 || y < 0 || z < 0) {
            return;
        }
        space.get(x).get(y).set(z, p.getName());
    }

    @Override
    public String toString() {
        List<List<String>> twoDimView = getEmptyTwoDimView();
        for (int i = 0; i < space.size(); i++) {
            for (int j = 0; j < space.size(); j++) {
                for (int k = 0; k < space.size(); k++) {
                    String possibleToShow = space.get(i).get(j).get(k);
                    if (twoDimView.get(i).get(j).equals(emptyLetter)) {
                        twoDimView.get(i).set(j, possibleToShow);
                    }
                }
            }
        }

        return twoDimToString(twoDimView);
    }

    private List<List<String>> getEmptyTwoDimView() {
        List<List<String>> twoDimView = new ArrayList<>();
        for (int i = 0; i < space.size(); i++) {
            twoDimView.add(new ArrayList<>());
            for (int j = 0; j < space.size(); j++) {
                twoDimView.get(i).add(emptyLetter);
            }
        }

        return twoDimView;
    }

    private String twoDimToString(List<List<String>> twoDimView) {
        StringBuilder builder = new StringBuilder();
        for (List<String> xAxis : twoDimView) {
            for (String letter : xAxis) {
                builder.append(letter);
                builder.append(' ');
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
