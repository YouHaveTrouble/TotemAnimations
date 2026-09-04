# TotemAnimations

## Download
You can download the plugin [on Modrinth](https://modrinth.com/plugin/totemanimations).

## Usage
`/totemanimation <player> <model_key>` command allows you to display totem animation with
provided model to specified player.

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
TotemAnimations.playTotemAnimation(Player player, Key model);
```