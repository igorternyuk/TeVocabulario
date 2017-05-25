/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

public interface ConstantModel {
    int getQuestionNumber();
    int getTotalQuestions();
    String getWord();
    String getAnswer(int index);
    int getCorrectAnswerIndex();
    int numCorrectAnswers();
    int numIncorrectAnswers();
  
  //  double getCorrectAnswersProcentage();
}
