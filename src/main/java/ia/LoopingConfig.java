package main.java.ia;

public class LoopingConfig {
    
    final private StoringConfig stconf;
    LoopingConfigNode array[];
    
    public LoopingConfig(StoringConfig stconf)
    {
        this.stconf = stconf;
        this.array = new LoopingConfigNode[stconf.config.length];
        
        int i;
        LoopingConfigNode node;
        //head insertion
        for (i=0; i < stconf.config.length; i++)
        {
            node = new LoopingConfigNode(stconf, i);
            node.next = this.array[stconf.getX(i)];
            this.array[stconf.getX(i)] = node;
        }
    }
}
