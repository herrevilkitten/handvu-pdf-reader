package cs6456.project.event.gesture;

public interface ThumbsEventListener {
	public void thumbsLeft(ThumbsLeftEvent event);

	public void thumbsRight(ThumbsRightEvent event);

	public void thumbsUp(ThumbsUpEvent event);

	public void thumbsDown(ThumbsDownEvent event);
}
