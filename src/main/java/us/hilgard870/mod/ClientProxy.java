package us.hilgard870.mod;

import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.entity.RenderSnowball;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import us.hilgard.entity.EntityBlinkProjectile;
import net.minecraft.init.Items;

public class ClientProxy extends CommonProxy {

	@Override
    public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		RenderingRegistry.registerEntityRenderingHandler(EntityBlinkProjectile.class, manager -> new RenderSnowball(manager, itemBlinkEssence, Minecraft.getMinecraft().getRenderItem()));
    }
	
	@Override
    public void init(FMLInitializationEvent e) {
		super.init(e);
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemMagicWand, stack -> new ModelResourceLocation(itemMagicWand.getUnlocalizedName().substring(5) , "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(itemBlinkEssence, stack -> new ModelResourceLocation(itemBlinkEssence.getUnlocalizedName().substring(5) , "inventory"));
		
    }

	@Override
    public void postInit(FMLPostInitializationEvent e) {
		super.postInit(e);
    }
}