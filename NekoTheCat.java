/* Tyler King
 * Neko The Cat Program
 * Lab 07
 * Spring 2016
 */

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.applet.*;             
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.*;          

public class NekoTheCat implements MouseListener, Runnable  
{

public static void main(String[] args)     
	  {
	  new NekoTheCat(); 
	  }
	
JFrame window     = new JFrame("Neko The Cat");

JPanel panel      = new JPanel(); 

Graphics g;                       

Image  redBall    = Toolkit.getDefaultToolkit().getImage(getClass().getResource("red-ball.gif"));
Image  nekoRight1 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("neko1.gif"));
Image  nekoRight2 = Toolkit.getDefaultToolkit().getImage(getClass().getResource("neko2.gif"));
Image  nekoLeft1  = Toolkit.getDefaultToolkit().getImage(getClass().getResource("neko3.gif"));
Image  nekoLeft2  = Toolkit.getDefaultToolkit().getImage(getClass().getResource("neko4.gif"));
Image  currentImage = nekoRight1; 
Image  cat1       = nekoRight1;
Image  cat2       = nekoRight2;
AudioClip soundFile = Applet.newAudioClip(getClass().getResource("spacemusic.au"));

int     catxPosition  = 1;
int     catyPosition  = 50;
int     lastCatxPosition;
int     lastCatyPosition;
int     lastImageWidth;
int     lastImageHeight;
int     ballxPosition = 0;
int     ballyPosition = 0;
int     sleepTime     = 100;
int     xBump         = 10; 
boolean catIsRunningToTheRight = true;
boolean catIsRunningToTheLeft  = false;
boolean ballHasBeenPlaced      = false;

public NekoTheCat()  
  {
  panel.setBackground(Color.white); 
  window.getContentPane().add(panel); 
  window.setSize(600, 600); 
  window.setVisible(true); 

  window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  
  soundFile.play();
 
  panel.addMouseListener(this); // call me!
  
  g = panel.getGraphics(); 
  
  g.drawImage(nekoRight1,0,0,panel);
  g.drawImage(nekoRight2,0,0,panel);
  g.drawImage(nekoLeft1, 0,0,panel);
  g.drawImage(nekoLeft2, 0,0,panel);
  g.drawImage(redBall,   0,0,panel);
  
  showInstructions();

  new Thread(this).start();
  } 

public void showInstructions()
  {
// Show game instructions on screen
  g = panel.getGraphics();	
  g.setFont(new Font("Times Roman", Font.BOLD, 20));
  g.drawString("Neko the cat is looking for it's red ball!",100,100);
  g.drawString("Click the mouse to place Neko's ball."     ,100,120);
  g.drawString("Can you move the ball to keep Neko from getting it?"  ,100,140);
  g.drawString("(Pull window larger to make game easier)"  ,100,160);
  }

public void run()
{  
soundFile.play();

while(true)
  {	
   
  while ((catxPosition > 0) && 
	     (catxPosition < panel.getSize().width)) 
        {

	    g = panel.getGraphics();
	    g.setColor(Color.WHITE);

        // 1. Blank out the last image
        g.fillRect(lastCatxPosition, lastCatyPosition, lastImageWidth, lastImageHeight);
	    
        // 3. Select the next image
        if(currentImage == cat1)
           currentImage =  cat2;
         else
           currentImage =  cat1;
       
        // 4. Draw the next cat image 
        catxPosition += xBump; 
        g.drawImage(currentImage, catxPosition, catyPosition, panel); 
     
     	// 2. Bump location for new image
        lastCatxPosition = catxPosition;
        lastCatyPosition = catyPosition;
        lastImageWidth   = currentImage.getWidth(panel);
        lastImageHeight  = currentImage.getHeight(panel);
        
        // 5. Pause briefly to let human eye see the new image!
        try {Thread.sleep(sleepTime);} 
        catch (InterruptedException e) {}
 
        // 6. If necessary, redirect the cat toward the ball.
        if (ballHasBeenPlaced)
           {
           if (catyPosition > ballyPosition  ) 
     	       catyPosition -= 10;            
           if (catyPosition < ballyPosition  ) 
     	       catyPosition += 10;             
      
           if ((ballxPosition > (catxPosition + currentImage.getWidth(panel)) 
            && catIsRunningToTheLeft))
        	    reverseFromLeftToRight();  
        	    
           if ((ballxPosition < catxPosition)
            && (catIsRunningToTheRight))
                reverseFromRightToLeft();    
          }
        
        // 7. Test to see if Neko is "at" the ball
        if ((Math.abs(catyPosition - ballyPosition) < 10)   // y within 10   
         && (Math.abs(catxPosition - ballxPosition) < 10))  // x within 10 pixels
           {
           panel.removeMouseListener(this);
           g.setColor(Color.RED);
           g.setFont(new Font("Times Roman", Font.BOLD, 50));
           g.drawString("At last, I have my ball!", 50, 50);
           try {Thread.sleep(5000);}
           catch (InterruptedException e) {}
           window.dispose();  
               	
           }
        } 
    
    if (catxPosition > panel.getSize().width)
       {
       reverseFromRightToLeft();
       catxPosition = panel.getSize().width -1;
       }
    
    if (catxPosition < 0)
       {
       reverseFromLeftToRight();
       catxPosition = 1;
       }
    } 
} 

private void reverseFromRightToLeft()
     {
     xBump = -xBump;
     cat1 = nekoLeft1;
     cat2 = nekoLeft2;
     catIsRunningToTheLeft  = true;
     catIsRunningToTheRight = false;
     soundFile.play();
     }

private void reverseFromLeftToRight()
     {
     xBump = -xBump;	
     cat1 = nekoRight1;
     cat2 = nekoRight2;
     catIsRunningToTheRight = true;
     catIsRunningToTheLeft  = false;
     soundFile.play();
     }

public void mouseClicked(MouseEvent me)
   {
   ballHasBeenPlaced = true;
   g.fillRect(ballxPosition, ballyPosition, redBall.getWidth(panel), redBall.getHeight(panel));
   ballxPosition = me.getX();
   ballyPosition = me.getY();
   g.drawImage(redBall, ballxPosition, ballyPosition, panel);
   }
   
public void mouseEntered(MouseEvent arg0)
   {}
public void mouseExited(MouseEvent arg0)
   {}
public void mousePressed(MouseEvent arg0)
   {}
public void mouseReleased(MouseEvent arg0)
   {}
} 
