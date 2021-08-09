/**
 * Welcome to Mappet's in-game scripting documentation!
 *
 * <p>Scripts are JavaScript (ES 5.1) programs that allow you to program game
 * logic.</p>
 *
 * <p>Scripts are stored in world's <code>mappet/scripts/</code> folder.
 * The filename without extension of the script is its ID. Depending on
 * the configuration of the script, there might be an additional JSON file
 * with extra non-code data.</p>
 *
 * <p>Here are global variable(s) that are provided by Mappet:</p>
 *
 * <ul>
 *     <li><code>mappet</code>, it's a {@link mchorse.mappet.api.scripts.user.IScriptFactory}.
 *     It allows you to create and query different Minecraft or Mappet data structures.</li>
 * </ul>
 *
 * <p>There are currently two packages available in Mappet:</p>
 *
 * <ul>
 *     <li><code>Scripting API</code> section covers functions that allow you to interact with
 *     Minecraft's mechanics to some extent and with Mappet.</li>
 *     <li><code>UI API</code> section covers functions that allow you to create custom user
 *     intrfaces (UI) which can be used for plethora of things.</li>
 * </ul>
 *
 * <p>Big thanks to TorayLife for scripting API suggestions.</p>
 */

package mchorse.mappet.api.scripts.user.mappet;