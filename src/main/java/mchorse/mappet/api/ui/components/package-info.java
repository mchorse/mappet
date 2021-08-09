/**
 * UI API is an Application Programming Inteface (API) which allows you
 * to create custom graphical User Intefaces (UI). It could be used for
 * plethora of things:
 *
 * <p>Custom dialogue system, disabling user input, diaries (discovering
 * notes, logs, lore fragments, etc.), mini-games, unlocking doors,
 * point-and-click games, player creation screen, custom administration
 * tools, custom quest quest offers, and so on.</p>
 *
 * <p>Here is a really basic example that drops an item upon pressing
 * a button:</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        var ui = mappet.createUI(c, "handler").background();
 *        var button = ui.button("Give me a diamond!").id("button");
 *
 *        button.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 *
 *    function handler(c)
 *    {
 *        var s = c.getSubject();
 *        var uiContext = s.getUIContext();
 *
 *        if (uiContext.getLast() === "button")
 *        {
 *            var pos = s.getPosition();
 *
 *            c.getWorld().dropItemStack(mappet.createItem("minecraft:diamond"), pos.x, pos.y + 2, pos.z);
 *        }
 *    }
 * }</pre>
 */

package mchorse.mappet.api.ui.components;