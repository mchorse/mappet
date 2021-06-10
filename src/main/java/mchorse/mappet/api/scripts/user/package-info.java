/**
 * Classes in this package define Mappet's scripting API.
 *
 * <p>
 * Scripts are JavaScript (ES 5.1) programs that allow to program game
 * logic.
 * </p>
 *
 * <p>Scripts are stored in world's <code>mappet/scripts/</code> folder.
 * The filename without extension of the script is its ID. Depending on
 * the configuration of the script, there might be an additional JSON file
 * with extra non-code data.</p>
 *
 * <p>Here is an example script. If you put following code into world's
 * <code>mappet/scripts/example.js</code> and execute command
 * <code>/mp script eval @r example</code>, then you'll get teleported to
 * (249, 56, -789) and you'll see the message in the chat.</p>
 *
 * <pre>{@code
 *    function main(c) {
 *        // Teleport the subject entity to specific coordinates, and
 *        // send a message to everyone on the server
 *        c.getSubject().setPosition(249, 56, -789);
 *
 *        c.send("Teleported " + c.getSubject().getName() + " to (249, 56, -789)");
 *    }
 * }</pre>
 *
 * <p>Big thanks to TorayLife for scripting API suggestions.</p>
 */

package mchorse.mappet.api.scripts.user;