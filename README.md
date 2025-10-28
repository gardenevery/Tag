# 🏷️ Minecraft 1.12.2 Tag System

A lightweight and extensible tag system for Minecraft mods, supporting **items**, **fluids**, and **blocks**.
The system automatically syncs all Ore Dictionary entries.  
**Please note: Block tags do not support different metadata variants of the same block.**

## 📦 Features

- ✅ Create and manage tags for **items**, **blocks**, and **fluids**
- ✅ Automatically synchronize ore dictionary entries to the tag system
- ✅ In-game command to check tags of held items
- ✅ Fast lookup using key-based storage

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
Use `/gettags` while holding an item to see its tags.

#### Code API
// Get all tags for an item  
`Set<String> tags = TagHelper.getTags(itemStack);`

// Check if an item has a tag  
`boolean hasTag = TagHelper.hasTag(itemStack, "minecraft:food");`

// Get all items with a tag  
`Set<ItemStack> items = TagHelper.getItemStacks("minecraft:food");`

### Ore Dictionary Sync

The system automatically syncs all Ore Dictionary entries to the tag system during the post-initialization phase.

## 🧩 Class Overview

| Class        | Purpose                            |
|--------------|------------------------------------|
| `TagBuilder` | Main entry point for creating tags |
| `TagHelper`  | Utility for querying tags          |
| `TagManager` | Internal tag storage and lookup    |
| `Tag`        | Generic tag container              |
| `TagSync`    | Syncs Ore Dictionary to tags       |
| `TagCommand` | In-game command for tag            |

---

# 🏷️ Minecraft 1.12.2 标签系统

一个轻量级、可扩展的 Minecraft 模组标签系统，支持**物品**、**流体**和**方块**。
标签系统会自动同步所有矿物词典条目。  
**注意：方块标签无法支持同一方块的不同元数据变体。**

## 📦 功能特性

- ✅ 为**物品**、**方块**和**流体**创建和管理标签
- ✅ 自动同步矿物词典条目到标签系统
- ✅ 游戏内命令检查手持物品的标签
- ✅ 使用键值存储实现快速查找

## 🛠️ 使用方法

### 创建标签

#### 物品标签

`TagBuilder.item("mymod:my_tag")`  
`.add(new ItemStack(Items.APPLE))`  
`.add(new ItemStack(Items.GOLDEN_APPLE));`

#### 流体标签

`TagBuilder.fluid("minecraft:lava")`  
`.add(new FluidStack(FluidRegistry.LAVA, 1000));`

#### 方块标签

`TagBuilder.block("minecraft:stone_variants")`  
`.add(Blocks.STONE)`  
`.add(Blocks.ANDESITE);`

### 查询标签

#### 游戏内命令
手持物品时使用 `/gettags` 查看其标签。

#### 代码 API
// 获取物品的所有标签  
`Set<String> tags = TagHelper.getTags(itemStack);`

// 检查物品是否有某个标签  
`boolean hasTag = TagHelper.hasTag(itemStack, "minecraft:food");`

// 获取拥有某个标签的所有物品  
`Set<ItemStack> items = TagHelper.getItemStacks("minecraft:food");`

### 矿物词典同步

在FMLPostInitializationEvent阶段自动将所有矿物词典条目同步到标签系统。


## 🧩 类概览

| 类名           | 用途         |
|--------------|------------|
| `TagBuilder` | 创建标签的主要入口  |
| `TagHelper`  | 查询标签的工具类   |
| `TagManager` | 内部标签存储和查找  |
| `Tag`        | 通用标签容器     |
| `TagSync`    | 将矿物词典同步到标签 |
| `TagCommand` | 游戏内标签命令    |

---
