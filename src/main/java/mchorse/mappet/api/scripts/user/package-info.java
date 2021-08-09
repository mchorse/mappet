/**
 * This section covers main Scripting API.
 *
 * <p>Here is an example script. If you put following code into world's
 * <code>mappet/scripts/example.js</code> and execute command
 * <code>/mp script eval @r example</code>, then you'll get teleported to
 * (249, 56, -789) and you'll see the message in the chat.</p>
 *
 * <pre>{@code
 *    function main(c)
 *    {
 *        // Teleport the subject entity to specific coordinates, and
 *        // send a message to everyone on the server
 *        c.getSubject().setPosition(249, 56, -789);
 *
 *        c.send("Teleported " + c.getSubject().getName() + " to (249, 56, -789)");
 *    }
 * }</pre>
 */

package mchorse.mappet.api.scripts.user;