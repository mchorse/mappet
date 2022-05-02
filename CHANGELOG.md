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