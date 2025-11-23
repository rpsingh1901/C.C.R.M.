package edu.ccrm.util;

public class DiamondDemo implements Alpha, Beta {
    // Diamond default-method resolution by explicit override
    @Override public String info(){ return "Alpha+Beta"; }

    public void printInfo(){ System.out.println("Diamond demo: " + info()); }
}
