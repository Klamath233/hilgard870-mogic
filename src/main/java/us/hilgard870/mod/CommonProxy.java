package us.hilgard870.mod;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import us.hilgard.entity.EntityBlinkProjectile;
import us.hilgard870.item.ItemBlinkEssence;
import us.hilgard870.item.ItemMagicWand;

public class CommonProxy {

	Item itemMagicWand;
	Item itemBlinkEssence;
	
    public void preInit(FMLPreInitializationEvent e) {
    	itemMagicWand = new ItemMagicWand("magic_wand");
    	itemBlinkEssence = new ItemBlinkEssence("blink_essence");
    	
    	GameRegistry.register(itemMagicWand);
    	GameRegistry.register(itemBlinkEssence);
    	
    	EntityRegistry.registerModEntity(EntityBlinkProjectile.class, "EntityBlinkProjectile", 0, Hilgard870.instance, 64, 20, true);
    }

    public void init(FMLInitializationEvent e) {
    	GameRegistry.addShapelessRecipe(new ItemStack(itemMagicWand, 1), new Object[] {Blocks.COBBLESTONE});
    }

    public void postInit(FMLPostInitializationEvent e) {

    }
}