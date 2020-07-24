package me.adeptr.adepsk.expressions;

import java.util.Arrays;

import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.SimplePropertyExpression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.util.coll.CollectionUtils;

public class ExprLore extends SimplePropertyExpression<ItemStack, String> {

	static {
		Skript.registerExpression(ExprLore.class, String.class, ExpressionType.PROPERTY, new String[] { 
			"[the] lore of %itemstack%",
			"%itemstack%'[s] lore" });
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

	@Override
	protected String getPropertyName() {
		return "lore";
	}

	@Override
	@Nullable
	public String convert(ItemStack itemStack) {
		String re = "";
		boolean fs = true;
		try {
			if (!itemStack.getItemMeta().hasLore())
				return "";
			for (String s : itemStack.getItemMeta().getLore()) {
				if (fs) {
					re = s;
				} else {
					re = String.valueOf(re) + "||" + s;
				}
				fs = false;
			}
		} catch (NullPointerException e) {
			return "";
		}
		return re;
	}

	@Override
	public void change(Event e, @Nullable Object[] delta, ChangeMode mode) {
		String l = (delta == null) ? "" : (String) delta[0];
		ItemStack i = (ItemStack) getExpr().getSingle(e);
		ItemMeta m = i.getItemMeta();
		if (i.getType() == Material.AIR)
			return;
		switch (mode) {
		case SET:
			m.setLore(Arrays.asList(l.split("\\|\\|")));
			i.setItemMeta(m);
			break;
		case RESET:
			i.getItemMeta().setLore(null);
			break;
		}
	}

	@Override
	@Nullable
	public Class<?>[] acceptChange(ChangeMode mode) {
		if (mode == ChangeMode.SET || mode == Changer.ChangeMode.RESET)
			return (Class[]) CollectionUtils.array((Object[]) new Class[] { String.class });
		return null;
	}
}