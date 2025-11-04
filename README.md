# 🏷️ Minecraft 1.12.2 Tag System

A lightweight Minecraft mod tag system supporting **items**, **fluids**, **blocks**, and **block states**.
The system automatically synchronizes all Ore Dictionary entries.  
**Note: Block tags do not support different metadata variants of the same block.**

## 📦 Features

- ✅ Create and manage tags for **items**, **fluids**,and **blocks**
- ✅ Automatically synchronize all Ore Dictionary entries to the tag system
- ✅ In-game command to check tags of held items/fluids
- ✅ Tag name validation (only letters, numbers, `:`, `_`, `/` allowed)
- ✅ Registration timing protection (only before FMLLoadCompleteEvent)
- ✅ High-performance lookups (based on fastutil collections)
- ✅ Safe unmodifiable collection returns
- ✅ Bidirectional fast queries: tags ↔ keys

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
- Can only contain: **letters**, **colon (`:`)**, **underscore (`_`)**, **slash (`/`)**
- Cannot be empty or null
- Examples: `"minecraft:food"`, `"forge:ores/iron"`, `"Special_123"`

### Important Limitations
- **Tag registration must be completed before `FMLLoadCompleteEvent`**
- Attempting to register tags after `FMLPostInitializationEvent` will throw `IllegalStateException`
- **Block tags do not support metadata variants** (use block state tags instead)

## 🔧 Integration

### Automatic Ore Dictionary Sync
The system automatically synchronizes all Ore Dictionary entries during `FMLPostInitializationEvent`:
- Regular item stacks are synchronized directly
- Wildcard metadata (`OreDictionary.WILDCARD_VALUE`) attempts to sync first 16 metadata variants
- Sync process logs success and failure counts

### Performance Characteristics
- Uses `fastutil` collections for optimized memory and lookup performance
- Bidirectional mapping storage: `tag → key sets` and `key → tag sets`
- Returns `Collections.unmodifiableSet()` for data safety

## 🧩 Class Overview

| Class        | Purpose                            | Description                                            |
|--------------|------------------------------------|--------------------------------------------------------|
| `TagBuilder` | Main entry point for creating tags | Provides static methods to create various tag builders |
| `TagHelper`  | Utility class for querying tags    | Entry point for all tag query operations               |
| `TagManager` | Internal tag storage and lookup    | Manages three types of Tag instances                   |
| `Tag`        | Generic tag container              | Uses generics to support different key types           |
| `TagSync`    | Syncs Ore Dictionary to tags       | Handles Ore Dictionary synchronization logic           |
| `TagCommand` | In-game tag command                | Implements `/tag, /tag info` command                   |
| `TagType`    | Tag type enum                      | Defines ITEM, FLUID, BLOCK                             |
| `TagMod`     | Main mod class                     | Registers events and commands                          |

## 📁 Package Structure

`com.gardenevery.tag/`  
`├── Tag.java              # Core tag storage logic`  
`├── TagBuilder.java       # Tag builder`  
`├── TagHelper.java        # Tag query utility`  
`├── TagManager.java       # Tag manager`  
`├── TagMod.java           # Main mod class`  
`├── TagSync.java          # Ore Dictionary synchronization`  
`├── TagCommand.java       # In-game command`  
`├── TagType.java          # Tag type enum`  
`└── key/                  # Key types package`  
`    ├── Key.java          # Key interface`  
`    ├── ItemKey.java      # Item key`  
`    ├── FluidKey.java     # Fluid key`  
`    ├── BlockKey.java     # Block key`

## ⚠️ Important Notes

1. **Registration Timing**: Ensure all tag registration is completed before `FMLLoadCompleteEvent`
2. **Block Metadata**: Block tags do not support metadata variants

---

## Credit
- [JEI Integration](https://github.com/SnowShock35/jei-integration/tree/1.12) [MIT]