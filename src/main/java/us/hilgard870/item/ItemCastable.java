package us.hilgard870.item;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public interface ItemCastable {
	boolean itemWillBeCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, Object[] args);
	boolean itemDidGetCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, int channelTime, Object[] args);
	boolean itemDidFinishCast(ItemMagicWand castingItem, EntityLivingBase caster, World world, Entity projectile, RayTraceResult result, Object[] args);
}
