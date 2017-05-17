import sys


'''

Minimizing : 0
Maximizing : 1

# General Heuristics 

ValidMoveWeight : 0
ValidPlacementWeight : 1
ValidMovementWeight : 2
NeighborWeight (absent) : 3
InHandWeight : 4
InPlayWeight : 5
IsPinnedWeight : 6 

# Insects Heuristics 

ValidMoveWeight : 0 --> Nombre de deplacement/placement possible
ValidPlacementWeight : 1 --> Nombre de Placements possibles si piece en main
ValidMovementWeight : 2 --> Nombre de deplacements possibles si pieces en jeu
NeighborWeight : 3   --> Possède des voisins
InHandWeight : 4 --> Est en main ??
InPlayWeight : 5 --> Est en jeu ??
IsPinnedWeight : 6 --> Est pinned.

General : 0
QueenBee : 1
Spider : 2
Beetle : 3
Grasshopper : 4
SoldierAnt : 5


  // Basic Queen Management
             mw.Set(Player.Maximizing, BugType.QueenBee, BugTypeWeight.NeighborWeight, -5.0); // Your Queen has company!
            mw.Set(Player.Maximizing, BugType.QueenBee, BugTypeWeight.IsPinnedWeight, -42.0); // Your Queen is pinned!
 
            mw.Set(Player.Minimizing, BugType.QueenBee, BugTypeWeight.NeighborWeight, 7.0); // Surround the enemy Queen!
            mw.Set(Player.Minimizing, BugType.QueenBee, BugTypeWeight.IsPinnedWeight, 23.0); // The enemy Queen is pinned!

---> Cela signifierait :
    -> Maximizing = Tes pièces
    -> Minimizing = L'ennemi.

'''

heuri=open("heuristics","r")
heuread = heuri.read()
heursplit = heuread.split("\n")

Nice_list = distance = [[[0 for k in range(7)] for j in range(6)] for i in range(2)]

min_max=['Ennemi','Joueur']
Insect=['General','QueenBee','Spider','Beetle','Grasshopper','SoldierAnt']
Type_heuristic=['ValidMoveWeight','ValidPlacementWeight','ValidMovementWeight','NeighborWeight','InHandWeight','InPlayWeight','IsPinnedWeight']

for z in heursplit :
    y = z.split(">")
    #print(y[0])
    k = y[1].split("</")
    #print(k[0])
    value = k[0]
    #print(k[1])
    data = k[1].split(".")
    #print(data)
    
    if data[0] == "Minimizing" :
        a=0
    elif data[0] == "Maximizing" :
        a=1
    else :
        print("This shouldn't happen")
    if data[-1] == "ValidMoveWeight" :
        c = 0
    elif data[-1] == "ValidPlacementWeight" :
        c = 1
    elif data[-1] == "ValidMovementWeight" :
        c = 2
    elif data[-1] == "NeighborWeight" :
        c = 3
    elif data[-1] == "InHandWeight" :
        c = 4
    elif data[-1] == "InPlayWeight" :
        c = 5
    elif data[-1] == "IsPinnedWeight" :
        c = 6
    else :
        print("This shouldn't happen")
    if len(data) == 2 :
        b = 0
    elif data[1] == "QueenBee" :
        b = 1
    elif data[1] == "Spider" :
        b = 2
    elif data[1] == "Beetle" :
        b = 3
    elif data[1] == "Grasshopper" :
        b = 4
    elif data[1] == "SoldierAnt" :
        b = 5
    else :
        print("This shouldn't happen")
    Nice_list[a][b][c]=value

print("Valeur positive Forte :")

for i in range(len(Nice_list)) :
    for j in range(len(Nice_list[i])) :
        for k in range(len(Nice_list[i][j])) :
            if float(Nice_list[i][j][k]) > 1.0 :
                print(min_max[i],Insect[j],Type_heuristic[k],Nice_list[i][j][k])
                
print("Valeur Negative Forte :")
for i in range(len(Nice_list)) :
    for j in range(len(Nice_list[i])) :
        for k in range(len(Nice_list[i][j])) :
            if float(Nice_list[i][j][k]) < -1.0 :
                print(min_max[i],Insect[j],Type_heuristic[k],Nice_list[i][j][k])
