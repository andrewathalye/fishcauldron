package tk.zenithseeker.fishcauldron.mixin;


import net.minecraft.block.CauldronBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.AbstractBlock;

import net.minecraft.entity.player.PlayerEntity;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.FishBucketItem;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.ActionResult;


import net.minecraft.stat.Stats;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;

import net.minecraft.world.World;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(CauldronBlock.class)
public abstract class CauldronBlockMixin {
	@Shadow
	public abstract void setLevel(World world, BlockPos pos, BlockState state, int i);

	@Inject(at=@At(value="INVOKE_ASSIGN", target="Lnet/minecraft/item/ItemStack;getItem()Lnet/minecraft/item/Item;"), method="onUse(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/util/hit/BlockHitResult;)Lnet/minecraft/util/ActionResult;", locals=LocalCapture.CAPTURE_FAILEXCEPTION)
	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit, CallbackInfoReturnable<ActionResult> cir, ItemStack itemStack, int i, Item item) {
		if (item instanceof FishBucketItem && i < 3 && !world.isClient) {
		 	if (!player.abilities.creativeMode) {
                    		player.setStackInHand(hand, new ItemStack(Items.BUCKET));
                	}
                	player.incrementStat(Stats.FILL_CAULDRON);
                	this.setLevel(world, pos, state, 3);
               		world.playSound(null, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0f, 1.0f);
        		return ActionResult.success(world.isClient);
		}
		return ActionResult.PASS;
	}
}
