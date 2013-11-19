package cs6456.project.cv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * This thread connects to the HandVu Server and listens to the events it emits.
 * HandVu emits one event per line, which is then parsed into a HandVuEvent
 * If the string is parsed into an object, then it will be sent to the
 * HandVuEventDispatcher
 */
public class HandVuInputThread extends Thread {
	int port;
	HandVuEventDispatcher dispatcher;
	
	public HandVuInputThread(int port, HandVuEventDispatcher dispatcher) {
		this.port = port;
		this.dispatcher = dispatcher;
		setDaemon(true);
	}

	@Override
	public void run() {
		Socket socket = null;
		try {
			socket = new Socket("localhost", this.port);
			HandVuEventParser parser = new HandVuEventParser();
			while (true) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;
				while ( (line = in.readLine()) != null ) {
					HandVuEvent event = parser.parseEventString(line);
					if ( event != null ) {
						dispatcher.dispatch(event);
					}	
				}	
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if ( socket != null ) {
					socket.close();					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
