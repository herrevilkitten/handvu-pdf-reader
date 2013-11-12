package cs6456.project.cv;

public class HandVuEvent {
	long timestamp;
	boolean tracking;
	boolean postureDetected;
	String posture;
	double x;
	double y;

	public long getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(long timestamp) {
		this.timestamp = timestamp;
	}

	public boolean isTracking() {
		return tracking;
	}

	public void setTracking(boolean tracking) {
		this.tracking = tracking;
	}

	public boolean isPostureDetected() {
		return postureDetected;
	}

	public void setPostureDetected(boolean postureDetected) {
		this.postureDetected = postureDetected;
	}

	public String getPosture() {
		return posture;
	}

	public void setPosture(String posture) {
		this.posture = posture;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}
}
