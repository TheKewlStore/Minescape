# FAQ/Knowledge Base
This file (as mentioned in the readme) is intended to be a list of snags and gotcha's I've run into
during the development cycle of the Runecraft project. This list is designed for other developers
and as such will be written under the assumption that you understand or can look up any references to the
Minecraft Forge system. No special knowledge of undocumented systems will be expected as that is the intention
of this document.

## Block Break Speed
In Minecraft Forge, there exists PlayerEvent.BreakSpeed which is an event fired on the bus that allows handlers
to adjust the break speed assigned to a block in reference to a given player in the world.
A decent understanding of vanilla block break mechanics/calculations can be obtained here 
and will be important understanding for the discussion that follows: https://minecraft.gamepedia.com/Breaking

Firstly, a general overview of all the mechanics that Minecraft/Forge uses to determine a block's breaking speed:
- The block in questions' hardness
- Whether or not the block is 'harvestable' (see link above) based on the player in questions' equipped tool (or hands)
- Whether the player is standing in water, lava, etc. or in the air.
 
 The most obvious question is how to determine what TYPE of block is being broken in this event handler.
 The answer is simple but not as intuitive as it should be without any documentation, the event in the handler
 has a parameter blockState, which has a reference to the block accessed like so:
 
     event.getState().getBlock()
     
 If, as in my case, you want to check this against vanilla blocks you aren't creating yourself, 
 the way to do so is again simple but not intuitive:
 
     event.getState().getBlock() != Blocks.DIRT
     
 The Blocks class contains references to all (or at least most, hopefully all) vanilla blocks accessed in the same way as above.
 
 Firstly, the routine onPlayerDamageBlock in the PlayerControllerMP class is called. 
 Ignoring some other irrelevant logic, this method calls getPlayerRelativeBlockHardness on the BlockState being hit.
 This function, as defined in the BlockStateContainer implementation, 
 calls ForgeHooks.blockStrength through some extra layers in the Block class.
 In this function, we see our first reference to some actual calculations, namely the following:
 
     player.getDigSpeed(BlockState, BlockPos) / BlockState.getHardness() / harvestModifier
     
 Where harvestModifier is 30F is the block is harvestable by the player, 100F otherwise.
 player.getDigSpeed(BlockState, BlockPos) is essentially the handler that will call any BreakSpeed event handlers
 and return the final result.  
 This block strength value is returned to PlayerControllerMp.
 
 The final relevant logic in PlayerControllerMp is as follows:
 
     this.curBlockDamageMP += iblockstate.getPlayerRelativeBlockHardness(this.mc.player, this.mc.player.world, posBlock);
     
     if (this.curBlockDamageMP >= 1.0F)
     {
         this.isHittingBlock = false;
         this.connection.sendPacket(new CPacketPlayerDigging(CPacketPlayerDigging.Action.STOP_DESTROY_BLOCK, posBlock, directionFacing));
         this.onPlayerDestroyBlock(posBlock);
         this.curBlockDamageMP = 0.0F;
         this.stepSoundTickCounter = 0.0F;
         this.blockHitDelay = 5;
     }
     
 Which tells us that when the final value calculated above has accumulated to a value greater than 1.0, 
 the block is broken.
 
 Now that we have a good idea of what kind of values we need to consider when determining how to modify 
 the block speed, the final consideration that must be made is that this event appears to be fired both 
 Server and Client (logical) side, but in a slightly surprising way. 
 It appears that when the block is initially broken, the event is fired on the server, after which point
 it is fired successively for each swing on the client side until the block is determined broken at which point
 that event will be sent back to the server for processing.
 
 # TODO
 - Add in explanation of how to draw textures of vanilla items (specifically to a GUI)
 - Add in explanation of issue with texture scaling.
 