package laneChanger;

import static org.junit.Assert.assertEquals;


import org.junit.jupiter.api.Test;

class LaneChanger {
	
	private Street street = new Street();
	private Car car = new Car(0, 0, street);
	private static final int END_OF_THE_ROAD = 99;
	private static final int MIDDLE_OF_THE_ROAD = 50;
	private static final int RIGHT_LANE = 0;
	private static final int MIDDLE_LANE = 1;
	private static final int LEFT_LANE = 2;
	private static final int CAR_DEFAULT_SPEED = 1;
	private static final int LANE_WIDTH = 5;
	private static final int SENSOR_UPPER_BOUNDS = 50;
	/**	This tests if the cars location can be found when it is at a random stretch of the road	*/
	@Test
	void testLocation() {
		street.placeCar(car, MIDDLE_OF_THE_ROAD, MIDDLE_LANE);
		assertEquals(car.whereIs(), MIDDLE_OF_THE_ROAD);
	}
	/** This tests if the car's location changes by the correct amount when the moveForward method is called */
	@Test
	void testMoveForward() {
		street = new Street();
		car = new Car(0, 0, street);
		car.moveForward(street);
		assertEquals(car.whereIs(), CAR_DEFAULT_SPEED);
		assertEquals(street.getCar(car.whereIs(), car.getActuators().getLane()), car);
		
	}
	/** This tests that the car does not keep moving past the end of the road */
	@Test
	void testMoveOutOfBounds() {
		street = new Street();
		car = new Car(END_OF_THE_ROAD, RIGHT_LANE, street);
		car.moveForward(street);
		assertEquals(car.whereIs(), END_OF_THE_ROAD);
	}
	/** This tests if the car can start before the beginning of the road */
	@Test
	void testStartOutOfBoundsBefore() {
		street = new Street();
		car = new Car(-CAR_DEFAULT_SPEED, RIGHT_LANE, street);
		car.moveForward(street);
		assertEquals(car.whereIs(), CAR_DEFAULT_SPEED);
	}
	/** This tests if the car can start after the end of the road */
	@Test
	void testStartOutOfBoundsAfter() {
		street = new Street();
		car = new Car(END_OF_THE_ROAD + CAR_DEFAULT_SPEED, RIGHT_LANE, street);
		car.moveForward(street);
		assertEquals(car.whereIs(), CAR_DEFAULT_SPEED);
	}
	/** Tests if the car can detect that the lane to it's left is clear */
	@Test
	void testLeftLaneDetect() {
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.leftLaneDetect(), 1);
	}
	/** Tests if the car can detect that the lane to it's left is not clear */
	@Test
	void testLeftLaneDetectLaneOcupied() {
		car.setSensors(LANE_WIDTH - 1, LANE_WIDTH - 1, LANE_WIDTH - 1, LANE_WIDTH);
		assertEquals(car.leftLaneDetect(), 0);
	}
	/** Tests if the car can detect that the lane is not clear */
	@Test
	void testLeftLaneDetectLaneOcupiedOneSensor() {
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH - 1, LANE_WIDTH);
		assertEquals(car.leftLaneDetect(), 0);
	}
	/** Tests if the car can detect that the lane to it's left is clear when large sensor readings are involved */
	@Test
	void testLeftLaneDetectLargeValues() {
		car.setSensors(SENSOR_UPPER_BOUNDS, SENSOR_UPPER_BOUNDS, LANE_WIDTH, SENSOR_UPPER_BOUNDS);
		assertEquals(car.leftLaneDetect(), 1);
	}
	/** Tests if the car can detect that the lane to it's left is not clear when negative sensor readings are involved */
	@Test
	void testLeftLaneDetectOutOfBoundsNegativeValues() {
		car.setSensors(-1, -1, 0, LANE_WIDTH);
		assertEquals(car.leftLaneDetect(), -0);
	}
	/** Tests if the car thinks the lane is clear if at least three of the sensor values returned are too high */
	@Test
	void testLeftLaneDetectOutOfBoundsPositiveValues() {
		car.setSensors(SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH);
		assertEquals(car.leftLaneDetect(), -1);
	}
	/** Tests if the car can change lanes */
	@Test
	void testChangeLane() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, MIDDLE_LANE, street);
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), true);
		assertEquals(car.whereIs(), MIDDLE_OF_THE_ROAD + CAR_DEFAULT_SPEED);
	}
	/** Tests that he car can not change lanes once it has reached the end of the road */
	@Test
	void testChangeLaneEndOfTheRoad() {
		street = new Street();
		car = new Car(END_OF_THE_ROAD, MIDDLE_LANE, street);
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), false);
		assertEquals(car.whereIs(), END_OF_THE_ROAD);
	}
	/** Tests that the car can not change lanes when it is in the leftmost lane */
	@Test
	void testChangeLaneLeftLane() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, LEFT_LANE, street);
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), false);
	}
	/** Tests that the car is given a default value when the lane is negative */
	@Test
	void testChangeLaneNegativeValue() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, -1, street);
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), true);
	}
	/** Tests that the car still moves when it fails to change lanes */
	@Test
	void testChangeLaneMove() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, MIDDLE_LANE, street);
		car.setSensors(0, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), false);
		assertEquals(car.whereIs(), MIDDLE_OF_THE_ROAD + CAR_DEFAULT_SPEED);
	}
	/** Tests that the car moves forward after changing lanes */
	@Test
	void testChangeLaneMoveAndChange() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, MIDDLE_LANE, street);
		car.setSensors(LANE_WIDTH, LANE_WIDTH, LANE_WIDTH, LANE_WIDTH);
		assertEquals(car.changeLane(street), true);
		assertEquals(car.whereIs(), MIDDLE_OF_THE_ROAD + CAR_DEFAULT_SPEED);
	}
	/** Tests if the car can detect that the sensor values are too unreliable to make use of */
	@Test
	void testChangeLaneAllSensorsOutOfBounds() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, MIDDLE_LANE, street);
		car.setSensors(SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH, SENSOR_UPPER_BOUNDS + LANE_WIDTH);
		assertEquals(car.changeLane(street), false);
		assertEquals(car.whereIs(), MIDDLE_OF_THE_ROAD + CAR_DEFAULT_SPEED);
	}
	/** Tests the sensors, this is temp text remember to replace it later you windowlicker */
	@Test
	void testNewSensors() {
		street = new Street();
		car = new Car(MIDDLE_OF_THE_ROAD, 0, street);
		Car car2 = new Car(MIDDLE_OF_THE_ROAD, MIDDLE_LANE, street);
		car.getSensors()[0].detectLane(street, car.getActuators());
		assertEquals(car.getSensors()[0].getSensor(), 0);
	}
}
