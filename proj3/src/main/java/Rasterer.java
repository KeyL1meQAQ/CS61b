import java.util.HashMap;
import java.util.Map;

/**
 * This class provides all code necessary to take a query box and produce
 * a query result. The getMapRaster method must return a Map containing all
 * seven of the required fields, otherwise the front end code will probably
 * not draw the output correctly.
 */
public class Rasterer {

    private double[] lonDPPs;

    public Rasterer() {
        // YOUR CODE HERE
        lonDPPs = new double[8];
        double lonDPP0 = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / MapServer.TILE_SIZE;
        lonDPPs[0] = lonDPP0;
        for (int i = 1; i < 8; i++) {
            lonDPPs[i] = lonDPPs[i - 1] / 2;
        }
    }

    /**
     * Takes a user query and finds the grid of images that best matches the query. These
     * images will be combined into one big image (rastered) by the front end. <br>
     *
     *     The grid of images must obey the following properties, where image in the
     *     grid is referred to as a "tile".
     *     <ul>
     *         <li>The tiles collected must cover the most longitudinal distance per pixel
     *         (LonDPP) possible, while still covering less than or equal to the amount of
     *         longitudinal distance per pixel in the query box for the user viewport size. </li>
     *         <li>Contains all tiles that intersect the query bounding box that fulfill the
     *         above condition.</li>
     *         <li>The tiles must be arranged in-order to reconstruct the full image.</li>
     *     </ul>
     *
     * @param params Map of the HTTP GET request's query parameters - the query box and
     *               the user viewport width and height.
     *
     * @return A map of results for the front end as specified: <br>
     * "render_grid"   : String[][], the files to display. <br>
     * "raster_ul_lon" : Number, the bounding upper left longitude of the rastered image. <br>
     * "raster_ul_lat" : Number, the bounding upper left latitude of the rastered image. <br>
     * "raster_lr_lon" : Number, the bounding lower right longitude of the rastered image. <br>
     * "raster_lr_lat" : Number, the bounding lower right latitude of the rastered image. <br>
     * "depth"         : Number, the depth of the nodes of the rastered image <br>
     * "query_success" : Boolean, whether the query was able to successfully complete; don't
     *                    forget to set this to true on success! <br>
     */
    public Map<String, Object> getMapRaster(Map<String, Double> params) {
        // System.out.println(params);
        Map<String, Object> results = new HashMap<>();
        double ullon = params.get("ullon");
        double ullat = params.get("ullat");
        double lrlon = params.get("lrlon");
        double lrlat = params.get("lrlat");
        double width = params.get("w");
        double height = params.get("h");

        if (ullon >= lrlon) {
            results.put("query_success", false);
            return results;
        } else if (ullon >= MapServer.ROOT_LRLON || lrlon <= MapServer.ROOT_ULLON
            || ullat <= MapServer.ROOT_LRLAT || lrlat >= MapServer.ROOT_ULLAT) {
            results.put("query_success", false);
            return results;
        }

        //Calculate the depth of image
        double lonDPP = (lrlon - ullon) / width;
        int depth = findDepth(lonDPP);
        results.put("depth", depth);

        //calculate the images usable
        int startTileX = startX(depth, ullon);
        int endTileX = endX(depth, lrlon);
        int startTileY = startY(depth, ullat);
        int endTileY = endY(depth, lrlat);
        String[][] grid = new String[endTileY - startTileY + 1][endTileX - startTileX + 1];
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[0].length; j++) {
                grid[i][j] = "d" + depth + "_x" + (startTileX + j) + "_y"
                        + (startTileY + i) + ".png";
            }
        }
        results.put("render_grid", grid);

        //Calculate the ul and lr coordinate of the grid
        double gapHorizontal = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        double gapVertical = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        double rasterUllon = MapServer.ROOT_ULLON + startTileX * gapHorizontal;
        double rasterLrlon = MapServer.ROOT_ULLON + (endTileX + 1) * gapHorizontal;
        double rasterUllat = MapServer.ROOT_ULLAT - startTileY * gapVertical;
        double rasterLrlat = MapServer.ROOT_ULLAT - (endTileY + 1) * gapVertical;
        results.put("raster_ul_lon", rasterUllon);
        results.put("raster_lr_lon", rasterLrlon);
        results.put("raster_ul_lat", rasterUllat);
        results.put("raster_lr_lat", rasterLrlat);

        //successful query
        results.put("query_success", true);

        return results;
    }

    private int findDepth(double lonDPP) {
        if (lonDPP > this.lonDPPs[0]) {
            return 0;
        }
        for (int i = 1; i < 8; i++) {
            if (lonDPP <= this.lonDPPs[i - 1] && lonDPP >= this.lonDPPs[i]) {
                return i;
            }
        }
        return 7;
    }

    private int startX(int depth, double ullon) {
        double gap = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int startTile = (int) Math.floor((ullon - MapServer.ROOT_ULLON) / gap);
        return startTile;
    }

    private int endX(int depth, double lrlon) {
        double gap = (MapServer.ROOT_LRLON - MapServer.ROOT_ULLON) / Math.pow(2, depth);
        int endTile = (int) Math.floor((lrlon - MapServer.ROOT_ULLON) / gap);
        return endTile;
    }

    private int startY(int depth, double ullat) {
        double gap = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int startTile = (int) Math.floor((MapServer.ROOT_ULLAT - ullat) / gap);
        return startTile;
    }

    private int endY(int depth, double lrlat) {
        double gap = (MapServer.ROOT_ULLAT - MapServer.ROOT_LRLAT) / Math.pow(2, depth);
        int endTile = (int) Math.floor((MapServer.ROOT_ULLAT - lrlat) / gap);
        return endTile;
    }
}
