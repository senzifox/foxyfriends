<img src="src/main/resources/assets/foxyfriends/icon.png" alt="FoxyFriends icon" width="128">

# FoxyFriends

Make foxes your friends  

> The mod is in early development, features and balance may change between versions

## Features
### Taming

Feed a fox raw prey: chicken, rabbit and [more](#customization), a fox that already trusts you is tamed on the first feeding, a wild one has a 1 in 10 chance  
A tamed fox doesn't run away from other players

### Modes

Right-click your tamed fox with an empty hand to switch its mode:
- *Follow* - follows you (wow)
- *Sit* - stays in place and dozes off when it's calm and sheltered
- *Wander* - roams around its home point, like a wild fox

### Defense

A tamed fox defends you from anyone who hurts you, joins the fights you start and strikes back when attacked

### Care

Tamed foxes are stronger: 25 hp and 3 attack dmg, anyone can heal a fox with [raw or cooked prey](#customization), and a fox slowly regenerates when it hasn't been hurt for a while

### Collar

Every tamed fox wears a collar, use any dye on your fox to change its color

### Armor

Foxes can wear leather, copper, iron, golden, diamond or netherite armor  
Right-click your adult fox with armor to put it on and use shears to take it off

Craft armor from a material and a piece of leather, upgrade diamond armor to netherite at a smithing table

|&nbsp;|&nbsp;|&nbsp;|
|-|-|-|
|`M`|`M`|·|
|·|`M`|`L`|
|`M`|·|`M`|

`M` - leather, copper ingot, iron ingot, gold ingot or diamond  
`L` - leather

Leather armor can be dyed and washed in a cauldron just like any other leather armor, it also keeps a fox warm in powder snow

## Customization

Data packs can change fox behavior through tags

| tag | purpose |
|-|-|
| `#foxyfriends:fox_taming_food` | items that tame foxes |
| `#foxyfriends:fox_healing_food` | items that heal tamed foxes |
| `#foxyfriends:fox_wont_attack` | entities tamed foxes never attack |

## Now supported on

| minecraft | fabric loader | fabric api | java |
|-|-|-|-|
| 26.3      | 0.20.1+       | 0.165.0+   | 25+  |

Requires [Fabric API](https://modrinth.com/mod/fabric-api) on both server and client sides

## Building

```sh
./gradlew build
```

This also runs the game tests. The jar ends up in build/libs/.

## Credits
Thanks to Vortianski for [Foxes & Stuff](https://github.com/Vortianski/Foxes---Stuff), the mod that sparked this
