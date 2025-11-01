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
Use `/gettags` while holding an item to see its tags:
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
| `TagManager` | Internal tag storage and lookup    | Manages four types of Tag instances                    |
| `Tag`        | Generic tag container              | Uses generics to support different key types           |
| `TagSync`    | Syncs Ore Dictionary to tags       | Handles Ore Dictionary synchronization logic           |
| `TagCommand` | In-game tag command                | Implements `/gettags` command                          |
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
3. **Thread Safety**: Tag query operations are thread-safe, but registration should be done on the main thread

## 🔍 Debugging Tips

Use the `/gettags` command to quickly verify if tags are applied correctly, or check logs for Ore Dictionary sync results:

`[TagSync] === Starting Ore Dictionary Sync ===`  
`[TagSync] Found 155 ore dictionary categories`  
`[TagSync] === Sync completed: 403 successful, 0 failed ===`

---

# 🏷️ Minecraft 1.12.2 标签系统

一个轻量级的 Minecraft 模组标签系统，支持**物品**、**流体**和**方块**。
标签系统会自动同步所有矿物词典条目。  
**注意：方块标签无法支持同一方块的不同元数据变体。**

## 📦 功能特性

- ✅ 创建和管理**物品**、**流体**、**方块**标签
- ✅ 自动同步所有矿物词典条目到标签系统
- ✅ 游戏内命令检查手持物品/流体标签
- ✅ 标签命名验证（仅允许字母、`:`、`_`、`/`）
- ✅ 注册时机保护（只能在 FMLLoadCompleteEvent 前注册）
- ✅ 高性能查找（基于 fastutil 集合库）
- ✅ 安全的不可修改集合返回
- ✅ 双向快速查询：标签 ↔ 键值

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
手持物品时使用 `/gettags` 查看其标签：
- 如果手持物品是**流体容器**（如桶），则显示**流体标签**
- 否则显示**物品标签**

#### 代码 API

##### 获取所有标签
`// 获取物品的所有标签`  
`Set<String> tags = TagHelper.tags(itemStack);`

`// 获取流体的所有标签`  
`Set<String> tags = TagHelper.tags(fluidStack);`

`// 获取方块的所有标签`  
`Set<String> tags = TagHelper.tags(block);`

##### 检查特定标签
`// 检查物品是否有某个标签`  
`boolean hasTag = TagHelper.hasTag(itemStack, "minecraft:food");`

`// 检查流体是否有某个标签`  
`boolean hasTag = TagHelper.hasTag(fluidStack, "forge:lava");`

##### 检查任意标签
`// 检查物品是否有任意指定标签`  
`boolean hasAny = TagHelper.hasAnyTags(itemStack, "minecraft:food", "forge:tools");`

`// 使用集合检查`  
`List<String> tagList = Arrays.asList("minecraft:food", "forge:tools");`  
`boolean hasAny = TagHelper.hasAnyTags(itemStack, tagList);`

##### 获取标签对应的所有对象
`// 获取拥有某个标签的所有物品`  
`Set<ItemStack> items = TagHelper.getItemStacks("minecraft:food");`

`// 获取拥有某个标签的所有流体`  
`Set<FluidStack> fluids = TagHelper.getFluidStacks("forge:lava");`

##### 标签存在性检查
`// 检查标签是否存在（任何类型）`  
`boolean exists = TagHelper.tagNameExist("minecraft:food");`

`// 检查特定类型的标签是否存在`  
`boolean exists = TagHelper.tagNameExist("minecraft:food", TagType.ITEM);`

##### 获取所有标签名
`// 获取所有物品标签名`  
`Set<String> allItemTags = TagHelper.getAllTags(TagType.ITEM);`

`// 获取所有流体标签名`  
`Set<String> allFluidTags = TagHelper.getAllTags(TagType.FLUID);`

### 标签命名规则
- 只能包含：**字母**、**数字**、**冒号(`:`)**、**下划线(`_`)**、**斜杠(`/`)**
- 不能为空或 null
- 示例：`"minecraft:food"`、`"forge:ores/iron"`、`"Special_123"`

### 重要限制
- **标签注册必须在 `FMLLoadCompleteEvent` 之前完成**
- 在 `FMLPostInitializationEvent` 后尝试注册标签会抛出 `IllegalStateException`
- **方块标签不支持元数据变体**

## 🔧 集成说明

### 自动 Ore Dictionary 同步
系统在 `FMLPostInitializationEvent` 阶段自动同步所有 Ore Dictionary 条目：
- 普通物品栈直接同步
- 通配符元数据（`OreDictionary.WILDCARD_VALUE`）会尝试同步前 16 个元数据变体
- 同步过程会记录成功和失败的数量

### 性能特点
- 使用 `fastutil` 集合库优化内存和查找性能
- 双向映射存储：`标签 → 键集合` 和 `键 → 标签集合`
- 返回 `Collections.unmodifiableSet()` 确保数据安全

## 🧩 类概览

| 类名           | 用途         | 说明                     |
|--------------|------------|------------------------|
| `TagBuilder` | 创建标签的主要入口  | 提供静态方法创建各类标签构建器        |
| `TagHelper`  | 查询标签的工具类   | 所有标签查询操作的入口点           |
| `TagManager` | 内部标签存储和查找  | 管理四种类型的 Tag 实例         |
| `Tag`        | 通用标签容器     | 使用泛型支持不同类型的键           |
| `TagSync`    | 将矿物词典同步到标签 | 处理 Ore Dictionary 同步逻辑 |
| `TagCommand` | 游戏内标签命令    | 实现 `/gettags` 命令       |
| `TagType`    | 标签类型枚举     | 定义 ITEM, FLUID, BLOCK  |
| `TagMod`     | 模组主类       | 注册事件和命令                |

## 📁 包结构

`com.gardenevery.tag/`  
`├── Tag.java              # 核心标签存储逻辑`  
`├── TagBuilder.java       # 标签构建器`  
`├── TagHelper.java        # 标签查询工具`  
`├── TagManager.java       # 标签管理器`  
`├── TagMod.java           # 模组主类`  
`├── TagSync.java          # Ore Dictionary 同步`  
`├── TagCommand.java       # 游戏内命令`  
`├── TagType.java          # 标签类型枚举`  
`└── key/                  # 键类型包`  
`    ├── Key.java          # 键接口`  
`    ├── ItemKey.java      # 物品键`  
`    ├── FluidKey.java     # 流体键`  
`    ├── BlockKey.java     # 方块键`

## ⚠️ 注意事项

1. **注册时机**：确保在 `FMLLoadCompleteEvent` 前完成所有标签注册
2. **方块元数据**：方块标签不支持元数据变体
3. **线程安全**：标签查询操作是线程安全的，但注册操作应在主线程完成

## 🔍 调试技巧

使用 `/gettags` 命令快速验证标签是否正确应用，或通过日志查看 Ore Dictionary 同步结果：

`[TagSync] === Starting Ore Dictionary Sync ===`  
`[TagSync] Found 155 ore dictionary categories`  
`[TagSync] === Sync completed: 403 successful, 0 failed ===`

---
