/*
 *
Heuristic Data : heuristicData[player][insect][typeHeuristique]
    avec player 0 -> IA
         player 1 -> opponent
                insect -> 0 queen, 1 spider, 2 grasshopper, 3 beetle, 4 ant, 5 general
                        et type ->  0 valid move (deplacement et placement)
                                    1 valid placement
                                    2 valid movement (deplacement)
                                    3 voisins (encerclement)
                                    4 en main
                                    5 en jeu
                                    6 pinned
 */
package main.java.ia;

public class HeuristicConst {

    public static final double[] MAXIMISE_QUEEN = {0.5, 0, 1, -30, 0, 0, -18};
    public static final double[] MAXIMISE_SPIDER = {0.5, -4.7, 0.5, -8.4, 1, -2.7, -0.5};
    public static final double[] MAXIMISE_GH = {0.5, 0.5, 3.5, 0, 1.5, 0.5, 1.9};
    public static final double[] MAXIMISE_BEETLE = {0.5, -1.7, 0.5, 2, 1, 1.1, -3};
    public static final double[] MAXIMISE_ANT = {0.5, 1.5, 0.5, 0, 1, 1.2, 4.19};
    public static final double[] MAXIMISE_GENERAL = {0, 0, 0, 0, 0, 0, -1};
    public static final double[] MINIMISE_QUEEN = {-0.5, 1.1, -0.5, 30, -1, -0.5, 18};
    public static final double[] MINIMISE_SPIDER = {-0.5, -0.5, -1.7, 0, -1, -1.1, 0.5};
    public static final double[] MINIMISE_GH = {-11, -0.5, -0.5, 0, -1.5, -0.5, 1};
    public static final double[] MINIMISE_BEETLE = {-0.5, -0.5, -2.3, 1, -1, -1.1, 3};
    public static final double[] MINIMISE_ANT = {-0.5, -2.8, -0.5, 0, -1, -1.5, -1.6};
    public static final double[] MINIMISE_GENERAL = {-1.8, 4, -0, 0, 0, 2.8, 1.1};

    
    public static final double QUEEN_NEIGHBOR_FACTOR = 1.3;
    
    public static final int NB_MOVE = 0;
    public static final int NB_ADD = 1;
    public static final int NB_MOVEMENT = 2;
    public static final int NB_NEIGHBOR = 3;
    public static final int IN_HAND = 4;
    public static final int ON_BOARD = 5;
    public static final int IS_PINNED = 6;
    
    public static final double[][][] getHeuristicDataFromConsts() {
        double[][][] heuristicData = new double[2][6][7];

        heuristicData[0][0] = MAXIMISE_QUEEN;
        heuristicData[0][1] = MAXIMISE_SPIDER;
        heuristicData[0][2] = MAXIMISE_GH;
        heuristicData[0][3] = MAXIMISE_BEETLE;
        heuristicData[0][4] = MAXIMISE_ANT;
        heuristicData[0][5] = MAXIMISE_GENERAL;

        heuristicData[1][0] = MINIMISE_QUEEN;
        heuristicData[1][1] = MINIMISE_SPIDER;
        heuristicData[1][2] = MINIMISE_GH;
        heuristicData[1][3] = MINIMISE_BEETLE;
        heuristicData[1][4] = MINIMISE_ANT;
        heuristicData[1][5] = MINIMISE_GENERAL;
        return heuristicData;
    }

}


/* Original heuristic from the guy
public static final double[] MAXIMISE_QUEEN = {0.12874060754497368, 1.1445122137434838, 0.08861736634528522, 4109.7933103188579, -0.35450704319100212, 0.0046116358532150258, 17.11963323212596};
public static final double[] MAXIMISE_SPIDER = {0.0029985500151337875, 0.22193076292434746, -1.7294176665397978, -0.79355880279707836, -0.7637216472264996, -0.039959961702868627, -0.33212180711454059};
public static final double[] MAXIMISE_GH = {-11.881129648623469, 0.666412762731396, 0.040666647328229021, -0.020970753076712884, -0.014843832945330981, -0.00539804572564186, 0.099050919081589514};
public static final double[] MAXIMISE_BEETLE = {-0.10267541594485208, 0.16163404180225582, -4.3429937974595862, 0.17569293016338353, 0.029227761302961831, -0.081875817176838672, -0.857575516367572};
public static final double[] MAXIMISE_ANT = {-0.70124813535602, -2.83654640675534, 0.34764491447931306, 0.0044933494886377863, -0.32509664218038609, -0.17995971583134421, -1.5886067340435788};
public static final double[] MAXIMISE_GENERAL = {-1.869665823654745, 4.0573103561793644, -0.36027022962172889, 0.0, 0.40869008700934417, 2.8627208848308241, 1.132811878839773};
public static final double[] MINIMISE_QUEEN = {-0.079230483143581734, 0.96326784018241807, 0.17169014322585022, -739.58631865147061, 0.66800735067949724, -0.085063577682892688, 10.351344543744999};
public static final double[] MINIMISE_SPIDER = {0.063462033031659537, -4.7389391078633745, 0.011377167303786054, -8.45661778635653, 0.952579441447011, -2.7481064300557563, -0.69529101410510719};
public static final double[] MINIMISE_GH = {0.18812298228619015, -0.0041418995104384954, 3.5072659350938067, 0.096992394592799444, 0.85724523959428267, 0.034743331848925522, 1.9897454741714549};
public static final double[] MINIMISE_BEETLE = {0.003765849523447537, -1.7489242310263748, -0.024355097980445779, 4.3652927041450535, 0.021555515298245325, -0.99360217388415517, -0.56276142856439137};
public static final double[] MINIMISE_ANT = {0.92583343413758135, 0.31100406658912494, 1.4088853720734798, -0.30667695965706354, 1.2758077144197426, 0.0470243463776905, 4.194908848724535};
public static final double[] MINIMISE_GENERAL = {-0.075498979525094087, -0.14202093136339761, -0.34601947869445787, 0.0, 0.0090537986784408053, -0.20045479052286633, -0.049414635353429777};
*/
