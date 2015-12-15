package uk.co.jatra.bezierplay;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by tim on 13/12/2015.
 */
@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    void inject(DisplayView displayView);
    void inject(Bezier bezier);
}
