package com.github.cao.awa.lycoris.disc;

import com.github.cao.awa.lycoris.Lycoris;
import net.minecraft.client.render.entity.ProjectileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.BlazeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.SnowballEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import static com.github.cao.awa.lycoris.disc.MusicDiscManager.FLYING_DISC_ENTITY;

public class FlyingDiscEntity extends ThrownItemEntity {
    private boolean dealtDamage;
    private static final double DISC_DAMAGE = 11.45141919810;
    public int flyingTime;
    public int returnTimer;
    public FlyingDiscEntity(EntityType<? extends FlyingDiscEntity> entityType, World world) {
        super(entityType, world);
    }
    public FlyingDiscEntity(World world, LivingEntity owner, ItemStack stack) {
        super(FLYING_DISC_ENTITY, owner, world, stack);

    }
    private ParticleEffect getParticleParameters() {
        ItemStack itemStack = getStack();
        return (ParticleEffect)(itemStack.isEmpty() ? ParticleTypes.ITEM_SNOWBALL : new ItemStackParticleEffect(ParticleTypes.ITEM, itemStack));
    }
    @Override
    public void handleStatus(byte status) {
        if (status == 3) {
            ParticleEffect particleEffect = getParticleParameters();

            for (int i = 0; i < 8; i++) {
                getWorld().addParticle(particleEffect, getX(), getY(), getZ(), 0.0, 0.0, 0.0);
            }
        }
    }
    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        dealtDamage = nbt.getBoolean("DealtDamage");
        flyingTime = nbt.getInt("FlyingTime");
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        nbt.putBoolean("DealtDamage", dealtDamage);
        nbt.putInt("FlyingTime", flyingTime);
    }
    @Override
    public void tick() {
        flyingTime++;
        if (flyingTime > 20 && !dealtDamage) {
            dealtDamage = true;
        }

        Entity entity = getOwner();
        if (dealtDamage && entity != null) {
            if (!isOwnerAlive()) {
                if (getWorld() instanceof ServerWorld serverWorld) {
                    dropStack(serverWorld, asItemStack(), 0.1F);
                }

                discard();
            } else {
                if (!(entity instanceof PlayerEntity) && getPos().distanceTo(entity.getEyePos()) < (double)entity.getWidth() + 1.0) {
                    discard();
                    return;
                }

                //setNoClip(true);
                Vec3d vec3d = entity.getEyePos().subtract(getPos());
                setPos(getX(), getY() + vec3d.y * 0.015 , getZ());
                double d = 0.05;
                setVelocity(getVelocity().multiply(0.95).add(vec3d.normalize().multiply(d)));

                returnTimer++;
            }
        }
        super.tick();


    }
    public void setNoClip(boolean noClip) {
        this.noClip = noClip;
    }
    private boolean isOwnerAlive() {
        Entity entity = getOwner();
        return entity != null && entity.isAlive() && (!(entity instanceof ServerPlayerEntity) || !entity.isSpectator());
    }
    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        super.onEntityHit(entityHitResult);
        Entity entity = entityHitResult.getEntity();
        if(entity instanceof PlayerEntity player && this.isOwner(player) && this.flyingTime < 50){
            return;
        }
        entity.serverDamage(getDamageSources().thrown(this, getOwner()), (float) DISC_DAMAGE);
    }
    protected ItemStack asItemStack() {
        return getStack();
    }
    public boolean isNoClip() {
        return noClip;
    }
    protected boolean tryPickup(PlayerEntity player) {
        return isNoClip() && isOwner(player) && player.getInventory().insertStack(asItemStack());
    }
    @Override
    public void onPlayerCollision(PlayerEntity player) {

        if ((isOwner(player) && flyingTime > 50) || getOwner() == null) {
            player.serverDamage(getDamageSources().thrown(this, getOwner()), (float) DISC_DAMAGE);
            if (!getWorld().isClient) {
                if (tryPickup(player)) {
                    player.sendPickup(this, 1);
                    discard();
                }
            }
        }
    }
    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);
        if (!getWorld().isClient) {
            if(this.getStack().getItem() == Items.MUSIC_DISC_11){
                this.getWorld().playSound(this, this.getBlockPos(),SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1,1);
                this.discard();
            }
            getWorld().sendEntityStatus(this, (byte)3);
            int i = Lycoris.RANDOM.nextInt(0,90);
            if(i <= 30){
                this.dropStack((ServerWorld) getWorld(),this.getStack());
                discard();
            }else if(i >=30 && 1 <= 60 && !dealtDamage){
                dealtDamage = true;
            }
            else{
                this.getWorld().playSound(this, this.getBlockPos(),SoundEvents.ITEM_SHIELD_BREAK, SoundCategory.BLOCKS, 1,1);
                this.discard();
            }
        }
    }
    @Override
    protected Item getDefaultItem() {
        return Items.MUSIC_DISC_13;
    }
    @Override
    protected double getGravity() {
        return 0.001;
    }
}
