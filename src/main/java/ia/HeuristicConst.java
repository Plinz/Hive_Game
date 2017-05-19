/*
 *
 */
package main.java.ia;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class HeuristicConst {
  
public static final Double[] AI_QUEEN = {0.12874060754497368, 1.1445122137434838, 0.08861736634528522, 4109.7933103188579, -0.35450704319100212, 0.0046116358532150258, 17.11963323212596};
public static final Double[] AI_SPIDER = {0.0029985500151337875, 0.22193076292434746, -1.7294176665397978, -0.79355880279707836, -0.7637216472264996, -0.039959961702868627, -0.33212180711454059};
public static final Double[] AI_GH = {-11.881129648623469, 0.666412762731396, 0.040666647328229021, -0.020970753076712884, -0.014843832945330981, -0.00539804572564186, 0.099050919081589514};
public static final Double[] AI_BEETLE = {-0.10267541594485208, 0.16163404180225582, -4.3429937974595862, 0.17569293016338353, 0.029227761302961831, -0.081875817176838672, -0.857575516367572};
public static final Double[] AI_ANT = {-0.70124813535602, -2.83654640675534, 0.34764491447931306, 0.0044933494886377863, -0.32509664218038609, -0.17995971583134421, -1.5886067340435788};
public static final Double[] AI_GENERAL = {-1.869665823654745, 4.0573103561793644, -0.36027022962172889, 0.0, 0.40869008700934417, 2.8627208848308241, 1.132811878839773};
public static final Double[] HUM_QUEEN = {-0.079230483143581734, 0.96326784018241807, 0.17169014322585022, -739.58631865147061, 0.66800735067949724, -0.085063577682892688, 10.351344543744999};
public static final Double[] HUM_SPIDER = {0.063462033031659537, -4.7389391078633745, 0.011377167303786054, -8.45661778635653, 0.952579441447011, -2.7481064300557563, -0.69529101410510719};
public static final Double[] HUM_GH = {0.18812298228619015, -0.0041418995104384954, 3.5072659350938067, 0.096992394592799444, 0.85724523959428267, 0.034743331848925522, 1.9897454741714549};
public static final Double[] HUM_BEETLE = {0.003765849523447537, -1.7489242310263748, -0.024355097980445779, 4.3652927041450535, 0.021555515298245325, -0.99360217388415517, -0.56276142856439137};
public static final Double[] HUM_ANT = {0.92583343413758135, 0.31100406658912494, 1.4088853720734798, -0.30667695965706354, 1.2758077144197426, 0.0470243463776905, 4.194908848724535};
public static final Double[] HUM_GENERAL = {-0.075498979525094087, -0.14202093136339761, -0.34601947869445787, 0.0, 0.0090537986784408053, -0.20045479052286633, -0.049414635353429777};


public List<List<List<Double>>> getHeuristicDataFromConsts(){
    List<List<List<Double>>> heuristicData = new ArrayList<>(2);
    heuristicData.stream().forEach((listListDouble) -> {
        listListDouble = new ArrayList<>(6);
    });
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_QUEEN));
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_SPIDER));
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_GH));
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_BEETLE));
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_ANT));
    heuristicData.get(0).add((ArrayList<Double>)Arrays.asList(AI_GENERAL));
    
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_QUEEN));
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_SPIDER));
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_GH));
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_BEETLE));
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_ANT));
    heuristicData.get(1).add((ArrayList<Double>)Arrays.asList(HUM_GENERAL));
    return heuristicData; 
}

}