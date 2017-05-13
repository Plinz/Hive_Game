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
    
    /*@Before
    public void init()
    {
        StoringConfig stconf = new StoringConfig(TOTALNBPIECES);

        //white queen in (5,5)
        stconf.setX(0, (byte) 5);
        stconf.setY(0, (byte) 5);
        stconf.setIsOnBoard(0, true);

        //black queen in (4,5)
        stconf.setX(11, (byte) 4);
        stconf.setY(11, (byte) 5);
        stconf.setIsOnBoard(11, true);

        //white beetle 1 in (6,4)
        stconf.setX(6, (byte) 6);
        stconf.setY(6, (byte) 4);
        stconf.setIsOnBoard(6, true);

        //black spider 1 in (4,4)
        stconf.setX(12, (byte) 4);
        stconf.setY(12, (byte) 4);
        stconf.setIsOnBoard(12, true);

        //white beetle 2 in (6,3)
        stconf.setX(7, (byte) 6);
        stconf.setY(7, (byte) 3);
        stconf.setIsOnBoard(7, true);

        //black spider 2 in (3,4)
        stconf.setX(13, (byte) 3);
        stconf.setY(13, (byte) 4);
        stconf.setIsOnBoard(13, true);
    }*/
            
    @Test
    public void testQueenRespectsOneHive()
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
        
        /*** game initialized ***/
        
        PieceNode queenNode = new PieceNode(stconf, 0);
        GameConfig gameconf = new GameConfig(stconf, 5);
        
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
        System.out.print(queenNode.toString());
        GameConfig gameconf = new GameConfig(stconf, 7);
        System.out.println(stconf.toString());
        List<StoringConfig> outputList = new ArrayList<>(Arrays.asList(outputconf1, outputconf2));

        assertEquals(gameconf.getPossibleQueenDestinations(queenNode),outputList);
    }

    /*@Test
    public void testKeepsPermanentContact()
    {*/
        /*** initializing test game ***/
        //TO BE DONE
        //white queen in (5,5)
        /*stconf.setX(QUEEN, (byte) 5);
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
        stconf.setIsOnBoard(SPIDER1 + 14, true);*/
        
        /*** game initialized ***/
        
        //PieceNode queenNode = new PieceNode(stconf, 0);
        //GameConfig gameconf = new GameConfig(stconf, 5);

        //assertThat(gameconf.getPossibleQueenDestinations(queenNode), is(empty()));
        
        //List<String> actual = Arrays.asList("fee", "fi", "foe");
        
    //}
}
