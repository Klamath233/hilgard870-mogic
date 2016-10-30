package us.hilgard870.item;

import java.util.Date;
import java.util.Random;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityEndGateway;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import us.hilgard.entity.EntityBlinkProjectile;

public class ItemBlinkEssence extends Item implements ItemCastable {
	
	public static int coolDownInSecond = 5;
	private boolean launched;
	private Random rand;
	private static int didCastCount = 0;
	private static int didFinishedCount = 0;
	private static final TextComponentString cooldownPrompt = new TextComponentString("冷却时间的决定权也是很重要的！");
	
	static {
		cooldownPrompt.setStyle(cooldownPrompt.getStyle().setColor(TextFormatting.RED));
	}
	
	public ItemBlinkEssence() {
		super();
		launched = false;
		rand = new Random();
		rand.setSeed(new Date().getTime());
		this.setCreativeTab(CreativeTabs.COMBAT);
	}
	
	public ItemBlinkEssence(String name) {
		this();
		Hilgard870Item.setItemName(this, name);
	}

	@Override
	public boolean itemWillBeCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, Object[] args) {
		if (!(caster instanceof EntityPlayer)) {
			return false;
		}
		
		EntityPlayer casterPlayer = (EntityPlayer) caster;
		
		System.out.println(launched);
		
		// Check cooldown.
		if (casterPlayer.getCooldownTracker().hasCooldown(this) || launched == true) {
			if (!world.isRemote) {
				casterPlayer.addChatMessage(cooldownPrompt);
			}
			return false;
		}
		
		// Check if main hand is actually the essence
		ItemStack is = casterPlayer.getHeldItem(EnumHand.MAIN_HAND);
		if (null == is || this != is.getItem())
        {
            return false;
        }
		
		return true;
	}

	@Override
	public boolean itemDidGetCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, int channelTime, Object[] args) {
		if (!(caster instanceof EntityPlayer)) {
			return false;
		}
		System.out.println("I1");
		EntityPlayer casterPlayer = (EntityPlayer) caster;
		
		System.out.println(++ItemBlinkEssence.didCastCount);
		launched = true;
		casterPlayer.getCooldownTracker().setCooldown(this, Integer.MAX_VALUE);
		
        float f = ItemBow.getArrowVelocity(channelTime);

        if ((double)f >= 0.1D)
        {
            if (!world.isRemote)
            {
                EntityBlinkProjectile projectile = new EntityBlinkProjectile(world, casterPlayer);
                projectile.setAim(casterPlayer, casterPlayer.rotationPitch, casterPlayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                world.spawnEntityInWorld(projectile);
            }

            world.playSound((EntityPlayer)null, casterPlayer.posX, casterPlayer.posY, casterPlayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.NEUTRAL, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

            casterPlayer.addStat(StatList.getObjectUseStats(this));
        } else {
        	launched = false;
        	return false;
        }
		
		return true;
	}

	@Override
	public boolean itemDidFinishCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, Entity projectile, RayTraceResult result, Object[] args) {
		if (!(caster instanceof EntityPlayer)) {
			return false;
		}
		
		EntityPlayer casterPlayer = (EntityPlayer) caster;
		
		System.out.println(++ItemBlinkEssence.didFinishedCount);
		launched = false;
		casterPlayer.getCooldownTracker().setCooldown(this, 20 * coolDownInSecond);
		
		if (result.entityHit != null)
        {
            if (result.entityHit == caster)
            {
                return true;
            }
        }

        if (result.typeOfHit == RayTraceResult.Type.BLOCK)
        {
            BlockPos blockpos = result.getBlockPos();
            TileEntity tileentity = world.getTileEntity(blockpos);

            if (tileentity instanceof TileEntityEndGateway)
            {
                TileEntityEndGateway tileentityendgateway = (TileEntityEndGateway)tileentity;

                if (caster != null)
                {
                    tileentityendgateway.teleportEntity(caster);
                    projectile.setDead();
                }

                tileentityendgateway.teleportEntity(projectile);
                return true;
            }
        }

        for (int i = 0; i < 32; ++i)
        {
        	world.spawnParticle(EnumParticleTypes.PORTAL, projectile.posX, projectile.posY + this.rand.nextDouble() * 2.0D, projectile.posZ, this.rand.nextGaussian(), 0.0D, this.rand.nextGaussian(), new int[0]);
        }

        if (!world.isRemote)
        {
            if (caster instanceof EntityPlayerMP)
            {
                EntityPlayerMP entityplayermp = (EntityPlayerMP)caster;

                if (entityplayermp.connection.getNetworkManager().isChannelOpen() && entityplayermp.worldObj == world && !entityplayermp.isPlayerSleeping())
                {
                    
                    if (caster.isRiding())
                    {
                    	caster.dismountRidingEntity();
                    }

                    caster.setPositionAndUpdate(projectile.posX, projectile.posY, projectile.posZ);
                    caster.fallDistance = 0.0F;
                }
            }
            else if (caster != null)
            {
            	caster.setPositionAndUpdate(projectile.posX, projectile.posY, projectile.posZ);
            	caster.fallDistance = 0.0F;
            }

            projectile.setDead();
        }
		
		return true;
	}
}
