package me.adeptr.adepsk.expressions;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import me.adeptr.adepsk.util.Registry;

public class ExprGlowingItemStack extends SimplePropertyExpression<ItemStack, ItemStack> {

	static {
		Registry.newSimple(ExprGlowingItemStack.class, "(glowing|shiny) %itemstacks%");
	}
	
    @Override
    protected String getPropertyName() {
        return "glowy forme";
    }

    @Override
    public ItemStack convert(ItemStack itemStack) {
        if (itemStack.getType() == Material.BOW) itemStack.addUnsafeEnchantment(Enchantment.WATER_WORKER, 69);
        else itemStack.addUnsafeEnchantment(Enchantment.ARROW_INFINITE, 69);
        ItemMeta metadata = itemStack.getItemMeta();
	    metadata.addItemFlags(ItemFlag.HIDE_ENCHANTS);
	    itemStack.setItemMeta(metadata);
        return itemStack;
    }

    @Override
    public Class<? extends ItemStack> getReturnType() {
        return ItemStack.class;
    }
}