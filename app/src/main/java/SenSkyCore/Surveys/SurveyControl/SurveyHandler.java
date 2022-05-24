package SenSkyCore.Surveys.SurveyControl;

import android.os.Environment;
import android.util.Xml;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import static SenSkyCore.Surveys.SurveyControl.ParseSurveyV3.SURVEY_NEXT_BY_ANSWER;


public class SurveyHandler
{
  private Section currenSection;
  private Question currentQuestion;
  private int indexCurrentQuestion = 0;
  private int indexCurrentSection = 0;


  public static final String SURVEY_TYPE_LONG_TEXT = "long-text";
  public static final String SURVEY_TYPE_MULTIPLE_ANSWER = "multianswer";
  public static final String SURVEY_TYPE_MULTIPLE_OPTION = "multioption";
  public static final String SURVEY_TYPE_LICKERT_GROUP= "lickertgroup";
  public static final String SURVEY_TYPE_SHORT_TEXT = "short-text";
  public static final String SURVEY_TYPE_LICKERT = "lickert";
  public static final String SURVEY_TYPE_NUMERIC = "numeric";

  private Survey survey;
  
  public SurveyHandler(Survey paramSurvey)
  {
    this.survey = paramSurvey;
    newValues();
  }
  
  public static String createFileSurvey(String surveyPathFile, List<Section> paramList, String paramString3, String idSurvey)
  {
    File localFile1 = Environment.getExternalStorageDirectory();

    File localFile2 = new File(localFile1, surveyPathFile);
    try
    {
      localFile2.createNewFile();
    }
    catch (IOException localIOException)
    {
    }


    FileOutputStream localObject = null;
    try
    {
      localObject = new FileOutputStream(localFile2);
    }
    catch (FileNotFoundException localFileNotFoundException)
    {
    }
    XmlSerializer localXmlSerializer = Xml.newSerializer();
    try
    {
      localXmlSerializer.setOutput( localObject, "UTF-8");
      localXmlSerializer.startDocument(null, Boolean.valueOf(true));
      if (paramList.size() > 0)
      {
        int counterId=1;
        localXmlSerializer.startTag(null, "survey");
        localXmlSerializer.attribute(null, "version", paramString3);
        localXmlSerializer.attribute(null,"xmlns","https://www.w3schools.com");
        localXmlSerializer.attribute(null,"id",idSurvey);
        Iterator localIterator1 = paramList.iterator();
        while (localIterator1.hasNext())
        {
          Section localSection = (Section)localIterator1.next();
          localXmlSerializer.startTag(null, "topic");
          localXmlSerializer.attribute(null,"id_section",String.valueOf(localSection.getId()));
          localXmlSerializer.attribute(null, "name", localSection.getName());
          Iterator localIterator2 = localSection.getListQuestion().iterator();
          while (localIterator2.hasNext()) {
            Question question =(Question)localIterator2.next();
            if(localSection.getId()==1 && (question.getId()==1 || question.getId()==4) || localSection.getId()==4 && question.getId()==2)
            saveQuestion(question, localXmlSerializer,1, String.valueOf(counterId));
            else
              saveQuestion(question, localXmlSerializer,0, String.valueOf(counterId));
            counterId++;

          }
          localXmlSerializer.endTag(null, "topic");
        }
        localXmlSerializer.endTag(null, "survey");
      }
      localXmlSerializer.endDocument();
      localXmlSerializer.flush();
      localObject.close();
      String str = localFile2.getName();
      return str;
    }
    catch (Exception localException)
    {
    }
    return "NULL";
  }
  private static void getAnswers(Question paramQuestion)
  {
    if (paramQuestion.getType().compareTo("multioption") == 0)
    {
      Iterator localIterator2 = paramQuestion.getListMultipleOptions().iterator();
      while (localIterator2.hasNext())
      {
        multipleOption localmultipleOption2 = (multipleOption)localIterator2.next();
        if (localmultipleOption2.getSelected())
        {
          paramQuestion.setAnswer(localmultipleOption2.getAnswer());
          return;
        }
      }
    }
    if (paramQuestion.getType().compareTo("multianswer") == 0)
    {
      String str = "";
      Iterator localIterator1 = paramQuestion.getListMultipleOptions().iterator();
      while (localIterator1.hasNext())
      {
        multipleOption localmultipleOption1 = (multipleOption)localIterator1.next();
        if (localmultipleOption1.getSelected())
        {
          StringBuilder localStringBuilder1 = new StringBuilder();
          localStringBuilder1.append(str);
          localStringBuilder1.append(localmultipleOption1.getAnswer());
          str = localStringBuilder1.toString();
          if (str.compareTo("") != 0)
          {
            StringBuilder localStringBuilder2 = new StringBuilder();
            localStringBuilder2.append(str);
            localStringBuilder2.append(":");
            str = localStringBuilder2.toString();
          }
        }
      }
      paramQuestion.setAnswer(str);
    }
  }
  private void newIndexValues(int paramInt1, int paramInt2) {
    this.indexCurrentSection = paramInt1;
    this.indexCurrentQuestion = paramInt2;
    newValues();
  }
  private void newValues() {

    this.currenSection = ((Section)this.survey.getSections().get(this.indexCurrentSection));
    this.currentQuestion = ((Question)this.currenSection.getListQuestion().get(this.indexCurrentQuestion));
  }
  private static void saveQuestion(Question paramQuestion, XmlSerializer paramXmlSerializer, int encrypted, String id) {
    try
    {
      paramXmlSerializer.startTag(null, "question");
      paramXmlSerializer.attribute(null, "id", id);
      paramXmlSerializer.attribute(null, "id_question",String.valueOf(paramQuestion.getId()));
      paramXmlSerializer.attribute(null,"type",paramQuestion.getType());
      paramXmlSerializer.startTag(null, "text");
      paramXmlSerializer.text(paramQuestion.getQuestion());
      paramXmlSerializer.endTag(null, "text");
      getAnswers(paramQuestion);
      paramXmlSerializer.startTag(null, "answer");
      /*if(encrypted==1)
        paramXmlSerializer.text(ToolsHandler.encryptAnswer(paramQuestion.getAnswer()));
      else
      */
        paramXmlSerializer.text(paramQuestion.getAnswer());


      paramXmlSerializer.endTag(null, "answer");
      paramXmlSerializer.startTag(null, "note");
      paramXmlSerializer.text(paramQuestion.getNote());
      paramXmlSerializer.endTag(null, "note");
      paramXmlSerializer.endTag(null, "question");
      return;
    }
    catch (Exception localException)
    {
    }
  }
  public boolean endQuestion(Question paramQuestion) {
    boolean bool = false;
    Iterator localIterator = paramQuestion.getListMultipleOptions().iterator();
    while (localIterator.hasNext())
    {
      multipleOption localmultipleOption = (multipleOption)localIterator.next();
      if ((localmultipleOption.getSelected())  && (localmultipleOption.getType().compareTo("end") == 0)) {
        bool = true;
      }
    }
    return bool;
  }
  public Section getCurrenSection()
  {
    return this.currenSection;
  }
  public Question getCurrentQuestion()
  {
    return this.currentQuestion;
  }
  public Question gotoQuestion(String paramString) {
    String[] arrayOfString = paramString.split(":");
    if (arrayOfString.length == 2) {
      newIndexValues(Integer.valueOf(arrayOfString[0]).intValue() - 1, Integer.valueOf(arrayOfString[1]).intValue() - 1);
    } else {
      newIndexValues(this.indexCurrentSection, Integer.valueOf(arrayOfString[0]).intValue() - 1);
    }
    return this.currentQuestion;
  }
  public boolean isSurveyEnd() {
    int i = this.survey.getSections().size() - 1;
    return (this.indexCurrentSection == i) && (this.indexCurrentQuestion == ((Section)this.survey.getSections().get(i)).getListQuestion().size() - 1);
  }
  public Question jumpQuestion(Question paramQuestion) {
    Question localQuestion = null;
    int i = 0;
    Iterator localIterator = paramQuestion.getListMultipleOptions().iterator();
    while (localIterator.hasNext())
    {
      multipleOption localmultipleOption = (multipleOption)localIterator.next();
      if ((localmultipleOption.getSelected()) && (localmultipleOption.getGoto().compareTo(SURVEY_NEXT_BY_ANSWER) != 0))
      {
        localQuestion = gotoQuestion(localmultipleOption.getGoto());
        i = 1;
      }
    }
    if (i == 0) {
      localQuestion = getNextQuestion();
    }
    return localQuestion;
  }

  private Question getNextQuestion() {
    if (this.indexCurrentQuestion < -1 + this.currenSection.getListQuestion().size())
    {
      this.indexCurrentQuestion = (1 + this.indexCurrentQuestion);
      newValues();
    }
    else if (this.indexCurrentSection < -1 + this.survey.getSections().size())
    {
      this.indexCurrentSection = (1 + this.indexCurrentSection);
      this.indexCurrentQuestion = 0;
      newValues();

    }
    return this.currentQuestion;
  }

  public Question nextQuestion() {

    currentQuestion.setQuestionAnswered(true);
    if (isSurveyEnd()) {
      //fin de la encuesta
      return null;
    }

    if (currentQuestion.getType().compareTo(SURVEY_TYPE_MULTIPLE_OPTION) == 0) {

      if (endQuestion(currentQuestion)) {
        //fin de la encuesta
        return null;
      }
      currentQuestion = jumpQuestion(currentQuestion);


      return currentQuestion;
    }

    currentQuestion = getNextQuestion();
    return currentQuestion;
  }

  public String getTitleSurvey(){
    return this.survey.getTitle();
  }


  public Survey getSurvey() {
    return this.survey;
  }



}
