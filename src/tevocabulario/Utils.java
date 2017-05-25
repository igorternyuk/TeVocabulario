/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

public class Utils {
    public enum LanguageMode{
        ENGLISH_RUSSIAN("English-Russian"),
        ENGLISH_GERMAN("English-German"),
        ENGLISH_SPANISH("English-Spanish"),
        SPANISH_ENGLISH("Spanish-English"),
        SPANISH_GERMAN("Spanish-German"),
        SPANISH_RUSSIAN("Spanish-Russian"),
        GERMAN_ENGLISH("German-English"),
        GERMAN_RUSSIAN("German-Russian"),
        GERMAN_SPANISH("German-Spanish"),
        RUSSIAN_ENGLISH("Russian-English"),
        RUSSIAN_SPANISH("Russian-Spanish"),
        RUSSIAN_GERMAN("Russian-German");
        private final String description;
        public String getDescription(){
            return description;
        }
        private LanguageMode(String description){
            this.description = description;
        }
    }
}
