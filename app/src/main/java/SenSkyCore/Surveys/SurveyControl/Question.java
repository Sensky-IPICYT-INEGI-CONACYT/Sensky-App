package SenSkyCore.Surveys.SurveyControl;

import java.util.ArrayList;
import java.util.List;

public class Question
{
  private int Mandatory;
  private String Note;
  private String Type;
  private String answer = "Sin respuesta";
  private int id = 0;
  private int minNumeric=0;
  private int maxNumeric= 100;
  private int numberSelected = -1;
  private String lickertHigh;
  private String lickertLow;
  private int lickertGroupHigh;
  private int lickertGroupLow;
  private List<multipleOption> listMultipleOptions = new ArrayList();
  private String question;
  private Boolean questionAnswered = false;
  private String sectionTitle="";


  public String getSectionTitle() {
    return sectionTitle;
  }

  public void setSectionTitle(String sectionTitle) {
    this.sectionTitle = sectionTitle;
  }

  public void setMaxNumeric(int maxNumeric) {
    this.maxNumeric = maxNumeric;
  }

  public void setMinNumeric(int minNumeric) {
    this.minNumeric = minNumeric;
  }

  public int getMaxNumeric() {
    return maxNumeric;
  }

  public int getMinNumeric() {
    return minNumeric;
  }

  public void setNumberSelected(int numberSelected) {
    this.numberSelected = numberSelected;
  }

  public int getNumberSelected() {
    return numberSelected;
  }

  public String getAnswer()
  {
    return this.answer;
  }
  
  public int getId()
  {
    return this.id;
  }
  
  public String getLickertHigh()
  {
    return this.lickertHigh;
  }

  public String getLickertLow()
  {
    return this.lickertLow;
  }

  public int getLickertGroupHigh() {
    return lickertGroupHigh;
  }

  public int getLickertGroupLow() {
    return lickertGroupLow;
  }

  public List<multipleOption> getListMultipleOptions()
  {
    return this.listMultipleOptions;
  }
  
  public int getMandatory()
  {
    return this.Mandatory;
  }
  
  public String getNote()
  {
    return this.Note;
  }
  
  public String getQuestion()
  {
    return this.question;
  }
  
  public Boolean getQuestionAnswered()
  {
    return this.questionAnswered;
  }
  
  public String getType()
  {
    return this.Type;
  }
  
  public void setAnswer(String paramString)
  {
    this.answer = paramString;
  }
  
  public void setId(int paramInt)
  {
    this.id = paramInt;
  }
  
  public void setLickertHigh(String paramString)
  {
    this.lickertHigh = paramString;
  }
  
  public void setLickertLow(String paramString)
  {
    this.lickertLow = paramString;
  }

  public void setLickertGroupHigh(int lickerGrouptHigh) {
    this.lickertGroupHigh = lickerGrouptHigh;
  }

  public void setLickertGroupLow(int lickertGroupLow) {
    this.lickertGroupLow = lickertGroupLow;
  }

  public void setListMultipleOptions(List<multipleOption> paramList)
  {
    this.listMultipleOptions = paramList;
  }
  
  public void setMandatory(int paramInt)
  {
    this.Mandatory = paramInt;
  }
  
  public void setNote(String paramString)
  {
    this.Note = paramString;
  }
  
  public void setQuestion(String paramString)
  {
    this.question = paramString;
  }
  
  public void setQuestionAnswered(boolean paramBoolean)
  {
    this.questionAnswered = Boolean.valueOf(paramBoolean);
  }
  
  public void setType(String paramString)
  {
    this.Type = paramString;
  }
}



/* Location:           C:\Users\leonardo\Desktop\Nueva carpeta\dex2jar-0.0.9.15\classes2_dex2jar.jar

 * Qualified Name:     Ripples.Objects.Question

 * JD-Core Version:    0.7.0.1

 */