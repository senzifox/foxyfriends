# FoxyFriends wiki
English | [Русский](ru.md)

## Contents
- [Installation](#installation)
- [Taming](#taming)
- [Commands](#commands)
- [Sleep](#sleep)
- [Defense](#defense)
- [Health and healing](#health-and-healing)
- [Petting](#petting)
- [Mouth item](#mouth-item)
- [Breeding](#breeding)
- [Species](#species)
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
Only the owner can name a tamed fox with a name tag, a wild one - any player

## Commands
Command - right-click own fox with an empty hand without sneaking, order *Follow* --> *Sit* --> *Wander*, the new command shows above the hotbar, after taming - *Follow*

| action\command | Follow | Sit | Wander |
|-|-|-|-|
| movement | after the owner | stays put | within 16 blocks of "home" |
| hunting | no | no | yes (`wander_hunting`) |
| sleep | by the owner at night | dozes | like vanilla, by the owner at night, on a bed by day |
| defends owner | yes | no | yes |
| strikes back | yes | yes | yes |

### Follow
- starts following beyond 10 blocks, stops at 2 blocks
- teleports to the owner when 12 or more blocks behind
- doesn't follow while leashed, riding, fighting or when the owner is in another dimension
- doesn't sleep or seek shelter by day, sleeps at the owner's feet at night

### Sit
- dozes by day without a thunderstorm, under a roof, with no threats within 12 blocks and not in powder snow, wakes up when any condition breaks
- gets up in water, on fire, when freezing and under attack, steps away from danger or strikes back then sits down at the new spot

### Wander
- home - the spot where the fox started wandering, moves when the leash is removed and when the fox changes dimension
- behaves like a vanilla fox: sleeps by day, seeks shelter
- naps on a nearby bed by day, sleeps at the owner's feet at night
- returns when it strays beyond the radius

## Sleep
### By the owner at night
Owner lies down in a bed --> their fox on *Follow* or *Wander* within 10 blocks walks to the foot of the bed and falls asleep
- one fox per bed
- doesn't go while leashed or riding
- wakes up 1–3 seconds after the owner, right away when in danger
- sits at the foot without falling asleep during a thunderstorm

### On a bed by day
A fox on *Wander* naps on a free bed within 6 blocks, same conditions as dozing on *Sit*: daytime without a thunderstorm, a roof overhead, no threats within 12 blocks  
A fox asleep on the floor gets up and goes to the bed

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

## Species
| species | where it spawns |
|-|-|
| red | as in vanilla |
| snow | as in vanilla |
| dune | desert (weight 1) and savannas (weight 2), groups of 1–2 |

- the species is picked by biome when a fox appears: natural spawn, spawn egg, `/summon` without NBT
- dune fox biomes - `#foxyfriends:spawns_corsac_foxes`, in the desert foxes spawn on sand, which the mod adds to `minecraft:foxes_spawnable_on`
- a kit gets the species of a random parent
- summon a dune fox: `/summon fox ~ ~ ~ {"fabric:attachments":{"foxyfriends:species":"corsac"}}`
- turned off with the `new_species` key, dune foxes don't spawn and aren't picked by biome, existing dune foxes and their kits stay dune foxes
- textures - `foxyfriends:textures/entity/fox/corsac/`, can be changed with a resource pack

## Collar
All tamed foxes have one, babies included, default color - red, the owner recolors it with any dye  
A glow ink sac makes the collar glow in the dark, an ink sac removes the glow

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
### Husbandry
| advancement | condition |
|-|-|
| Who's a Good Fox? | pet any fox |
| So Fluffin' Cute | pet a baby fox |
| Firefox and Iceweasel | pet a red fox and a snow fox, in any order |
| It's Called a Hustle, Sweetheart | a fox picks up an item thrown by the player |
| You May Now Kiss the Fox | breed foxes |
| An Act Too Often Neglected | tame a fox, grants vanilla "Best Friends Forever" along with it |
| [Love at First Bite](#spoiler) | tame a wild fox that didn't trust the player on the first feeding |

### FoxyFriends
Own tab from the built-in data pack "FoxyFriends: Advancement Tab", enabled by default, turned off with `/datapack disable "foxyfriends:advancement_tab"`  
The tab appears after the first petting or taming of a fox

| advancement | condition |
|-|-|
| Sneak 100 | pet a sleeping wild fox |
| Employee of the Month | pet foxes 999 times, counted in the "Foxes Patted" statistic |
| What Does the Fox Say? | look at a fox through a spyglass |
| Alolan Form | tame a red fox and a snow fox, in any order |
| Five Nights at Foxy's | 5 own foxes within 16 blocks at the moment of taming or birth of a tamed kit |
| [Never Gonna Give You Up](#spoiler) | tame a wild fox that didn't trust the player on the player's 22nd feeding or later |
| I Am Your Father | a tamed kit is born to the player |
| They Grow Up So Fast | own kit grows up while the owner is online |
| Dress to Impress | dye the collar of own fox |
| Shine Bright Like a Fox | make the collar of own fox glow with a glow ink sac |
| Sixteen Shades of Fox | all 16 colors on collars of own foxes, red counts on taming |
| Say My Name | name own fox with a name tag |
| Drip Check | put fox armor on own fox by clicking |
| Berserker Armor | put netherite fox armor on by clicking |
| Your Wish Is My Command | give own fox a command |
| Home Alone | come within 16 blocks of own fox on *Sit* or *Wander* after being away for a game day or longer |
| Fantastic Mr. Fox | own fox on *Wander* kills a chicken, a rabbit or a fish, not reachable with `wander_hunting` off |
| Whatever Floats Your Fox | sit in one boat with own fox, in any order |
| To Hell and Back | own fox enters the Nether while the owner is online |
| One Small Step for a Fox | own fox enters the End while the owner is online |
| [Nap Time](#spoiler) | own fox on *Sit* dozes off with the owner within 8 blocks |
| I Need Your Clothes, Your Boots… | take an item from the mouth of own fox |
| [The Fox's Share](#spoiler) | take a treasure from the mouth of own fox: emerald, diamond, gold, netherite, amethyst shard, rabbit's foot or their blocks, also any rare or epic item, enchanted ones included |
| Need a Medic! | feed own fox with 20% health or less |
| Chill Guy | pet own fox while it fights or was just hurt |
| Outfoxed | own fox kills the mob that last hurt the owner or that the owner last hurt |
| [Armed and Fluffy](#spoiler) | like Outfoxed, with a weapon in the fox's mouth: sword, spear, axe or mace (`#minecraft:enchantable/weapon`) |
| [Saved by the Tail](#spoiler) | own fox kills the mob that last hurt the owner while the owner has 3 hearts or less |
| [Nine Lives?](#spoiler) | a totem of undying in the mouth of own fox saves its life while the owner is online |
| Toss a Coin to Your Fox | own foxes kill 50 hostile mobs, counted in the Hostiles Defeated by Foxes statistic |
| Helm's Deep | win a raid with a living own fox not on *Sit* within 32 blocks |

### Statistics
Rows in the vanilla statistics menu, counted since the mod was installed

| statistic | when it grows |
|-|-|
| Foxes Patted | a counted petting of any fox |
| Foxes Tamed | taming by feeding |
| Taming Attempts | any taming feeding |
| Kits Born | a tamed kit is born to the player |
| Kits Raised | own kit grows up while the owner is online |
| Hostiles Defeated by Foxes | own fox kills a hostile mob while the owner is online |

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
| `new_species` | `true` | bool | dune foxes in deserts and savannas, spawning changes after rejoining the world |

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
| `#foxyfriends:spawns_corsac_foxes` | biomes where an appearing fox becomes a dune fox | desert, `#minecraft:is_savanna` |

The mod adds to vanilla tags

| tag | what is added |
|-|-|
| `minecraft:piglin_loved` | golden armor |
| `minecraft:cauldron_can_remove_dye` | leather armor |
| `minecraft:freeze_immune_wearables` | leather armor |
| `minecraft:foxes_spawnable_on` | sand |

## Compatibility
Jade and other mods showing a pet's owner - tamed foxes implement vanilla `OwnableEntity`

PatPat - takes over sneak + right-click, petting and taking the mouth item don't work, fix - add foxes to PatPat's ignore list or give it another key

Mod Menu, YACL - config screen
