Runecraft is based off of the Minecraft Forge API.
This project is still in very early development.

# Proposed Skills
* [Digging](Digging.MD)
* Mining
* Farming
* Fishing
* Woodcutting
* Athletics
* Dungeonnering
* Herblore
* Crafting
* Smithing?
* Runecrafting
* Prayer

# Combat Stats
Constitution stat
* Melee
  * sword
  * axe
* Ranged
  * bow
  * thrown
* Magic
  * wand
  * staff
* Defense
  * armor
  * shield

# Extensible Skills System
Part of the Runecraft mod system is an extensible skills system which allows other mod makers to hook into a Skills interface to create their own custom skills and define the interactions that skill has with players. This system comes with a mechanism to display these skills to users as part of the inventory gui and individual skills have the ability to define rewards and benefits from leveling up these skills.

## Using the system to create a new skill
The first thing to do when creating a new skill to register to the system, other than planning out the mechanics of the skill, is to decide how you will define the skill within the system. You have two options to do this:
1) Create a class extending from the BasicSkill class -- This approach is the simplest approach and gives you the most functionality for free, but takes away some freedom. The BasicSkill class comes with a fixed experience requirement per level approach, containing an array xpLevels which denotes the total xp required for each level. Having this implementation provided takes away a lot of the work of implementing a custom skill, but as mentioned you lose the ability to specify xp requirements by i.e a function approximation.

# Knowledge Base
This project is both a full-blown mod and a learning process for myself, as this is my first minecraft mod.

Throughout this process I have and will likely continue to run into several snags due to the complexity of this mod 
and the lacking amount of documentation on the Forge project.

Therefore, I have decided to compile a FAQ/List of different problems I've solved along the way including an explanation
 of the issue and an explanation of the solution in a way that could be extended to any other problems similar.
 
This list will be maintained in the [FAQ file](FAQ.md)
