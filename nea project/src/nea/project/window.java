/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nea.project;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFrame;

/**
 *
 * @author 15choasi
 */
public class window extends JFrame implements MouseWheelListener, MouseListener, MouseMotionListener {

    private final int Max_iter = 570;
    private final double zoom = 250;
    private double zoomfactor = 300;
    private double prevzoomfactor = 1;
    private BufferedImage Image;
    private double zx, zy, cX, cY, tmp;
    private final int colour = 20;
    int mf = 35;
    int w = 800;
    int h = 600;
    private double xOffset=0;
    private double yOffset =0 ;
    private int xDiff;
    private int yDiff;
    private Point startPoint;
    
    private boolean zoomer, dragger, released;

    public window() {
        super("Mandelbrot Set");
        setBounds(485, 100, 800, 600);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        

        JButton J = new JButton("show Pallete editor");
        Container box = Box.createVerticalBox();
        box.add(J);
        add(box);
        J.setActionCommand("editor");
        ActionListener l = new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                String action = ae.getActionCommand();
                if (action.equals("editor")) {
                    new editor().setVisible(true);
                }
            }
        };
        J.addActionListener(l);

        Image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                zx = zy = 0;
                cX = (x - 500) / zoom;
                cY = (y - 300) / zoom;
                int iter = Max_iter;
                while (zx * zx + zy * zy < 4 && iter > 0) {
                    tmp = zx * zx - zy * zy + cX;
                    zy = 2.0 * zx * zy + cY;
                    zx = tmp;
                    iter--;
                    
                }
                Image.setRGB(x, y, iter | (iter << mf));
            } 
        }
        
        
       
        
    }
    
    

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
    if (zoomer) {
        AffineTransform at = new AffineTransform();
        
        double xRel = MouseInfo.getPointerInfo().getLocation().getX() - getLocationOnScreen().getX();
        double yRel = MouseInfo.getPointerInfo().getLocation().getY() - getLocationOnScreen().getY();

        double zoomDiv = zoomfactor / prevzoomfactor;

        xOffset = (zoomDiv) * (xOffset) + (1 - zoomDiv) * xRel;
        yOffset = (zoomDiv) * (yOffset) + (1 - zoomDiv) * yRel;
        
        
        at.scale(zoomfactor, zoomfactor);
        prevzoomfactor = zoomfactor;
        g2.transform(at);
        zoomer = false;
    }
    if(dragger){
        AffineTransform at = new AffineTransform();
            at.translate(xOffset + xDiff, yOffset + yDiff);
            at.scale(zoomfactor, zoomfactor);
            g2.transform(at);

            if (released) {
                xOffset += xDiff;
                yOffset += yDiff;
                dragger = false;
            }

        }
        
        g.drawImage(Image, 0, 0, this);
        
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        zoomer = true;

        //Zoom in
        if (e.getWheelRotation() < 0) {
            zoomfactor *= 1.1;
            repaint();
        }
        //Zoom out
        if (e.getWheelRotation() > 0) {
            zoomfactor /= 1.1;
            repaint();
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
      
    }

    @Override
    public void mousePressed(MouseEvent e) {
        released = false;
        startPoint = MouseInfo.getPointerInfo().getLocation();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        released = true;
        repaint(); 
    }

    @Override
    public void mouseEntered(MouseEvent e) {
       
    }

    @Override
    public void mouseExited(MouseEvent e) {
      
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Point curPoint = e.getLocationOnScreen();
        xDiff = curPoint.x - startPoint.x;
        yDiff = curPoint.y - startPoint.y;

        dragger = true;
        repaint(); 
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        System.out.println("l");
    }

}
