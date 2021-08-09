package mchorse.mappet.api.scripts.user.entities;

import mchorse.mappet.api.scripts.code.mappet.MappetUIContext;
import mchorse.mappet.api.scripts.user.items.IScriptInventory;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIBuilder;
import mchorse.mappet.api.scripts.user.mappet.IMappetQuests;
import mchorse.mappet.api.scripts.user.mappet.IMappetUIContext;
import net.minecraft.entity.player.EntityPlayerMP;

/**
 * Player entity interface.
 *
 * <p>This interface represents a player entity.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        if (c.getSubject().isPlayer())
 *        {
 *            // Do something with the player...
 *        }
 *    }
 * }</pre>
 */
public interface IScriptPlayer extends IScriptEntity
{
    /**
     * Get Minecraft player entity instance. <b>BEWARE:</b> you need to know the
     * MCP mappings in order to directly call methods on this instance!
     */
    public EntityPlayerMP getMinecraftPlayer();

    /**
     * Get player's game mode.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var player = c.getSubject();
     *    var gamemode = player.getGameMode();
     *
     *    if (gamemode === 0)
     *    {
     *        player.send("You're in survival mode!");
     *    }
     * }</pre>
     *
     * @return Player's game mode as an integer, <code>0</code> is survival, <code>1</code>
     * is creative, <code>2</code> is adventure , and <code>3</code> is spectator.
     */
    public int getGameMode();

    /**
     * Set player's game mode.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var player = c.getSubject();
     *    var gamemode = c.getSubject().getGameMode();
     *
     *    // When player exits the mining region, set their game mode back to adventure
     *    if (gamemode === 0 && !player.getStates().has("region.mining_factory"))
     *    {
     *        player.setGameMode(2);
     *    }
     * }</pre>
     *
     * @param gameMode Player's game mode <code>0</code> is survival, <code>1</code>
     * is creative, <code>2</code> is adventure , and <code>3</code> is spectator.
     */
    public void setGameMode(int gameMode);

    /**
     * Get player's inventory.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var inventory = c.getSubject().getInventory();
     *    var item = mappet.createItem("minecraft:diamond_sword");
     *
     *    // This will change the first slot in the hotbar to a diamond sword
     *    inventory.setStack(0, item);
     * }</pre>
     */
    public IScriptInventory getInventory();

    /**
     * Get player's ender chest inventory.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var inventory = c.getSubject().getEnderChest();
     *    var item = mappet.createItem("minecraft:diamond_sword");
     *
     *    // This will change the first slot in player's ender chest to a diamond sword
     *    inventory.setStack(0, item);
     * }</pre>
     */
    public IScriptInventory getEnderChest();

    /**
     * Send a message to this entity.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    c.send("I love all my players equally.");
     *    c.getSubject().send("...but between you and me, you're my favorite player ;)");
     * }</pre>
     */
    public void send(String message);

    /**
     * Get player's skin.
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var player = c.getSubject();
     *    var morph = mappet.createMorph('{Name:"blockbuster.fred",Skin:"' + player.getSkin() + '",Pose:"dabbing"}');
     *
     *    player.setMorph(morph);
     * }</pre>
     *
     * @return Resource location in format of "minecraft:skins/..." (which can be used in morphs)
     */
    public String getSkin();

    /* Mappet stuff */

    /**
     * Get entity's quests (if it has some, only players have quests).
     *
     * <pre>{@code
     *    // Assuming that c.getSubject() is a player
     *    var quests = c.getSubject().getQuests();
     *
     *    if (!quests.has("important_quest"))
     *    {
     *        c.getSubject().send("I think you should complete the main quest chain before attempting side quests...");
     *    }
     * }</pre>
     */
    public IMappetQuests getQuests();

    /**
     * Open UI for this player.
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI().background();
     *        var button = ui.button("Push me").id("button");
     *
     *        // Place a button in the middle of the screen
     *        button.rxy(0.5, 0.5).wh(80, 20).anchor(0.5);
     *        c.getSubject().openUI(ui);
     *    }
     * }</pre>
     */
    public default void openUI(IMappetUIBuilder builder)
    {
        this.openUI(builder, false);
    }

    /**
     * Open UI for this player with default data populated.
     *
     * <p>By default, default data population is disabled, meaning that
     * once the UI was opened, UI context's data will be empty. By enabling
     * default data population, UI context's data gets filled with all
     * component's default data.</p>
     *
     * <p>This is useful when you need to data to be present in the handler
     * at start, so you wouldn't need to do extra checks.</p>
     *
     * <pre>{@code
     *    function main(c)
     *    {
     *        var ui = mappet.createUI(c, "handler").background();
     *        var button = ui.button("Push me").id("button");
     *        var name = ui.textbox("John").id("name");
     *        var lastname = ui.textbox("Smith").id("lastname");
     *
     *        // Place a button in the middle of the screen
     *        button.rxy(0.5, 0.5).wh(80, 20).anchor(0.5);
     *        name.rx(0.5).ry(0.5, 25).wh(80, 20).anchor(0.5);
     *        lastname.rx(0.5).ry(0.5, 50).wh(80, 20).anchor(0.5);
     *
     *        // Open the UI with default data populated
     *        c.getSubject().openUI(ui, true);
     *    }
     *
     *    function handler(c)
     *    {
     *        var uiContext = c.getSubject().getUIContext();
     *        var data = uiContext.getData();
     *
     *        // If false was passed into openUI as second argument
     *        // Then name or last name wouldn't be immediately populated
     *        // as John Smith
     *        c.send("Your name is: " + data.getString("name") + " " + data.getString("lastname"));
     *    }
     * }</pre>
     */
    public boolean openUI(IMappetUIBuilder builder, boolean defaultData);

    /**
     * Close the user interface
     */
    public void closeUI();

    /**
     * Get the UI context of currently opened user UI. See {@link MappetUIContext}
     * for code examples.
     */
    public IMappetUIContext getUIContext();
}