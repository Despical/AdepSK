package me.adeptr.adepsk.util;

import org.bukkit.event.Event;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.util.SimpleEvent;

public abstract class AbstractTask implements Runnable {

	protected void registerEvent(String name, Class<? extends Event> event, String... patterns) {
		registerEvent(name, SimpleEvent.class, event, patterns);
	}

	protected void registerEvent(String name, Class<? extends SkriptEvent> handler, Class<? extends Event> event, final String... patterns) {
		Skript.registerEvent(name, handler, event, patterns);
	}
}