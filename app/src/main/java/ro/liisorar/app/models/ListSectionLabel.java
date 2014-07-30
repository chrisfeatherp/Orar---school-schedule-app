package ro.liisorar.app.models;


public class ListSectionLabel implements ListItem {

    private Section mSection;

    public ListSectionLabel(Section mSection){
        this.mSection = mSection;
    }


    public Section getSection(){
        return this.mSection;
    }

    @Override
    public boolean isSectionLabel() {
        return true;
    }
}