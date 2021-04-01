import edu.princeton.cs.algs4.Picture;

import java.awt.*;

public class SeamCarver {
    private Picture picture;

    private double[][] pictureArr;

    /**
     * CONSTRUCTOR
     */
    public SeamCarver(Picture picture) {
        this.picture = picture;
    }

    /**
     * @return Current picture
     */
    public Picture picture() {
        return picture;
    }

    /**
     * @return The width of current picture
     */
    public int width() {
        return picture.width();
    }

    /**
     * @return The height of current picture
     */
    public int height() {
        return picture.height();
    }

    /**
     * @return The energy of current picture
     */
    public double energy(int x, int y) {
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IndexOutOfBoundsException();
        }

        Color left = picture.get(getLeft(x), y);
        Color right = picture.get(getRight(x), y);
        Color up = picture.get(x, getUp(y));
        Color down = picture.get(x, getDown(y));
        int rx = right.getRed() - left.getRed();
        int gx = right.getGreen() - left.getGreen();
        int bx = right.getBlue() - left.getBlue();
        int ry = up.getRed() - down.getRed();
        int gy = up.getGreen() - down.getGreen();
        int by = up.getBlue() - down.getBlue();
        double dx2 = Math.pow(rx, 2) + Math.pow(gx, 2) + Math.pow(bx, 2);
        double dy2 = Math.pow(ry, 2) + Math.pow(gy, 2) + Math.pow(by, 2);
        return dx2 + dy2;
    }

    private int getLeft(int x) {
        return x - 1 < 0 ? width() - 1 : x - 1;
    }

    private int getRight(int x) {
        return x + 1 == width() ? 0 : x + 1;
    }

    private int getUp(int y) {
        return y - 1 < 0 ? height() - 1 : y - 1;
    }

    private int getDown(int y) {
        return y + 1 == height() ? 0 : y + 1;
    }

    public int[] findVerticalSeam() {
        pictureArr = new double[height()][width()];
        int[][] from = new int[height()][width()];
        for (int i = 0; i < width(); i++) {
            pictureArr[0][i] = energy(i, 0);
        }
        for (int i = 1; i < height(); i++) {
            for (int j = 0; j < width(); j++) {
                if (j == 0) {
                    int minIndex = getMinIndex(i - 1, j, j + 1, -1);
                    pictureArr[i][j] = energy(j, i) + pictureArr[i - 1][minIndex];
                    from[i][j] = minIndex;
                } else if (j == width() - 1) {
                    int minIndex = getMinIndex(i - 1, j - 1, j, -1);
                    pictureArr[i][j] = energy(j, i) + pictureArr[i - 1][minIndex];
                    from[i][j] = minIndex;
                } else {
                    int minIndex = getMinIndex(i - 1, j - 1, j, j + 1);
                    pictureArr[i][j] = energy(j, i) + pictureArr[i - 1][minIndex];
                    from[i][j] = minIndex;
                }
            }
        }
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < width(); i++) {
            if (pictureArr[height() - 1][i] < min) {
                min = pictureArr[height() - 1][i];
                index = i;
            }
        }
        int[] result = new int[height()];
        result[height() - 1] = index;
        for (int i = height() - 1, j = from[i][index]; i > 0; i--, j = from[i][j]) {
            result[i - 1] = j;
        }
        return result;
    }

    public int[] findHorizontalSeam() {
        Picture picture = new Picture(height(), width());
        Picture originPic = this.picture;
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                picture.set(j, i, originPic.get(i, j));
            }
        }
        this.picture = picture;
        int[] result = findVerticalSeam();
        this.picture = originPic;
        return result;
    }

    public void removeHorizontalSeam(int[] seam) {
        SeamRemover.removeHorizontalSeam(picture, seam);
    }

    public void removeVerticalSeam(int[] seam) {
        SeamRemover.removeVerticalSeam(picture, seam);
    }

    private int getMinIndex(int row, int firstJ, int secondJ, int thirdJ) {
        if (thirdJ < 0) {
            if (pictureArr[row][firstJ] < pictureArr[row][secondJ]) {
                return firstJ;
            }
            return secondJ;
        }
        double[] compare = new double[]{pictureArr[row][firstJ], pictureArr[row][secondJ],
                pictureArr[row][thirdJ]};
        double min = Double.MAX_VALUE;
        int index = -1;
        for (int i = 0; i < 3; i++) {
            if (compare[i] < min) {
                min = compare[i];
                index = i + firstJ;
            }
        }
        return index;
    }
}
