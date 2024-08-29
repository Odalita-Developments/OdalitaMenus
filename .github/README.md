# OdalitaMenus

Advanced yet simple to use inventory api for Paper plugins
___

[![Maven Central Version](https://img.shields.io/maven-central/v/io.github.odalita-developments.odalitamenus/core?style=for-the-badge&color=0459c8)](https://central.sonatype.com/artifact/io.github.odalita-developments.odalitamenus/core)
[![GitHub Release](https://img.shields.io/github/v/release/Odalita-Developments/OdalitaMenus?display_name=release&style=for-the-badge&label=latest%20release&color=4493f8)](https://github.com/Odalita-Developments/OdalitaMenus/releases/latest)
[![License](https://img.shields.io/github/license/Odalita-Developments/OdalitaMenus?style=for-the-badge&color=b2204c)](../LICENSE)
[![Spigot Downloads](https://img.shields.io/spiget/downloads/110376?label=spigot%20downloads&style=for-the-badge&color=ee8917)](https://www.spigotmc.org/resources/110376/)
___

# Getting started ([Wiki](https://github.com/Odalita-Developments/OdalitaMenus/wiki/Getting-started))
**(NOTE)** If you're running on 1.20.6 or later, please make sure you use a Paper based server software. We no longer support Spigot for newer versions.

In order to get started you would need to add the libary to your project using **maven** or **gradle**

Maven dependency:
```xml
<dependency>
    <groupId>io.github.odalita-developments.odalitamenus</groupId>
    <artifactId>core</artifactId>
    <version>0.5.11</version>
</dependency>
```

Gradle dependency:
```gradle
repositories {
    mavenCentral()
}

dependencies {
    implementation 'io.github.odalita-developments.odalitamenus:core:0.5.11'
}
```

# Features

- Simple menu setup (with annotation)
- Menu types (inventory types)
- Inbuilt items and option to create your own items 
  - DisplayItem 
  - ClickableItem
  - UpdatableItem
  - BackItem
  - CloseItem
  - OpenMenuItem
  - PageItem
  - ScrollItem 
- Refreshable items 
  - Decide yourself when to refresh an item 
- Scheduler 
- Menu cache 
  - Option to use global caching using a key which syncs the cache across the menus with the same key 
- Change title without reopening the menu 
- Option to cancel menu from closing **NEW**
- Player inventory interaction 
  - Click event 
  - Item meta changer (client side)
  - Placeable items (option to place items from player inventory into your menu)
    - Shift click support **NEW** 
- Menu providers 
  - This is the way how a menu should initialize it's items, this has to option to create custom providers for your own use 
- Iterators 
  - Normal iterators 
  - Iterators based on patterns 
  - Object iterators 
    - This can be used to easily sort and filter items due to it using objects instead of items to fill the iterator 
    - This can also be used in pagination 
- Patterns 
- Pagination 
  - Async page switching 
- Scollable 
- Frames 
  - Create a smaller piece of a menu with a frame that you can use across multiple menus 
- Option to change inventory properties 
- Option to listen to bukkit inventory and player events **NEW**
  - Could be used to prevent players from picking up items from the ground while the menu is open for example 
- Multi version support (1.16.5 - 1.21)
- Async support 
  - You can change items in the inventory anytime you want, on any thread you want 
- Option to register your own providers
  - ColorProvider
  - CooldownProvider
  - DefaultItemProvider
  - MenuItemDataProvider
  - PacketListenerProvider