# Structure blocks!

## How to use:
This mod adds three new blocks.

## 1. The structure block

When placed, interacting with it will open a menu.
This menu has two tabs - load and save structure.

### Save structure tab
- Size - the size of the selection zone
- Offset - offset of the selection zone from the structure block
- Structure ID - The ID of the structure, formatted: (mod):(structure)
- Replace with - A block state save string. When saving the structure, the structure block itself will be replaced with this.
- Convert air to void - When on, all air blocks in your structure will be converted into structure void upon saving

To save a structure, simply line up the selection zone (represented with a red outline)
with the desired structure using the apply button and input the desired structure id and replace block, and then press Save Structure.

Saved structures are stored at (save folder)/mods/assets/(mod)/structures/(structure).zip

### Load structure tab
To load a structure, first input the structure id, then press apply. This will show the outline of the structure.
Align the outline with wherever you want the place the structure and then press Load Structure.

## 2. Structure void
Unlike other blocks, structure void will not replace any blocks when loading a structure.

## 3. Structure air
When saving a structure, any structure air blocks will be automatically converted to regular air.

This is useful when the "Convert air to void" setting is enabled, but you still want to have air in your structure