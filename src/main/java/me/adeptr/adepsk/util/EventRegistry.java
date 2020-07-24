package me.adeptr.adepsk.util;

import org.bukkit.event.server.ServerListPingEvent;

public class EventRegistry extends AbstractTask {

	@Override
	public void run() {
		registerEvent("Server Ping", ServerListPingEvent.class, "[server] [list] ping");
	}
}