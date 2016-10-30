package us.hilgard.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import us.hilgard870.item.ItemBlinkEssence;
import us.hilgard870.item.ItemMagicWand;

public class EntityBlinkProjectile extends EntityThrowable {

	public EntityBlinkProjectile(World worldIn, EntityLivingBase throwerIn) {
		super(worldIn, throwerIn);
	}
	
	public EntityBlinkProjectile(World worldIn) {
		super(worldIn);
	}
	
	public EntityBlinkProjectile(World worldIn, double x, double y, double z) {
		super(worldIn, x, y, z);
	}

	public void setAim(Entity p_184547_1_, float p_184547_2_, float p_184547_3_, float p_184547_4_, float p_184547_5_, float p_184547_6_)
    {
        float f = -MathHelper.sin(p_184547_3_ * 0.017453292F) * MathHelper.cos(p_184547_2_ * 0.017453292F);
        float f1 = -MathHelper.sin(p_184547_2_ * 0.017453292F);
        float f2 = MathHelper.cos(p_184547_3_ * 0.017453292F) * MathHelper.cos(p_184547_2_ * 0.017453292F);
        this.setThrowableHeading((double)f, (double)f1, (double)f2, p_184547_5_, p_184547_6_);
        this.motionX += p_184547_1_.motionX;
        this.motionZ += p_184547_1_.motionZ;

        if (!p_184547_1_.onGround)
        {
            this.motionY += p_184547_1_.motionY;
        }
    }

	@Override
	protected void onImpact(RayTraceResult result) {
		// TODO Auto-generated method stub
		EntityLivingBase entitylivingbase = this.getThrower();
		
		if (null == entitylivingbase) {
			return;
		}
		
		// Search for magic wand.
		ItemStack itemStackOffHand = entitylivingbase.getHeldItemOffhand();
		
		// Do nothing if the wand is unloaded.
		if (null == itemStackOffHand) {
			return;
		}
		
		Item itemOffHand = itemStackOffHand.getItem();
		
		if (null == itemOffHand || !(itemOffHand instanceof ItemMagicWand)) {
			return;
		}
		
		ItemStack itemStackEssence = entitylivingbase.getHeldItemMainhand();
		Item itemEssence = null;
		
		if (null != itemStackEssence) {
			// Search in inventory.
			itemEssence = itemStackEssence.getItem();
			if (null == itemEssence || !(itemEssence instanceof ItemBlinkEssence)) {
				itemEssence = null;
			}
		}
		
		if (null == itemEssence) {
			if (entitylivingbase instanceof EntityPlayer) {
				EntityPlayer player = (EntityPlayer) entitylivingbase;
				for (int i = 0; i < player.inventory.getHotbarSize(); i++) {
					itemStackEssence = player.inventory.getStackInSlot(i);
					if (null != itemStackEssence) {
						itemEssence = itemStackEssence.getItem();
						if (itemEssence instanceof ItemBlinkEssence) {
							// Success! Found a blink essence.
							break;
						}
					}
				}
			}
		}
		
		if (null != itemEssence) {
			((ItemBlinkEssence) itemEssence).itemDidFinishCast((ItemMagicWand) itemOffHand, entitylivingbase, worldObj, this, result, null);
		}
	}

}
