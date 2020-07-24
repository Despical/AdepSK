package me.adeptr.adepsk.expressions;

import java.util.ArrayList;

import org.bukkit.event.Event;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import tk.shanebee.bee.api.NBTApi;
import tk.shanebee.bee.api.NBT.NBTContainer;

public class ExprTagOfNBT extends SimpleExpression<Object> {

    private static final NBTApi NBT_API;

	static {
		Skript.registerExpression(ExprTagOfNBT.class, Object.class, ExpressionType.SIMPLE, "tag %string% of %string%", "%string% tag of %string%");
    	NBT_API = new NBTApi();
    }

    private Expression<String> a;
    private Expression<String> b;

    @SuppressWarnings("unchecked")
    @Override
    public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parser) {
        this.a = (Expression<String>) expressions[0];
        this.b = (Expression<String>) expressions[1];
        return true;
    }

    @SuppressWarnings("rawtypes")
	@Override
    @Nullable
    protected Object[] get(Event e) {
        String t = a.getSingle(e);
        String n = b.getSingle(e);
        if (t.contains(";")) {
            return getNested(t, n);
        }
        Object nbt = NBT_API.getTag(t, n);
        if (nbt instanceof ArrayList) {
            return ((ArrayList) nbt).toArray();
        }
        return new Object[]{nbt};
    }

    @Override
    public boolean isSingle() {
        return true;
    }

    @Override
    public String toString(@Nullable Event e, boolean d) {
        return "Tag \"" + a.toString(e, d) + "\" of " + b.toString(e, d);
    }

    @Override
    public Class<? extends Object> getReturnType() {
        return Object.class;
    }

    private Object[] getNested(String tag, String nbt) {
        String[] split = tag.split(";");
        Object nbtNew = nbt;
        for (String s : split) {
            nbtNew = NBT_API.getTag(s, new NBTContainer(nbtNew).toString());
        }
        return new Object[]{nbtNew};
    }
}