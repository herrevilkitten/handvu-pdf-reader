package cs6456.project.cv;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class HandVuInputThread extends Thread {
	int port;
	
	public HandVuInputThread(int port) {
		this.port = port;
		setDaemon(true);
	}

	@Override
	public void run() {
		try {
			Socket socket = new Socket("localhost", 7045);
			HandVuEventParser parser = new HandVuEventParser();
			while (true) {
				BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				String line;
				while ( (line = in.readLine()) != null ) {
					HandVuEvent event = parser.parseEventString(line);
					if ( event != null ) {
						
					}
				}	
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}
}
