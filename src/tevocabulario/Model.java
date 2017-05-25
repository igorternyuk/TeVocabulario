/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import tevocabulario.Utils.LanguageMode;

public class Model implements ConstantModel{
    private final int NUM_QUESTIONS = 100;
    private final int NUM_ANSWER_OPTIONS = 4;
    private ArrayList<String> english;
    private ArrayList<String> german;
    private ArrayList<String> spanish;
    private ArrayList<String> russian;
    private final ArrayList<String> answers;
    private int indexCorrectAnswer = 0;
    private int numCorrectAnswers = 0;
    private int numIncorrectAnswers = 0;
    private int questionNumber = 0;
    private LanguageMode langMode = LanguageMode.ENGLISH_RUSSIAN;
    private String currentWord = new String();
    private final Random rnd;
    private final ArrayList<ViewUpdater> views;
    public Model(){
        try {
                english = new ArrayList<>();
                readFromFile("dict/en.txt", english);
                System.out.println("english.size() = " + english.size());
                german = new ArrayList<>();
                readFromFile("dict/de.txt", german);
                System.out.println("german.size() = " + german.size());
                spanish = new ArrayList<>();
                readFromFile("dict/es.txt", spanish);
                System.out.println("spanish.size() = " + spanish.size());
                russian = new ArrayList<>();
                readFromFile("dict/ru.txt", russian);
                System.out.println("russian.size() = " + russian.size());
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
        answers = new ArrayList<>();
        for(int i = 0; i < NUM_ANSWER_OPTIONS; ++i){
            answers.add("");
        }
        rnd = new Random();
        views = new ArrayList<>();
    }       
   
    public void setMode(LanguageMode mode){
        this.langMode = mode; 
        reset();
    }
    
    public void reset(){
        indexCorrectAnswer = 0;
        numCorrectAnswers = 0;
        numIncorrectAnswers = 0;
        questionNumber = 0;
        newQuestion();
        updateAllViews();
    }

    public void newQuestion() {
        switch (langMode) {
            case ENGLISH_RUSSIAN :
                prepareAnswers(english, russian);
                break;
            case ENGLISH_GERMAN :
                prepareAnswers(english, german);
                break;
            case ENGLISH_SPANISH :
                prepareAnswers(english, spanish);
                break;
            case SPANISH_ENGLISH :
                prepareAnswers(spanish, english);
                break;
            case SPANISH_GERMAN :
                prepareAnswers(spanish, german);
                break;
            case SPANISH_RUSSIAN :
                prepareAnswers(spanish, russian);
                break;
            case GERMAN_ENGLISH:
                prepareAnswers(german, english);
                break;
            case GERMAN_RUSSIAN:
                prepareAnswers(german, russian);
                break;
            case GERMAN_SPANISH:
                prepareAnswers(german, spanish);
                break;
            case RUSSIAN_ENGLISH:
                prepareAnswers(russian, english);
                break;
            case RUSSIAN_SPANISH:
                prepareAnswers(russian, spanish);
                break;
            case RUSSIAN_GERMAN:
                prepareAnswers(russian, german);
                break;
        }
        updateAllViews();
    }
   
    private void fillAnswersBySpaces() {
        for (int i = 0; i < answers.size(); ++i) {
            answers.set(i, "");
        }
    }
    
    private boolean isTestPassed(){
        return questionNumber > NUM_QUESTIONS;
    }
    
    private void prepareAnswers(ArrayList<String> wordsLang1, ArrayList<String> wordsLang2){
        if(!isTestPassed()){
            //Очищаем список ответов
            fillAnswersBySpaces();
            //Выбираем случайным образом слово из списка на первом языке
            int indexWord = rnd.nextInt(wordsLang1.size());
            currentWord = wordsLang1.get(indexWord);
            //Вытаскиваем правильный ответ из списка на втором языке
            String correctAnswer = wordsLang2.get(indexWord);
            //Выбираем случайным образом какой из четырех ответов будет правильным
            indexCorrectAnswer = rnd.nextInt(NUM_ANSWER_OPTIONS);
            answers.set(indexCorrectAnswer, correctAnswer);
            //Выбираем случайным образом три неповторяющихся неправильных ответа
            for (int i = 0; i < NUM_ANSWER_OPTIONS; ++i) {
                if(i == indexCorrectAnswer) continue;
                String randAnswer = wordsLang2.get(rnd.nextInt(wordsLang2.size()));
                while (answers.contains(randAnswer)) {
                    randAnswer = wordsLang2.get(rnd.nextInt(wordsLang2.size()));
                }
                //Если нашли еще не имеющийся у нас в списке неправильный ответ
                //то добавляем его в наш список из четырех вариантов ответов
                answers.set(i,randAnswer);
            }
            //Инкрементируем номер вопроса
            ++questionNumber;
        }
    }
    
    public boolean answer(int index){
        boolean result;
        if(index == indexCorrectAnswer){
            ++numCorrectAnswers;
            result = true;
        } else {
            ++numIncorrectAnswers;
            result = false;
        }
        updateAllViews();
        return result;
    }
    
    @Override
    public int getQuestionNumber() {
        return isTestPassed() ? NUM_QUESTIONS : questionNumber;
    }
    
    @Override
    public int getTotalQuestions(){
        return NUM_QUESTIONS;
    }
    
    @Override
    public String getWord() {
        return isTestPassed() ? (numCorrectAnswers > 0.75 * NUM_QUESTIONS ? 
               "Test was passed" : "Test failed") : currentWord;
    }

    @Override
    public String getAnswer(int index) {
        return isTestPassed() ? "" : answers.get(index);
    }

    @Override
    public int getCorrectAnswerIndex() {
        return indexCorrectAnswer;
    }
    
    @Override
    public int numCorrectAnswers() {
        return numCorrectAnswers;
    }

    @Override
    public int numIncorrectAnswers() {
        return numIncorrectAnswers;
    }
    
    public void addView(ViewUpdater view){
        views.add(view);
    }
    
    public void removeView(ViewUpdater view){
        views.remove(view);
    }
    
    private void updateAllViews(){
        for(ViewUpdater view : views){
            view.updateView();
        }
    }
    
    private void readFromFile(String pathToFile, ArrayList<String> list) throws UnsupportedEncodingException {
        try (InputStream in = getClass().getResourceAsStream(pathToFile);
             BufferedReader br = new BufferedReader(new InputStreamReader(in, "UTF-8"));) {
            String str;
            while ((str = br.readLine()) != null) {
                list.add(str);
            }
        } catch (IOException ex) {
            Logger.getLogger(Model.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
