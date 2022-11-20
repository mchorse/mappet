## Version 0.2.4

This update is brought to you by TorayLife and McHorse.

**Compatible** with McLib **2.4.1**, Blockbuster **2.6** and Metamorph **1.3.1**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

* Added support for any Java scripting engine. This means that scripts can be written in any language for which there is an engine. You can search for your language by `java <language> scripting engine`. Mappet itself will determine which engine to use by file extension. (`js` - `javascript`, `kts` - `Kotlin Script`, etc). (Made in cooperation with [dyamo](https://github.com/dyam0))
* Added the ability to customize the syntax highlighting in the script editor. Use the files in the `config/mappet/highlights/` directory to customize it. Files for js and kts extensions are supplied by default.
* Added a special error text if no script engine is found.
* The `unique` toggle for scripts is now enabled by default.
* Added a folder system for events, dialogues, quests, scripts. etc. Use RMB on the file add, rename, remove icons for new functionality. (May contain bugs)

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
* Added mod option to limit non-creative players to use NPC tool
* Added mod option to limit non-creative players to open Mappet dashboard (even with OP)
* Added toggle to player journal to toggle visibility of a quest in HUD
* Added comment node to events as well
* Added scripting methods:
    * Global triggers now pass `event` variable (which is a Forge event), it's useful for advanced scripting (requires knowledge of working with MCP-SRG maps)
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

This is a small patch which also features new expression condition block. For full change log, check out [this page](https://github.com/mchorse/mappet/wiki/Change-log#01-to-011).

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1

I don't really have anything to say about this update. It has a couple of neat QoL features, but I think that is it. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change-log#-rc5-to-01). 

**Compatible** with McLib **2.3.5**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc5)

This update features new **world morphs** feature, which allows to display client side morphs without spawning entities (which is a really good tool for visual effects), more scripting methods, more global triggers, and a couple of neat tweaks. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change-log#-rc4-to--rc5).

**Compatible** with McLib **2.3.3**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc4)

This update features more UI component scripting methods, more global triggers, and many neat tweaks. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change-log#-rc3-to--rc4).

**Compatible** with McLib **2.3.3**, Blockbuster **2.3** and Metamorph **1.2.9**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1 (rc3)

This update features new UI scripting API and a couple of useful scripting functions. For full list of changes, check [this page](https://github.com/mchorse/mappet/wiki/Change-log#-rc1-to--rc3).

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

The list of changes is available [here](https://github.com/mchorse/mappet/wiki/Change-log#-dev3-to--rc1). Main features of this update are: in-game scripting documentation, scripting REPL, states editors, HUD scenes and Trigger node.

**Compatible** with McLib **2.3**, Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.

## Version 0.1-alpha

This first release provides tons of features: states, conditions, expressions, emitter block, trigger block, region block, crafting tables, quests, NPCs, factions, events, scripts, dialogues, quest chains, many commands, custom sound events, target selectors, global triggers, and much more.

**Compatible** with McLib **2.3** and optionally with Blockbuster **2.3** and Metamorph **1.2.7**. It doesn't mean that future versions of McLib, Blockbuster and Metamorph would be incompatible, but older versions are most likely incompatible.