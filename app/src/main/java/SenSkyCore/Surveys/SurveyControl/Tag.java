package SenSkyCore.Surveys.SurveyControl;

public class Tag
{

    private boolean selected = false;
    private String value = "";

    public boolean getSelected()
    {
        return this.selected;
    }
    public String getValue()
    {
        return this.value;
    }
    public void setSelected(boolean paramBoolean)
    {
        this.selected = paramBoolean;
    }
    public void setValue(String paramString)
    {
        this.value = paramString;
    }
}
