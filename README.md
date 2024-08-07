# OdalitaMenus

Advanced yet simple to use inventory api for Paper plugins
___

# Getting started ([Wiki](https://github.com/Odalita-Developments/OdalitaMenus/wiki/Getting-started))
**(NOTE)** If you're running on 1.20.6 or later, please make sure you use Paper as your server software. Spigot is no longer supported for newer versions.

In order to get started you would need to add the libary to your project using **maven** or **gradle**

Maven dependency:
```xml
<repository>
    <id>repsy</id>
    <url>https://repo.repsy.io/mvn/kiwisap/odalitamenus</url>
</repository>

<dependency>
    <groupId>nl.odalitadevelopments.odalitamenus</groupId>
    <artifactId>core</artifactId>
    <version>0.5.8</version>
</dependency>
```

Gradle dependency:
```gradle
allprojects {
    repositories {
        ...
        maven { url 'https://repo.repsy.io/mvn/kiwisap/odalitamenus' }
    }
}

dependencies {
    implementation 'nl.odalitadevelopments.odalitamenus:core:0.5.8'
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