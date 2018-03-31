package spring;

import static play.Play.isDev;
import static play.Play.isTest;

public enum RuntimeEnvironment {

    PROD, TEST, DEV;

    public static RuntimeEnvironment currentRuntimeEnvironment() {
        if (isTest()) {
            return TEST;
        } else if (isDev()) {
            return DEV;
        } else {
            return PROD;
        }
    }
}