package cs6456.project.event;

import java.util.concurrent.BlockingQueue;

import javax.swing.SwingUtilities;

public class EventLoopThread extends Thread {
	BlockingQueue<GesturesEvent> queue;
	
	public EventLoopThread(BlockingQueue<GesturesEvent> queue) {
		super("EventLoop");
		this.setDaemon(true);

		this.queue = queue;
	}

	@Override
	public void run() {
		
		try {
			while (true) {
				final GesturesEvent event = queue.take();
				SwingUtilities.invokeLater(new Runnable() {

					@Override
					public void run() {
						event.perform();
					}					
				});
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public BlockingQueue<GesturesEvent> getQueue() {
		return queue;
	}

	public void setQueue(BlockingQueue<GesturesEvent> queue) {
		this.queue = queue;
	}

}
