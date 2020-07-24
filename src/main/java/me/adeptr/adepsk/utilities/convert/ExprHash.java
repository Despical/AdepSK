package me.adeptr.adepsk.utilities.convert;

import java.math.BigInteger;
import java.security.MessageDigest;

import javax.annotation.Nullable;

import org.bukkit.event.Event;

import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import me.adeptr.adepsk.Main;

/**
 * Created by tim740 on 14/03/2016
 */
public class ExprHash extends SimpleExpression<String> {
	private Expression<String> str, hash;

	@Override
	@Nullable
	protected String[] get(Event e) {
		MessageDigest hashStr;
		try {
			hashStr = MessageDigest.getInstance(hash.getSingle(e).toUpperCase());
			hashStr.update(str.getSingle(e).getBytes(), 0, str.getSingle(e).length());
			return new String[] { new BigInteger(1, hashStr.digest()).toString(16) };
		} catch (Exception x) {
			Main.prSysE(x.getMessage(), getClass().getSimpleName(), x);
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean init(Expression<?>[] e, int i, Kleenean k, SkriptParser.ParseResult p) {
		str = (Expression<String>) e[0];
		hash = (Expression<String>) e[1];
		return true;
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public String toString(@Nullable Event e, boolean b) {
		return getClass().getName();
	}
}
