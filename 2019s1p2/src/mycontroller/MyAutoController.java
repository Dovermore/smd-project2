package mycontroller;

import controller.CarController;
import mycontroller.Strategy.IStrategy;
import mycontroller.Strategy.StrategyFactory;
import swen30006.driving.Simulation;
import utilities.Coordinate;
import world.Car;
import world.World;

/**
 * Xulin Yang, 904904
 *
 * @create 2019-05-21 20:33
 * description: our ai car controller
 **/

public class MyAutoController extends CarController {


    /**
     * customized map information holder and updater
     */
    private MapRecorder mapRecorder;

    /**
     * drive strategy to search next location to go to
     */
    private IStrategy driveStrategy;

    /**
     * car's remaining fuel
     */
//    private float carFuel;

    private Coordinate previousPosition;

    private float maxHealth;

	/**
	 * @param car : car object
	 */
	public MyAutoController(Car car) {
	    super(car);

	    this.mapRecorder = new MapRecorder();
	    this.mapRecorder.updateInitialMap(super.getMap());

        driveStrategy = StrategyFactory.getInstance()
                                        .createConserveStrategy(
                                                Simulation.toConserve());

//        carFuel = car.getFuel();

        maxHealth = getHealth();

        previousPosition = new Coordinate(getPosition());
	}

    /**
     * update the car status to the controller and decide car's next state
     */
	@Override
	public void update() {
	    if (getHealth() > maxHealth) {
	        maxHealth = getHealth();
        }

	    mapRecorder.updateMapRecorder(super.getView());
        System.out.println();
//	    mapRecorder.print();

        Coordinate carPosition = new Coordinate(getPosition());

		Coordinate next = driveStrategy.getNextCoordinate(mapRecorder,
                                                          carPosition,
                                                          maxHealth,
                                                          getHealth(),
                                                          0,
                                                          getVelocity(),
                                                          getOrientation(),
                                                          numParcels() == numParcelsFound());
		makeMove(carPosition, next);
	}

	private float getVelocity() {
        if (getSpeed() == 0) {
            return 0;
        }

	    switch (getOrientation()) {
            case NORTH:

            case SOUTH:

            case WEST:

            case EAST:
        }
    }

    /**
     * based on the current location and next location to go, apply the car's
     * reaction to the next location to go to
     * @param from : current location
     * @param to :   next location to go
     */
	private void makeMove(Coordinate from, Coordinate to) {
	    assert(to != null);

        System.out.println(from.toString() + " -> " + to.toString() +
                "(" + mapRecorder.getTileAdapter(to).getType().toString() + ")");

        /* spend 1 fuel for an attempt move */
        // TODO a constant 1?
//        carFuel -= 1;

        if (from.equals(to)) {
            applyBrake();
            /* brake has no move attempt thus no fuel consumption */
            // TODO a constant 1?
//            carFuel += 1;
            return;
        } else if (from.x < to.x) {
            switch (getOrientation()) {
                case NORTH:
                    applyForwardAcceleration();
                    turnRight();
                    return;

                case SOUTH:
                    applyForwardAcceleration();
                    turnLeft();
                    return;

                case EAST:
                    applyForwardAcceleration();
                    return;

                case WEST:
//                    applyBrake();
                    applyReverseAcceleration();
                    return;
            }

        } else if (from.x > to.x) {
            switch (getOrientation()) {
                case NORTH:
                    applyForwardAcceleration();
                    turnLeft();
                    return;

                case SOUTH:
                    applyForwardAcceleration();
                    turnRight();
                    return;

                case EAST:
//                    applyBrake();
                    applyReverseAcceleration();
                    return;

                case WEST:
                    applyForwardAcceleration();
                    return;
            }

        } else if (from.y < to.y) {
            switch (getOrientation()) {

                case NORTH:
                    applyForwardAcceleration();
                    return;

                case SOUTH:
//                    applyBrake();
                    applyReverseAcceleration();
                    return;

                case EAST:
                    applyForwardAcceleration();
                    turnLeft();
                    return;

                case WEST:
                    applyForwardAcceleration();
                    turnRight();
                    return;
            }

        } else if (from.y > to.y) {
            switch (getOrientation()) {

                case NORTH:
//                    applyBrake();
                    applyReverseAcceleration();
                    return;

                case SOUTH:
                    applyForwardAcceleration();
                    return;

                case EAST:
                    applyForwardAcceleration();
                    turnRight();
                    return;

                case WEST:
                    applyForwardAcceleration();
                    turnLeft();
                    return;
            }
        }

        /* debug */
        System.out.println("Invalid");
        System.exit(1);
    }
}
