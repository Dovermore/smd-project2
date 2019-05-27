package mycontroller.Strategy;

import mycontroller.GraphAlgorithm.Dijkstra;
import mycontroller.GraphAlgorithm.DijkstraPair;
import mycontroller.GraphAlgorithm.Node;
import mycontroller.MapRecorder;
import mycontroller.TileAdapter.ITileAdapter;
import mycontroller.TileStatus;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-05-26 14:15
 * description:
 **/

public class ExitStrategy implements IStrategy {
    private Comparator<Node> comparator;

    public ExitStrategy(Comparator<Node> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Coordinate getNextCoordinate(MapRecorder map,
                                        Coordinate carPosition,
                                        float maxHealth,
                                        float health,
                                        float fuel,
                                        boolean enoughParcel) {
        ArrayList<Coordinate> finishes = map.getCoordinates(ITileAdapter.TileType.FINISH);
        assert(!finishes.isEmpty());


        DijkstraPair res = Dijkstra.dijkstra(map,
                carPosition,
                maxHealth,
                health,
                fuel,
                comparator,
                new ArrayList<>(Collections.singletonList(TileStatus.EXPLORED)));

        Coordinate next = choosePath(finishes, res, comparator, maxHealth);

        if (next == null) {
            res = Dijkstra.dijkstra(map,
                    carPosition,
                    maxHealth,
                    health,
                    fuel,
                    comparator,
                    new ArrayList<>(Arrays.asList(TileStatus.UNEXPLORED, TileStatus.EXPLORED)));
            next = choosePath(finishes, res, comparator, maxHealth);
        }

        return next;
    }
}