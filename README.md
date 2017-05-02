# Hive_Game

**Objectif**

L'objectif de ce projet est d’implémenter le jeu de plateau Hive, ainsi qu'une IA.

***

**Règles du jeu**

http://inf362.forge.imag.fr/Projet/Regles/french_rules_for_hexagonal_box.pdf

https://fr.wikipedia.org/wiki/Hive_(jeu_de_soci%C3%A9t%C3%A9)

http://www.gen42.com/downloads/rules/Hive_Carbon_French_Rules.pdf

http://www.gen42.com/downloads/rules/hive_pocket_rules.pdf

***

**Exemple de code**

https://github.com/jclopes/hive

https://github.com/hexparrot/hive-ai

https://github.com/mobeets/hive

https://github.com/shaw3257/hive

***

**Exemple de jeu en ligne**

https://fr.boardgamearena.com/#!gamepanel?game=hive  Il faut s'y inscrire

** Ressources**

http://www.redblobgames.com/grids/hexagons Comment construire une grille d'hexagones


## Membres du groupe

* Barrois Florian
* Duquennoy Antoine
* Duverney Thomas
* Gontard Benjamin
* Marco Florian
* Raynal Maxime

## Liste des contraintes du jeu

- [ ] Depart au milieu, le second joueur peut exceptionnellement se coller à la piece de l'adversaire
- [ ] Pose de pièce uniquement collé à ses propres pièce, impossible à coté des pièces énemies.
- [ ] Reine posée dans les 4 premiers tours
- [ ] Deplacement des insectes possible après la pose de la Reine selon leurs caracs
- [ ] Deplacement possible que si l'insecte ne brise pas la ruche ou n'est pas en dessous d'une pièce

## Caracteristique des deplacements des insectes 

- [ ] Reine : Deplacement d'une case sur le coté.
- [ ] Scarabé : 1 case sur le coté, peut escalader une pièce et la bloquer tant qu'il est au dessus.
- [ ] Araignée : Deplacement de trois case uniquement.
- [ ] Sauterelle : Saute par dessus les pieces qui l'entoure, en ligne droite.
- [ ] Fourmi : Peut se déplacer n'importe ou

**Autres insectes trouvés :**

- [ ] Cloporte : une case sur le coté, peut se mettre sous une pièce et la déplacer
- [ ] Moustique : Copie un insecte adjacent 
- [ ] Coccinelle : Escalade sur deux cases puis redescend

## Stratégies 

* Poser le scarabé sur la reine pour l'empêcher de bouger.
* Garder les sauterelles pour la fin du jeu ou elles pourront traverser facilement la ruche.
* Placer les araignées au début car elles sont puissantes sur les courtes distances quand la ruche est petite.
* Garder un oeil sur les pièces de l'adversaire, on peut le bloquer si il n'a plus de scarabé/sauterelles.
* Ne pas hésiter à bouger sa reine pour éviter les encerclements 
* Si l'adversaire joue deux pièces en ligne, il est facile de les bloquer et de l'empêcher de les utiliser. (2 pour un)
* Opening classique : Reine,Araignée,Fourmi ou Araignée,Reine,Araignée de cette forme : /\ (une de chaque coté de la reine)
* Garder ses pièces à l'extérieur de la ruche et celles de l'ennemi à l'intérieur.
* Il n'y a pas d'avantage à être le premier à jouer, par contre il y en a un à garder sa reine mobile : on peut jouer de manière agressive et forcer son adversaire à jouer de manière défensive.
* Y'en a qui disent qu'en fait si t'as un avantage à être le premier à jouer, si chaque joueur fait le "play" parfait ^^ ( Et les nouvelles pièces ont été ajoutées pour réduire l'avantage du mec qui commence. )
* Celui qui commence a l'initiative, le second joue de manière défense, jusqu'à que celui qui commence ne le menace plus.
