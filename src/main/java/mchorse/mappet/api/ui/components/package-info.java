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
 * <p><b>All</b> UI components are based off {@link mchorse.mappet.api.ui.components.UIComponent},
 * therefore they all have UIComponent's methods.</p>
 *
 * <p>Here is a really basic example that drops an item upon pressing
 * a button:</p>
 *
 * <pre>{@code
 *    function handler(c)
 *    {
 *        let s = c.getSubject();
 *        let uiContext = s.getUIContext();
 *
 *        if (uiContext.getLast() === "button")
 *        {
 *            let pos = s.getPosition();
 *
 *            c.getWorld().dropItemStack(mappet.createItem("minecraft:diamond"), pos.x, pos.y + 2, pos.z);
 *        }
 *    }
 *
 *    function main(c)
 *    {
 *        let ui = mappet.createUI(handler).background();
 *        let button = ui.button("Give me a diamond!").id("button");
 *
 *        button.rxy(0.5, 0.5).wh(160, 20).anchor(0.5);
 *        c.getSubject().openUI(ui);
 *    }
 * }</pre>
 */

package mchorse.mappet.api.ui.components;
