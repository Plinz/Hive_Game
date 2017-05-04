/*
this class is used to store a game configuration in an optimal way
the int contains :
    ________________________
   |  x |  y |  z | b | b| b| , x,y :5 bits each ; z : 3 bits ; and 3 booleans

The pieces are associated to int between 0 and 10 in Consts.
 */
package main.java.ia;

public class StoringConfig {

    short config[];
    
    private StoringConfig(int nb_pieces) {
        config = new short[nb_pieces];
    }

    public byte getX(int index) {
        return (byte) ((config[index] & 0xF800) >>11);
    }

    public byte getY(int index) {
        return (byte) ((config[index] & 0x07C0) >> 6);
    }

    public byte getZ(int index) {
        return (byte) ((config[index] & 0x0038) >> 3);
    }
    
    public boolean getStuck(int index) {
        return  (((config[index] & 0x0004) >> 2)==1);
    }
    
    public boolean getIsVisited (int index){
        return  (((config[index] & 0x0002) >> 1)==1);
    }
    
    public boolean getBool3 (int index){
        return  ((config[index] & 1)==1);
    }
}
