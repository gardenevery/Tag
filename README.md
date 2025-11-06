# 🏷️ Minecraft 1.12.2 Tag System

A lightweight tag system for Minecraft mods, supporting items, fluids, and blocks.
Automatically synchronizes with Ore Dictionary.
**Note: Block tags cannot distinguish between different metadata variants of the same block.**

## 📦 Features

- Create and manage tags for items, fluids, and blocks
- Automatic Ore Dictionary synchronization
- In-game command to check item/fluid tags
- High-performance lookups using fastutil collections
- Bidirectional queries: tags ↔ items/fluids/blocks

## 🛠️ Usage

### Creating Tags

#### Item Tags
`TagBuilder.item("mymod:my_tag")`  
`.add(new ItemStack(Items.APPLE))`  
`.add(new ItemStack(Items.GOLDEN_APPLE));`

#### Fluid Tags
`TagBuilder.fluid("minecraft:lava")`  
`.add(new FluidStack(FluidRegistry.LAVA, 1000));`

#### Block Tags
`TagBuilder.block("minecraft:stone_variants")`  
`.add(Blocks.STONE)`  
`.add(Blocks.ANDESITE);`

### Querying Tags

#### In-Game Command
Use the `/tag info` command to view all tags  
Use `/tag` while holding an item to see its tags:
- If holding a **fluid container** (like a bucket), shows **fluid tags**
- Otherwise, shows **item tags**

#### Code API

##### Get All Tags
`// Get all tags for an item`  
`Set<String> tags = TagHelper.tags(itemStack);`

`// Get all tags for a fluid`  
`Set<String> tags = TagHelper.tags(fluidStack);`

`// Get all tags for a block`  
`Set<String> tags = TagHelper.tags(block);`

##### Check Specific Tag
`// Check if an item has a specific tag`  
`boolean hasTag = TagHelper.hasTag(itemStack, "minecraft:food");`

`// Check if a fluid has a specific tag`  
`boolean hasTag = TagHelper.hasTag(fluidStack, "forge:lava");`

##### Check Any Tags
`// Check if an item has any of the specified tags`  
`boolean hasAny = TagHelper.hasAnyTags(itemStack, "minecraft:food", "forge:tools");`

`// Check using a collection`  
`List<String> tagList = Arrays.asList("minecraft:food", "forge:tools");`  
`boolean hasAny = TagHelper.hasAnyTags(itemStack, tagList);`

##### Get All Objects with a Tag
`// Get all items with a specific tag`  
`Set<ItemStack> items = TagHelper.getItemStacks("minecraft:food");`

`// Get all fluids with a specific tag`  
`Set<FluidStack> fluids = TagHelper.getFluidStacks("forge:lava");`

##### Tag Existence Check
`// Check if a tag exists (any type)`  
`boolean exists = TagHelper.tagNameExist("minecraft:food");`

`// Check if a tag exists for a specific type`  
`boolean exists = TagHelper.tagNameExist("minecraft:food", TagType.ITEM);`

##### Get All Tag Names
`// Get all item tag names`  
`Set<String> allItemTags = TagHelper.getAllTags(TagType.ITEM);`

`// Get all fluid tag names`  
`Set<String> allFluidTags = TagHelper.getAllTags(TagType.FLUID);`

### Tag Naming Rules
- Can only contain: **letters**, **number**, **colon (`:`)**, **underscore (`_`)**, **slash (`/`)**
- Cannot be null
- Examples: `"minecraft:food"`, `"forge:ores/iron"`, `"Special_123"`

### Important Limitations
- **Tag registration must be completed before `FMLLoadCompleteEvent`**

---

## Credit
- [JEI Integration](https://github.com/SnowShock35/jei-integration/tree/1.12) [MIT]
- [ForgeDevEnv](https://github.com/CleanroomMC/ForgeDevEnv) [MIT]
