<img src="src/main/resources/assets/foxyfriends/icon.png" alt="иконка FoxyFriends" width="128">

# FoxyFriends

Лисы станут твоими друзьями

[English](README.md) | Русский

<a href="https://modrinth.com/mod/foxyfriends"><img src=".github/badges/modrinth-available-ru.svg" alt="Доступно на Modrinth"></a> <a href="https://www.curseforge.com/minecraft/mc-mods/foxyfriends"><img src=".github/badges/curseforge-available-ru.svg" alt="Доступно на CurseForge"></a><br>
<a href="https://modrinth.com/mod/foxyfriends"><img src="https://img.shields.io/modrinth/dt/foxyfriends?style=for-the-badge&label=%D1%81%D0%BA%D0%B0%D1%87%D0%B8%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9&color=00d845" alt="Скачиваний на Modrinth" width="167"></a> <a href="https://www.curseforge.com/minecraft/mc-mods/foxyfriends"><img src="https://img.shields.io/curseforge/dt/1775645?style=for-the-badge&label=%D1%81%D0%BA%D0%B0%D1%87%D0%B8%D0%B2%D0%B0%D0%BD%D0%B8%D0%B9&color=f16436" alt="Скачиваний на CurseForge" width="171"></a>

> Мод на ранней стадии разработки, возможности и баланс могут меняться от версии к версии

## Возможности
- **Приручение** - накорми лиса сырой курицей, крольчатиной или рыбой, лис который уже тебе доверяет приручается сразу, дикий с шансом 10%
- **Команды** - кликни по своему лису пустой рукой, чтобы дать команду: *Рядом* (вау), *Сидеть* (дремлет если спокойно и есть укрытие) или *Гуляй* (гуляет вокруг точки дома, поведение почти как у ванильного лиса)
- **Защита** - защищает тебя от всех кто тебя ранил, вступает в твои драки и даёт сдачи
- **Уход** - 25 хп и 3 дамага, любой может вылечить её мясом, рыбой или ягодами, также понемногу восстанавливается сама
- **Гладить** - шифт + клик пустой рукой по любому лису, свой лис успокаивается и пару секунд не лезет в драки
- **Отдай!** - шифт + клик пустой рукой по своему лису чтобы забрать то что он держит во рту
- **Здоровье** - посмотри на своего лиса и его здоровье появится над хотбаром
- **Разведение** - детёныш двух ручных лис рождается ручным у того, кто покормил первого родителя, с одним ручным родителем - 50 на 50
- **Ошейник** - используй любой краситель на своём лисе чтобы перекрасить ошейник
- **Броня** - кожаная, медная, железная, золотая, алмазная или незеритовая, кликни бронёй по взрослому лису, чтобы надеть, снимается ножницами

Подробнее в [вики](https://senzi.dev/foxyfriends/wiki)

<details>
<summary>Рецепт брони</summary>

|&nbsp;|&nbsp;|&nbsp;|
|-|-|-|
|`M`|`M`|·|
|·|`M`|`L`|
|`M`|·|`M`|

`M` - кожа, медный слиток, железный слиток, золотой слиток или алмаз  
`L` - кожа

Незеритовая броня улучшается из алмазной на кузнечном столе  
Кожаную броню можно красить и отмывать в котле, а ещё она греет лиса в рыхлом снегу

</details>
<details>
<summary>Теги для датапаков</summary>

| тег | назначение |
|-|-|
| `#foxyfriends:fox_taming_food` | чем приручать лис |
| `#foxyfriends:fox_healing_food` | чем лечить ручных лис |
| `#foxyfriends:fox_wont_attack` | на кого ручные лисы никогда не нападают |

</details>
<details>
<summary>Конфиг</summary>

`config/foxyfriends.json` на сервере - охота на прогулке, шанс приручения дикого, здоровье, урон и регенерация ручного лиса, `/foxyfriends reload`  
`config/foxyfriends-client.json` на клиенте - здоровье лиса над хотбаром  
С [Mod Menu](https://modrinth.com/mod/modmenu) и [YACL](https://modrinth.com/mod/yacl) оба редактируются прямо в игре

</details>

## Требования

| minecraft | fabric loader | fabric api | java |
|-|-|-|-|
| 26.3      | 0.20.1+       | 0.165.0+   | 25+  |

Мод и [Fabric API](https://modrinth.com/mod/fabric-api) нужны и на сервере, и на клиенте  
[Mod Menu](https://modrinth.com/mod/modmenu) и [YACL](https://modrinth.com/mod/yacl) по желанию, только на клиенте, для экрана настроек  
Сборка `./gradlew build`, jar появится в `build/libs/`

## Обратная связь
С багами, идеями или любыми другими вопросами пиши в [issues](https://github.com/senzifox/foxyfriends/issues) или [мне лично](https://senzi.dev)

## Благодарности
Спасибо Vortianski за [Foxes & Stuff](https://github.com/Vortianski/Foxes---Stuff), мод который вдохновил на создание оного  
Copyright (c) 2026 senzifox, [GPL-3.0](LICENSE)
