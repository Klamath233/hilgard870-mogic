package us.hilgard870.item;

import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import us.hilgard870.mod.Hilgard870;

public class Hilgard870Item extends Item {
	public Hilgard870Item(String name) {
		setItemName(this, name);
	}
	
	public static void setItemName(Item item, String name) {
		item.setRegistryName(name);
		
		item.setUnlocalizedName(item.getRegistryName().toString());
	}
	
}
