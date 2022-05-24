package SenSkyCore.Surveys.SurveyControl;

import java.util.ArrayList;
import java.util.List;

public class Survey
{
  private String Description;
  private List<Section> Sections = new ArrayList<>();
  private String Title;
  private int Version;
  private String id;

  public String getId() {
    return id;
  }

  public String getDescription()
  {
    return this.Description;
  }
  
  public List<Section> getSections()
  {
    return this.Sections;
  }
  
  public String getTitle()
  {
    return this.Title;
  }
  
  public int getVersion()
  {
    return this.Version;
  }

  public void setId(String id) {
    this.id = id;
  }

  public void setDescription(String paramString)
  {
    this.Description = paramString;
  }
  
  public void setSections(List<Section> paramList)
  {
    this.Sections = paramList;
  }
  
  public void setTitle(String paramString)
  {
    this.Title = paramString;
  }
  
  public void setVersion(int paramInt)
  {
    this.Version = paramInt;
  }
}



/* Location:           C:\Users\leonardo\Desktop\Nueva carpeta\dex2jar-0.0.9.15\classes2_dex2jar.jar

 * Qualified Name:     Ripples.Objects.Survey

 * JD-Core Version:    0.7.0.1

 */