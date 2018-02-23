/*
 * Open Source Software published under the Apache Licence, Version 2.0.
 */

import com.google.inject.AbstractModule;
import com.google.inject.Module;
import io.github.vocabhunter.gui.main.CoreGuiModule;
import io.github.vocabhunter.gui.main.FileDialogueFactoryImpl;
import io.github.vocabhunter.gui.main.StandardEventSourceModule;
import io.github.vocabhunter.gui.main.VocabHunterGuiExecutable;
import io.github.vocabhunter.analysis.core.GuiTaskHandler;
import io.github.vocabhunter.analysis.core.ThreadPoolToolImpl;
import io.github.vocabhunter.analysis.settings.FileListManager;
import io.github.vocabhunter.analysis.settings.FileListManagerImpl;
import io.github.vocabhunter.gui.dialogues.FileDialogueFactory;
import io.github.vocabhunter.gui.services.*;
import io.github.vocabhunter.gui.settings.SettingsManager;
import io.github.vocabhunter.gui.settings.SettingsManagerImpl;

import java.nio.file.Paths;

import static org.testfx.api.FxToolkit.registerPrimaryStage;
import static org.testfx.api.FxToolkit.setupApplication;

public class WICCrashReproducer {

    public static void main(String[] args) throws Exception {
        System.setProperty("testfx.robot", "glass");
        System.setProperty("testfx.headless", "true");
        System.setProperty("prism.order", "sw");
        System.setProperty("prism.text", "t2k");
        System.setProperty("java.awt.headless", "true");
        registerPrimaryStage();

        Module testModule = new AbstractModule() {
            @Override
            protected void configure() {
                bind(SettingsManager.class).toInstance(new SettingsManagerImpl(Paths.get("test.txt")));
                bind(FileListManager.class).toInstance(new FileListManagerImpl(Paths.get("test.txt")));
                bind(FileDialogueFactory.class).toInstance(new FileDialogueFactoryImpl(new SettingsManagerImpl(Paths.get("test.txt"))));
                bind(EnvironmentManager.class).toInstance(new EnvironmentManagerImpl());
                bind(PlacementManager.class).toInstance(new PlacementManagerImpl(new EnvironmentManagerImpl(), new SettingsManagerImpl(Paths.get("test.txt"))));
                bind(WebPageTool.class).toInstance(new WebPageToolImpl(new ThreadPoolToolImpl()));
                bind(GuiTaskHandler.class).to(TestGuiTaskHandler.class);
            }
        };
        VocabHunterGuiExecutable.setModules(new CoreGuiModule(), testModule, new StandardEventSourceModule());

        setupApplication(VocabHunterGuiExecutable.class);
    }

    public static class TestGuiTaskHandler implements  GuiTaskHandler {

        @Override
        public void executeInBackground(final Runnable task) {
        }

        @Override
        public void executeOnGuiThread(final Runnable task) {
        }

        @Override
        public void pauseThenExecuteOnGuiThread(final Runnable task) {
        }
    }
}
