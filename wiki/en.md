# FoxyFriends wiki
English | [Русский](ru.md)

## Contents
- [Installation](#installation)
- [Taming](#taming)
- [Commands](#commands)
- [Defense](#defense)
- [Health and healing](#health-and-healing)
- [Petting](#petting)
- [Mouth item](#mouth-item)
- [Breeding](#breeding)
- [Collar](#collar)
- [Armor](#armor)
- [Advancements](#advancements)
- [Config](#config)
- [Chat commands](#chat-commands)
- [Data pack tags](#data-pack-tags)
- [Compatibility](#compatibility)

## Installation
| minecraft | fabric loader | fabric api | java |
|-|-|-|-|
| 26.3 | 0.20.1+ | 0.165.0+ | 25+ |

The mod and Fabric API are needed on both server and client, if the mod versions don't match, the server won't let you in and tells which version it needs  
Mod Menu and YACL are optional, client only, needed for the config screen  
The built-in resource pack "FoxyFriends: He's a Fox" renames the vanilla Russian «лисица» to «лис», enabled by default, can be turned off on the resource packs screen  
Building from source `./gradlew build` on JDK 25, the jar appears in `build/libs/`

## Taming
Food - raw chicken, rabbit, cod, salmon, tropical fish (`#foxyfriends:fox_taming_food`), 1 item per feeding  
Chance - 100% if the fox trusts the player otherwise 10% (`wild_tame_chance`), hearts on success, smoke on failure  
A fox trusts the player who bred it, as in vanilla

| stats\fox | wild | tamed |
|-|-|-|
| health | 10 | 25 (`tamed_max_health`) |
| attack damage | 2 | 3 (`tamed_attack_damage`) |

On taming the fox heals to full, trusts its owner and gets a [collar](#collar)  
Death of a tamed fox - message to the owner, like with wolves, depends on the `showDeathMessages` game rule

## Commands
Command - right-click own fox with an empty hand without sneaking, order *Follow* --> *Sit* --> *Wander*, the new command shows above the hotbar, after taming - *Follow*

| action\command | Follow | Sit | Wander |
|-|-|-|-|
| movement | after the owner | stays put | within 16 blocks of "home" |
| hunting | no | no | yes (`wander_hunting`) |
| sleep | no | dozes | like vanilla |
| defends owner | yes | no | yes |
| strikes back | yes | yes | yes |

### Follow
- starts following beyond 10 blocks, stops at 2 blocks
- teleports to the owner when 12 or more blocks behind
- doesn't follow while leashed, riding, fighting or when the owner is in another dimension
- doesn't sleep or seek shelter by day

### Sit
- dozes by day without a thunderstorm, under a roof, with no threats within 12 blocks and not in powder snow, wakes up when any condition breaks
- gets up in water, on fire, when freezing and under attack, steps away from danger or strikes back then sits down at the new spot

### Wander
- home - the spot where the fox started wandering, moves when the leash is removed and when the fox changes dimension
- behaves like a vanilla fox: sleeps by day, seeks shelter
- returns when it strays beyond the radius

## Defense
In *Follow* and *Wander*:
- attacks whoever hurt the owner
- attacks the owner's target
- chases a target up to 16 blocks

Always strikes back at whoever hurt it

Never attacks:
- its owner
- tamed foxes, tamed animals (wolves, cats, parrots) and tamed horses
- mobs in `#foxyfriends:fox_wont_attack`

Runs from attacks by tamed animals and mobs in `#foxyfriends:fox_wont_attack`, in *Follow* drops a fight when more than 24 blocks from the owner  
Doesn't run from players, panics only from environmental damage: fire, lava, freezing and the like

## Health and healing
### Healing
Food - raw and cooked meat and fish, sweet and glow berries (`#foxyfriends:fox_healing_food`), any player can heal  
Restores as much health as the food's hunger value: cooked beef - 8, sweet berries - 2  
Food isn't used up at full health

### Regeneration
1 health every 30 seconds if the fox took no damage for 10 seconds (`regen_amount`, `regen_interval_ticks`, `regen_calm_ticks`)

### Indicator
Health of own fox shows above the hotbar when looking at it and when it gets hurt within 32 blocks, visible to the owner only, turned off in the client config (`health_actionbar`)

## Petting
Sneak + right-click any fox with an empty hand, effect - hearts, a sound, the fox "squashes"  
Petting by the owner calms the fox for 5 seconds: clears its target, interrupts crouching and pouncing, disables owner defense, striking back and running from attackers

## Mouth item
Tamed foxes pick up items like vanilla ones, sneak + right-click own fox with an empty hand takes the item from its mouth, with an empty mouth - petting  
Never picks up fox armor

## Breeding
With berries as in vanilla

| parents | baby |
|-|-|
| both tamed | tamed to the player who fed the first parent |
| one tamed | tamed to the same player with a 50% chance |
| both wild | wild |

Tamed foxes can only be bred at full health, otherwise healing  
A tamed baby follows adult foxes only in *Wander*

## Collar
All tamed foxes have one, babies included, default color - red, the owner recolors it with any dye

## Armor
| armor | protection |
|-|-|
| leather | 3 |
| copper | 4 |
| iron | 5 |
| golden | 7 |
| diamond | 11 |
| netherite | 19 |

- put on by the owner with a click on an adult tamed fox or by a dispenser
- taken off by the owner with shears
- doesn't wear out, always drops on death
- not available for babies
- leather is dyed at a crafting table, washed in a cauldron, protects from freezing in powder snow
- netherite doesn't burn in lava
- piglins love golden

### Recipe
| | | |
|-|-|-|
| `M` | `M` | · |
| · | `M` | `L` |
| `M` | · | `M` |

`M` - leather, copper ingot, iron ingot, gold ingot or diamond, `L` - leather

Netherite - diamond upgrade at a smithing table: upgrade template + diamond fox armor + netherite ingot  
Recipes unlock in the recipe book on getting the material

## Advancements
In the Husbandry tab

| advancement | condition |
|-|-|
| Who's a Good Fox? | pet any fox |
| So Fluffin' Cute | pet a baby fox |
| Firefox and Iceweasel | pet a red fox and a snow fox, in any order |
| It's Called a Hustle, Sweetheart | a fox picks up an item thrown by the player |
| You May Now Kiss the Fox | breed foxes |
| An Act Too Often Neglected | tame a fox, grants vanilla "Best Friends Forever" along with it |
| Love at First Bite | tame a wild fox that didn't trust the player on the first feeding |

## Config
### Server
`config/foxyfriends.json`, applied to all loaded foxes with `/foxyfriends reload`

| key | default | range | description |
|-|-|-|-|
| `wander_hunting` | `true` | bool | tamed foxes hunt in *Wander*, wild ones always hunt |
| `wild_tame_chance` | `0.1` | 0–1 | chance to tame a wild fox per feeding |
| `tamed_max_health` | `25.0` | 1–1024 | health of a tamed fox |
| `tamed_attack_damage` | `3.0` | 0–2048 | attack damage of a tamed fox |
| `regen_amount` | `1.0` | 0–1024 | health per regeneration |
| `regen_interval_ticks` | `600` | 1–72000 | ticks between regenerations |
| `regen_calm_ticks` | `200` | 0–72000 | ticks without damage before regeneration starts |

### Client
`config/foxyfriends-client.json`

| key | default | description |
|-|-|-|
| `health_actionbar` | `true` | own fox's health above the hotbar |

### Config screen
Mod Menu --> FoxyFriends, needs Mod Menu and YACL, in singleplayer changes apply right away, on a dedicated server only the local file changes

## Chat commands
| command | access | description |
|-|-|-|
| `/foxyfriends reload` | operators level 2+ | reloads `config/foxyfriends.json` |

The fox's owner is available to vanilla commands via `on owner`, for example `/execute as @e[type=fox] on owner run ...`

## Data pack tags
| tag | purpose | default |
|-|-|-|
| `#foxyfriends:fox_taming_food` | taming food | raw chicken, rabbit, cod, salmon, tropical fish |
| `#foxyfriends:fox_healing_food` | healing food | raw and cooked meat and fish, sweet and glow berries |
| `#foxyfriends:fox_wont_attack` | mobs tamed foxes never attack | creeper, ghast, happy ghast, armor stand, iron golem, warden, ravager, wither, ender dragon, elder guardian |

The mod adds its armor to vanilla tags

| tag | armor |
|-|-|
| `minecraft:piglin_loved` | golden |
| `minecraft:cauldron_can_remove_dye` | leather |
| `minecraft:freeze_immune_wearables` | leather |

## Compatibility
Jade and other mods showing a pet's owner - tamed foxes implement vanilla `OwnableEntity`

PatPat - takes over sneak + right-click, petting and taking the mouth item don't work, fix - add foxes to PatPat's ignore list or give it another key

Mod Menu, YACL - config screen
