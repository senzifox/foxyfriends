<img src="src/main/resources/assets/foxyfriends/icon.png" alt="FoxyFriends icon" width="128">

# FoxyFriends

Make foxes your friends  

[![Available on CurseForge](https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/cozy/available/curseforge_vector.svg)](https://www.curseforge.com/minecraft/mc-mods/foxyfriends)

> The mod is in early development, features and balance may change between versions

## Features
- **Taming** - feed a fox raw chicken, rabbit or fish, a fox that already trusts you is tamed right away, a wild one has a 1 in 10 chance
- **Modes** - right-click your fox with an empty hand to switch between *Follow* (wow), *Sit* (dozes off when calm and sheltered) and *Wander* (roams around its home point)
- **Defense** - defends you from anyone who hurts you, joins your fights and strikes back when attacked
- **Care** - 25 hp and 3 attack dmg, anyone can heal it with meat, fish or berries, and it slowly regenerates when left alone
- **Collar** - use any dye on your fox to recolor its collar
- **Armor** - leather, copper, iron, golden, diamond or netherite, right-click an adult fox to put it on, shears to take it off

<details>
<summary>Armor recipe</summary>

|&nbsp;|&nbsp;|&nbsp;|
|-|-|-|
|`M`|`M`|·|
|·|`M`|`L`|
|`M`|·|`M`|

`M` - leather, copper ingot, iron ingot, gold ingot or diamond  
`L` - leather

Netherite armor is upgraded from diamond at a smithing table  
Leather armor can be dyed and washed in a cauldron, it also keeps a fox warm in powder snow

</details>
<details>
<summary>Data pack tags</summary>

| tag | purpose |
|-|-|
| `#foxyfriends:fox_taming_food` | items that tame foxes |
| `#foxyfriends:fox_healing_food` | items that heal tamed foxes |
| `#foxyfriends:fox_wont_attack` | entities tamed foxes never attack |

</details>

## Requirements

| minecraft | fabric loader | fabric api | java |
|-|-|-|-|
| 26.3      | 0.20.1+       | 0.165.0+   | 25+  |

Mod and [Fabric API](https://modrinth.com/mod/fabric-api) are needed on both server and client sides  
Build with `./gradlew build`, jar in `build/libs/`

## Feedback
For bugs, ideas or any other questions, [open an issue](https://github.com/senzifox/foxyfriends/issues) or [dm me](https://senzi.dev)

## Credits
Thanks to Vortianski for [Foxes & Stuff](https://github.com/Vortianski/Foxes---Stuff), the mod that sparked this  
Copyright (c) 2026 senzifox, [GPL-3.0](LICENSE)
