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

- [ ] Cloporte : une case sur le coté, peut se mettre sous une pièce et la soulever
- [ ] Moustique : Copie un insecte adjacent 
- [ ] Coccinelle : Escalade sur deux cases puis redescend

