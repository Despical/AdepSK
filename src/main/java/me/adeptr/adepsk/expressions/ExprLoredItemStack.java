package me.adeptr.adepsk.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.aliases.ItemType;

import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser;
import ch.njol.util.Kleenean;
import java.util.Arrays;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.inventory.meta.ItemMeta;

public class ExprLoredItemStack extends PropertyExpression<ItemType, ItemType> {
	
	static {
		Skript.registerExpression(ExprLoredItemStack.class, ItemType.class, ExpressionType.PROPERTY, new String[] {"%itemtypes% with lore %string%"});
	}
	
	private Expression<String> lore;

	protected ItemType[] get(Event event, ItemType[] itemTypes) {
		String n = (String) this.lore.getSingle(event);
		if (n == null)
			return new ItemType[0];
		ItemType[] r = (ItemType[]) itemTypes.clone();
		for (int i = 0; i < r.length; i++) {
			r[i] = itemTypes[i].clone();
			ItemMeta m = (r[i].getItemMeta() == null) ? Bukkit.getItemFactory().getItemMeta(Material.STONE)
					: (ItemMeta) r[i].getItemMeta();
			m.setLore(Arrays.asList(n.split("\\|\\|")));
			r[i].setItemMeta(m);
		}
		return r;
	}

	public Class<? extends ItemType> getReturnType() {
		return ItemType.class;
	}

	public String toString(Event event, boolean b) {
		return "lore";
	}

	public boolean init(Expression[] expressions, int i, Kleenean kleenean, SkriptParser.ParseResult parseResult) {
		setExpr(expressions[0]);
		this.lore = expressions[1];
		return true;
	}
}