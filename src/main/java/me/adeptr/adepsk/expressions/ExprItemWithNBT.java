package me.adeptr.adepsk.expressions;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import tk.shanebee.bee.api.NBTApi;

/**
 * @author Despical
 * <p>
 * Created at 17.07.2020
 */
public class ExprItemWithNBT extends PropertyExpression<ItemType, ItemType> {

	private static final NBTApi NBT_API;
	
	static {
		Skript.registerExpression(ExprItemWithNBT.class, ItemType.class, ExpressionType.PROPERTY, "%itemtype% with [item( |-)]nbt %string%");
		NBT_API = new NBTApi();
	}

	private Expression<String> nbt;

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] exprs, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		setExpr((Expression<ItemType>) exprs[0]);
		nbt = (Expression<String>) exprs[1];
		return true;
	}

	@Override
	protected ItemType[] get(Event e, ItemType[] source) {
		String nbt = this.nbt.getSingle(e);
		if (!NBT_API.validateNBT(nbt))
			return null;
		return get(source, item -> {
			NBT_API.addNBT(item, nbt);
			return item;
		});
	}

	@Override
	public Class<? extends ItemType> getReturnType() {
		return ItemType.class;
	}

	@Override
	public String toString(@Nullable Event e, boolean debug) {
		return getExpr().toString(e, debug) + " with nbt " + nbt.toString(e, debug);
	}
}