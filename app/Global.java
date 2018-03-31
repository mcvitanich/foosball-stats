import lombok.SneakyThrows;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import spring.SpringApplicationContext;


public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
        Logger.info("Starting up the application...");
        SpringApplicationContext.initialize();
    }

    @Override
    @SneakyThrows
    public void onStop(Application app) {
        super.onStop(app);
    }

    @Override
    public <C> C getControllerInstance(Class<C> clazz) {
        return SpringApplicationContext.getBean(clazz);
    }

}
