package ia.tests;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import main.java.ia.GameConfig;
import main.java.ia.PieceNode;
import main.java.ia.StoringConfig;
import static main.java.utils.Consts.*;
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
    public void testQueenCannotMoveWhenStuck()
    {
        /*** initializing test game ***/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (5,6,0)
        stconf.setX(QUEEN + 14, (byte) 5);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (5,5,1)
        stconf.setX(BEETLE1, (byte) 5);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setZ(BEETLE1, (byte) 1);
        stconf.setIsStuck(QUEEN, true);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black grasshopper 1 in (4,7,0)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        /*** game initialized ***/
        
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertTrue(gameconf.getPossibleQueenDestinations(queenNode).isEmpty());
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
        
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
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
        
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
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
        
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
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
            
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
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
        
        PieceNode queenNode = new PieceNode(stconf, QUEEN);
        GameConfig gameconf = new GameConfig(stconf, 9);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleQueenDestinations(queenNode), outputList);      
    }
    
    @Test
    public void testSpiderCannotMoveWhenStuck()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (5,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white spider 1 in (6,5,0)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 5);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black grasshopper 1 in (4,7,0)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        //white beetle 1 in (6,5,1)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setZ(BEETLE1, (byte) 1);
        stconf.setIsStuck(SPIDER1, true);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black grasshopper 2 in (3,7,0)
        stconf.setX(GRASSHOPPER2 + 14, (byte) 3);
        stconf.setY(GRASSHOPPER2 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER2 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode spiderNode = new PieceNode(stconf, SPIDER1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        
        assertTrue(gameconf.getPossibleSpiderDestinations(spiderNode).isEmpty());
    }
    
    @Test
    public void testSpiderRespectsOneHiveWithOneNeighbor()
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
        
        //black grasshopper 1 in (4,7)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode spiderNode = new PieceNode(stconf, SPIDER1);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
        assertTrue(gameconf.RespectsOneHive(spiderNode));
    }
    
    @Test
    public void testSpiderRespectsOneHiveWithManyNeighbors()
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

        //white spider 1 in (5,6)
        stconf.setX(SPIDER1, (byte) 5);
        stconf.setY(SPIDER1, (byte) 6);
        stconf.setIsOnBoard(SPIDER1, true);

        //black ant 1 in (3,6)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 6);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        //white beetle 1 in (6,5)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black beetle 1 in (4,5)
        stconf.setX(BEETLE1 + 14, (byte) 4);
        stconf.setY(BEETLE1 + 14, (byte) 5);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode spiderNode = new PieceNode(stconf, 1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        
        assertTrue(gameconf.RespectsOneHive(spiderNode));
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
        
        PieceNode spiderNode = new PieceNode(stconf, SPIDER1);
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
            
        PieceNode spiderNode = new PieceNode(stconf, SPIDER1);
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
            
        PieceNode spiderNode = new PieceNode(stconf, SPIDER1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleSpiderDestinations(spiderNode),outputList);    
    }
    
    @Test
    public void testGrassHopperCannotMoveWhenStuck()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (5,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white spider 1 in (6,5,0)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black grasshopper 1 in (4,7,0)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        //white beetle 1 in (6,5,1)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setZ(BEETLE1, (byte) 1);
        stconf.setIsStuck(GRASSHOPPER1, true);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black grasshopper 2 in (3,7,0)
        stconf.setX(GRASSHOPPER2 + 14, (byte) 3);
        stconf.setY(GRASSHOPPER2 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER2 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        
        assertTrue(gameconf.getPossibleSpiderDestinations(grassHopperNode).isEmpty());
    }
    
    @Test
    public void testGrassHopperRespectsOneHiveWithOneNeighbor()
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

        //white grasshopper 1 in (6,5)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black grasshopper 1 in (4,7)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
        assertTrue(gameconf.RespectsOneHive(grassHopperNode));
    }
    
    @Test
    public void testGrassHopperRespectsOneHiveWithManyNeighbors()
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

        //white grasshopper 1 in (5,6)
        stconf.setX(GRASSHOPPER1, (byte) 5);
        stconf.setY(GRASSHOPPER1, (byte) 6);
        stconf.setIsOnBoard(GRASSHOPPER1, true);

        //black ant 1 in (3,6)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 6);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        //white beetle 1 in (6,5)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black beetle 1 in (4,5)
        stconf.setX(BEETLE1 + 14, (byte) 4);
        stconf.setY(BEETLE1 + 14, (byte) 5);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        
        assertTrue(gameconf.RespectsOneHive(grassHopperNode));
    }
    
    @Test
    public void testGrassHopperDoesNotRespectOneHiveWithManyNeighbors()
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

        //white grasshopper 1 in (6,5)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);

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
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertFalse(gameconf.RespectsOneHive(grassHopperNode));
    }
    
    @Test
    public void testGrassHopperJumpsOverAllPiecesOfEachLine()
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
        
        //white grasshopper 1 in (4,5)
        stconf.setX(GRASSHOPPER1, (byte) 4);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white spider 1 in (4,4)
        stconf.setX(SPIDER1, (byte) 4);
        stconf.setY(SPIDER1, (byte) 4);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black spider 2 in (5,4)
        stconf.setX(SPIDER2 + 14, (byte) 5);
        stconf.setY(SPIDER2 + 14, (byte) 4);
        stconf.setIsOnBoard(SPIDER2 + 14, true);
        
        //white beetle 1 in (6,5)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black ant 1 in (3,5)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        //white grasshopper 2 in (7,5)
        stconf.setX(GRASSHOPPER2, (byte) 7);
        stconf.setY(GRASSHOPPER2, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER2, true);
        
        //black ant 2 in (2,7)
        stconf.setX(ANT2 + 14, (byte) 2);
        stconf.setY(ANT2 + 14, (byte) 7);
        stconf.setIsOnBoard(ANT2 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white grasshopper 1 moves to (8,5)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(GRASSHOPPER1, (byte) 8);
        outputconf1.setY(GRASSHOPPER1, (byte) 5);
        
        // output config n°2 : white grasshopper 1 moves to (4,7)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(GRASSHOPPER1, (byte) 4);
        outputconf2.setY(GRASSHOPPER1, (byte) 7);
        
        // output config n°3 : white grasshopper 1 moves to (1,8)
        this.outputconf3 = new StoringConfig(this.stconf);
        outputconf3.setX(GRASSHOPPER1, (byte) 1);
        outputconf3.setY(GRASSHOPPER1, (byte) 8);
        
        // output config n°4 : white grasshopper 1 moves to (2,5)
        this.outputconf4 = new StoringConfig(this.stconf);
        outputconf4.setX(GRASSHOPPER1, (byte) 2);
        outputconf4.setY(GRASSHOPPER1, (byte) 5);
        
        // output config n°5 : white grasshopper 1 moves to (4,3)
        this.outputconf5 = new StoringConfig(this.stconf);
        outputconf5.setX(GRASSHOPPER1, (byte) 4);
        outputconf5.setY(GRASSHOPPER1, (byte) 3);
        
        // output config n°6 : white grasshopper 1 moves to (6,3)
        this.outputconf6 = new StoringConfig(this.stconf);
        outputconf6.setX(GRASSHOPPER1, (byte) 6);
        outputconf6.setY(GRASSHOPPER1, (byte) 3);

        /*************** output StoringConfigs created ***************/
            
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 15);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2, outputconf3,
                                                                       outputconf4, outputconf5, outputconf6));

        assertEquals(gameconf.getPossibleGrassHopperDestinations(grassHopperNode),outputList);
    }
    
    @Test
    public void testGrassHopperJumpsOverAtLeastOnePiece()
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
        
        //white grasshopper 1 in (6,5)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white grasshopper 1 moves to (4,5)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(GRASSHOPPER1, (byte) 4);
        outputconf1.setY(GRASSHOPPER1, (byte) 5);
        
        /*************** output StoringConfigs created ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 5);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1));

        assertEquals(gameconf.getPossibleGrassHopperDestinations(grassHopperNode),outputList);
    }
    
    @Test
    public void testGrassHopperDoesNotJumpWhenFirstTileIsEmpty()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);
        
        //black queen in (4,5)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 5);
        stconf.setIsOnBoard(QUEEN + 14, true);
        
        //white grasshopper 1 in (6,5)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black spider 1 in (5,4)
        stconf.setX(SPIDER1 + 14, (byte) 5);
        stconf.setY(SPIDER1 + 14, (byte) 4);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white spider 1 in (6,3)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 3);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black ant 1 in (4,4)
        stconf.setX(ANT1 + 14, (byte) 4);
        stconf.setY(ANT1 + 14, (byte) 4);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white grasshopper moves to (3,5)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(GRASSHOPPER1, (byte) 3);
        outputconf1.setY(GRASSHOPPER1, (byte) 5);
       
        /*************** output StoringConfigs created ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1));

        assertEquals(gameconf.getPossibleGrassHopperDestinations(grassHopperNode),outputList);
    }
    
    @Test
    public void testGrassHopperLandsOnFirstEmptyTileEvenWithOtherPiecesFartherOnLine()
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
        
        //white grasshopper 1 in (6,5)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black spider 1 in (3,6)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white spider 1 in (6,4)
        stconf.setX(SPIDER1, (byte) 6);
        stconf.setY(SPIDER1, (byte) 4);
        stconf.setIsOnBoard(SPIDER1, true);
        
        //black ant 1 in (3,5)
        stconf.setX(ANT1 + 14, (byte) 3);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white grasshopper moves to (4,5)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(GRASSHOPPER1, (byte) 4);
        outputconf1.setY(GRASSHOPPER1, (byte) 5);
        
         //output config n°2 : white grasshopper moves to (6,3)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(GRASSHOPPER1, (byte) 6);
        outputconf2.setY(GRASSHOPPER1, (byte) 3);
        
        /*************** output StoringConfigs created ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleGrassHopperDestinations(grassHopperNode),outputList);
    }
    
    @Test
    public void testGrassHopperCanJumpOverManyStagesOfPieces()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);
        
        //black queen in (4,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);
        
        //white grasshopper 1 in (6,5,0)
        stconf.setX(GRASSHOPPER1, (byte) 6);
        stconf.setY(GRASSHOPPER1, (byte) 5);
        stconf.setIsOnBoard(GRASSHOPPER1, true);
        
        //black spider 1 in (4,5,0)
        stconf.setX(SPIDER1 + 14, (byte) 4);
        stconf.setY(SPIDER1 + 14, (byte) 5);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white beetle 1 in (5,5,1)
        stconf.setX(BEETLE1, (byte) 5);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setZ(BEETLE1, (byte) 1);
        stconf.setIsOnBoard(BEETLE1, true);
        stconf.setIsStuck(QUEEN, true);
        
        //black beetle 1 in (5,5,2)
        stconf.setX(BEETLE1 + 14, (byte) 5);
        stconf.setY(BEETLE1 + 14, (byte) 5);
        stconf.setZ(BEETLE1 + 14, (byte) 2);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        stconf.setIsStuck(BEETLE1, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white grasshopper moves to (3,5)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(GRASSHOPPER1, (byte) 3);
        outputconf1.setY(GRASSHOPPER1, (byte) 5);
        
        /*************** output StoringConfigs created ***************/
        
        PieceNode grassHopperNode = new PieceNode(stconf, GRASSHOPPER1);
        GameConfig gameconf = new GameConfig(stconf, 13);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1));

        assertEquals(gameconf.getPossibleGrassHopperDestinations(grassHopperNode),outputList);
    }
    
    @Test
    public void testBeetleCannotMoveWhenStuck()
    {
        /*** initializing test game ***/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (5,6,0)
        stconf.setX(QUEEN + 14, (byte) 5);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (5,5,1)
        stconf.setX(BEETLE1, (byte) 5);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setZ(BEETLE1, (byte) 1);
        stconf.setIsStuck(QUEEN, true);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black beetle 1 in (5,5,2)
        stconf.setX(BEETLE1 + 14, (byte) 5);
        stconf.setY(BEETLE1 + 14, (byte) 5);
        stconf.setZ(BEETLE1 + 14, (byte) 2);
        stconf.setIsStuck(BEETLE1, true);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        
        //white ant 1 in (6,4,0)
        stconf.setX(ANT1, (byte) 6);
        stconf.setY(ANT1, (byte) 4);
        stconf.setIsOnBoard(ANT1, true);
        
        /*** game initialized ***/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 11);
        
        assertTrue(gameconf.getPossibleQueenDestinations(beetleNode).isEmpty());
    }
    
    @Test
    public void testBeetleRespectsOneHiveWithOneNeighbor()
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

        //white beetle 1 in (6,5)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);
        
        //black grasshopper 1 in (4,7)
        stconf.setX(GRASSHOPPER1 + 14, (byte) 4);
        stconf.setY(GRASSHOPPER1 + 14, (byte) 7);
        stconf.setIsOnBoard(GRASSHOPPER1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
        assertTrue(gameconf.RespectsOneHive(beetleNode));
    }
    
    @Test
    public void testBeetleRespectsOneHiveWithManyNeighbors()
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

        //white beetle 1 in (5,6)
        stconf.setX(BEETLE1, (byte) 5);
        stconf.setY(BEETLE1, (byte) 6);
        stconf.setIsOnBoard(BEETLE1, true);

        //black ant 1 in (6,5)
        stconf.setX(ANT1 + 14, (byte) 6);
        stconf.setY(ANT1 + 14, (byte) 5);
        stconf.setIsOnBoard(ANT1 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertTrue(gameconf.RespectsOneHive(beetleNode));
    }
    
    @Test
    public void testBeetleDoesNotRespectOneHiveWithManyNeighbors()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (6,5,0)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);

        //black spider 1 in (3,6,0)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        //white ant 1 in (7,5,0)
        stconf.setX(ANT1, (byte) 7);
        stconf.setY(ANT1, (byte) 5);
        stconf.setIsOnBoard(ANT1, true);
        
        //black spider 2 in (2,6,0)
        stconf.setX(SPIDER2 + 14, (byte) 2);
        stconf.setY(SPIDER2 + 14, (byte) 6);
        stconf.setIsOnBoard(SPIDER2 + 14, true);
        
        /*************** game initialized ***************/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 7);
        
        assertFalse(gameconf.RespectsOneHive(beetleNode));
    }
    
    @Test
    public void testBeetleCanMoveToAnyAdjacentTile()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (4,5,0)
        stconf.setX(BEETLE1, (byte) 4);
        stconf.setY(BEETLE1, (byte) 5);
        stconf.setIsOnBoard(BEETLE1, true);

        //black beetle 1 in (3,6,0)
        stconf.setX(BEETLE1 + 14, (byte) 3);
        stconf.setY(BEETLE1 + 14, (byte) 6);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        
        //black spider 1 in (3,5,0)
        stconf.setX(SPIDER1 + 14, (byte) 3);
        stconf.setY(SPIDER1 + 14, (byte) 5);
        stconf.setIsOnBoard(SPIDER1 + 14, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white beetle moves to (5,5,1)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(BEETLE1, (byte) 5);
        outputconf1.setY(BEETLE1, (byte) 5);
        outputconf1.setZ(BEETLE1, (byte) 1);       
        outputconf1.setIsStuck(QUEEN, true);
        
        //output config n°2 : white beetle moves to (4,6,1)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(BEETLE1, (byte) 4);
        outputconf2.setY(BEETLE1, (byte) 6);
        outputconf2.setZ(BEETLE1, (byte) 1);
        outputconf2.setIsStuck(QUEEN + 14, true);
        
        //output config n°3 : white beetle moves to (3,6,1)
        this.outputconf3 = new StoringConfig(this.stconf);
        outputconf3.setX(BEETLE1, (byte) 3);
        outputconf3.setY(BEETLE1, (byte) 6);
        outputconf3.setZ(BEETLE1, (byte) 1);
        outputconf3.setIsStuck(BEETLE1 + 14, true);
        
        //output config n°4 : white beetle moves to (3,5,1)
        this.outputconf4 = new StoringConfig(this.stconf);
        outputconf4.setX(BEETLE1, (byte) 3);
        outputconf4.setY(BEETLE1, (byte) 5);
        outputconf4.setZ(BEETLE1, (byte) 1);
        outputconf4.setIsStuck(SPIDER1 + 14, true);

        //output config n°5 : white beetle moves to (4,4,0)
        this.outputconf5 = new StoringConfig(this.stconf);
        outputconf5.setX(BEETLE1, (byte) 4);
        outputconf5.setY(BEETLE1, (byte) 4);
        
        //output config n°6 : white beetle moves to (5,4,0)
        this.outputconf6 = new StoringConfig(this.stconf);
        outputconf6.setX(BEETLE1, (byte) 5);
        outputconf6.setY(BEETLE1, (byte) 4);
        
        /*************** output StoringConfigs created ***************/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2, outputconf3,
                                                                        outputconf4, outputconf5, outputconf6));

        assertEquals(gameconf.getPossibleBeetleDestinations(beetleNode),outputList);
    }
    
    @Test
    public void testBeetleCanClimbManyStagesInOneMove()
    {
        /*************** initializing test game ***************/
        
        //white queen in (5,5,0)
        stconf.setX(QUEEN, (byte) 5);
        stconf.setY(QUEEN, (byte) 5);
        stconf.setIsOnBoard(QUEEN, true);

        //black queen in (4,6,0)
        stconf.setX(QUEEN + 14, (byte) 4);
        stconf.setY(QUEEN + 14, (byte) 6);
        stconf.setIsOnBoard(QUEEN + 14, true);

        //white beetle 1 in (6,4,0)
        stconf.setX(BEETLE1, (byte) 6);
        stconf.setY(BEETLE1, (byte) 4);
        stconf.setIsOnBoard(BEETLE1, true);

        //black beetle 1 in (5,5,2)
        stconf.setX(BEETLE1 + 14, (byte) 5);
        stconf.setY(BEETLE1 + 14, (byte) 5);
        stconf.setZ(BEETLE1 + 14, (byte) 2);
        stconf.setIsStuck(BEETLE2, true);
        stconf.setIsOnBoard(BEETLE1 + 14, true);
        
        //white beetle 2 in (5,5,1)
        stconf.setX(BEETLE2, (byte) 5);
        stconf.setY(BEETLE2, (byte) 5);
        stconf.setZ(BEETLE2, (byte) 1);
        stconf.setIsStuck(QUEEN, true);
        stconf.setIsOnBoard(BEETLE2, true);
        
        /*************** game initialized ***************/
        
        /*************** creating output StoringConfigs ***************/
        
        //output config n°1 : white beetle 1 moves to (6,5,0)
        this.outputconf1 = new StoringConfig(this.stconf);
        outputconf1.setX(BEETLE1, (byte) 6);
        outputconf1.setY(BEETLE1, (byte) 5);       
        
        //output config n°2 : white beetle 1 moves to (5,5,3)
        this.outputconf2 = new StoringConfig(this.stconf);
        outputconf2.setX(BEETLE1, (byte) 5);
        outputconf2.setY(BEETLE1, (byte) 5);
        outputconf2.setZ(BEETLE1, (byte) 3);
        outputconf2.setIsStuck(BEETLE1 + 14, true);
        
        //output config n°3 : white beetle 1 moves to (5,4,0)
        this.outputconf3 = new StoringConfig(this.stconf);
        outputconf3.setX(BEETLE1, (byte) 5);
        outputconf3.setY(BEETLE1, (byte) 4);        
        
        /*************** output StoringConfigs created ***************/
        
        PieceNode beetleNode = new PieceNode(stconf, BEETLE1);
        GameConfig gameconf = new GameConfig(stconf, 9);
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2, outputconf3));

        assertEquals(gameconf.getPossibleBeetleDestinations(beetleNode),outputList);
    }
    
}
