## Version 0.7

This update is brought to you by TorayLife and OtakuGamer. This update features lots of new scripting API methods, QoL features, and brand new **Scripted Items** feature!

**Compatible** with McLib **2.4.2**, Aperture **1.8.1**, Blockbuster **2.6.1** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* New triggers:
  * Added **On NPC Collision** trigger
* New features:
  * Added **Scripted items** feature! It allows you to add triggers to ANY item.
  * Added **Paste player's rotation** option to script editor context menu.
  * Added **/mp playsound** command and a config option to load sounds on joining a world.
  * Added **global** option to HUDs
  * Added **shadow size**, **has no gravity**, **can be steered** and **steering properties** features on NPC's
  * Added sorting to new folders lists, so folders appears first (by TorayLife)
  * Added context menu (just like in regular script editor) to inline scripts (by TorayLife)
* API changes:
  * Added `IScriptEntity.getObservedEntity()` and `IScriptEntity.getBoundingBox()` methods
  * Added a lot of NPC's methods
  * Added `IScriptPlayer.setSpawnPoint()` and `IScriptPlayer.getSpawnPoint()` methods
  * Added `IScriptFactory.createBlockState()` and `IScriptFactory.vector()` methods
  * Added `IScriptWorld.replaceBlocks()`, `IScriptWorld.removeBlock()`, `IScriptWorld.getEntities()`, `IScriptWorld.setModelBlockEnabled()`, 'IScriptWorld.isModelBlockEnabled()' and `IScriptWorld.getBlock(ScriptVector pos)` methods
  * Added `IScriptItemStack.add()` and `IScriptItemStack.equals()` methods
  * Changed `IScriptEntity.getEyeHeight()` to work with Metamorph morphs
  * Moved `IScriptPlayer.giveItem()` to `IScriptEntity`
* Bugfixes:
  * Fixed `IScriptWorld.setModelBlockMorph()` method
  * Fixed `IScriptItemStack.addCanDestroyBlock()` method
  * Fixed forge triggers (by TorayLife)
  * Fixed keybinds (:facepalm:) (by TorayLife)
  * Fixed crafting recipe ignore NBT feature (by TorayLife)
  * Fixed build.gradle (by TorayLife)
* Other:
  * NPC tool is ustackable now

## Version 0.6

This update is brought to you by TorayLife. It features a lot of scripting related features like listening to Forge events, inline scripts, documentation improvements, and more!

**Compatible** with McLib **2.4.1**, Aperture **1.8.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* New triggers:
  * Added **Player: toss an item** trigger
* New features:
  * Added Forge event triggers that allow you to listen to Forge events. **WARNING**: this feature can corrupt your world. Use at own fear and risk!
  * Added **Inline** scripts feature from BBS, that allows you to write JS (ES5) code directly in script trigger, without creating a script file
  * Added `F6` keybind to run currently open script in the script editor
  * Added `Ctrl + D` keybind to duplicate current line in script editor
  * Added ability to translate documentation. Copy `docs.json` file from Mappet’s jar file, rename it to your language code (i.e. `ru_ru.json` for Russian) and put into `.minecraft/config/mappet/documentation/`
  * Added **Ignore NBT** option in crafts and events
  * Added search to documentation overlay
  * Added link to JavaDocs in documentation
  * Added new documentation sctructure. You can enable it in the config
* API changes:
  * Added `IScriptNpc.setFaction()` method
  * Added `entityItem` variable to **Player: picked up an item** trigger
  * Added `IScriptEntity.setMaxHp(float)` method
  * Added `IScriptFactory.getIScriptEntity(minecraftEntity)` method
  * Added `IMappetSchematic` interface, that works with existing .schematic format
  * Added methods to disable creative search and count in UIStackComponent
  * Added `IScriptEntityItemStack` interface, that represents EntityItemStack (item that dropped into the world)
  * Added `IScriptPlayer.giveItem(IScriptItemStack)` method
* Bugfixes:
  * Fixed `UIStringListComponent` doesn’t update if you set empty array as a value
  * Fixed `IScriptEvent.scheduleScript(int)` doesn’t work
  * Fixed region block doesn’t work, if you disable `passible` option
  * Fixed Kotlin doesn’t work with obfuscated methods
  * Fixed crashes when you try to cancel uncancelable event
  * Fixed crash when you open `executeScript` in documentation
  * Fixed **Player: Respawn** trigger not working
  * Fixed `createBlockState` issue with metadata
* Other:
  * Completely rewrite Russian localization

## Version 0.5.1

This patch update features Entity attacked trigger by TorayLife.

**Compatible** with McLib **2.4.1**, Aperture **1.8.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed `Entity: attacked` global trigger and its description

## Version 0.5

This is another HUGE update developed by OtakuGamer and TorayLife! This update features lots of new scripting API methods, global triggers, scripting logs panel and a conditional model block!

**IMPORTANT**: `IScriptEntity.shootBBGunProjectile()` to shoot the projectile with the whole data of the bb*gun settings (e.g. damage, etc.).

**Compatible** with McLib **2.4.1**, Aperture **1.8.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added new global triggers:
  * By TorayLife: `Entity: attacked` and `Player: open container`
  * By OtakuGamer: `Projectile: impact`, `Living: equipment change`, `Mappet: state changed`, and `Living: knockback`
* Added **logs** panel, which will display script error logs, or records entered by scripts (TorayLife)
* Added **Condition model block**, which will display different morphs depends on given conditions (TorayLife)
* Added **Check entities** option to region block (TorayLife)
* Added **onTick** trigger to region block (TorayLife)
* Added `/mp script engines` command, which returns list of installed script engines (TorayLife)
* Added **Look at player** option's range now depends on path finding distance
* Added more scripting API:
  * Added `IScriptEvent.scheduleScript(int, Consumer<IScriptEvent>)` overload with Consumer argument, so it works in different engines, such as Kotlin or Python (TorayLife)
  * Added `IScriptEntity.setName()`, `IScriptEntity.setInvisible()`, `IScriptEntity.moveTo()`, `IScriptEntity.executeCommand()`, `IScriptEntity.executeScript()`, `IScriptEntity.getFactions()`, `IScriptEntity.lockPosition()`, `IScriptEntity.unlockPosition()`, `IScriptEntity.isPositionLocked()`, `IScriptEntity.lockRotation()`, `IScriptEntity.unlockRotation()`, and `IScriptEntity.isRotationLocked()` (OtakuGamer)
  * Added `IScriptEntity.getMount()` method (TorayLife)
  * Added `IScriptFactory.getLogger()` method, which returns a `MappetLogger` which can be used to log messages (TorayLife)
  * Added `IScriptFactory.isPointInBounds()` and `IScriptFactory.toNBT()` (OtakuGamer)
  * Added `info()`, `debug()`, `warning()`, `error()` methods to MappetLogger (TorayLife)
  * Added `IScriptWorld.getBlockStackWithTile()`, `IScriptWorld.shootBBGunProjectile()` (OtakuGamer)
  * Added `IScriptServer.entityExists()` and `IScriptServer.executeScript()` (OtakuGamer)
  * Added `IScriptNBTCompound.addCompound()` and `IScriptNBTCompound.dumpJSON()` (OtakuGamer)
  * Added AI methods `IScriptEntity.observe()`, `IScriptEntity.addEntityPatrol()`, `IScriptEntity.clearEntityPatrols()`, `IScriptEntity.setRotationsAI()`, `IScriptEntity.clearRotationsAI()`, `IScriptEntity.executeRepeatingCommand()`, `IScriptEntity.removeRepeatingCommand()`, and `IScriptEntity.clearAllRepeatingCommands()` (OtakuGamer)
  * Added `IScriptFancyWorld` and moved some `IScriptWorld` methods to it (OtakuGamer)
  * Added `getFancyWorld()` to `IScriptEvent`, `IScriptServer` and `IScriptEntity` (just like `getWorld()`) (OtakuGamer)
  * Added `IScriptFancyWorld.explode()`, `IScriptFancyWorld.tpExplode()`, `IScriptFancyWorld.setBlock()`, `IScriptFancyWorld.fill()`, `IScriptFancyWorld.setTileEntity()`, `IScriptFancyWorld.fillTileEntities()`, `IScriptFancyWorld.clone()`, `IScriptFancyWorld.loadSchematic()`, and `IScriptFancyWorld.spawnNpc()` (OtakuGamer)
  * Added `IScriptPlayer.getDisplayedHUDs()`, `IScriptPlayer.playScene()`, and `IScriptPlayer.stopScene()` (OtakuGamer)
* Fixed `IScriptFactory.convertToNBT()` method (TorayLife)
* Fixed `IScriptItemStack.setLore()` method that doesn't add lore if item has no lore (TorayLife)
* Fixed python errors in console (TorayLife)
* Fixed python doesn't print unicode characters (TorayLife)
* Fixed Kotlin libraries issue (OtakuGamer)
* Improved HUD morphs to persist upon player's relogging (OtakuGamer)
* Improved `/mp hud *` commands' tab completion (OtakuGamer)
* Improved interaction events to support Little Tiles mod's doors (OtakuGamer)
* Updated NPC tool texture, added holiday textures (by TorayLife, textures by 3DMmc)

## Version 0.4

This is a HUGE scripting API update by OtakuGamer with some minor contributions by TorayLife.

**Compatible** with McLib **2.4.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added scripting `INBTCompound.get(String)` and `INBTCompound.equals(INBTCompound)`
* Added scripting `INBTList.toArray()`
* Added scripting `IScriptItemStack.getDisplayName()`, `IScriptItemStack.setDisplayName(String)`, `IScriptItemStack.getLore(int)`, `IScriptItemStack.getLoreList()`, `IScriptItemStack.setLore(int, String)`, `IScriptItemStack.addLore(String)`, `IScriptItemStack.clearAllLores()`, `IScriptItemStack.clearLore(int)`, `IScriptItemStack.clearAllEnchantments()`, `IScriptItemStack.getCanDestroyBlocks()`, `IScriptItemStack.addCanDestroyBlock(String)`, `IScriptItemStack.clearAllCanDestroyBlocks()`, `IScriptItemStack.clearCanDestroyBlock(String)`, `IScriptItemStack.getCanPlaceOnBlocks()`, `IScriptItemStack.addCanPlaceOnBlock(String)`, `IScriptItemStack.clearAllCanPlaceOnBlocks()`, `IScriptItemStack.clearCanPlaceOnBlock(String)`, `IScriptItemStack.getRepairCost()`, `IScriptItemStack.setRepairCost(int)`, `IScriptItemStack.isUnbreakable()`, and `IScriptItemStack.setUnbreakable(boolean)`
* Added scripting `IScriptEntity.getWorld()`, `IScriptEntity.addMotion(double, double, double)`, `IScriptEntity.getEyeHeight()`, `IScriptEntity.isInWater()`, `IScriptEntity.isInLava()`, `IScriptEntity.getHelmet()`, `IScriptEntity.getChestplate()`, `IScriptEntity.getLeggings()`, `IScriptEntity.getBoots()`, `IScriptEntity.setHelmet(IScriptItemStack)`, `IScriptEntity.setChestplate(IScriptItemStack)`, `IScriptEntity.setLeggings(IScriptItemStack)`, `IScriptEntity.setBoots(IScriptItemStack)`, `IScriptEntity.setArmor(IScriptItemStack, IScriptItemStack, IScriptItemStack, IScriptItemStack)`, `IScriptEntity.clearArmor()`, `IScriptEntity.isEntityInRadius(IScriptEntity, double)`, `IScriptEntity.isInBlock(int, int, int)`, `IScriptEntity.isInBlock(double, double, double, double, double, double)`, `IScriptEntity.mount(IScriptEntity)`, `IScriptEntity.dismount()`, `IScriptEntity.dropItem(int)`, `IScriptEntity.dropItem()`, `IScriptEntity.dropItem(IScriptItemStack)`, `IScriptEntity.setModifier(String, double)`, `IScriptEntity.getModifier(String)`, `IScriptEntity.removeModifier(String)`, `IScriptEntity.removeAllModifiers()`, `IScriptEntity.displayMorph(AbstractMorph, int, double, double, double, float, float, boolean, IScriptPlayer)`, `IScriptEntity.shootBBGunProjectile(String)`
* Added scripting `IScriptNpc.canPickUpLoot(boolean)`, `IScriptNpc.follow(String)`, `IScriptNpc.setOnTickTrigger(String, String, int, int)`, `IScriptNpc.addOnTickTrigger(String, String, int)`, `IScriptNpc.clearOnTickTriggers()`, `IScriptNpc.setOnInteractTrigger(String, String, int)`, `IScriptNpc.addOnInteractTrigger(String, String)`, `IScriptNpc.clearOnTickTriggers()`, `IScriptNpc.setPatrol(int, int, int, String, String, int)`, `IScriptNpc.addPatrol(int, int, int, String, String)`, `IScriptNpc.clearPatrolPoints()`, and `IScriptNpc.getFaction()`
* Added scripting `IScriptPlayer.executeCommand(String)`, `IScriptPlayer.isFlying()`, `IScriptPlayer.getWalkSpeed()`, `IScriptPlayer.getFlySpeed()`, `IScriptPlayer.setWalkSpeed(float)`, `IScriptPlayer.setFlySpeed(float)`, `IScriptPlayer.resetFlySpeed()`, `IScriptPlayer.resetWalkSpeed()`, `IScriptPlayer.getCooldown(int)`, `IScriptPlayer.getCooldown(IScriptItemStack)`, `IScriptPlayer.setCooldown(int, int)`, `IScriptPlayer.setCooldown(IScriptItemStack, int)`, `IScriptPlayer.resetCooldown(int)`, `IScriptPlayer.resetCooldown(IScriptItemStack)`, `IScriptPlayer.getHotbarIndex()`, and `IScriptPlayer.setHotbarIndex(int)`
* Added scripting `IScriptWorld.setModelBlockMorph(String, int, int, int, boolean)`, `IScriptWorld.isActive(int, int, int)`, `IScriptWorld.testForBlock(int, int, int, String, int)`, `IScriptWorld.fill(IScriptBlockState, int, int, int, int, int, int)`, `IScriptWorld.summonFallingBlock(double, double, double, String, int)`, `IScriptWorld.setFallingBlock(int, int, int)`, `IScriptWorld.fancyExplode(int, int, int, int, int, int, float)`, `IScriptWorld.fancyExplode(int, int, int, int, float)`, `IScriptWorld.tpExplode(int, int, int, int, int, int, float)`, `IScriptWorld.tpExplode(int, int, int, int, float)`, and `IScriptWorld.displayMorph(AbstractMorph, int, double, double, double, float, float, int, IScriptPlayer)`
* Added scripting `IScriptServer.isOnline(String)`
* Added scripting `IScriptFactory.random(double)`, `IScriptFactory.random(double, double)`,`IScriptFactory.random(double, double, long)`, `IScriptFactory.style(String...)`, `IScriptFactory.vector2()`, `IScriptFactory.vector2(double, double)`, `IScriptFactory.vector2(Vector2d)`, `IScriptFactory.vector3()`, `IScriptFactory.vector3(double, double, double)`, `IScriptFactory.vector3(Vector3d)`, `IScriptFactory.vector4()`, `IScriptFactory.vector4(double, double, double, double)`, `IScriptFactory.vector4(Vector4d)`, `IScriptFactory.matrix3()`, `IScriptFactory.matrix3(Matrix3d)`, `IScriptFactory.matrix4()`, and `IScriptFactory.matrix4(Matrix4d)`
* Added Can pick up loot option to NPCs (by Otaku)
* Fixed Russian translation of trigger blocks' frequency option (by TorayLife)
* Fixed encoding problems by switching file reading strictly to UTF*8 (by Pyxl*ion and TorayLife)
* Removed command `/mp data rencode`

## Version 0.3.1

This patch hot fix fixes global triggers and scripts (TorayLife).

**Compatible** with McLib **2.4.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed scripts not working correctly in global triggers

## Version 0.3

This update is brought to you by TorayLife and McHorse.

**Compatible** with McLib **2.4.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added support for any Java scripting engine. This means that scripts can be written in any language for which there is an engine. You can search for your language by `java <language> scripting engine`. Mappet itself will determine which engine to use by file extension. (`js` * `javascript`, `kts` * `Kotlin Script`, etc). Made in cooperation with [dyamo](https://github.com/dyam0)
* Added the ability to customize the syntax highlighting in the script editor. Use the files in the `config/mappet/highlights/` directory to customize it. Files for `js` and `kts` extensions are supplied by default
* Added a special error text if no script engine is found
* Added a folder system for events, dialogues, quests, scripts. etc. Use RMB on the file add, rename, remove icons for new functionality. (May contain bugs)
* Added an error when trying to set a position or rotation for an entity as NaN
* Added `IScriptEntity.getWorld()`
* Added open folder context menu to all name lists in Mappet data dashboard panels when in singleplayer
* Added `minecraft:player` to kill objective entity list
* Changed `unique` toggle for scripts to be enabled by default
* Changed `IScriptEntity.setTarget(IScriptEntity)` to accept `null`, which indicates resetting the attack target
* Changed `IScriptPlayer.openUI()` to allow opening a UI when another scripted UI is opened
* Fixed `IScriptPlayer.getSkin()` now it works much better
* Fixed GraalJS not working with Mappet's scripts
* Fixed Ingredients label present when a recipe has no ingredients

## Version 0.2.3

This patch is required to work with McLib 2.4.1 and Blockbuster 2.6. Also, Ukrainian translation was added.

**Compatible** with McLib **2.4.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added Ukrainian translation (thanks to Kirkus)

## Version 0.2.2

This quick patch update patches and updates.

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added `inventory` (which is a `IScriptInventory`) variable to `Player: inventory closes` global trigger
* Added `/mp event stop <id>` subcommand which stops execution of delayed events (with Timer nodes) and scripts (`IScriptEvent.scheduleScript()`)
* Added `Player: interact with an entity` global trigger
* Added scripting methods:
  * Added `IScriptWorld.explode()`
  * Added `IScriptItemStack.getMaxCount()`
  * Added `IScriptBlockState.isOpaque()` and `IScriptBlockState.hasCollision()`
  * Added `IScriptWorld.rayTrace()` and `IScriptWorld.rayTraceBlocks()`
  * Added `IScriptEntity.isOnGround()`
  * Added `IScriptWorld.stopSound(String, String)`, `IScriptWorld.stopAllSounds()`, `IScriptPlayer.stopSound(String, String)`, and `IScriptPlayer.stopAllSounds()`
  * Added `IScriptEntity.getCombinedLight()`
* Fixed an error with quest node in a dialogue not loading when quest isn't accepted
* Fixed respawn options can't be edited by `/mp npc edit` command
* Fixed a couple of misspellings in documentation
* Fixed NPC's target trigger

## Version 0.2.1

This quick patch update adds `IScriptEntity.getMorph()` and fixes a couple of documenation errors.

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added `IScriptEntity.getMorph()`
* Added `block` and `meta` variables to Block: interact global trigger
* Fixed a couple of documentation sample errors (thanks to OtakuGamer)

## Version 0.2

This update is brought to you by TorayLife (again), featuring NPC respawning mechanic, patrol points triggers, QoL tweaks and bug fixes! Thanks to Falkiner, ORION, OtakuGamer, Hrymka, and dyamo for ideas and suggestions!

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added NPC respawn mechanism with multiple options such as: respawn delay, respawn at died coordinates, and respawn trigger
* Added patrol trigger for NPC's patrol points
* Added mod option to limit non*creative players to use NPC tool
* Added mod option to limit non*creative players to open Mappet dashboard (even with OP)
* Added toggle to player journal to toggle visibility of a quest in HUD
* Added comment node to events as well
* Added scripting methods:
  * Global triggers now pass `event` variable (which is a Forge event), it's useful for advanced scripting (requires knowledge of working with MCP*SRG maps)
  * Added sound category to `playSound` and `playStaticSound` methods which allow to specify a channel in which sound would be playing
  * Added `IScriptPlayer.setHunger(int)`, `IScriptPlayer.getHunger()`, `IScriptPlayer.setSaturation(int)`, and `IScriptPlayer.getSaturation()`
  * Added `IScriptNpc.setNpcState(String)` and `IScriptNpc.getNpcState()`
  * Added `UIStringListComponent.setValues(List<String>)` (alias for `UIStringListComponent.values(List<String>)`) and `UIStringListComponent.getValues()`
  * Added selected index of a string list UI component to the context data (in `%component_id%.index`)
  * Added `INBTCompound.getNBTTagCompound()` (fixes a typo of deprecated method `INBTCompound.getNBTTagComound()`)
* Fixed some places missing OP checks
* Fixed morph UI component erroring on a dedicated server
* Fixed crash when trying to copy non selected data entry in one of the Mappet dashboard panels

## Version 0.1.5

This update is brought to you by TorayLife, with some neat QoL features, triggers and bug fixes!

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added new triggers:
  * `Player: log out` trigger gets triggered with a subject player when player logs off the server
  * `Player: interact an item` trigger gets triggered when a player right clicks (interacts with) an item in the air
* Added scripting methods:
  * `IMappetUIBuilder.paused(boolean)` which allows to make UI pausable in singleplayer (by McHorse)
  * `IMappetStates.isNumber(String)` and `IMappetStates.isString(String)` allow to check whether given state is of number or string type
  * `IScriptItemStack.copy()` which allows to duplicate an item stack
* Added new string value comparison types:
  * Exact match of given string (value == "...")
  * Contains somewhere in the value a string (value contains "...")
  * Regular expression match (value matches /.../)
* Added comment node to dialogues, which passes execution to its connected child nodes, but the main feature is to label or comment sections of the dialogue
* Added a feature for emitter block to check against players within the radius (rather than globally) when `radius > 0`
* Fixed `Entity: hurt` trigger getting triggered only when a player hurts an entity (and not when other entities hurt each other)
* Fixed server states editor switching back to `~` upon return back to panel instead of refreshing currently selected states (by McHorse)

## Version 0.1.4

More bug fixes and tiny tweaks.

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added limits to health fields
* Added scripting `Graphic.anchor(float)`, `Graphic.anchor(float, float)`, `Graphic.anchorX(float)` and `Graphic.anchorY(float)`
* Changed `IScriptEvent.executeCommand(String)` return type from `void` to `int`
* Fixed quest dialogue node not correctly comparing quest completion
* Fixed server states are being discarded due to old JSON loading code and string based states
* Fixed division by 0 error with regeneration frequency
* Fixed targeting for non post NPCs
* Fixed textures are not being displayed in graphics UI component due to RL lowercasing the path
* Removed `Binary` toggle for `switch` node

## Version 0.1.3

This is a small patch update which fixes a couple of other things.

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added `IScriptServer.getEntity(String)` scripting method
* Added Copy text context menu item to REPL messages, `clear()` function to clear all REPL messages, and `s` variable for fast `c.getSubject()` access within REPL
* Fixed player gets teleported (when they shouldn't) when using `IScriptPlayer.setRotations(float, float, float)`
* Fixed `UILayoutComponent.width(int)` not working properly with scrolling layout
* Fixed NPC ID gets erased when using `/mp npc state` command

## Version 0.1.2

This is a small patch update which fixes a couple of things.

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added Damage delay and Fallback distance options to NPCs (thanks to Evanechecssss)
* Fixed player item pick up `Player: picked up an item` not working
* Fixed wrong global trigger tooltip information
* Fixed accelerated animation of morphs in HUD scenes when multiple players present

## Version 0.1.1

This is a small patch which also features new expression condition block. For full change log, check out [this page](https://github.com/mchorse/mappet/wiki/Change*log#01*to*011).

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1

I don't really have anything to say about this update. It has a couple of neat QoL features, but I think that is it. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change*log#*rc5*to*01). 

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc5)

This update features new **world morphs** feature, which allows to display client side morphs without spawning entities (which is a really good tool for visual effects), more scripting methods, more global triggers, and a couple of neat tweaks. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change*log#*rc4*to**rc5).

**Compatible** with McLib **2.3.3**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc4)

This update features more UI component scripting methods, more global triggers, and many neat tweaks. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change*log#*rc3*to**rc4).

**Compatible** with McLib **2.3.3**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc3)

This update features new UI scripting API and a couple of useful scripting functions. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change*log#*rc1*to**rc3).

**Compatible** with McLib **2.3.2**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc2)

This bugfix update features only some important bug and crash fixes.

**Compatible** with McLib **2.3**, Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Fixed crash on dedicated server due to new HUD morph changes
* Fixed players losing states and quests data on Mohist
* Fixed crash when empty reaction was given in the dialogue
* Fixed missing /mp hud commands help messages
* Fixed HUD morphs not animating when updating with /mp hud morph command
* Fixed JS scripts over 64kb kick out of the server (big thanks to Joziah2 for thoroughly testing it)

## Version 0.1 (rc1)

The list of changes is available [here](https://github.com/mchorse/mappet/wiki/Change*log#*dev3*to**rc1). Main features of this update are: in*game scripting documentation, scripting REPL, states editors, HUD scenes and Trigger node.

**Compatible** with McLib **2.3**, Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1*alpha

This first release provides tons of features: states, conditions, expressions, emitter block, trigger block, region block, crafting tables, quests, NPCs, factions, events, scripts, dialogues, quest chains, many commands, custom sound events, target selectors, global triggers, and much more.

**Compatible** with McLib **2.3** and optionally with Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.