package me.adeptr.adepsk.expressions;

import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Chunk;
import org.bukkit.entity.Entity;
import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.util.Registry;

public class ExprEntities extends SimpleExpression<Entity> {

	private Expression<Chunk> chunk;

	static {
		Registry.newSimple(ExprEntities.class, "entities of chunk %chunk%");
	}

	@Override
	protected Entity[] get(Event event) {
		ArrayList<Entity> entities = new ArrayList<>();
		for (Chunk c : chunk.getAll(event)) {
			entities.addAll(Arrays.asList(c.getEntities()));
		}
		return entities.toArray(new Entity[entities.size()]);
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Entity> getReturnType() {
		return Entity.class;
	}

	@Override
	public String toString(Event event, boolean b) {
		return "tile entities";
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		chunk = (Expression<Chunk>) expressions[0];
		return true;
	}
}
