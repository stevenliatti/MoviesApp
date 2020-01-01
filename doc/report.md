---
lang: fr-CH
title: Movies App
subtitle: Mini-projet MobOP
documentclass: scrreprt
author:
- Jeremy Favre
- Steven Liatti
papersize : a4
fontsize : 11pt
geometry : margin = 2.5cm
---

# Introduction

## Buts

De nos jours, les médias sont à la portée de tous et accessibles très facilement notamment grâce aux plateformes telle que Netflix, streaming et bien d'autres encore. Il est donc difficle d'effectuer un choix parmi ces milliers de films mis à disposition. C'est là qu'entre en jeu notre mini-projet que nous avons réalisé dans le cadre du cours de *Systèmes d'exploitation mobiles et applications*. L'objectif est de développer une application dediée à la plateforme Android qui propose à ses utilisateurs divers services, tels que la liste des films à la une, obtenir les films similaires à un film, les acteurs concernés et la possibilité de rechercher un film en particulier.
2019 oblige, un aspect social est également présent au sein de cette application, l'utilisateur pourra renseigner son appréciation personnelle à propos d'un film, mais également de pouvoir suivre d'autres utilisateurs afin de connaitre leurs appréciations sur différents films qu'ils ont visionnés.
La forme "nous" est utilisée tout au long de ce rapport étant donné que ce projet est réalisé par binôme.

## Motivations

Le périmètre de ce projet semblait parfaitement adapté pour exploiter la majorité des techniques et méthodes vues durant le cours de développement d'application mobiles.
Ce projet est une formidable occasion pour relier la mise en pratique des connaissances acquises en cours et notre passion cinéphile commune. Nous sommes également convaincus que notre entourage (ou un réseau plus large) se servirait volontiers d'une telle application.

## Méthodologie de travail

Sur la base de cette analyse, nous avons séparé le travail en plusieurs tâches que nous avons assigné à chaque membre du binôme de manière équitable afin d'effectuer le travail en parallèle.
Nous avons adopté une pseudo méthode "agile", en factorisant le projet en petites tâches distinctes et en nous fixant des délais pour les réaliser. Le partage du code s'est fait avec git et gitlab. Nous nous sommes servis des "issues" gitlab pour représenter nos tâches et du "board" du projet pour avoir une vision globale du travail accompli (par qui et quand) et du travail restant.



# Conception et analyse

Nous avons commencé par dessiner les maquettes des vues de notre application afin de se faire une idée de l'aspect visuel de chaque vue et des relations entre les vues. Tout cela dans le but de simplifier et clarifier la phase de développement proprement dite.
Durant cette phase, nous nous sommes également documentés sur les APIs existantes proposant des services relatifs aux films, nous avons étudié les différentes technologies permettant de mettre en place une API REST et finalement nous avons analysé les composants et méthodologies étudiés en cours qui seraient utiles d'intégrer dans notre application.
Les sous-sections suivantes décrivent les vues et les fonctionnalités que l'application devra offrir.

## Films tendance {#sec:films-tendance}

Lorsqu'un utilisateur ouvre l'application, la vue principale (home) lui affiche une liste des films à la une, classés en fonction de leur popularité. Chaque film affiche ses informations de base (poster, titre, courte description). L'utilisateur a la possibilité de cliquer sur un film afin d'obtenir d'avantage d'informations.

<!-- TODO: -->
-------------------- SCREENSHOT

## Détails d'un film

Comme décrit à la @sec:films-tendance, si l'utilisateur souhaite en savoir plus sur un film, il peut accéder à cette vue présentant les détails d'un film. Elle comporte les informations détaillées du film tel que le titre, la description complète, la liste des acteurs, la liste des réalisateurs, les différentes bande-annonces, les genres associés et une grille qui contient des films similaire au film concerné. La possibilité de cliquer sur un film de la grille afin de naviguer vers ses détails est également intégrée.
Si l'utilisateur souhaite visionner une bande-annonce, il lui suffit de cliquer dessus afin que le *player* YouTube soit automatiquement lancé avec la vidéo en question.
C'est également depuis cette vue qu'il pourra indiquer son appréciation pour le film ("like" ou "dislike"), appréciation qui sera sauvegardée dans son profil.

-------------------- SCREENSHOT

## Recherche

Une interface de recherche est mise à disposition. La recherche peut être lancée parmi les films, les acteurs ou les autres utilisateur de l'application selon le choix de l'utilisateur (bouton "radio" ou mécanisme similaire).
Les résultats de la recherche seront présentés sous forme de liste, présentant les différentes actions selon le type de recherche qui aura été effectué :
- Si l'utilisateur cherche un film, il obtiendra la liste des films qui correspondent aux critères entrés dans le champs texte de recherche.
- S'il cherche un acteur, il obtiendra une liste d'acteurs avec la possibilité de cliquer dessus (comme pour les films) afin d'afficher plus de détails à leur sujet.
- S'il cherche un autre utilisateur, il aura alors la possibilité de le suivre en l'ajoutant à son réseau d'amis.

-------------------- SCREENSHOT
-------------------- SCREENSHOT
-------------------- SCREENSHOT

## "Peoples"

Au même titre que la liste des films tendance, nous avons une interface proposant la liste des acteurs actuellement populaires avec la possibilité de cliquer sur chacun d'entre eux afin d'obtenir plus d'informations à leur sujet.

-------------------- SCREENSHOT

## Détails d'un "people"

Comme pour les détails d'un film, nous avons également une vue présentant les détails d'un acteur, elle permet de consulter diverse informations, telles que sa biographie personnelle et la liste des films dans lesquels il a joué.

-------------------- SCREENSHOT

## Mes films {#sec:mes-films}

Si l'utilisateur possède un compte dans l'application, il pourra retrouver la liste des films appréciés ou non appréciés, sous une forme similaire à la liste des films tendance (@sec:films-tendance).

-------------------- SCREENSHOT

## Réseau d'amis {#sec:reseau-amis}

La vue réseau d'amis est disponible uniquement si l'utilisateur détient un compte. Son réseau d'amis sera constitué d'une liste d'utilisateurs le suivant (ses "followers") ainsi que d'une liste d'utilisateurs auxquels il est abonné (ses "following").

-------------------- SCREENSHOT

## Authentification

Au sein de l'application, l'authentification est requise pour consulter certaines vues spécifiques, comme le réseau d'amis (@sec:reseau-amis) ou les films préférés (@sec:mes-films) et les vues comprenant des éventuelles informations persistées.
Les autres vues de l'application ne requièrent aucune authentificaion. Ce principe a été mis en place afin de ne pas reproduire un des principaux points faibles des applications actuelles qui est de forcer l'utilisateur à s'authentifier sous peine de ne pas pouvoir utiliser l'application. Notre application demandera à l'utilisateur de s'authentifier uniquement lorsque cela est nécessaire.
 
### Connexion

La vue connexion comporte simplement deux champs permettant de renseigner le pseudo et le mot de passe afin de s'authentifier.

-------------------- SCREENSHOT

### Enregistrement

Cette vue comporte les champs nécessaires à la création d'un compte, à savoir un pseudo (unique), un email et un mot de passe.

-------------------- SCREENSHOT

## Navigation

Pour naviguer d'une vue à une autre, l'utilisateur dispose de différents mécanismes.

### Tabs de navigation

En bas de l'écran, nous retrouvons une liste de "tabs" (onglets) permettant de naviguer entre les différentes vues de l'application.

-------------------- SCREENSHOT

### Barre de navigation

En haut de l'écran se trouve une barre comportant le nom de la vue courrante, un bouton "home" (@sec:films-tendance) et un contrôle faisant apparaitre le menu latéral.

-------------------- SCREENSHOT

### Menu latéral

Finalement, nous avons un menu latéral qui apparaît de gauche à droite de l'écran. Ce menu permet d'afficher les informations relatives au profil de l'utilisateur courant. D'autre part, il propose les action de connexion, création de compte et déconnexion.
Si l'utilisateur est connecté, il pourra aussi naviguer vers les vues des films aimés et du réseau d'amis.

-------------------- SCREENSHOT

# Architecture

## Diagramme de classes

<!-- TODO: -->
===========> A FAIRE

# Implémentation

Dans cette partie concernant l'implémentation, nous allons présenter les différentes technologies, méthodes et composants utilisés afin de mener à bien le développement de notre application. Des captures d'écran illustreront le résultat final.

## APIs

Pour échanger les informations requises, notre application interagit avec des APIs externes permettant d'obtenir et persister les données nécessaires aux différentes fonctionnalités proposées.

### API TMDb

L'API principalement utilisée dans notre projet est celle proposée par ["The Movie Database" (TMDb)](https://www.themoviedb.org/) qui offre toutes les informations sur les films et les acteurs. Nous avons également considéré ["The Open Movie Database" (OMDb API)](https://www.omdbapi.com/), mais elle offre beaucoup moins de possibilités. Nous interagissons avec cette API en envoyant des requêtes HTTP en fonctions des besoins, nous recevons en retour les informations au format JSON.
Voici un exemple de réponse de l'API, nous permettant d'obtenir la liste des films à la une :


La route GET : `/discover/movie` nous retournera les informations suivantes au format JSON : 
```json
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
      "genre_ids": [ 28, 12, 878 ],
      "title": "Star Wars: The Rise of Skywalker",
      "vote_average": 6.7,
      "overview": "The surviving Resistance faces the First Order ...",
      "release_date": "2019-12-18"
    },
    ...
  ]
```

### API REST Backend

Certaines données de l'application nécessitent d'être persistées, c'est pourquoi nous avons choisi de mettre en place un backend basé sur [Akka HTTP](https://doc.akka.io/docs/akka-http/current/index.html) en Scala. Le Scala est un langage relativement proche du Kotlin qui fait également usage de la JVM et offre de nombreux atouts comme un typage fort, abstraction et POO, et permet d'écrire le code de manière simple, concise et déclarative. Pour toutes ces raisons, le choix de ce langage nous semblait judicieux.

Les routes mises à disposition par cette API REST nous permettent de persister les informations telles que les comptes des utilisateurs, les relations d'abonnements entre eux et les appréciations des films. Elles permettent également de s'authentifier, s'enregistrer et rechercher un utilisateur.

<!-- TODO: je donnerai même pas autant de détails, mais si tu y tiens ^^ -->
Etant donné que l'aspect persistance des données sort du cadre du cours d'Android, nous avons choisi de mettre en place une implémentation dite "in memory" qui substitue de manière abstraite une technologie quelconque de persistance. Les données sont donc stockées en RAM.
Cette implémentation est faite à l'aide d'une Map Scala (table de hachage) qui fait parfaitement l'affaire pour ce genre de situations.
===>  PETIT SCHEMA

### API REST YouTube

Pour chaque film, TMDb nous offre une liste de vidéos YouTube, comprenant des *trailers* et *teasers* du film. Nous avons alors fait usage de l'API officielle YouTube pour intégrer ces vidéos. Il faut obligatoirement créer une clé API dans son compte Google pour pouvoir utiliser cette API. On peut utiliser cette API globalement de deux manières différentes sur Android :

- `YouTubePlayerView` et `YouTubePlayerFragment` : offrent un plus grand contrôle sur la gestion de l'interface et des contrôles d'une vidéo ainsi que de son cycle de vie au prix de plus de code à écrire et à gérer.
- `YouTubeStandalonePlayer` : la manière plus simple et rapide d'utiliser l'API, utilise l'application officielle YouTube. Le player attend l'id d'une vidéo et la clé API et démarre l'activité de l'application officielle YouTube.

Nous avons décidé d'utiliser la deuxième méthode car nous n'avons pas besoin d'un contrôle fin sur le lancement des vidéos. Pour chaque vidéo obtenue sur la vue des détails d'un film, nous commençons par récupérer un *thumbnail* de chaque vidéo avec ce lien : `https://img.youtube.com/vi/$youtubeId/1.jpg`. Nous superposons ensuite une icône "play" et ajoutons un listener qui démarre la vidéo sur chaque *thumbnail*. Un `catch` de `ActivityNotFoundException` est inclu avec affichage d'un message dans un "toast" dans le cas ou l'utilisateur n'a pas l'application officielle YouTube installée sur son appareil.

## Fragments

Toutes les vues de l'application sont implémentées à l'aide de fragments. Chaque fragment est associé à un layout (fichier xml) qui décrit son aspect graphique. Le code Kotlin propre au fragment nous permet uniquement de définir une implémentation qui permet l'interaction entre les différents composants graphiques de la vue et la logique métier de l'application. Notre code est ainsi réutilisable dans plusieurs vues différentes.

## Single Activity

Selon le cours et la documentation officielle d'Android, l'architecture actuelle conseillée pour le développement d'une application repose sur le principe de n'utiliser qu'une seule activité composée de fragments, représentant les vues de l'application.
===>  SCHEMMAA

## Navigation Graph

Plusieurs possibilités s'offraient à nous concernant la navigation entre les différents fragments. Nous avons choisi d'experimenter la nouvelle méthode proposée par la documentation officielle d'Android qui est très intéressante et efficace.
Cette méthode repose sur un graphe de navigation qui est éditable graphiquement ou en XML. 

Une fois de graph de navigation défini, des clasees sont automatiquement générées, elles f

## Volley requests

## Drawer

## Bottom tabs

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
