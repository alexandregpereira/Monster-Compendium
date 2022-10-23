# Adding New Content

Follow the instructions below to create the required JSON files. After that, you are going to need to host these JSON files and paste these JSON urls on the app. 

## Adding Custom Monster Images

Create a JSON file with the following content:

```json
[
    {
        "monster_index": "aboleth",
        "background_color": {
            "light": "#d3dedc",
            "dark": "#d3dedc"
        },
        "is_horizontal_image": true,
        "image_url": "https://my/image/url.png"
    }
]
```

- **monster_index**: Must be a valid index from an existent monster.

- **background_color**: The colors used as image background. If the image has no transparency, you are not going to see it.

- **is_horizontal_image**: Indicates if the image with is higher than the image height.

- **image_url**: The image url to be showed on the app.

## Adding New Monsters

Create a JSON file with the following content:

```json
[
    {
        "source": {
            "name": "My New Source",
            "acronym": "MNS"
        },
        "totalMonsters": 1
    }
]
```

On the root directory that you host this JSON, you are going to need to create a folder `sources/{source.acronym}` contenting a JSON file name `monsters.json` with the new monsters content. You can create the `monsters.json` file following the [Monster JSON schema](https://github.com/alexandregpereira/monster-compendium-content#monstersjson).
