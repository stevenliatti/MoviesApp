---
title: IoT Project
subtitle: A Smart Building Software based on three home automation communication protocols (KNX, ZWAVE, BLE and Kafka)
author:
- Jeremy Favre
- Steven Liatti
papersize : a4
fontsize : 10pt
geometry : margin = 2.5cm
---

# Introduction
## Buts
De nos jours, les medias sont à la portée de tous et accessible très facilement nottament grâce aux platefromes tel que netflix, streaming et bien d'autre encore. Il est donc difficle d'effectuer un choix parmis ces centaine de milliers de films mis à dispositions. C'est donc la qu'entre en jeu notre mini-projet que nous avons réalisé dans le cadre du cours de programmation Android au cours du premier semestre de l'année 2019. L'objectif est de développer une application dediée à la plateforme Android qui propose à ses utilisateurs un service permettant de consulter les différents films à la une, obtenir les films similaires à ses derniers, masis égalemnt les différents acteurs conercernés et la possibilité de rechercher un film en particulier.
Un aspect social est également présent au sein de cette application, il permetra de pouvoir renseigner son appréciation personnelle à prpos d'un film, mais également de pouvoir suivre les autres utilisateurs afin de connaitre leurs appréciation sur les différents films qu'ils ont visionnés.
La forme "nous" est utilisée tout au long de ce rapport étant donné que ce projet est réalisé par binôme. De plus les différentes tâches ont été répartie comme nous le verrons dans la rubrique dédiée.
## Motivations
Le périmètre de ce projet semblait parfaitement adapté pour exploiter la majorité des composants et méthodes vues durant le cours de développement Android.
De plus, nous avons montré un intéret particulier pour ce sujet étant donnée que nous sommes tout deux interessé par les films c'est dailleurs quelque chose que nous apprécions grandement durant notre temps libre, c'est pourquoi la réalisation d'une telle applicaiton était parfaitement compatible avec nos centres d'intérets respectifs. 
Enfin, le fait de pouvoir rendre service aux utilisateurs confrontés aux nombreux films existant nous interessait également.


# Conception et architecture
La première phase de conception consiste à dessiner les maquettes des vues de notre application afin de se faire une idée de l'aspect visuel de chaque vue et de l'architecture définissant les relations entre les vues.Tout cela dans le but de simplifier les choses une fois arrivé à la phase de développement.
Durant cette phase nous nous sommes également interessé aux APIs existantes proposant des services relatifs aux films, nous avons étudé les différentes technologies permettant de mettre en place une API rest et finalement nous avons analysé les compostants et méthodologies étudiés en cours qui seraint interessant d'intégrer dans notre application.
## Méthodologie de travail
Sur la base de cette analyse, nous avons séparé le travail en plusieurs tâches que nous avons assigné à chaque membre du groupe de manière équitable afin d'effecter le travail en paralle.
Pour cela nous avons utilisé l'utilitaire de gitlab qui permet de représenter les issues à l'aide d'un board similaire a une métohde agile.
## Diagramme de classes
===========> A FAIRE
## Vue des films tendance
Lorsqu'un utilisateur ouvre l'application il tombe directement sur la vue principale (home) qui est une liste de tous les films à la une, classés en fonction de leurs popularité. Cette vue permet à l'utilisateur d'obtenir les informations de base sur un film (image, titre, courte description) elle permet également de cliquer sur un film afin d'en connaitre d'avantage.

-------------------- SCREENSHOT

## Vue détails d'un film
Comme vue dans la rubrique précédente (Vue des films tendance), si l'utilisateur souhaite en savoir d'avantage sur un film, il peut accéder à cette vue présentant les détails d'un film. Elle comporte les informations détaillée du film tel que le titre, la déscription complète, la liste des acteurs, la liste des réalisateurs, les différentes bande-annonce, les genres associés et une grille qui contient tous les films similaire au film concerné. La possibilité de cliquer sur un film de la grille afin de naviguer vers ses détails est également integrée.
Si l'utilisateur souhaite visionner une bande annonce, il suffit de cliquer dessus afin que le player youtube soit automatiquement lancé avec la vidéo en question. 
### Appréciation des films
Si l'utilisateur est conncté avec son profil personnel, la possibilité de "liker" ou disliker" le film est proposée afin de renseigner son appréciation et pouvoir par la suite les retrouver dans son profil personnel ou partgaer avec ses amis.

-------------------- SCREENSHOT
## Recherche
Une interface permettant la recherche d'un film est mise à disposition, elle permet de rechercher un film, un acteur ou un autre utilisateur de l'application en fonction du type de recherche qui est à spécifier avant de lancer la recherche.
Les résulatats de la rechercher seront présentés sous forme de liste, présentant les différentes actions selon le type de recherche qui aura été effectué.
Si l'utilisateur cherche un film, il obtiendra la liste des films qui correspond aux critères entrés dans le champs texte de recherche. S'il cherche un acteur il aura donc une liste d'acteur avec la possibilité de cliquer dessus (comme pour les films) afin d'afficher plus de détails à son sujet. Enfin, s'il cherche un autre utilisateur il aura alors la possibilité de le suivre en l'ajoutant à son réseau d'amis.
-------------------- SCREENSHOT
-------------------- SCREENSHOT
-------------------- SCREENSHOT

## Peoples
Au même titre que la liste des films tendance, nous avons une interface proposant la liste des acteurs actuellement popluaire avec la possibilité de cliquer chacun d'entre eux afin d'obtenir leurs informations.

-------------------- SCREENSHOT
## Details d'un pepol
Comme pour les détails d'un film, nous avons également une vue présentant les détails d'un acteur, elle permet de consulter les informations tel que sa biographie personnelle et la liste des films dans lesquels lequel il à joué.
-------------------- SCREENSHOT
## Mes films
Si l'utilisateur est authentifié dans l'application, il aura alors la possiblité de consulter ces films personnels qui comporte 2 sous fenêtres naviguable à l'aide de sous onglets. Le premier permet de consulter sous forme de liste, tous les films appréciés (likes) et le second tous les films que l'utilisateur courrant n'a pas apprécié (dislikes).
-------------------- SCREENSHOT
## Réseau d'amis
La vue réseau d'amis est disponible uniquement si l'utilisateur est authentifié. Elle intègre 2 sous-onglets permettant de consulter la liste des utilisateurs que l'utilisateur courant suit et la liste des utilisateurs qui suivent l'utilisateur courant.
-------------------- SCREENSHOT
## Authentification
Au sein de l'application, l'authentification est requise pour consulter certaines vues spécifique au réseau d'amis et les vues comprenant des éventuelles informations persistées.
Les autres vues de l'application ne requièrent aucune authentificaion pour naviguer entre les différentes vues. Ce principe à été mis en place afin de ne pas reproduire un des principaux point faible des applications actuelles qui est de forcer l'utilisateur à s'authentifier sous peine de ne pas pouvoir utilisé l'application. Notre application demandera à l'utilisateur de s'authentifier que lorsque cela est nécessaire.
Afin de s'authentifier l'utilisateur dispose d'une interface prévue à cet effet. 
### Connexion
La vue connexion comporte simplement deux champs permettant de renseigner le pseudo et le mot de passe afin de s'authentifier.
-------------------- SCREENSHOT
### Enregistrement
Cette vue comporte les champs text nécessaire aux utilisateurs pour s'enregister dans l'application (création d'un nouveau compte). L'utilisateur doit obligatoirement entré un pseudo unique qui n'a pas déjà été utilisé auparavant.
-------------------- SCREENSHOT
## Barre de navigation
En haut de l'écran, nous avons mis à disposition une barre permettant de retourner à l'écran d'accueil (Vue des films tendance) à l'aide du boutton home en haut à droite, mais également de faire apparaitre le menu latéral à l'aide du boutton en haut à gauche de l'écran. Enfin cette barre permet d'afficher le nom de la vue courrante.
-------------------- SCREENSHOT
## Tabs de naviagtion
En bas de l'écran, nous retrouveons une liste de tabs (onglets) permettant de naviguer entre les différentes vues de l'application.
-------------------- SCREENSHOT
## Menu latéral
Finalement, nous avons un menu latéral qui apparaît de gauche à droite de l'écran. Ce menu permet d'afficher les informations relatives au profil de l'utilisateur actuellement connecté à l'application. D'autre part, il propose les action de connexion, enregistrement d'un nouveau compte et deconnexion de l'utilisateur courant. 
Si l'utilisateur est connecté, il permet aussi de naviguer vers les vues des films aimés et du réseau d'amis.
-------------------- SCREENSHOT

# Implémentation
Dans cette partie concernant l'implémentation, nous allons présenter les différentes technologies, méthodes et composants utilisés afin de mener à bien le dévloppement de notre application.
## APIs
Dans le cadre de ce projet, notre application qui est le client intéragis avec différentes APIs externes permettant d'obtenir, persister les données nécessaires aux différentes fonctionnalités proposées.
### API TMDB
L'API prinicipalement utilisée dans notre projet est celle porposée par "the movies database" (TMDB) qui dispose de toutes les routes nécesaires à la récupération des informations sur les films et les acteurs.
Nous interagissons avec cette API en envoyant des requêtes en fonctions des besoins, nous recevons en retour les informations au format JSON.
Voici un exemple d'utilisation de l'API et de retour nous permettant d'obtenir la liste des films à la une : 
La route GET : `/discover/movie` nous retournera les informations suivantes au format JSON : 
```
{
  "page": 1,
  "total_results": 10000,
  "total_pages": 500,
  "results": [
    {
      "popularity": 533.832,
      "vote_count": 1707,
      "video": false,
      "poster_path": "/db32LaOibwEliAmSL2jjDF6oDdj.jpg",
      "id": 181812,
      "adult": false,
      "backdrop_path": "/jOzrELAzFxtMx2I4uDGHOotdfsS.jpg",
      "original_language": "en",
      "original_title": "Star Wars: The Rise of Skywalker",
      "genre_ids": [
        28,
        12,
        878
      ],
      "title": "Star Wars: The Rise of Skywalker",
      "vote_average": 6.7,
      "overview": "The surviving Resistance faces the First Order once again as the journey of Rey, Finn and Poe Dameron continues. With the power and knowledge of generations behind them, the final battle begins.",
      "release_date": "2019-12-18"
    },
    {
      "popularity": 498.196,
      "vote_count": 1586,
      "video": false,
      "poster_path": "/xBHvZcjRiWyobQ9kxBhO6B2dtRI.jpg",
      "id": 419704,
      "adult": false,
      "backdrop_path": "/p3TCqUDoVsrIm8fHK9KOTfWnDjZ.jpg",
      "original_language": "en",
      "original_title": "Ad Astra",
      "genre_ids": [
        12,
        18,
        9648,
        878,
        53
      ],
      "title": "Ad Astra",
      "vote_average": 6,
      "overview": "The near future, a time when both hope and hardships drive humanity to look to the stars and beyond. While a mysterious phenomenon menaces to destroy life on planet Earth, astronaut Roy McBride undertakes a mission across the immensity of space and its many perils to uncover the truth about a lost expedition that decades before boldly faced emptiness and silence in search of the unknown.",
      "release_date": "2019-09-17"
    }
  ]
```
### API REST Backend
Certaines données de l'application nécessitaient d'être persistées, c'est porquoi nous avons choisi de mettre en place un backend basé sur akka http implémenté en Scala. Le Scala est un lanage realativement proche du Kotlin qui fait également usage de la JVM c'est pourquoi le choix de se langage nous semblait judicieux dans le cadre de ce cours.

Les routes mises à disposition par cette API REST, nous permettent de persister les informations tels que les compte des utilisateurs, les relations de follow entre eux, les films "likés"/"dislikés".
Elles permettent également de s'authentifier, s'enregistrer, rechercher un utilisateur, suivre un utilisateur, ne plus suivre un utilisateur, obtenir la liste des suivis et suiveurs pour un utilisateur donné et enfinla liste des films qu'il apprécie et qu'il n'apprécie pas.

Etant donné que l'aspect persistance des données sort du cadre du cours d'Android, nous avons choisi de mettre en place une implémentation dite "in memory" qui substitue de manière abstraite une technologie quelconque de persistance. Les données sont donc stockées en RAM.
Cette implémentation est faite à l'aide d'une Map Scala qui fait parfaitement l'affaire pour ce genre de situations.
===>  PETIT SCHEMA

### API REST Youtube
STEVEN ??? 

## Fragments
Toutes les vues de l'applications mentionnées et décrites dans la partie conception sont au fait impléementées à l'aide de fragments. Chaque fragment est associée à un layout (fichier xml) qui décrit son aspect graphique. Le code Kotlin propre au fragment nous permet uniquement de définir une implémentation qui permet l'interaction entre les différents compostant graphique de la vue et la logique de l'application.
## Single Activity
Selon le cours et la documentation officiel d'Android, la méthode actuelle repose sur le principe d'utiliser qu'une seule activité composée principalement d'un fragment et naviguer entre les différents fragments représentant les vues de l'application.
===>  SCHEMMAA
## Navigation Graph
Plusiereurs possibilités s'offraient à nous concernant la navigation entre les différents fragments. Nous avons choisi d'experimenter la nouvelle métohde proposée par la documentation officielle d'Android qui fut très interessante et efficace.
Cette méthode repose sur un graph de navigation qui est éditable graphiquement ou au niveau du code XML. Il permet e définir les relations entre les différents fragments. Une fois de graph de navigation défini, des clasees sont automatiquement générées, elles représentent les liens entre les différents fragments et sont utilisés afin de naviguer depuis un fragment vers un autre.
Voici le graph de navigation de notre application : 
=========>>>> Screen NAVGRAPH

Voici un exemple de code qui permet de naviguer depuis le graph de la liste des films vers les détails d'un film en utilisant cette fameuse classe générée automatiquement :
`view.findNavController().navigate(ListMoviesFragmentDirections.actionListMoviesFragmentToMovieDetailsFragment(item.id, item.urlImg))`
### Arguments
Il est également possible de définir des arguments qui sont passable entre les fragments, dans ce cas la classe générée automatiquement prendra en compte ces derniers.
Du côté du fragment qui sera appelé, Android met à disposition une méthode très pratique permettant de récéptionner ces arguments.
Voici un exemple de code permettant cela :
`private val args: MovieDetailsFragmentArgs by navArgs()`
```
movieId = args.id
urlImg = args.urlImg
```

## Volley requests
Comme nous l'avons vu notre application est majoritairement composée d'appel HTTP à diverses APIs. Nous avons donc utilisé une librairie=???? Android permettant d'effectuer les différents cals HTTP.
La librairie utilisée est donc volley request, voici un exemple de son utilisation : 
```
VIEUX CODE HTTP REQUEST 
```

### Assynchronicité
Dans la majorité de cas, la logique de l'application requiert d'avoir recus certaines infos en provenances des API avant de pouvoir les afficher sur la vue. Nous avions donc besoin de garantir que l'integralité des données était récéptionnée avant de les afficher, mais tout cela avec la contrainte de ne pas bloquer l'exécution du code.
Nous avons donc utilié le mechanisme de fonctions de call back permettant de prendre en compte cette contrainte.
Son principe est simple, la fonction de callback sera appelée que lorsque la requête HTTP sera effectué et les données receptionnées.
### Amélioration
Etant donné le nombre d'appels HTTP effectués dans notre application, le code était rapidement polué par ce code long et répétitif nous avons donc simplifié cela en cérant une classe `VolleyRequestController` permettant de mettre à disposition les méthodes relatives à tous les appels HTTP.
Voici un exemple d'une requête HTTP GET qui illustre cela : 

```
fun httpGet(URL: String, context: Context, callback: ServerCallback<JSONObject>) {
    val jsonObjReq = JsonObjectRequest(Request.Method.GET, URL, null,
    Response.Listener { response ->
        callback.onSuccess(response) // call call back function here
    },
    Response.ErrorListener { error ->
        Log.println(Log.DEBUG, this.javaClass.name, "error in httpGet : $error,\n$URL\n$callback")
    })

    // Adding request to request queue
    HttpQueue.getInstance(context).addToRequestQueue(jsonObjReq)
}
```

## Drawer
Afin de rendre l'UI plus conviviale, nous avons implémenté un drawer (menu latéral dans la partie conception). Ce drawer repose également la dernière méthode proposée par la documentation d'Android.
Explication........... 
## Bottom tabs
En bas de l'écran, nous avons à disposition des onglets de navigation permettant de naviguer entre les vues principales de l'application, 
## View pager
## Search / input
## Shared preferences
## FAB
## Callback
## Generica adapter
## Relations entre les vues

# Conclusion
## Problèmes rencontrés
## Améliorations
