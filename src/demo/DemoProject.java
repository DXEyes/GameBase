/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package demo;

import gamebase.GameFrame;

/**
 *
 * @author vcowal
 */
public class DemoProject {
    //running this file should do the trick
    public static void main(String[] args) {
        GameFrame g=new GameFrame(new DemoLoop(320,240),"Demo Project",1000);
        //g.setSize(640, 480);
        g.setFullScreen(true);
        g.Run();
    }
}
