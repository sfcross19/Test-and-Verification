package laneChanger;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TestScenarios {
	private Street street = new Street();
	private Car car1 = new Car(0, 0, street, '1');
	private Car car2 = new Car(0, 1, street, '2');
	private Car car3 = new Car(50, 2, street,'3');
	private Car car4 = new Car(99, 0, street,'4');
	private Car[] cars = { car1, car2, car3, car4};
	private final static int DELAY = 100;
	
	private static final int END_OF_THE_ROAD = 99;
	private static final int MIDDLE_OF_THE_ROAD = 49;
	private static final int RIGHT_LANE = 0;
	private static final int MIDDLE_LANE = 1;
	private static final int LEFT_LANE = 2;
	private static final int CAR_DEFAULT_SPEED = 5;
	private static final int LANE_WIDTH = 5;
	private static final int SENSOR_UPPER_BOUNDS = 50;
	
	
public void printStreet(Car[] cars) {
		
		for(int k = 6;k>=-3;k--) {
			char[]cl= {' ',' ',' '};
			for(int n=1;n<cars.length;n++) {
				if(cars[n].whereIs()-car1.whereIs()==k) {
					cl[cars[n].getActuators().getLane()]= cars[n].getSymbol();
				}	
			}
			if(k==0) {
				cl[car1.getActuators().getLane()]= car1.getSymbol();
				
			}
			System.out.println((k+car1.whereIs())+"\t"+"| "+cl[0]+" | "+cl[1]+" | "+cl[2]+" |");
		}
		System.out.println(" ");
	}
	
	@Before
	public void testIt() {
		
		street = new Street();

		
		cars[0].getActuators().setSpeed(2);				// 10 units/ sec
		cars[1].getActuators().setSpeed(1);				//  5 units/ sec
		cars[2].getActuators().setSpeed(1);
		cars[3].getActuators().setSpeed(1);
		
		// printStreet(cars);
		
	}
	
	@Test
	public void testTrafficChangeLane(){
		street.placeCar(cars[0], 0, RIGHT_LANE);
		street.placeCar(cars[1], 0, MIDDLE_LANE);
		street.placeCar(cars[2], 50, LEFT_LANE);
		street.placeCar(cars[3], 99, RIGHT_LANE);
		
		for(int i = 0; i < 25; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
			}
		
		}assertEquals(cars[0].whereIs(), MIDDLE_OF_THE_ROAD+1);
		
		cars[0].changeLane(street);
		assertEquals(cars[0].getActuators().getLane(), MIDDLE_LANE);
		assertEquals(cars[0].whereIs(), MIDDLE_OF_THE_ROAD +3);
		
		printStreet(cars);
		
		while(cars[0].whereIs() < END_OF_THE_ROAD -1) {
			for(int n = 0; n < cars.length; n++) {
			//	if(cars[0].whereIs() > 90) {
			//	 System.out.println("Hello!");
			//	}
				cars[n].moveForward(street);
			//	printStreet(cars);
			}
		}
		assertEquals(cars[0].whereIs(), END_OF_THE_ROAD -1);
		assertEquals(cars[0].getActuators().getLane(), MIDDLE_LANE);
		
		printStreet(cars);
	
	
	}
	
	@Test
	public void changeLaneFromLeftmostLane() {
		cars[0] = new Car(0, LEFT_LANE, street, '1');
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), LEFT_LANE);
		
		
	}
	
	@Test
	public void changeLaneOccupiedLeft() {
		
		cars[0] = new Car(0, RIGHT_LANE, street, '1');
		cars[1] = new Car(0, MIDDLE_LANE, street, '2');
		cars[0].getActuators().setSpeed(1);			
	
		for(int i = 0; i < 10; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
			}
		}
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), RIGHT_LANE);
		//printStreet(cars);
		
	}
	
	@Test
	public void brokenSensorsHalfway() {
		
		cars[0] = new Car(0, RIGHT_LANE, street, '1');
		cars[1] = new Car(0, MIDDLE_LANE, street, '2');	
		cars[0].getActuators().setSpeed(2);
	
		for(int i = 0; i < 10; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
				cars[0].setSensors(5, SENSOR_UPPER_BOUNDS+1, SENSOR_UPPER_BOUNDS+1, 5);
			}
		}
		cars[0].leftLaneDetect();
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), MIDDLE_LANE);
		//printStreet(cars);
		
	}
	
	@Test
	public void firstQueryEmpty2ndOccupied() {
		
		cars[0] = new Car(0, RIGHT_LANE, street, '1');
		cars[1] = new Car(1, MIDDLE_LANE, street, '2');	
			
		for(int i = 0; i < 9; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
				car1.updateSensors(street);
				//System.out.println(cars[0].getSensors());
			}
			assertEquals(car1.getSensors()[0].getSensor(), 10);
		}
		cars[0].moveForward(street);
		//System.out.println(cars[0].getSensors());
		cars[0].leftLaneDetect();
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), RIGHT_LANE);
		//printStreet(cars);
		
	}
	
	@Test
	public void brokenThreeSensorsHalfway() {
		
		cars[0] = new Car(0, RIGHT_LANE, street, '1');
		cars[1] = new Car(0, MIDDLE_LANE, street, '2');	
		cars[0].getActuators().setSpeed(2);
	
		for(int i = 0; i < 10; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
				cars[0].setSensors(SENSOR_UPPER_BOUNDS+1, SENSOR_UPPER_BOUNDS+1, SENSOR_UPPER_BOUNDS+1, 5);
			}
		}
		cars[0].leftLaneDetect();
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), RIGHT_LANE);
		//printStreet(cars);
		
	}
	
	@Test
	public void brokenThreeSensorsInLeftmostLane() {
		
		cars[0] = new Car(0, LEFT_LANE, street, '1');
		cars[1] = new Car(END_OF_THE_ROAD, RIGHT_LANE, street, '2');
		cars[0].getActuators().setSpeed(1);
	
		for(int i = 0; i < 10; i++) {
			for(int n = 0; n < cars.length; n++) {
				cars[n].moveForward(street);
				cars[0].setSensors(SENSOR_UPPER_BOUNDS+1, SENSOR_UPPER_BOUNDS+1, SENSOR_UPPER_BOUNDS+1, 5);
			}
		}
		cars[1].changeLane(street);
		cars[0].changeLane(street);
		
		assertEquals(cars[0].getActuators().getLane(), LEFT_LANE);
		assertEquals(cars[1].getActuators().getLane(), RIGHT_LANE);
		//printStreet(cars);
		
	}
	
	@Test
	public void carPlacementCoverage() {
		
		cars[0] = new Car(0, RIGHT_LANE, street, '1');
		cars[1] = new Car(0, RIGHT_LANE, street, '2');	
		
		assertEquals(street.getCar(0, RIGHT_LANE).getSymbol() , cars[0].getSymbol());
	}
	
	
}
	
