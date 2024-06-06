//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package io.cucumber.core.runtime;

import io.cucumber.core.eventbus.EventBus;
import io.cucumber.core.feature.FeatureParser;
import io.cucumber.core.filter.Filters;
import io.cucumber.core.gherkin.Feature;
import io.cucumber.core.gherkin.Pickle;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.cucumber.core.options.RuntimeOptions;
import io.cucumber.core.order.PickleOrder;
import io.cucumber.core.plugin.PluginFactory;
import io.cucumber.core.plugin.Plugins;
import io.cucumber.core.resource.ClassLoaders;
import io.cucumber.plugin.Plugin;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.Status;

import java.lang.reflect.Field;
import java.time.Clock;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class MyRuntime {
    private static final Logger log = LoggerFactory.getLogger(Runtime.class);
    private final ExitStatus exitStatus;
    private final Predicate<Pickle> filter;
    private final int limit;
    private final FeatureSupplier featureSupplier;
    private final ExecutorService executor;
    private final PickleOrder pickleOrder;
    public final CucumberExecutionContext context;

    private MyRuntime(ExitStatus exitStatus, CucumberExecutionContext context, Predicate<Pickle> filter, int limit, FeatureSupplier featureSupplier, ExecutorService executor, PickleOrder pickleOrder) {
        this.filter = filter;
        this.context = context;
        this.limit = limit;
        this.featureSupplier = featureSupplier;
        this.executor = executor;
        this.exitStatus = exitStatus;
        this.pickleOrder = pickleOrder;
    }

    public static Builder builder() {
        return new Builder();
    }

    public void run() {
        List<Feature> features = this.featureSupplier.get();

        this.context.runFeatures(() -> {
            this.runFeatures(features);
        });
    }

    private void runFeatures(List<Feature> features) {
        CucumberExecutionContext var10001 = this.context;
        Objects.requireNonNull(var10001);
        features.forEach(var10001::beforeFeature);
        List<Future<?>> executingPickles = (List) ((Stream) features.stream().flatMap((feature) -> {
            return feature.getPickles().stream();
        }).filter(this.filter).collect(Collectors.collectingAndThen(Collectors.toList(), (list) -> {
            return this.pickleOrder.orderPickles(list).stream();
        }))).limit(this.limit > 0 ? (long) this.limit : 2147483647L).map((pickle) -> {
            return this.executor.submit(this.executePickle(((Pickle) pickle)));
        }).collect(Collectors.toList());
        this.executor.shutdown();
        Iterator var3 = executingPickles.iterator();

        while (var3.hasNext()) {
            Future<?> executingPickle = (Future) var3.next();

            try {
                executingPickle.get();
            } catch (ExecutionException var6) {
                log.error(var6, () -> {
                    return "Exception while executing pickle";
                });
            } catch (InterruptedException var7) {
                log.debug(var7, () -> {
                    return "Interrupted while executing pickle";
                });
                this.executor.shutdownNow();
            }
        }

    }

    private Runnable executePickle(Pickle pickle) {
        return () -> {
            this.context.runTestCase((runner) -> {
                runner.runPickle(pickle);
            });
        };
    }

    public Status exitingStatus() {
        return this.exitStatus.getStatus();
    }

    public Throwable getError()  {
        List<Result> results = new ArrayList<>();
        try {
            Field resultsField = ExitStatus.class.getDeclaredField("results");
            resultsField.setAccessible(true);
            results = (List<Result>) resultsField.get(this.exitStatus);
        } catch (Exception ignored) {}
        return results.get(0).getError();


    }

    public static class Builder {
        private EventBus eventBus;
        private Supplier<ClassLoader> classLoader;
        private RuntimeOptions runtimeOptions;
        private BackendSupplier backendSupplier;
        private FeatureSupplier featureSupplier;
        private List<Plugin> additionalPlugins;
        private Predicate<Pickle> nameFilter = pickle -> true; // Default is run all

        private Builder() {
            this.classLoader = ClassLoaders::getDefaultClassLoader;
            this.runtimeOptions = RuntimeOptions.defaultOptions();
            this.additionalPlugins = Collections.emptyList();
        }

        public Builder withScenarioNames(List<String> scenarioNames) {
            nameFilter = pickle -> scenarioNames.contains(pickle.getName());
            return this;
        }

        public Builder withRuntimeOptions(RuntimeOptions runtimeOptions) {
            this.runtimeOptions = runtimeOptions;
            return this;
        }

        public Builder withClassLoader(Supplier<ClassLoader> classLoader) {
            this.classLoader = classLoader;
            return this;
        }

        public MyRuntime build() {
            ObjectFactoryServiceLoader objectFactoryServiceLoader = new ObjectFactoryServiceLoader(this.classLoader, this.runtimeOptions);
            ObjectFactorySupplier objectFactorySupplier = this.runtimeOptions.isMultiThreaded() ? new ThreadLocalObjectFactorySupplier(objectFactoryServiceLoader) : new SingletonObjectFactorySupplier(objectFactoryServiceLoader);
            BackendSupplier backendSupplier = this.backendSupplier != null ? this.backendSupplier : new BackendServiceLoader(this.classLoader, (ObjectFactorySupplier) objectFactorySupplier);
            Plugins plugins = new Plugins(new PluginFactory(), this.runtimeOptions);
            Iterator var5 = this.additionalPlugins.iterator();

            while (var5.hasNext()) {
                Plugin plugin = (Plugin) var5.next();
                plugins.addPlugin(plugin);
            }

            ExitStatus exitStatus = new ExitStatus(this.runtimeOptions);
            plugins.addPlugin(exitStatus);
            if (this.eventBus == null) {
                UuidGeneratorServiceLoader uuidGeneratorServiceLoader = new UuidGeneratorServiceLoader(this.classLoader, this.runtimeOptions);
                this.eventBus = new TimeServiceEventBus(Clock.systemUTC(), uuidGeneratorServiceLoader.loadUuidGenerator());
            }

            EventBus eventBus = SynchronizedEventBus.synchronize(this.eventBus);
            if (this.runtimeOptions.isMultiThreaded()) {
                plugins.setSerialEventBusOnEventListenerPlugins(eventBus);
            } else {
                plugins.setEventBusOnEventListenerPlugins(eventBus);
            }

            RunnerSupplier runnerSupplier = this.runtimeOptions.isMultiThreaded() ? new ThreadLocalRunnerSupplier(this.runtimeOptions, eventBus, (BackendSupplier) backendSupplier, (ObjectFactorySupplier) objectFactorySupplier) : new SingletonRunnerSupplier(this.runtimeOptions, eventBus, (BackendSupplier) backendSupplier, (ObjectFactorySupplier) objectFactorySupplier);
            ExecutorService executor = this.runtimeOptions.isMultiThreaded() ? Executors.newFixedThreadPool(this.runtimeOptions.getThreads(), new CucumberThreadFactory()) : new SameThreadExecutorService();
            Objects.requireNonNull(eventBus);
            FeatureParser parser = new FeatureParser(eventBus::generateId);
            FeatureSupplier featureSupplier = this.featureSupplier != null ? this.featureSupplier : new FeaturePathFeatureSupplier(this.classLoader, this.runtimeOptions, parser);
            Predicate<Pickle> filter = new Filters(this.runtimeOptions).and(this.nameFilter); // Apply name filter
            int limit = this.runtimeOptions.getLimitCount();
            PickleOrder pickleOrder = this.runtimeOptions.getPickleOrder();
            CucumberExecutionContext context = new CucumberExecutionContext(eventBus, exitStatus, (RunnerSupplier) runnerSupplier);
            return new MyRuntime(exitStatus, context, filter, limit, (FeatureSupplier) featureSupplier, (ExecutorService) executor, pickleOrder);
        }
    }

    private static final class SameThreadExecutorService extends AbstractExecutorService {
        private SameThreadExecutorService() {
        }

        public void execute(Runnable command) {
            command.run();
        }

        public void shutdown() {
        }

        public List<Runnable> shutdownNow() {
            return Collections.emptyList();
        }

        public boolean isShutdown() {
            return true;
        }

        public boolean isTerminated() {
            return true;
        }

        public boolean awaitTermination(long timeout, TimeUnit unit) {
            return true;
        }
    }

    private static final class CucumberThreadFactory implements ThreadFactory {
        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;

        CucumberThreadFactory() {
            this.namePrefix = "cucumber-runner-" + poolNumber.getAndIncrement() + "-thread-";
        }

        public Thread newThread(Runnable r) {
            return new Thread(r, this.namePrefix + this.threadNumber.getAndIncrement());
        }
    }
}
