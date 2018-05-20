package xyz.johansson.simplesearchengine;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class Application {

    /**
     * Main method.
     *
     * @param args not used
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector();
        CLI cli = injector.getInstance(CLI.class);
        cli.run();
    }
}
