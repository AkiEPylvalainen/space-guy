package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.swing.JFrame;
import javax.swing.JPanel;

public abstract class GameApp implements Runnable {
    
    protected boolean running;
    protected Thread thread;
    protected JFrame frame;
    protected JPanel panel;
    protected BufferedImage _image;
    protected Graphics2D _g;
    
    public GameApp(String title, int width, int height, int scale) {
        Dimension dim = new Dimension((int)(width * scale), (int)(height * scale));
        
        panel = new JPanel();
        panel.setPreferredSize(dim);
        panel.setFocusable(true);
        panel.requestFocus();
        
        frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(panel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        
        _image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        
        _g = (Graphics2D)_image.getGraphics();
        _g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        _g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        
        init();
    }
    
    public void start() {
        if(thread == null) {
            panel.addKeyListener(new KeyInput());
            running = true;
            thread = new Thread(this);
            thread.start();
        }
    }
    
    @Override
    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;
        
        while(running) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            while(delta >= 1) {
                update(1.0/60 * 1000);
                Input.reset();
                delta--;
            }
            
            _g.setColor(Color.black);
            _g.fillRect(0, 0, panel.getWidth(), panel.getHeight());
        
            draw(_g);
            
            Graphics g2 = panel.getGraphics();
            g2.drawImage(_image, 0, 0, panel.getWidth(), panel.getHeight(), null);
            g2.dispose();
        }
    }
    
    public abstract void init();
    
    public abstract void update(double deltaTime);
    
    public abstract void draw(Graphics2D g);
    
    class KeyInput extends KeyAdapter {
        
        @Override
        public void keyPressed(KeyEvent e) {
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                System.exit(0);
            }
            
            Input.keyPressed(e);
        }

        @Override
        public void keyReleased(KeyEvent e) {
            Input.keyReleased(e);
        }
    }
}
