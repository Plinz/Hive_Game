package main.java.ia;

/*
this class is used to run algorithms on in an optimal way
representation : it's an array of nodes
each node contents : piece (type & team), coords (x,y,z) + some bools (stuck, visited ...)
Advantage over StoringConfig -> more or less constant time search by coords
-> almost constant time translation from storingConfig to LoopingConfig & opposite
 */
public class LoopingConfig {

    final private StoringConfig stconf;
    LoopingConfigNode array[];

    public LoopingConfig(StoringConfig stconf) {
        this.stconf = stconf;
        this.array = new LoopingConfigNode[stconf.config.length];

        int i;
        LoopingConfigNode node;
        //head insertion
        for (i = 0; i < stconf.config.length; i++) {
            node = new LoopingConfigNode(stconf, i);
            node.next = this.array[stconf.getX(i)];
            this.array[stconf.getX(i)] = node;
        }
    }
}
