package SenSkyCore.Surveys.SurveyControl;

import java.util.ArrayList;
import java.util.List;

public class Section
{
  private int Id;
  private String Name;
  private String instrucciones;
  private List<Question> listQuestion = new ArrayList();
  private int level =0;

  public int getLevel() {
    return level;
  }

  public void setLevel(int level) {
    this.level = level;
  }

  public void addQuestion(Question paramQuestion)
  {
    this.listQuestion.add(paramQuestion);
  }
  
  public int getId()
  {
    return this.Id;
  }
  
  public String getInstrucciones()
  {
    return this.instrucciones;
  }
  
  public List<Question> getListQuestion()
  {
    return this.listQuestion;
  }
  
  public String getName()
  {
    return this.Name;
  }
  
  public void setId(int paramInt)
  {
    this.Id = paramInt;
  }
  
  public void setInstrucciones(String paramString)
  {
    this.instrucciones = paramString;
  }
  
  public void setLstQuestion(List<Question> paramList)
  {
    this.listQuestion = paramList;
  }
  
  public void setName(String paramString)
  {
    this.Name = paramString;
  }
}



/* Location:           C:\Users\leonardo\Desktop\Nueva carpeta\dex2jar-0.0.9.15\classes2_dex2jar.jar

 * Qualified Name:     Ripples.Objects.Section

 * JD-Core Version:    0.7.0.1

 */