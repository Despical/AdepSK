package me.adeptr.adepsk.expressions;

import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.eclipse.jdt.annotation.Nullable;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.adeptr.adepsk.util.Registry;

/**
 * @author Despical
 * <p>
 * Created at 17.07.2020
 */
public class ExprNoItemNBT extends SimplePropertyExpression<ItemStack, ItemStack>{
	
	static {
		Registry.newSimple(ExprNoItemNBT.class, "%itemstack% with([out] any| no) NBT");
	}
	
	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}
	@Override
	protected String getPropertyName() {
		return "%itemstack% with([out] any| no) NBT";
	}
	@Override
	@Nullable
	public ItemStack convert(ItemStack item) {
		ItemMeta metadata = item.getItemMeta();
		metadata.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
	    metadata.addItemFlags(ItemFlag.HIDE_DESTROYS);
	    metadata.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    metadata.addItemFlags(ItemFlag.HIDE_POTION_EFFECTS);
	    item.setItemMeta(metadata);
	    return item;
	}
}