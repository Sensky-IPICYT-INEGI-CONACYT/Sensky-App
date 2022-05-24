package SenSkyCore.Surveys.SurveyControl;

public class multipleOption
{
  private String Answer;
  private String Backto;
  private String Goto;
  private String Type = "none";
  private String Value = "";
  private boolean selected;

  private int weight=0;

  public int getWeight() {
    return weight;
  }

  public void setWeight(int weight) {
    this.weight = weight;
  }

  public String getAnswer()
  {
    return this.Answer;
  }
  
  public String getBackto()
  {
    return this.Backto;
  }
  
  public String getGoto()
  {
    return this.Goto;
  }
  
  public boolean getSelected()
  {
    return this.selected;
  }
  
  public String getType()
  {
    return this.Type;
  }
  
  public String getValue()
  {
    return this.Value;
  }
  
  public void setAnswer(String paramString)
  {
    this.Answer = paramString;
  }
  
  public void setBackto(String paramString)
  {
    this.Backto = paramString;
  }
  
  public void setGoto(String paramString)
  {
    this.Goto = paramString;
  }
  
  public void setSelected(boolean paramBoolean)
  {
    this.selected = paramBoolean;
  }
  
  public void setType(String paramString)
  {
    this.Type = paramString;
  }
  
  public void setValue(String paramString)
  {
    this.Value = paramString;
  }
}



/* Location:           C:\Users\leonardo\Desktop\Nueva carpeta\dex2jar-0.0.9.15\classes2_dex2jar.jar

 * Qualified Name:     Ripples.Objects.multipleOption

 * JD-Core Version:    0.7.0.1

 */