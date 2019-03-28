# FAQ/Knowledge Base
This file (as mentioned in the readme) is intended to be a list of snags and gotcha's I've run into
during the development cycle of the Runecraft project. This list is designed for other developers
and as such will be written under the assumption that you understand or can look up any references to the
Minecraft Forge system. No special knowledge of undocumented systems will be expected as that is the intention
of this document.

## Table of Contents
1. [Client/Server Duality](#client/server-duality)
1. [Block Break Speed](#block-break-speed)
 
## Client/Server Duality
### Intro
Read the documentation on sidedness in the forge documentation here before continuing: 
https://mcforge.readthedocs.io/en/latest/concepts/sides/

Also, read this blog post on recommendations for structuring proxies:
http://jabelarminecraft.blogspot.com/p/minecraft-modding-organizing-your-proxy.html

The forge documentation on sidedness is a good starting point 
to explain the logic behind how client and server architecture is defined, 
but again leaves something to be desired in the area of how to handle this architecture and best structure a mod.

### Best Practices
Using the SidedProxy annotation to create handlers for the client and server side is nice, but can easily
cause incompatibilities for single-player vs multi-player games. This problem arises from the difference of 
PHYSICAL and LOGICAL servers. The SidedProxy annotation, as stated in the docs, only separates based on the PHYSICAL
side, but in a singe-player game, there is only the CLIENT PHYSICAL side. This means that in a single-player world,
a Proxy handler annotated with Side.SERVER will never be created/called. This leaves a big hole in the documented way
to handle client/server communication.

My recommended solution/best practice to fix this odd situation in a way that will work for both multi-player and
single-player games is to use the serverStart event in a CommonProxy, which is ONLY called on the LOGICAL server side,
to do any server initialization that is required of your mod. That way, whether you're in a single or multi-player game,
your server handlers will all be initialized properly.

Look at the CommonProxy class in the runecraft source under the proxy package 
to see an example of handling the serverStarted event and subsequently registering 
all required events and network packets that the server requires.

This understanding led to all but deprecating my ServerProxy, as there is nothing my mod currently needs to do 
ONLY on a physical dedicated server and not in a single-player server as well.
And since none of the functions in the Side.SERVER proxy (including serverStart) are called in a logical-only server,
it has next to no purpose for my use (and probably most mod developers' use I would guess).

This leads into the next complexity with Client/Server duality, handling network packets.

### Packet Networking
Supplementary documentation for this section: https://mcforge.readthedocs.io/en/latest/networking/simpleimpl/

When registering packets with the minecraft network wrapper engine, the forge guide recommends 
using a static ID variable that gets post-incremented to ensure a unique ID for every message. 

What they don't tell you is that the ID (or discriminator) is what forge uses to map the server and client packet
handlers together. This means that the same ID must be registered to the same messages on the client and server side.
This, in turn, means that the post-incremented static variable is a really bad solution, because this variable is NOT
shared across client and server and the IDs are not guaranteed to match up. 

A proper solution is to create an enumeration containing all the message IDs for the packets in your mod, and register
them using the ordinal of it's enumeration value as the ID. This guarantees a unique ID for each packet in your mod,
that is also guaranteed to be the same by both client and server and is not order-dependent.

Furthermore, another solution that will reduce replication and complexity in your code is to create static functions on
each of your Packet Message classes that handle registering that message on the client and server side.
This allows you to keep better track of the sidedness of the message and not get confused in the proxies about registration. 
You can see an example of this in any of the Message classes in the network package of runecraft. 
Each proxy then only needs to concern itself with calling the proper method on each Message class when ready to register.

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
 This fact means that you effectively need to register an event handler for the 
 BreakSpeed event on both servers and clients, which will unfortunately add complexity 
 as clients will need to be fed any and all information required for you to modify the speed sent to them via packets.
 My original problem in relation to break speed was due to only handling the event 
 on the server side because I assumed that was all that was necessary. 
 This doesn't cause an error or any kind of crash, 
 instead it will only cause any change to the 
 actual break speed if you set the break speed high enough to break the block instantly.
 This is where the understanding that the break speed is only fired once on 
 the server and on the client for every swing after that was born.
 
 I will likely include a section in here on best practices and extra considerations for Client/Server duality.
 
 ## Textures & GUI
 ### Intro
 This section will discuss a few different issues I've had with creating a custom GUI on the inventory screen
 and couldn't find much helpful documentation for.
 
 ### Texture Scaling
 First up, texture scaling, a very big nuisance that is not discussed anywhere I could find. 
 The function drawTexturedModalRect, and any of it's offshoots, EXPECTS, nay requires, a size of 256X256.
 This is true because of the way it specifies texture coordinates against the in-game world coordinates,
 it uses a modifier value (defined as f for the width and fl for the height) as 0.00390625. 
 There isn't any documentation for this value, and it's not very plainly visible, but this is equivalent to 
 1/256, and when written in that form it's instantly clear what it's doing, or at least what the relationship is.
 
 First off, for the simple case, make an image 256X256, put it in the src/main/resources/assets/{modid}/textures folder
 (or any dividend), bind the texture to the GlStateManager, then call drawTexturedModalRect. 
 For those with no experience with game texturing, you specify where to draw this texture on the screen, as well as
 coordinates in the image itself to draw from and to. This means that you can create a 256X256 image, only use
 the top 1/4 of the image, and draw only that 1/4 of the image, 
 and it will work just the same as having just a 64X64 image. But if you make the image 64X64 and call the same function
 the same way, it won't work because of the modifiers described above. 
 
 Here is a simple example of drawing a texture used in runecraft:
 
    // Push a new translation matrix into the state manager, so that
    // anything that renders after or before us isn't affected by our color settings, scaling, etc.
    GlStateManager.pushMatrix(); 
    // Set the color of everything drawn to white, with 100% opacity (1.0f == 100%).
    // The parameters in the call below translate to (100% red, 100% green, 100% blue, 100% alpha/opacity).
    GlStateManager.color(1.0f, 1.0f, 1.0f, 1.0f);
    
    // Bind a ResourceLocation to the gl state manager to be used by drawTexturedModalRect
    // The SKILLS_TEXTURE is located on the filesystem for the project at src/main/resources/assets/runecraft/textures/gui/skills.png
    final ResourceLocation SKILLS_TEXTURE = new ResourceLocation("runecraft", "textures/gui/skills.png")
    this.mc.getTextureManager().bindTexture(SKILLS_TEXTURE);
    super.drawTexturedModalRect(
            this.guiLeft,
            this.guiTop,
            0,
            0,
            176,
            166);

    // Pop our matrix off the state manager, see the comment at pushMatrix for explanation.
    GlStateManager.popMatrix();
 
 In any case, everywhere the function drawTexturedModalRect is available, there SHOULD be two other functions, 
 drawModalRectWithCustomSizedTexture and drawScaledCustomSizeModalRect
  
 drawModalRectWithCustomSizedTexture works the same as drawTexturedModalRect, 
 but allows you to specify width and height instead of using the assumed 256X256.
 
 drawScaledCustomSizeModalRect allows you to draw a custom-sized texture, 
 but will scale it up or down from the texture size based on the parameters.
 Here is an example of using the scaled custom size modal rect:
 
     mc.getTextureManager().bindTexture(skillIcon.getTextureLocation());
     drawScaledCustomSizeModalRect(
             (int) iconPosition.getX(), // The position on the game screen
             (int) iconPosition.getY(), // The position on the game screen
             0.0f, // I think these are offsets of the texture coordinate, but not sure.
             0.0f, // ^
             skillIcon.getTexWidth(), // The width of the texture image.
             skillIcon.getTexHeight(), // The height of the texture image.
             12, // The scaled width to draw it as on the screen.
             12, // The scaled height to draw it as on the screen.
             skillIcon.getTexWidth(), // These are called tileWidth and height, not sure what this means.
             skillIcon.getTexHeight()); // ^
 
 ### Vanilla Textures
 Another thing that could stand to be better explained in the documentation is accessing default minecraft resources.
 They work exactly the same as accessing your own mod resources, but you use the identifier "minecraft" instead of 
 your mod id when creating the resource location. 
 
 For reference on all the vanilla textures and resources available,
 download one of the default resource packs here: https://minecraft-resourcepacks.com/default/
 
 For an example, here is a resource location definition for the texture of a wooden shovel:
 
    ResourceLocation shovelIcon = new ResourceLocation("minecraft", "textures/items/wood_shovel.png");
 
 As a note to myself and others because it caught me for a while, 
 wooden items in the game have ids such as "minecraft:wooden_shovel" but their textures are named "wood_shovel.png"
 which caused confusion.
 
 # TODO
Filled everything in for now. Will add more as development continues.