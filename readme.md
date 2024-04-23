# TotemAnimations

## Download
You can download the plugin [on Modrinth](https://modrinth.com/plugin/totemanimations).

## Usage
`/totemanimation <player> <custom_model_data>` command allows you to display totem animation with provided CustomModelData to specified player. Obviously, that player has to have resource pack with CustomModelData totems enabled.

## API
Plugin can be used as an API

```xml
<repository>
  <id>jitpack.io</id>
  <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
  <groupId>com.github.YouHaveTrouble</groupId>
  <artifactId>TotemAnimations</artifactId>
  <version>VERSION</version>
</dependency>
```

```java
TotemAnimations.playTotemAnimation(Player player, int customModelData);
```