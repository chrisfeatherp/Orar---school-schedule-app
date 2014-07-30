package ro.liisorar.app.models;


public class ListBlock implements ListItem {

    private Block mBlock;

    public ListBlock(Block mBlock){
        this.mBlock = mBlock;
    }

    public Block getBlock(){
        return this.mBlock;
    }

    @Override
    public boolean isSectionLabel() {
        return false;
    }
}
