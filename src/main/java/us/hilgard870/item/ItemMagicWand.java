package us.hilgard870.item;

import javax.annotation.Nullable;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import us.hilgard.entity.EntityBlinkProjectile;

public class ItemMagicWand extends ItemBow {
	
	private Item essence;
	
	private static final TextComponentString noMogic = new TextComponentString("假使这些完全……无中生有的膜法，我能帮你放出来，你等于……我也有责任吧？");
	private static final TextComponentString wrongMogic = new TextComponentString("你手上的这个膜法是按照基本法产生的吗？");
	
	static {
		noMogic.setStyle(noMogic.getStyle().setColor(TextFormatting.RED));
		wrongMogic.setStyle(noMogic.getStyle().setColor(TextFormatting.RED));
	}
	
	public ItemMagicWand() {
		super();
		ItemMagicWand self = this;
		this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter()
        {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn)
            {
                if (entityIn == null)
                {
                    return 0.0F;
                }
                else
                {
                    ItemStack itemstack = entityIn.getActiveItemStack();
                    return itemstack != null && itemstack.getItem() == Item.REGISTRY.getObject(self.getRegistryName()) ? (float)(stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F : 0.0F;
                }
            }
        });
	}
	
	public ItemMagicWand(String name) {
		this();
		Hilgard870Item.setItemName(this, name);
	}
	
	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        System.out.println("onItemUseFinish");
        return stack;
    }
	
	public ActionResult<ItemStack> onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn, EnumHand hand)
    {
        boolean flag = true;
        ItemStack is = playerIn.getHeldItem(EnumHand.MAIN_HAND);
        System.out.println("P1");
        if (null == is) {
        	if (!worldIn.isRemote) {
        		playerIn.addChatMessage(noMogic);
        	}
        	return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        }
        System.out.println("P2");
        Item mainHandItem = is.getItem();
        if (null == mainHandItem || !(mainHandItem instanceof ItemCastable)) {
        	flag = false;
        	if (!worldIn.isRemote) {
        		playerIn.addChatMessage(wrongMogic);
        	}
        	return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        }
        System.out.println("P3");
        ItemCastable essence = (ItemCastable) mainHandItem;
        flag = essence.itemWillBeCast(this, playerIn, worldIn, null);

        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onArrowNock(itemStackIn, worldIn, playerIn, hand, flag);
        if (ret != null) return ret;
        System.out.println("P4");
        if (!flag) {
            return new ActionResult(EnumActionResult.FAIL, itemStackIn);
        }
        System.out.println("P5");
        playerIn.setActiveHand(hand);
        this.essence = mainHandItem;
        return new ActionResult(EnumActionResult.SUCCESS, itemStackIn);

    }
	
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft)
    {
		System.out.println("onItemUseFinish");
        if (entityLiving instanceof EntityPlayer)
        {
            EntityPlayer entityplayer = (EntityPlayer)entityLiving;

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            
            ItemStack mainHandItemStack = entityplayer.getHeldItemMainhand();
            System.out.println("F1");
            if (null == mainHandItemStack) {
            	return;
            }
            System.out.println("F2");
            Item mainHandItem = mainHandItemStack.getItem();
            
            if (null == mainHandItem || (mainHandItem != this.essence) || i < 0) {
            	return;
            }
            System.out.println("F3");
            ItemCastable essence = (ItemCastable) mainHandItem;
            
            essence.itemDidGetCast(this, entityplayer, worldIn, i, null);
            
        }
    }

}
