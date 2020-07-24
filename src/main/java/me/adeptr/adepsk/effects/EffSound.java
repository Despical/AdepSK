package me.adeptr.adepsk.effects;

import org.bukkit.Location;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.ReflectionUtils;
import me.adeptr.adepsk.util.Registry;

public class EffSound extends Effect {
	
	static {
		Registry.newEffect(EffSound.class, "play raw sound %string/sound% at %locations% with volume %number% and pitch %number%");
	}
	
	private Expression<String> sound;
	private Expression<Location> loc;
	private Expression<Number> pit;
	private Expression<Number> vol;

	@Override
	protected void execute(Event event) {
		String s = (String) this.sound.getSingle(event);
		Location[] l = (Location[]) this.loc.getAll(event);
		float p = ((Number) this.pit.getSingle(event)).floatValue();
		float v = ((Number) this.vol.getSingle(event)).floatValue();
		if (s == null || l == null)
			return;
		byte b;
		int i;
		Location[] arrayOfLocation1;
		for (i = (arrayOfLocation1 = l).length, b = 0; b < i;) {
			Location fl = arrayOfLocation1[b];
			try {
				Class<?> craftWorldClass = ReflectionUtils.getOBCClass("CraftWorld");
				Object worldServer = craftWorldClass.getMethod("getHandle", new Class[0]).invoke(fl.getWorld(), new Object[0]);
				worldServer.getClass().getMethod("makeSound", new Class[] { double.class, double.class, double.class, String.class, float.class, float.class }).invoke(worldServer, new Object[] { Double.valueOf(fl.getX()), Double.valueOf(fl.getY()), Double.valueOf(fl.getZ()), s, Float.valueOf(p), Float.valueOf(v) });
			} catch (IllegalAccessException | NoSuchMethodException | java.lang.reflect.InvocationTargetException e) {
				e.printStackTrace();
			}
			b++;
		}
	}

	@Override
	public String toString(Event event, boolean b) {
		return "sound";
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public boolean init(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		this.sound = expressions[0];
		this.loc = expressions[1];
		this.pit = expressions[3];
		this.vol = expressions[2];
		return true;
	}
}