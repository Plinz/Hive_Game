package ia.tests;
import static java.lang.Boolean.valueOf;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import static java.util.Optional.empty;
import main.java.ia.GameConfig;
import main.java.ia.PieceNode;
import main.java.ia.StoringConfig;
import static main.java.utils.Consts.*;
import main.java.utils.Coord;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;

public class BugsDisplacementsTests 
{
    
    public static final int TOTALNBPIECES = 14*2;
    StoringConfig stconf;
    StoringConfig outputconf1, outputconf2, outputconf3, 
                  outputconf4, outputconf5, outputconf6;
    
    @Before
    public void init()
    {
        this.stconf = new StoringConfig(TOTALNBPIECES);
    }
        
    @Test
    public void testQueenRespectsOneHiveWithOneNeighbor()
    {
        /*** initializing test game ***/
        
        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (5,6)
        stconf.setX(QUEEN + 14, (byte) 5);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (6,6)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 6);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black grasshopper 1 in (4,7)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        /*** game initialized ***/
        
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
        assertTrue(gameconf.RespectsOneHive(queenNode));
    }
    
    @Test
    public void testQueenRespectsOneHiveWithManyNeighbors()
    {
        /*** initializing test game ***/
        
        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,5)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 5);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (5,4)
        stconf.setX(BEETLE1, (byte) 5);
        stconf.setY(BEETLE1, (byte) 4);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black spider 1 in (4,4)
        stconf.setX(SPIDER1 + 14, (byte) 4);
        stconf.setY(SPIDER1 + 14, (byte) 4);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //black ant 1 in (3,5)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*** game initialized ***/
        
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertTrue(gameconf.RespectsOneHive(queenNode));
    }
    
    @Test
    public void testQueenDoesNotRespectOneHiveWithManyNeighbors()
    {
        /*** initializing test game ***/
        
        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,5)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 5);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (6,4)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 4);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black spider 1 in (4,4)
        stconf.setX(SPIDER1 + 14, (byte) 4);
        stconf.setY(SPIDER1 + 14, (byte) 4);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white grasshopper 1 in (6,3)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 3);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black ant 1 in (3,5)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*** game initialized ***/
        
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertFalse(gameconf.RespectsOneHive(queenNode));
    }
        
    @Test
    public void testQueenCannotPassGates()
    {
        /*************** initializing test game ***************/

        //white beetle 1 in (6,4)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 4);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black spider 1 in (5,5)
        stconf.setX(SPIDER1 + 14, (byte) 5);
        stconf.setY(SPIDER1 + 14, (byte) 5);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white beetle 2 in (6,3)
        stconf.setX(BEETLE2, (byte) 6);
        stconf.setY(BEETLE2, (byte) 3);
        stconf.setIsOnBoard(BEETLE2, true);
        
        //black queen in (4,5)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 5);
        stconf.setIsOnBoard(QUEEN + 14, true);
        
        //white queen in (5,3)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 3);
        stconf.setIsOnBoard(QUEEN, true);

        //black spider 2 in (4,4)
        stconf.setX(SPIDER2 + 14, (byte) 4);
        stconf.setY(SPIDER2 + 14, (byte) 4);
        stconf.setIsOnBoard(SPIDER2 + 14, true);

        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white queen moves to (4,3)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(QUEEN, (byte) 4);
        outputconf1.setY(QUEEN, (byte) 3);
        
        // output config n°2 : white queen moves to (6,2)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(QUEEN, (byte) 6);
        outputconf2.setY(QUEEN, (byte) 2);

        /*************** output StoringConfigs created ***************/
            
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 7);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleQueenDestinations(queenNode),outputList);
    }

    @Test
    public void testQueenKeepsPermanentContact()
    {
        /*************** initializing test game ***************/

        //white queen in (6,3)
        stconf.setX(QUEEN, (byte) 6);
        stconf.setY(QUEEN, (byte) 3);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (6,4)
        stconf.setX(QUEEN + 14, (byte) 6);
        stconf.setY(QUEEN + 14, (byte) 4);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white ant 1 in (4,5)
        stconf.setX(ANT1, (byte) 4);
        stconf.setY(ANT1, (byte) 5);
        stconf.setIsOnBoard(ANT1, true);

        //black spider 1 in (5,5)
        stconf.setX(SPIDER1 + 14, (byte) 5);
        stconf.setY(SPIDER1 + 14, (byte) 5);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //black ant 1 in (4,4)
        stconf.setX(ANT1 + 14, (byte) 4);
        stconf.setY(ANT1 + 14, (byte) 4);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        // output config n°1 : white queen moves to (7,3)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(QUEEN, (byte) 7);
        outputconf1.setY(QUEEN, (byte) 3);
        
        //output config n°2 : white queen moves to (5,4)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(QUEEN, (byte) 5);
        outputconf2.setY(QUEEN, (byte) 4);

        /*************** output StoringConfigs created ***************/
        
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 9);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleQueenDestinations(queenNode), outputList);      
    }
    
    @Test
    public void testSpiderDoesNotRespectOneHiveWithManyNeighbors()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,6)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white spider 1 in (6,5)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 5);
        stconf.setIsOnBoard(SPIDER1, true);

        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white ant 1 in (7,5)
        stconf.setX(ANT1, (byte) 7);
        stconf.setY(ANT1, (byte) 5);
        stconf.setIsOnBoard(ANT1, true);
        
        //black spider 2 in (2,6)
        stconf.setX(SPIDER2 + 14, (byte) 2);
        stconf.setY(SPIDER2 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER2 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode spiderNode = new PieceNode(stconf, 1);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
        assertFalse(gameconf.RespectsOneHive(spiderNode));
    }
    
    @Test
    public void testSpiderCannotPassGates()
    {
        /*************** initializing test game ***************/

        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);
        
        //black queen in (4,6)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);
        
        //white ant 1 in (5,4)
        stconf.setX(ANT1, (byte) 5);
        stconf.setY(ANT1, (byte) 4);
        stconf.setIsOnBoard(ANT1, true);
        
        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white spider 1 in (6,3)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 3);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black spider 2 in (3,5)
        stconf.setX(SPIDER2 + 14, (byte) 3);
        stconf.setY(SPIDER2 + 14, (byte) 5);
        stconf.setIsOnBoard(SPIDER2 + 14, true);

        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white spider moves to (5,6)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(SPIDER1, (byte) 5);
        outputconf1.setY(SPIDER1, (byte) 6);
        
        // output config n°2 : white spider moves to (3,4)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(SPIDER1, (byte) 3);
        outputconf2.setY(SPIDER1, (byte) 4);

        /*************** output StoringConfigs created ***************/
            
        PieceNode spiderNode = new PieceNode(stconf, 1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleSpiderDestinations(spiderNode),outputList);
    }

    @Test
    public void testSpiderKeepsPermanentContact()
    {
        /*************** initializing test game ***************/

        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,6)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white ant 1 in (5,4)
        stconf.setX(ANT1, (byte) 5);
        stconf.setY(ANT1, (byte) 4);
        stconf.setIsOnBoard(ANT1, true);
        
        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white spider 1 in (6,3)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 3);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black ant 1 in (3,5)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white spider moves to (5,6)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(SPIDER1, (byte) 5);
        outputconf1.setY(SPIDER1, (byte) 6);
        
        // output config n°2 : white spider moves to (3,4)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(SPIDER1, (byte) 3);
        outputconf2.setY(SPIDER1, (byte) 4);

        /*************** output StoringConfigs created ***************/
            
        PieceNode spiderNode = new PieceNode(stconf, 1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleSpiderDestinations(spiderNode),outputList);    
    }
    
}
