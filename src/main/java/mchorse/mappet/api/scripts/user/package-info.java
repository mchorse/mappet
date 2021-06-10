/**
 * Classes in this package define Mappet's scripting API.
 *
 * Scripts are JavaScript (ES 5.1) programs that allow to program game
 * logic.
 *
 * Scripts are stored in world's <code>mappet/scripts/</code> folder.
 * The filename without extension of the script is its ID. Depending on
 * the configuration of the script, there might be an additional JSON file
 * with extra non-code data.
 *
 * Here is an example script:
 *
 * <pre>{@code
 *     function main(c) {
 *         // Teleport the subject entity to specific coordinates, and
 *         // send a message to everyone on the server
 *         c.getSubject().setPosition(249, 56, -789);
 *
 *         c.send("Teleported " + c.getSubject().getName() + " to (249, 56, -789)");
 *     }
 * }</pre>
 *
 * Big thanks to TorayLife for scripting API suggestions.
 */

package mchorse.mappet.api.scripts.user;