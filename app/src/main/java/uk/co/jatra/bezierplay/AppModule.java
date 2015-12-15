package uk.co.jatra.bezierplay;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by tim on 13/12/2015.
 */
@Module
public class AppModule {

    private static Class paintFactoryClass = PaintFactory.class;

    @Provides @Singleton
    public PaintFactory providesPaintFactory() {
        PaintFactory paintFactory = null;
        try {
            paintFactory = (PaintFactory) paintFactoryClass.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return paintFactory;
    }

    public static void setPaintFactoryClass(Class paintFactoryClass) {
        AppModule.paintFactoryClass = paintFactoryClass;
    }
}
