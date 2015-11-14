package adf;

import adf.launcher.AgentConnector;

public class Main {
    public static void main(String... args) {
        try {
            AgentConnector connector = new AgentConnector(args);
            connector.start();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            System.out.println("[ERROR ] Loader not found.");
        }
    }
}