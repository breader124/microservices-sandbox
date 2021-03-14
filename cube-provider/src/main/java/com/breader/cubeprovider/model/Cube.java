package com.breader.cubeprovider.model;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

@Component
@Getter
public class Cube {
    private final List<List<List<String>>> space;
    private final List<Point> takenPoints;

    private String letter;
    private final int cubeSize;
    private final String emptyLetter;

    private final JsonFormatter jsonFormatter;
    private final StringFormatter stringFormatter;

    public Cube(@Value("${cube.size}") int cubeSize,
                @Value("${space.size.multiplier}") int sizeMultiplier,
                @Value("${cube.letter.nonempty}") String letter,
                @Value("${cube.letter.empty}") String emptyLetter,
                @Autowired JsonFormatter jsonFormatter,
                @Autowired StringFormatter stringFormatter) {
        this.space = new ArrayList<>();
        this.takenPoints = new ArrayList<>();
        this.cubeSize = cubeSize;
        this.letter = letter;
        this.emptyLetter = emptyLetter;
        this.jsonFormatter = jsonFormatter;
        this.stringFormatter = stringFormatter;
        initSpace(cubeSize * sizeMultiplier);
    }

    private void initSpace(int requiredSpaceAmount) {
        for (int i = 0; i < requiredSpaceAmount; i++) {
            if (i >= cubeSize) {
                letter = nextAlphabetLetter(letter);
            }
            space.add(new ArrayList<>());
            for (int j = 0; j < requiredSpaceAmount; j++) {
                space.get(i).add(new ArrayList<>());
                for (int k = 0; k < requiredSpaceAmount; k++) {
                    addLetterOnCords(i, j, k);
                }
            }
        }
    }

    private String nextAlphabetLetter(String currentOne) {
        if (currentOne.length() > 1) {
            throw new IllegalArgumentException("Provided String is not one letter length");
        }

        char[] codePoints = {(char) currentOne.codePointAt(0)};
        ++codePoints[0];
        return new String(codePoints);
    }

    private void addLetterOnCords(int x, int y, int z) {
        List<String> row = space.get(x).get(y);
        if (isFieldForLetter(x, y, z)) {
            row.add(letter);
            int absXPos = x - cubeSize;
            int absYPos = y - cubeSize;
            int absZPos = z - cubeSize;
            takenPoints.add(new Point(String.valueOf(letter), absXPos, absYPos, absZPos));
        } else {
            row.add(emptyLetter);
        }
    }

    private boolean isFieldForLetter(int x, int y, int z) {
        IntStream coordinates = IntStream.of(x, y, z);
        return coordinates.allMatch(coordinate -> coordinate >= cubeSize && coordinate < 2 * cubeSize);
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
                takenPoints.clear();
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
            setPointLetter(oldPoint, newPoint);
            takenPoints.add(newPoint);
        }
    }

    private void setPointLetter(Point oldPoint, Point newPoint) {
        int spaceSize = space.size();
        int xOld = (int) (Math.round(oldPoint.getXPos()) + cubeSize);
        int yOld = (int) (Math.round(oldPoint.getYPos()) + cubeSize);
        int zOld = (int) (Math.round(oldPoint.getZPos()) + cubeSize);
        int xNew = (int) (Math.round(newPoint.getXPos()) + cubeSize);
        int yNew = (int) (Math.round(newPoint.getYPos()) + cubeSize);
        int zNew = (int) (Math.round(newPoint.getZPos()) + cubeSize);

        IntStream coordinates = IntStream.of(xOld, yOld, zOld, xNew, yNew, zNew);
        if (!coordinates.allMatch(coordinate -> coordinate >= 0 && coordinate < spaceSize)) {
            return;
        }
        space.get(xOld).get(yOld).set(zOld, emptyLetter);
        space.get(xNew).get(yNew).set(zNew, newPoint.getName());
    }

    public String asJson() {
        return jsonFormatter.asJson(this);
    }

    public String asString() {
        return stringFormatter.asString(this);
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
