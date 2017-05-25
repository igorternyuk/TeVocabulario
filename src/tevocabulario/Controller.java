/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

import tevocabulario.Utils.LanguageMode;

public class Controller {
    Model model;    
    public Controller(Model model){
        this.model = model;
    }
    
    public void setLanguageMode(LanguageMode lmode){
        model.setMode(lmode);
    }
    
    public void newTest(){
        model.reset();
    }
    
    public boolean answer(int index){
        return model.answer(index);
    }
    
    public void nextQuestion(){
        model.newQuestion();
    }
    
    
}
