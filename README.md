
Bart
=======

Bart

## Planned changes
I doubt anyone will read this, but for anyone curious, the update i have planned is called Update Upgrade.

It'll rewrite the buff logic to hopefully make /bart execute not a thing anymore. (I will admit, it sucks. But it did get the job done.)

## Making Your Own Buffs For Dummies
Alright so this is written before the rewrite, what you currently do is:
* Put a .json file (the name is irrelevant) into data/namespace/bart/upgrades
* Make the file contain the following fields:
  * "target" - Either "player" or "bart", determines who the buff is for.
  * "name" - The name displayed on the first row. This uses Component.literal.
  * "altText" - The text displayed on the second row. This uses Component.literal.
  * "effect" - The text displayed on the third row. This uses Component.literal.
  * "command" - The command to run when this buff is chosen. Yes, this uses commands.
  * "icon" - The file location of the icon used for this buff.

Now, the rewrite plans to do the following:
* Put a .json file into data/namespace/bart/buffs
* Make the file contain the following:
  * "target" - As before the rewrite.
  * "action" - The thing to do when the buff is chosen. Consists of:
    * "type" - Either "attribute", "item", or "command".
    * "data" - The actual information for the attribute.
      * For attributes:
        * "modtype" - Either "add", "mult_base" or "mult_value". It's the stuff in the command.
        * "id" - What attribute to actually apply
        * "value" - Self-explanatory.
      * For items, you just provide an item under "item".
      * For commands, you just provide a command under "command".
