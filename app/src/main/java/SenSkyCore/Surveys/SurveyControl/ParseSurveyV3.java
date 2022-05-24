package SenSkyCore.Surveys.SurveyControl;

import android.os.Environment;
import android.util.Log;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ParseSurveyV3
{
  public static final String GOTO_QUESTION_SEPARATOR = ":";
  public static final String SURVEY_END_BY_ANSWER = "end";
  public static final String SURVEY_NEXT_BY_ANSWER = "next";

  public static final String SURVEY_VERSION = "2";
  private final String QUESTION_ANSWER_OPTIONS = "options";
  private final String QUESTION_ID = "id";
  private final String QUESTION_LICKERT_HIGH = "high";
  private final String QUESTION_LICKERT_LOW = "low";
  private final String QUESTION_MANDATORY = "mandatory";
  public final int QUESTION_MANDATORY_FALSE = 0;
  public final int QUESTION_MANDATORY_TRUE = 1;
  private final String QUESTION_NOTE = "note";
  private final String QUESTION_TEXT = "text";
  private final String QUESTION_TYPE = "type";
  public static final String SURVEY_OPTION_CLEAN = "clean";
  public static final String SURVEY_OPTION_OPEN = "open";
  private final String SECTION_ID = "id";
  private final String SECTION_NOTE = "note";
  private final String SECTION_QUESTION = "question";
  private final String SECTION_QUESTIONS = "questions";
  private final String SECTION_TITLE = "title";
  private final String SURVEY_ATT_VERSION = "version";
  private final String SURVEY_DESCRIPTION = "description";
  private final String SURVEY_SECTION = "section";
  private final String SURVEY_SECTIONS = "sections";
  private final String SURVEY_TITLE = "title";


  private Survey surveyV3;
  private boolean validated = false;
  
  private List<multipleOption> parseAnswers(Element paramElement)
  {
    NodeList localNodeList = paramElement.getElementsByTagName("option");
    List<multipleOption> localArrayList = new ArrayList<multipleOption>();
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        multipleOption localmultipleOption = new multipleOption();
        localmultipleOption.setAnswer(localElement.getTextContent());
        try
        {
          localmultipleOption.setGoto(localElement.getAttribute("goto"));
          if (localmultipleOption.getGoto().isEmpty()) {
            localmultipleOption.setGoto(SURVEY_NEXT_BY_ANSWER);
          }
          localmultipleOption.setType(localElement.getAttribute("attr"));
          if (localmultipleOption.getType().isEmpty()) {
            localmultipleOption.setType("none");
          }
        }
        catch (Exception localException)
        {
          localmultipleOption.setGoto(SURVEY_NEXT_BY_ANSWER);
          localmultipleOption.setType("none");
        }
        localArrayList.add(localmultipleOption);
      }
    }
    return localArrayList;
  }
  
  private List<Question> parseQuestionsofSection(Element paramElement,String senctionTitle)
  {
    List<Question> localArrayList = new ArrayList<Question>();
    NodeList localNodeList = paramElement.getElementsByTagName("question");
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      if (localNode.getNodeType() == 1)
      {
        Element localElement = (Element)localNode;
        Question localQuestion = new Question();
        localQuestion.setId(Integer.parseInt(localElement.getAttribute("id")));
        localQuestion.setSectionTitle(senctionTitle);
        localQuestion.setType(localElement.getAttribute("type"));
        localQuestion.setMandatory(Integer.parseInt(localElement.getAttribute("mandatory")));
        if (localQuestion.getMandatory() == 0)
        {
          localQuestion.setQuestion(localElement.getElementsByTagName("text").item(0).getTextContent());
        }
        else
        {
          StringBuilder localStringBuilder = new StringBuilder();
          localStringBuilder.append(localElement.getElementsByTagName("text").item(0).getTextContent());
          localStringBuilder.append("*");
          localQuestion.setQuestion(localStringBuilder.toString());
        }
        try
        {
          localQuestion.setNote(localElement.getElementsByTagName("note").item(0).getTextContent());
        }
        catch (Exception localException)
        {
          localQuestion.setNote("");
        }




        if ((localQuestion.getType().compareTo("multioption") == 0) || (localQuestion.getType().compareTo("multianswer") == 0) || localQuestion.getType().compareTo("lickertgroup")==0) {
          localQuestion.setListMultipleOptions(parseAnswers((Element)localElement.getElementsByTagName("options").item(0)));
        }

        if (localQuestion.getType().compareTo("lickert") == 0)
        {
          localQuestion.setLickertLow(localElement.getElementsByTagName("low").item(0).getTextContent());
          localQuestion.setLickertHigh(localElement.getElementsByTagName("high").item(0).getTextContent());
        }

        if(localQuestion.getType().compareTo("lickertgroup")==0)
        {
          localQuestion.setLickertGroupLow(Integer.parseInt(localElement.getElementsByTagName("lowValue").item(0).getTextContent()));
          localQuestion.setLickertGroupHigh(Integer.parseInt(localElement.getElementsByTagName("highValue").item(0).getTextContent()));

        }
        try {
          if (localQuestion.getType().compareTo("numeric") == 0) {
            localQuestion.setMinNumeric(Integer.parseInt(localElement.getElementsByTagName("min").item(0).getTextContent()));
            localQuestion.setMaxNumeric(Integer.parseInt(localElement.getElementsByTagName("max").item(0).getTextContent()));
          }
        }catch (Exception e){
          localQuestion.setMinNumeric(0);
          localQuestion.setMaxNumeric(100);
          e.printStackTrace();
        }

        if(localQuestion.getType().compareTo("multianswer")==0)
        {
          try {
            localQuestion.setNumberSelected(Integer.parseInt(localElement.getElementsByTagName("limit").item(0).getTextContent()));

          }catch (Exception localexception){
            localQuestion.setNumberSelected(-1);

          }        }


        localArrayList.add(localQuestion);
      }
    }
    return localArrayList;
  }
  
  private List<Section> parseSections(Element paramElement)
  {
    List<Section> localArrayList = new ArrayList<Section>();
    NodeList localNodeList = paramElement.getElementsByTagName("section");
    for (int i = 0; i < localNodeList.getLength(); i++)
    {
      Node localNode = localNodeList.item(i);
      if (localNode.getNodeType() == 1)
      {
        Section localSection = new Section();
        Element localElement = (Element)localNode;
        localSection.setId(Integer.parseInt(localElement.getAttribute("id")));
        localSection.setName(localElement.getElementsByTagName("title").item(0).getTextContent());
        localSection.setInstrucciones(localElement.getElementsByTagName("note").item(0).getTextContent());
        localSection.setLstQuestion(parseQuestionsofSection((Element)localElement.getElementsByTagName("questions").item(0),localSection.getName()));
        localArrayList.add(localSection);
      }
    }
    return localArrayList;
  }
  
  public Survey getSurvey(String pathSurveyFile)
  {
    try
    {
      File localFile = new File(Environment.getExternalStorageDirectory(), pathSurveyFile);
      if ((localFile.exists()) && (this.validated))
      {
        Document localDocument = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(localFile);
        localDocument.getDocumentElement().normalize();
        Element localElement = localDocument.getDocumentElement();
        Survey localSurvey = new Survey();
        localSurvey.setVersion(Integer.parseInt(localElement.getAttribute("version")));
        try {
          localSurvey.setId(localElement.getAttribute("id"));
        }catch (Exception e){
          localSurvey.setId("");
        }
        localSurvey.setTitle(localElement.getElementsByTagName("title").item(0).getTextContent());
        localSurvey.setDescription(localElement.getElementsByTagName("description").item(0).getTextContent());
        localSurvey.setSections(parseSections((Element)localElement.getElementsByTagName("sections").item(0)));
        this.surveyV3 = localSurvey;
      }
    }
    catch (Exception localException)
    {
      localException.printStackTrace();
      this.validated = false;
    }
    return this.surveyV3;
  }
  
  public boolean validateFile(String paramString)
  {
    if (!new File(Environment.getExternalStorageDirectory(), paramString).exists()) {
      this.validated = false;
    } else {
      this.validated = true;
    }
    return this.validated;
  }
}



/* Location:           C:\Users\leonardo\Desktop\Nueva carpeta\dex2jar-0.0.9.15\classes2_dex2jar.jar

 * Qualified Name:     Ripples.Tools.ParseSurveyV3

 * JD-Core Version:    0.7.0.1

 */