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
Comme vue dans la rubrique précédente (Vue des films tendance), si l'utilisateur souhaite en savoir d'avantage sur un film, il peut accéder à cette vue présentant les détails d'un film. Elle comporte les informations détaillée du film tel que le titre, la déscription complète, la liste des acteurs, la liste des réalisateurs et une grille qui contient tous les films similaire au film concerné. La possibilité de cliquer sur un film de la grille afin de naviguer vers ses détails est également integrée.
### Appréciation des films
Si l'utilisateur est conncté avec son profil personnel, la possibilité de "liker" ou disliker" le film est proposée afin de renseigner son appréciation et pouvoir par la suite les retrouver dans son profil personnel ou partgaer avec ses amis.

-------------------- SCREENSHOT
## Réseau des amis

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
Comme pour les détails d'un film, nous avons également une vue présentant les détails d'un acteur, elle permet de consulter les informations tel que ................. 
-------------------- SCREENSHOT
## Profil personnel
Si l'utilisateur est authentifié dans l'application, il aura alors la possiblité de consulter son porfil personnel que comporte 2 sous fenêtres naviguable à l'aide de sous onglets. Le premier permet de consulter sous forme de liste,tous les films que l'utilisateur courrant à apprécié (likes) et le second tous les films que l'utilisateur courrant n'a pas apprécié (dislikes).
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
## Tabs de naviagtion
## Menu latéral


# Implémentation
## API TMDB
## API REST Backend
## API REST Youtube
## Fragments
## Single Activity
## Navigation Graph
## Volley requests
## Drawer
## Bottom tabs
## View pager
## Search / input
## Shared preferences
## FAB
## Callback
## Generica adapter

# Conclusion
## Problèmes rencontrés
## Améliorations
