package uk.ac.brighton.uni.ab607.blackjack;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 * Custom window that supports double buffering
 * and with custom close operation
 * 
 * @author Almas
 * @version 1.2
 * 
 * v 1.1 - added support for general type applications
 * v 1.2 - showOptionDialog, showMessage
 */
@SuppressWarnings("serial")
public abstract class DoubleBufferWindow extends JFrame {
    
    /**
     * Wrappers for JOptionPane dialog response options
     */
    protected static final int DIALOG_YES = JOptionPane.YES_OPTION,
                            DIALOG_NO = JOptionPane.NO_OPTION,
                            DIALOG_CANCEL = JOptionPane.CANCEL_OPTION,
                            DIALOG_CLOSED = JOptionPane.CLOSED_OPTION;

    /**
     * Width and Height of the window
     */
    private final int W, H;
    
    /**
     * Constructs a window with given parameters
     * 
     * When clicking on the window 'X' button
     * user will be prompted with a yes/no/cancel dialog    
     * 
     * @param width
     * @param height
     * @param windowTitle
     */
    public DoubleBufferWindow(int width, int height, String windowTitle) { 
        W = width; H = height;
        setSize(W, H);
        setTitle(windowTitle);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                int reply = JOptionPane.showOptionDialog(DoubleBufferWindow.this,
                                "Do you really want to exit?",
                                "Exit application?",
                                JOptionPane.YES_NO_CANCEL_OPTION,
                                JOptionPane.QUESTION_MESSAGE,
                                null, null, null);
                
                switch (reply) {  
                    case JOptionPane.YES_OPTION:
                        DoubleBufferWindow.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                        break;
                        
                    case JOptionPane.NO_OPTION: //FALLTHRU
                    case JOptionPane.CANCEL_OPTION: //FALLTHRU
                    default:
                        break;
                }
            }
        });
        setVisible(true);
    }

    /**
     * Called to create (not draw on screen!) the current state of the game
     * @param g - Graphics context to use
     */
    protected abstract void renderPicture(Graphics2D g);
    
    /**
     * Called by repaint
     */
    @Override
    public void update(Graphics g) {
        drawPicture((Graphics2D) g);
    }

    /**
     * When first shown or damaged
     */
    @Override
    public void paint(Graphics g) {
        drawPicture((Graphics2D) g);
    }

    /**
     * Double buffer Image
     */
    private BufferedImage doubleBufferImage;
    
    /**
     * Double buffer (off-screen) Graphics
     */
    private Graphics2D doubleBufferGraphics;

    /**
     * Double buffer drawing to avoid flicker
     * by creating current picture but showing 
     * (drawing to screen) the previous one
     * @param g - graphics context
     */
    private void drawPicture(Graphics2D g) { 
        if (doubleBufferGraphics == null) {
            doubleBufferImage = (BufferedImage) createImage(W, H);
            doubleBufferGraphics = doubleBufferImage.createGraphics();
        }
        renderPicture(doubleBufferGraphics);           
        g.drawImage(doubleBufferImage, 0, 0, this);
    }
    
    /**
     * Creates and shows <b>input</b> dialog with given message and title
     * and provides with given options. 
     * 
     * If no options are provided any user input will be valid
     * 
     * @param message dialog message
     * @param title dialog title
     * @param options selectable values
     * @return user chosen option/typed value or null if dialog was cancelled
     */
    public Object showInputDialog(String message, String title, Object... options) {
        return JOptionPane.showInputDialog(this, message, title, JOptionPane.PLAIN_MESSAGE,
                null, options.length > 0 ? options : null, options.length > 0 ? options[0] : null);
    }
    
    /**
     * Creates and shows <b>option</b> dialog with given message and title
     * and provides with yes/no/cancel options if options not specified
     * 
     * @param message dialog message
     * @param title dialog title
     * @param options to replace yes/no/cancel, or leave them if null
     * @return 0 if user chose yes(1st option)
     *          1 if user chose no(2nd option)
     *          2 if user chose cancel(3rd option)
     *          -1 if user closed dialog
     */
    public int showOptionDialog(String message, String title, Object... options) {
        return JOptionPane.showOptionDialog(this, message, title, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,
                    null, options.length > 0 ? options : null, options.length > 0 ? options[0] : null);
    }
    
    /**
     * Displays a simple message on this window
     * with only 'OK' option
     *  
     * @param message the message to display
     */
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message);
    }
}
