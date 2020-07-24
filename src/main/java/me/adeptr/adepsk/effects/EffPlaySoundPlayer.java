package me.adeptr.adepsk.effects;

import javax.annotation.Nullable;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

public class EffPlaySoundPlayer extends Effect {
	
	static {
		Registry.newEffect(EffPlaySoundPlayer.class, "play sound %string% (for|to) %players% with volume %number% and pitch %number%");
	}

	private Expression<String> sound;
	private Expression<Player> player;
	private Expression<Number> volume;
	private Expression<Number> pitch;
	
	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		sound = (Expression<String>) e[0];
		player = (Expression<Player>) e[1];
		volume = (Expression<Number>) e[2];
		pitch = (Expression<Number>) e[3];
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return "play sound";
	}

	@Override
	protected void execute(Event e) {
		Object s = null;
		try {
			s = Sound.valueOf(sound.getSingle(e).replace("\"", "").replace(" ", "_").trim().toUpperCase(java.util.Locale.ENGLISH));
		} catch (IllegalArgumentException ignored) {
			Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "Unknown sound: " + sound.getSingle(e));
			return;
		}
		if (s == null) return;
		for (Player p : player.getAll(e)) {
			p.playSound(p.getLocation(), (Sound) s, volume.getSingle(e).floatValue(), pitch.getSingle(e).floatValue());
		}
	}
}