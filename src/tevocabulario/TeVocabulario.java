/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

public class TeVocabulario {
    public void go() {
        Model model = new Model();
        Controller controller = new Controller(model);
        View view = new View(model, controller);
        model.addView(view);
    }
    public static void main(String[] args) {
        TeVocabulario vocabulario = new TeVocabulario();
        vocabulario.go();
    }
}
