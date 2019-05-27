package mycontroller.Strategy;

import mycontroller.GraphAlgorithm.Dijkstra;
import mycontroller.GraphAlgorithm.DijkstraPair;
import mycontroller.GraphAlgorithm.Node;
import mycontroller.MapRecorder;
import mycontroller.TileAdapter.ITileAdapter;
import mycontroller.TileStatus;
import utilities.Coordinate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-05-26 19:41
 * description:
 **/

public class HealStrategy implements IStrategy {
    private Comparator<Node> comparator;

    public HealStrategy(Comparator<Node> comparator) {
        this.comparator = comparator;
    }

    @Override
    public Coordinate getNextCoordinate(MapRecorder map,
                                        Coordinate carPosition,
                                        float maxHealth,
                                        float health,
                                        float fuel,
                                        boolean enoughParcel) {
        ArrayList<Coordinate> heals = map.getCoordinates(ITileAdapter.TileType.WATER);
        heals.addAll(map.getCoordinates(ITileAdapter.TileType.HEALTH));

        if (heals.isEmpty()) {
            return null;
        }


        DijkstraPair res = Dijkstra.dijkstra(map,
                carPosition,
                maxHealth,
                health,
                fuel,
                comparator,
                new ArrayList<>(Collections.singletonList(TileStatus.EXPLORED)));

        return choosePath(heals, res, comparator, maxHealth);
    }
}