/**
 * Java. TeVocabulario
 *
 * @author Ternyuk Igor
 * @version 1.0 dated May 23, 2017
 */
package tevocabulario;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import tevocabulario.Utils.LanguageMode;

public class View implements ViewUpdater{
    private final ConstantModel model;
    private final Controller controller;
    private final String TITLE_OF_PROGRAM = "TeVocabulario";
    private final int WINDOW_WIDTH = 1000, WINDOW_HEIGHT = 500;
    private final Font font;
    private final Font fontSmall;  
    private final Font fontBtn;
    private final JFrame window;
    private final JMenuBar menuBar;
    private final JMenu actionMenu;
    private final JMenu settingsMenu;
    private final JMenu nextMenu;
    private final Canvas mainPanel;
    private final JLabel lblWord;
    private final JPanel btnPanel;
    private final Dimension btnDim;
    private final ArrayList<JButton> btnAnswers = new ArrayList<>();
    private final JButton btnNext;
    private final JButton btnNew;
    private final LanguageMode[] langModes = {
        LanguageMode.ENGLISH_RUSSIAN,
        LanguageMode.ENGLISH_GERMAN,
        LanguageMode.ENGLISH_SPANISH,
        LanguageMode.SPANISH_ENGLISH,
        LanguageMode.SPANISH_GERMAN,
        LanguageMode.SPANISH_RUSSIAN,
        LanguageMode.GERMAN_ENGLISH,
        LanguageMode.GERMAN_RUSSIAN,
        LanguageMode.GERMAN_SPANISH,
        LanguageMode.RUSSIAN_ENGLISH,
        LanguageMode.RUSSIAN_SPANISH,
        LanguageMode.RUSSIAN_GERMAN
    };

    public View(ConstantModel model, Controller controller) {
        this.model = model;
        this.controller = controller;
        font = new Font("Arial", Font.BOLD, 80);
        fontSmall = new Font("Arial", Font.PLAIN, 50);
        fontBtn = new Font("Arial", Font.PLAIN, 25);
        window = new JFrame(TITLE_OF_PROGRAM);
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setLocationRelativeTo(null);
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setFocusable(true);
        //Menu creation
        menuBar = new JMenuBar();
        actionMenu = new JMenu("Action");
        JMenuItem item1 = new JMenuItem("New test");
        item1.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                controller.newTest();
            }
        });
        JMenuItem item2 = new JMenuItem("Quit");
        item2.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                System.exit(0);
            }
        });
        actionMenu.add(item1);
        actionMenu.addSeparator();
        actionMenu.add(item2);
        settingsMenu = new JMenu("Language settings");
        ButtonGroup bg = new ButtonGroup();
        JMenuItem[] rb_items = new JRadioButtonMenuItem[12];
        for(int i = 0; i < rb_items.length; ++i){
            String name = langModes[i].getDescription();
            JRadioButtonMenuItem rb_item = new JRadioButtonMenuItem(name);
            bg.add(rb_item);
            if(langModes[i] == LanguageMode.ENGLISH_RUSSIAN){
                rb_item.setSelected(true);
            }
            rb_item.addMouseListener(new MyMouseAdapter(i));
            settingsMenu.add(rb_item);
        } 
        nextMenu = new JMenu("Question");
        JMenuItem item5 = new JMenuItem("Next");
        item5.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                controller.nextQuestion();
                restoreButtonsColor();
            }
        });
        nextMenu.add(item5);
        menuBar.add(actionMenu);
        menuBar.add(settingsMenu);
        menuBar.add(nextMenu);
        window.setJMenuBar(menuBar);
        mainPanel = new Canvas();
        //mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(Color.WHITE);
        lblWord = new JLabel("Press 'New test'");
        lblWord.setFont(font);
        lblWord.setForeground(Color.BLUE.brighter());
        mainPanel.add(lblWord);
        btnDim = new Dimension(350, 60);
        btnPanel = new JPanel();
        btnPanel.setLayout(new GridLayout(3, 2));
        for(int i = 0; i < 4; ++i){
            JButton btn = new JButton();
            btn.setPreferredSize(btnDim);
            btn.setFont(fontBtn);
            btnAnswers.add(btn);
            btnPanel.add(btn);
            btn.addMouseListener(new MouseAdapter(){
                @Override
                public void mouseReleased(MouseEvent e){
                    if(controller.answer(btnAnswers.indexOf(e.getComponent()))){
                        e.getComponent().setBackground(Color.GREEN);
                    }else{
                        e.getComponent().setBackground(Color.RED);
                        btnAnswers.get(model.getCorrectAnswerIndex()).setBackground(Color.GREEN);
                    }
                }
            });
        }
        restoreButtonsColor();
        btnNext = new JButton("Next question");
        btnNext.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
        btnNext.setForeground(Color.BLUE);
        btnNext.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                controller.nextQuestion();
                restoreButtonsColor();
            }
        });
        btnNew = new JButton("New test");
        btnNew.setFont(new Font("Arial", Font.BOLD | Font.ITALIC, 22));
        btnNew.setForeground(Color.BLUE);
        btnNew.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseReleased(MouseEvent e){
                controller.newTest();
                restoreButtonsColor();
            }
        });
        btnPanel.add(btnNew);        
        btnPanel.add(btnNext);
        
        restoreButtonsColor();
        window.getContentPane().add(BorderLayout.CENTER, mainPanel);
        window.getContentPane().add(BorderLayout.SOUTH, btnPanel);
        window.setVisible(true);
    }
    
    private void restoreButtonsColor() {
        for (JButton btn : btnAnswers) {
            btn.setBackground(Color.WHITE.darker());
        }
    }
    
    private class MyMouseAdapter extends MouseAdapter {
        private final int index;
        
        public MyMouseAdapter(int index) {
            this.index = index;
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            controller.setLanguageMode(langModes[index]);
            restoreButtonsColor();
        }
    }

    @Override
    public void updateView() {
        for (int i = 0; i < btnAnswers.size(); ++i) {
            btnAnswers.get(i).setText(model.getAnswer(i));
        }
        lblWord.setText(model.getWord());
        mainPanel.repaint();
    }
    
    public void drawInfo(Graphics2D g) {
        g.setFont(fontSmall);
        g.setColor(Color.BLUE);
        g.drawString("Questions:" + model.getQuestionNumber() + "/" + model.getTotalQuestions(), 300, 160);
        g.setColor(Color.GREEN.darker().darker());
        g.drawString("Correct answers:" + model.numCorrectAnswers(), 300, 210);
        g.setColor(Color.RED);
        g.drawString("Incorrect answers:" + model.numIncorrectAnswers(), 300, 260);
    }
    
    private class Canvas extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);
            drawInfo((Graphics2D)g);
        }
    }
}
