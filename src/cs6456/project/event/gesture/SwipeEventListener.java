package cs6456.project.event.gesture;

public interface SwipeEventListener {
	public void swipeLeft(SwipeLeftEvent event);

	public void swipeUp(SwipeUpEvent event);

	public void swipeDown(SwipeDownEvent event);

	public void swipeRight(SwipeRightEvent event);
}
