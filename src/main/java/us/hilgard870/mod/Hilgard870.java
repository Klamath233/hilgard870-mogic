package us.hilgard870.mod;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import us.hilgard870.item.ItemMagicWand;

@Mod(modid = Hilgard870.MODID, version = Hilgard870.VERSION)
public class Hilgard870
{
    public static final String MODID = "hilgard870";
    public static final String VERSION = "0.1";
    
    @Instance("hilgard870")
    public static Hilgard870 instance;
    
    @SidedProxy(clientSide="us.hilgard870.mod.ClientProxy", serverSide="us.hilgard870.mod.ServerProxy")
    public static CommonProxy proxy;
    private Item itemMagicWand;
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
    	proxy.preInit(event);
    }
    
    @EventHandler
    public void init(FMLInitializationEvent event)
    {
        proxy.init(event);
    }
    
    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
    	proxy.postInit(event);
    }
}
