/*
 * (C) Copyright 2022 VeriSoft (http://www.verisoft.co)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package co.verisoft.fw.async;

import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


/**
 * A listener to enable asynchronous operations. The listener is a Selenium
 * WebDrvier based listener which implements both the Junit 5 listener design
 * and the observer design pattern.<br>
 * <br>
 * <b>VeriSoft's framework asynchronous operation model explained:</b><br>
 * Since WebDriver is a single thread application, and it's operations are not
 * thread safe, a solution is needed to async operations within a single thread.
 * <br>
 * org.openqa.selenium. @see
 * JavascriptExecutor#executeAsyncScript(String, Object...) method does not
 * provide a solution since the js is indeed executed asynchronously, however
 * WebDriver does not, hence it is hanged while it waits for the JS to finish
 * it's execution.<br>
 * So, the current solution is based on the JS model of running async operations
 * on the same thread. It is async rather than paralel. In order to run
 * operations in an async way, breaking points during the execution are needed.
 * The most common breaking point in a Selenium WebDriver scenario is the
 * findElement operation. It is the most common operation and the most
 * frequently used operation. So... We hooked on, using the listener and
 * EventFiringWebDriver mechnism to hook in before every single findElement
 * method is invoked.<br>
 * In order to to not overdue, a time interval was introduced, so if <br>
 * a. a findElement method was invoked, and <br>
 * b. interval has elapsed, a method will be called. <br>
 * We use the observer pattern to manage all the async code in one place. If you
 * are not familiar with the observer patter,
 * <a href="https://en.wikipedia.org/wiki/Observer_pattern">visit the wikipedia</a>
 * site. Essentially, this class serves as both listener, which
 * implements the SearchingEventListener interface, and subject (from the
 * observer mechanism), which implements the Subject interface.
 *
 * @author <a href="mailto:nir@verisoft.co">Nir Gallner</a> @ <a href="http://www.verisoft.co">www.VeriSoft.co</a>
 * @since 2.0.1
 */
@Slf4j
public class AsyncListenerImp implements AsyncExecutor.AsyncListener {

    private LocalDateTime baseTime;
    private Duration interval;

    private final List<Observer> observers = new ArrayList<>();

    public AsyncListenerImp(Duration interval) {
        this.interval = interval;
        this.setDispatchInterval(interval);
        baseTime = LocalDateTime.now();
    }

    public AsyncListenerImp() {
        interval = Duration.ofSeconds(5);
        baseTime = LocalDateTime.now();
    }

    /**
     * Setter for the dispatch interval and the time unit. The defailt valie of the
     * dispatcher is 5 second and it is the minimum dispatcher possible. If tried to
     * set less than 5 second, setter will not update the values
     *
     * @param duration new interval for invocation
     */
    public void setDispatchInterval(Duration duration) {
        LocalDateTime t = LocalDateTime.now();
        LocalDateTime t1 = t.plus(duration);
        if (t1.minus(Duration.ofSeconds(5)).isBefore(t)) {
            interval = Duration.ofSeconds(5);
        } else {
            interval = duration;
        }

    }

    /**
     * Getter
     *
     * @return interval field
     */
    public Duration getDispatchInterval() {
        return interval;
    }

    @Override
    public void register(Observer o) {
        if (log.isDebugEnabled())
            log.debug("Added a new observer to the list " + o.toString());
        this.observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        int index = observers.indexOf(o);
        if (index == -1) {
            if (log.isDebugEnabled())
                log.debug("Attempt to delete an unregistered observer " + o.toString());
            return;
        }

        if (log.isDebugEnabled())
            log.debug("Observer " + index + 1 + " Deleted");
        observers.remove(o);
    }

    /**
     * Unregisters all observers from subject
     */
    public void unregisterAll() {
        observers.forEach(this::unregister);
    }

    @Override
    public void notifyObservers() {
        observers.forEach(Observer::update);
    }

    /**
     * Loops through the observers list and looks for observers which flags
     * themselves as observers who want to unregister themselves from the subject's
     * list. Technically, the method calls observer.isDisposed() for each observer
     * on the list, and if the result is true, the method performs
     * unregister(observer)
     */
    public void collectGarbage() {
        List<Observer> observersCopy = new ArrayList<>(observers);
        observersCopy.forEach(observer -> {
            if (observer.isDisposed())
                unregister(observer);
        });
    }


    @Override
    public void beforeAnyCall(Object target, Method method, Object[] args) {
        LocalDateTime currentTime = LocalDateTime.now();
        if (currentTime.minus(interval).isAfter(baseTime)) {
            baseTime = currentTime;
            notifyObservers();
            collectGarbage();
        }
    }
}
