# Fabric Example Mod
[![build](https://github.com/JulianWww/Fabric-Wiki/actions/workflows/build.yml/badge.svg)](https://github.com/JulianWww/Fabric-Wiki/actions/workflows/build.yml)
[![Crowdin](https://badges.crowdin.net/fabric-wiki/localized.svg)](https://crowdin.com/project/fabric-wiki)

## Setup
add this mod to your project by adding the following into your ``build.gradle``
```gradle
repositories {
	maven { url 'https://wandhoven.ddns.net/maven/' }
}
dependencies {
	modImplementation "net.denanu.wiki:wiki-1.19.4:0.0.1.1679655807506"
}
```
## Usage
### Adding your root page
The Wiki has a root page, that is opened automatically upon opening the wiki from modmenu. You register your root page in your ``fabric.main.json`` by adding the following
```json
"custom": {
  "wiki": {
    "root": "root_page_file_name"
  }
}
```
Then in your mods assets folder ``assets/modid/`` add a new folder named ``wiki``, henceforth refered to as the wiki folder.
In the wiki folder create a new file named ``root_page_file_name.json``, this json configure the just like any other wiki json file as discribed below. The root file als has the following additional information:
| Json Tag | Datatype | Description |
| --- | --- | --- |
| title | Translation key | The title of the wiki, will be displayed on top of every wiki page |


### Adding aditional pages
To add further pages, add a new json file in the wiki folder. This file has the following tags:

| Json Tag | Datatype | Description |
| --- | --- | --- |
| content | list | The contents of the page, see below |
| pages | list of Identifiers | A list of Identifiers linking to other pages |


 #### Set contents
 In the pages json file under ``content``, there must be a list of maps, that contain the contents. There is no rule on the order of these content tyes. This list must excist an empty one will lead to an empty list. Every element must contain the ``text`` tag, that will define the translation key uesed to determin the contents of this page element.
 
 ##### Header
 There are 6 types of headers (``h1``, ``h2``, ``h3``, ``h4``, ``h5``, and ``h6``). A header is added as follows:
 ```json
 {
  "style": "h1",
  "text": "myMod.text"
}
```

##### Text
Text is trivial and must only contain the ``text`` tag. Example:
```json
{
  "text": "loremipsum"
}
```

##### Recipe
You cann add a recipe to your wiki page by setting the ``style`` tag to ``recipe``, and adding the identifier of the recipe under ``recipe``, ``text`` is also required. Example:
```json
{
  "style": "recipe",
  "recipe": "minecraft:andesite_slab_from_andesite_stonecutting",
  "text": "title"
},
```
Note: The recipes will not show up if the client is not in game. This is due to them beeing loaded by the server. They will just silently be hidden.


