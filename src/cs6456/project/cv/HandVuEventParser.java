package cs6456.project.cv;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * Parses a "HandVu Gesture Event" into a HandVuEvent object
 * Returns null if it cannot be parsed.
 */
public class HandVuEventParser {
	final static private Pattern EVENT_PATTERN = Pattern
			.compile("^.*1\\.2\\s+(\\d+)\\s+0:\\s+(\\d),\\s+(\\d),\\s+\"(.*)\"\\s+\\(([0-9.]+),\\s+([0-9.]+)\\).*$");

	public HandVuEvent parseEventString(String input) {
		Matcher matcher = EVENT_PATTERN.matcher(input);
		if (matcher.matches()) {
			HandVuEvent event = new HandVuEvent();
			event.setTimestamp(Long.parseLong(matcher.group(1)));
			event.setTracking(matcher.group(2).equals("1"));
			event.setPostureDetected(matcher.group(3).equals("1"));
			event.setPosture(matcher.group(4));
			event.setX(Double.parseDouble(matcher.group(5)));
			event.setY(Double.parseDouble(matcher.group(6)));

			return event;
		}
		return null;
	}
}
