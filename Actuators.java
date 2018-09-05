package laneChanger;

public class Actuators {

	private int position, lane, speed;
	private final static int DEFAULT_SPEED = 1;
	public Actuators() {
		this(0, 0, DEFAULT_SPEED);
	}
	public Actuators(int position) {
		this(position, 0, DEFAULT_SPEED);
	}
	public Actuators(int position, int lane) {
		this(position, lane, DEFAULT_SPEED);
	}
	public Actuators(int position, int lane, int speed) {
		//System.out.println("!!!" + this.position);
		this.position = position;
		this.lane = lane;
		this.speed = speed;
	}
	public boolean moveForward(Street street) {
		try {
			street.MoveCar(this);
		}catch(CustomException e) {
			return false;
		}
		position = position + this.getSpeed();
		return true;
	}
	public void changeLane(Street street) {
		street.SwitchLane(this);
		
	}
	public void setSpeed(int speed) {
		this.speed = speed;
	}
	public void setPosition(int position) {
		this.position = position;
	}
	public void setLane(int lane) {
		this.lane = lane;
	}
	public int getPosition(){
		return position;
	}
	public int getLane(){
		return lane;
	}
	public int getSpeed(){
		return speed;
	}
	

}

