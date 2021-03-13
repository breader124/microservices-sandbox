package com.breader.cubeprovider.model;

import java.util.ArrayList;
import java.util.List;

public class Cube {
    private final List<List<List<Character>>> space;
    private final char emptyLetter = '*';

    public Cube(int cubeSize, char letter) {
        space = new ArrayList<>();
        initSpace(cubeSize * 2, letter);
    }

    private void initSpace(int requiredSpaceAmount, char letter) {
        int cubeSize = requiredSpaceAmount / 2;
        for (int i = 0; i < requiredSpaceAmount; i++) {
            if (i >= cubeSize) {
                letter++;
            }
            space.add(new ArrayList<>());
            for (int j = 0; j < requiredSpaceAmount; j++) {
                space.get(i).add(new ArrayList<>());
                for (int k = 0; k < requiredSpaceAmount; k++) {
                    List<Character> row = space.get(i).get(j);
                    if (i >= cubeSize && j >= cubeSize && k >= cubeSize) {
                        row.add(letter);
                    } else {
                        row.add(emptyLetter);
                    }
                }
            }
        }
    }

    public List<List<List<Character>>> rotateX(double angle) {
        return null;
    }

    public List<List<List<Character>>> rotateY(double angle) {
        return null;
    }

    public List<List<List<Character>>> rotateZ(double angle) {
        return null;
    }

    @Override
    public String toString() {
        List<List<Character>> twoDimView = getEmptyTwoDimView();
        for (int i = 0; i < space.size(); i++) {
            for (int j = 0; j < space.size(); j++) {
                for (int k = 0; k < space.size(); k++) {
                    char possibleToShow = space.get(i).get(j).get(k);
                    if (twoDimView.get(i).get(j).equals(emptyLetter)) {
                        twoDimView.get(i).set(j, possibleToShow);
                    }
                }
            }
        }

        return twoDimToString(twoDimView);
    }

    private List<List<Character>> getEmptyTwoDimView() {
        List<List<Character>> twoDimView = new ArrayList<>();
        for (int i = 0; i < space.size(); i++) {
            twoDimView.add(new ArrayList<>());
            for (int j = 0; j < space.size(); j++) {
                twoDimView.get(i).add(emptyLetter);
            }
        }

        return twoDimView;
    }

    private String twoDimToString(List<List<Character>> twoDimView) {
        StringBuilder builder = new StringBuilder();
        for (List<Character> xAxis : twoDimView) {
            for (Character letter : xAxis) {
                builder.append(letter);
                builder.append(' ');
            }
            builder.append("\n");
        }

        return builder.toString();
    }
}
