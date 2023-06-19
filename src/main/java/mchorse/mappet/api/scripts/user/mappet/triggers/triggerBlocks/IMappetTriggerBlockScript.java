package mchorse.mappet.api.scripts.user.mappet.triggers.triggerBlocks;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import mchorse.mappet.api.scripts.code.mappet.triggers.triggerBlocks.MappetTriggerBlockScript;
import mchorse.mappet.api.triggers.blocks.ScriptTriggerBlock;

/**
 * This section covers Mappet's script trigger blocks.
 */
public interface IMappetTriggerBlockScript
{

    /**
     * Get the minecraft script trigger block
     *
     * @return ScriptTriggerBlock
     */
    public ScriptTriggerBlock getMinecraftScriptTriggerBlock();

    /**
     * Set the script name
     *
     * <pre>{@code
     * // This example sets the script name of the script trigger block to "regionBlock.js"
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setScriptName("regionBlock.js");
     * }</pre>
     *
     * @param scriptName The script name
     * @return MappetScriptTriggerBlock
     */
    public MappetTriggerBlockScript setScriptName(String scriptName);

    /**
     * Get the script name
     *
     * <pre>{@code
     * // This example gets the script names of the script trigger blocks in the given region block
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var scriptTriggerBlock in scriptTriggerBlocks) {
     *     if (scriptTriggerBlock.getType() === "script") {
     *         var scriptName = scriptTriggerBlock.getScriptName();
     *         c.send("Script name: " + scriptName);
     *     }
     * }
     * }</pre>
     *
     * @return String
     */
    public String getScriptName();

    /**
     * Set the function name
     *
     * <pre>{@code
     * // This example sets the function name of the script trigger block to "onTick" in the script "regionBlock.js"
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setScriptName("regionBlock.js");
     * scriptTriggerBlock.setFunctionName("onTick");
     * }</pre>
     *
     * @param functionName The function name
     * @return MappetScriptTriggerBlock
     */
    public MappetTriggerBlockScript setFunctionName(String functionName);

    /**
     * Get the function name
     *
     * <pre>{@code
     * // This example gets the scripts names with their function names of the script trigger blocks in the given region block
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var scriptTriggerBlock in scriptTriggerBlocks) {
     *     if (scriptTriggerBlock.getType() === "script") {
     *         var functionName = scriptTriggerBlock.getFunctionName();
     *         c.send("Script function name: " + functionName + " in script " + scriptTriggerBlock.getScriptName());
     *     }
     * }
     * }</pre>
     *
     * @return String
     */
    public String getFunctionName();

    /**
     * Set the custom data
     *
     * <pre>{@code
     * // This example sets custom data of the script trigger block in the script "regionBlock.js"
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setScriptName("regionBlock.js");
     * scriptTriggerBlock.setCustomData("{x:0,y:4,z:0}");
     * }</pre>
     *
     * @param customData The custom data
     * @return MappetScriptTriggerBlock
     */
    public MappetTriggerBlockScript setCustomData(String customData);

    /**
     * Get the custom data
     *
     * <pre>{@code
     * // This example gets the custom data of the script trigger blocks in the given region block
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var scriptTriggerBlock in scriptTriggerBlocks) {
     *     if (scriptTriggerBlock.getType() === "script") {
     *         var customData = scriptTriggerBlock.getCustomData();
     *         c.send("Script custom data: " + customData + " in script " + scriptTriggerBlock.getScriptName());
     *     }
     * }
     * }</pre>
     *
     * @return String
     */
    public String getCustomData();

    /**
     * Set the inline code
     *
     * <pre>{@code
     * // This example sets the inline code of the script trigger block
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnEnterTrigger();
     * var scriptTriggerBlock = regionBlockTrigger.addScriptBlock();
     * scriptTriggerBlock.setInlineCode(function() {
     *     c.getSubject().send("Welcome to this region!");
     * });
     * }</pre>
     *
     * @param script The inline code
     * @return MappetScriptTriggerBlock
     */
    public MappetTriggerBlockScript setInlineCode(ScriptObjectMirror script);

    /**
     * Get the inline code
     *
     * <pre>{@code
     * // This example gets the inline code of the `on tick trigger` trigger blocks in the given region block
     * var regionBlock = c.getWorld().getRegionBlock(0, 4, 0);
     * var regionBlockTrigger = regionBlock.getOnTickTrigger();
     * var scriptTriggerBlocks = regionBlockTrigger.getAllBlocks();
     * for each (var scriptTriggerBlock in scriptTriggerBlocks) {
     *     if (scriptTriggerBlock.getType() === "script") {
     *         var inlineCode = scriptTriggerBlock.getInlineCode();
     *         c.send("Script inline code: " + inlineCode);
     *     }
     * }
     * }</pre>
     *
     * @return String
     */
    public String getInlineCode();
}
