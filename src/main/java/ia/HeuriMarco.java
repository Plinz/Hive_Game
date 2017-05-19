/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main.java.ia;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author marcof
 */
public class HeuriMarco {
  /*  
    public double get_score (Tablo) {
    double score = 0 ;
    // IA = 0 ; Human = 1
    
    for (int i = 0; i++; i < 2 ){
        int nb_main = nb_piece_en_main(i);
        int nb_jeu = nb_piece_en_jeu(i);
        int nb_pinned = nb_piece_pinned(i);
        int nb_mov = nb_piece_mov(i);
        int nb_dep = nb_piece_pla(i)
        int nb_pla = nb_mov + nb_dep ;
        score += Tablo[i][5][0] * nb_pla;
        score += Tablo[i][5][1] * nb_dep;
        score += Tablo[i][5][2] * nb_mov;
        score += Tablo[i][5][4] * nb_main;
        score += Tablo[i][5][5] * nb_jeu;
        score += Tablo[i][5][6] * nb_pinned;
    }
            
            
    for (All_IA_Tiles) {
        score+=get_tile_h(0,tile,Tablo);
    }
    for (All_Joueur_Tiles) {
        score+=get_tile_h(1,tile,Tablo);
}
    return score;
    }
    public double get_tile_h (joueur,piece,Tablo){
        double score = 0.0;
        int id_tile = piece.type;
        score += Tablo[joueur][id_tile][0] * nombre_mouvements(piece);
        score += Tablo[joueur][id_tile][1] * nombre_placements(piece);
        score += Tablo[joueur][id_tile][2] * nombre_deplacements(piece);
        score += Tablo[joueur][id_tile][3] * nombre_voisins(piece);
        score += Tablo[joueur][id_tile][4] * en_main(piece);
        score += Tablo[joueur][id_tile][5] * en_jeu(piece);
        score += Tablo[joueur][id_tile][6] * est_pinne(piece);
        return score;
    }
 */   
/*  
    Monsieur Raynal, votre mission si vous l'acceptez c'est de traduire ce langage en français en JAVA Correct 
    
    Fonctions pour le general avant l'appel pour la tile :
    nb_piece_mov : Nombre de mouvements possibles pour les pieces en jeu
    nb_piece_pla : Nombre d'endroits ou l'on peut placer une piece en jeu
    nb_piece_pinned : nombres de pieces pin
    nb_piece_en_jeu : nombres de pieces en jeu
    nb_piece_en_main : nombres de pieces en main
    
    Fonction pour la tile :
    en_jeu : Renvois 1 si la piece est en jeu, 0 si elle est en main.
    en_main : Renvois 1 si la piece est en main, 0 si elle est en main.
    est_pinne : Renvois 1 si la piece est pinned, 0 si elle est en main.
    nombre_voisins : Renvois le nombre de voisins de la piece si elle est en jeu sinon renvois 0.
    nombre_deplacement : renvois le nombre de deplacement de la piece si elle est en jeu, sinon 0
    nombre_placement : renvois le nombre de placement de la piece si elle est en main, sinon 0
    nombre_mouvement : renvois nombre_deplacement ou nombre_placement 
    
    J'ai réécris ma fonction si ça t'intéresse, mais c'est sale !
    
    Calculer_Le_Score_Du_Board ( Metrique, Joueur)
	
	Opponent = 1 - Joueur
	Score = 0
	// MAX : AJout du score general pour Joueur.
	score += Recuperer_Heuristique ( Joueur, poids_deplacement) * Somme(deplacement_valide[joueur])
	score += Recuperer_Heuristique ( Joueur, poids_placement) * Somme(placement_valide[joueur])
	score += Recuperer_Heuristique ( Joueur, poids_mouvement) * Somme(mouvement_valide[joueur])

	score += Recuperer_Heuristique( Joueur, poids_en_main) * Somme(Pièces_en_main[joueur])
	score += Recuperer_Heuristique( Joueur, poids_en_jeu) * Somme(Pièces_en_jeu[joueur])

	score += Recuperer_Heuristique( Joueur, poids_etre_pinned) * Somme(Pièces_Pinned[joueur])

	// MIN : AJout du score general pour l'opposant
	Pareil en remplaçant Joueur par Opposant

	Pour toutes les pièces du Joueur :
		score += calculer_score_piece(Joueur,piece)
	pour toute les pièces de l'opposant :
		score += calculer_score_piece(opposant,piece)



Calculer_score_piece(Opposant,piece) 
	type = bug_type(piece)
	score += Recuperer_Heuristique ( Joueur, type, type.poids_deplacement) * pds_deplacement_valide
	score += Recuperer_Heuristique ( Joueur, type, type.poids_placement) * pds_placement_valide
	score += Recuperer_Heuristique ( Joueur, type, type.poids_mouvement) * pds_mouvement_valide
##### PAS PRESENT DANS BOARD
	score += Recuperer_Heuristique (Joueur, type, type.poids_voisin ) * pds_nb_voisins
####

	score += Recuperer_Heuristique( Joueur, type, type.poids_en_main) * pds_Pièces_en_main
	score += Recuperer_Heuristique( Joueur, type, type.poids_en_jeu) * pds_Pièces_en_jeu

	score += Recuperer_Heuristique( Joueur, type, type.poids_etre_pinned) * pds_Pièces_Pinned


Deplacement_valide
Placement_valide : InHand * ValidMoveCount : 0 ou 1 * Nb Places
mouvement_valide : InPlay * ValidMoveCOunt : 0 ou 1 * Nb Places
pieces_en_main : InHand : 0 ou 1
piece_en_jeu : InPlay : 0 ou 1
piece_pinned : Ispinned : IsInPlay & validmovementcount == 0 : 1 sinon 0
nb_voisin : 

    
    
    */
    
    
    
    
}
