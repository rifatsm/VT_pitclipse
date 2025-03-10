package org.pitest.pitclipse.ui.tests;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.pitest.pitclipse.runner.config.PitConfiguration.DEFAULT_AVOID_CALLS_TO_LIST;
import static org.pitest.pitclipse.runner.config.PitConfiguration.DEFAULT_MUTATORS;
import static org.pitest.pitclipse.runner.config.PitExecutionMode.PROJECT_ISOLATION;
import static org.pitest.pitclipse.ui.behaviours.pageobjects.PageObjects.PAGES;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swtbot.swt.finder.junit.SWTBotJunit4ClassRunner;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.pitest.pitclipse.core.Mutators;
import org.pitest.pitclipse.core.PitCoreActivator;
import org.pitest.pitclipse.core.preferences.PitPreferences;
import org.pitest.pitclipse.runner.config.PitConfiguration;
import org.pitest.pitclipse.ui.behaviours.pageobjects.PitPreferenceSelector;

/**
 * @author Lorenzo Bettini
 * 
 */
@RunWith(SWTBotJunit4ClassRunner.class)
public class PitclipseOptionsTest extends AbstractPitclipseSWTBotTest {

    private static final String TEST_PROJECT = "org.pitest.pitclipse.testprojects.twoclasses";
    private static final String FOO_BAR_PACKAGE = "foo.bar";
    private static final String FOO_CLASS = "Foo";
    private static final String FOO_TEST_CLASS = "FooTest";
    private static final String BAR_CLASS = "Bar";
    private static final String BAR_TEST_CLASS = "BarTest";
    private static final String TEST_CONFIG_NAME = "PitclipseOptionsTest_config";

    @BeforeClass
    public static void setupJavaProject() throws CoreException {
        importTestProject(TEST_PROJECT);
        openEditor(FOO_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
        openEditor(FOO_TEST_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
        openEditor(BAR_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
        openEditor(BAR_TEST_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
    }


    @Before
    public void removeLaunchConfigurations() throws CoreException {
        removePitLaunchConfigurations();
    }

    @Test
    public void defaultOptions() {
        PitPreferenceSelector selector = PAGES.getWindowsMenu().openPreferences().andThen();
        assertEquals(PROJECT_ISOLATION, selector.getPitExecutionMode());
        assertTrue(selector.isPitRunInParallel());
        assertFalse(selector.isIncrementalAnalysisEnabled());
        assertEquals("The 'Excluded Classes' preference has not the expected value", 
                "*Test", selector.getExcludedClasses());
        assertTrue(selector.getExcludedMethods().isEmpty());
        assertThat(selector.getAvoidCallsTo(), equalTo(DEFAULT_AVOID_CALLS_TO_LIST));
        assertThat(selector.getTimeout(), equalTo(PitConfiguration.DEFAULT_TIMEOUT));
        assertThat(selector.getPitTimeoutFactor(), equalTo(PitConfiguration.DEFAULT_TIMEOUT_FACTOR));
        assertThat(selector.getMutators().toString(), equalTo(DEFAULT_MUTATORS));
        selector.close();
    }

//    @Test
//    public void useOldDefaultsMutators() throws CoreException {
//        // set OLD_DEFAULTS mutators
//        PAGES.getWindowsMenu().setMutatorGroup(Mutators.OLD_DEFAULTS);
//        try {
//            runPackageTest(FOO_BAR_PACKAGE, TEST_PROJECT);
//            coverageReportGenerated(2, 80, 0, 8, 0);
//            mutationsAre(   "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    7 | negated conditional\n" +
//                            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    10 | replaced return of integer sized value with (x == 0 ? 1 : 0)\n" +
//                            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    7 | negated conditional\n" +
//                            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    10 | replaced return of integer sized value with (x == 0 ? 1 : 0)\n" +
//                            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | Replaced integer addition with subtraction\n" +
//                            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | replaced return of integer sized value with (x == 0 ? 1 : 0)\n" +
//                            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | Replaced integer addition with subtraction\n" +
//                            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | replaced return of integer sized value with (x == 0 ? 1 : 0)");
//        } finally {
//            // it's crucial to reset it to the default or we break other tests
//            PAGES.getWindowsMenu().setMutatorGroup(Mutators.DEFAULTS);
//        }
//    }

//    @Test
//    public void useDefaultMutators() throws CoreException {
//        runPackageTest(FOO_BAR_PACKAGE, TEST_PROJECT);
//        coverageReportGenerated(2, 80, 0, 6, 0);
//        mutationsAre(
//        "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    7 | negated conditional\n" +
//        "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    7 | negated conditional\n" +
//        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | Replaced integer addition with subtraction\n" +
//        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | replaced int return with 0 for foo/bar/Bar::f\n" +
//        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | Replaced integer addition with subtraction\n" +
//        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | replaced int return with 0 for foo/bar/Foo::f");
//    }

//    @Test
//    public void useStrongerMutators() throws CoreException {
//        // now set STRONGER mutators
//        PAGES.getWindowsMenu().setMutatorGroup(Mutators.STRONGER);
//        try {
//            runPackageTest(FOO_BAR_PACKAGE, TEST_PROJECT);
//            coverageReportGenerated(2, 80, 0, 8, 0);
//            mutationsAre(
//            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    7 | negated conditional\n" +
//            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    7 | removed conditional - replaced equality check with false\n" +
//            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    7 | negated conditional\n" +
//            "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    7 | removed conditional - replaced equality check with false\n" +
//            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | Replaced integer addition with subtraction\n" +
//            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Bar |    8 | replaced int return with 0 for foo/bar/Bar::f\n" +
//            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | Replaced integer addition with subtraction\n" +
//            "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | replaced int return with 0 for foo/bar/Foo::f");
//        } finally {
//            // it's crucial to reset it to the default or we break other tests
//            PAGES.getWindowsMenu().setMutatorGroup(Mutators.DEFAULTS);
//        }
//    }

    @Test
    public void launchConfigurationsWithDefaultOptions() throws CoreException {
        runTest(FOO_TEST_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
        coverageReportGenerated(2, 40, 0, 6, 0);
        runtimeOptionsMatch(
        "classUnderTest           | classesToMutate  | excludedClasses | excludedMethods | runInParallel | incrementalAnalysis | avoidCallsTo                                                               \n" +
        "foo.bar.FooTest | foo.bar.BarTest, foo.bar.Foo, foo.bar.Bar, foo.bar.FooTest       | *Test           |                 | true          | false               | java.util.logging, org.apache.log4j, org.slf4j, org.apache.commons.logging, org.apache.logging.log4j"
        );
        launchConfigurationsMatch(
        "name    | runInParallel | useIncrementalAnalysis | excludedClasses | excludedMethods | avoidCallsTo                                                               \n"
      + "FooTest | true          | false                  | *Test           |                 | java.util.logging, org.apache.log4j, org.slf4j, org.apache.commons.logging, org.apache.logging.log4j"
        );
        runTest(BAR_TEST_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
        coverageReportGenerated(2, 40, 0, 6, 0);
        launchConfigurationsMatch(
        "name    | runInParallel | useIncrementalAnalysis | excludedClasses | excludedMethods | avoidCallsTo                                                               \n"
      + "BarTest | true          | false                  | *Test           |                 | java.util.logging, org.apache.log4j, org.slf4j, org.apache.commons.logging, org.apache.logging.log4j |\n"
      + "FooTest | true          | false                  | *Test           |                 | java.util.logging, org.apache.log4j, org.slf4j, org.apache.commons.logging, org.apache.logging.log4j"
        );
    }

    @Test
    public void launchConfigurationsWithChangedValues() throws CoreException {
        try {
            PitPreferenceSelector selector = PAGES.getWindowsMenu().openPreferences().andThen();
            selector.setPitTimeoutConst(2000);
            selector.setPitTimeoutFactor(2);
            selector.setPitRunInParallel(false);
            selector.setPitIncrementalAnalysisEnabled(true);
            selector.setExcludedClasses("org.foo.*IntTest, *DbTest");
            selector.setAvoidCallsTo("org.slf4j, org.apache");
            selector.setExcludedMethods("*toString*, doNotMutateMe*");
            selector.close();
            runTest(FOO_TEST_CLASS, FOO_BAR_PACKAGE, TEST_PROJECT);
            coverageReportGenerated(2, 40, 0, 6, 0);
            runtimeOptionsMatch(
            "classUnderTest  | timeoutConst | timeoutFactor | runInParallel | incrementalAnalysis | excludedClasses           | avoidCallsTo          | excludedMethods \n" +
            "foo.bar.FooTest | 2000         | 2             | false         | true                | org.foo.*IntTest, *DbTest | org.slf4j, org.apache | *toString*, doNotMutateMe*"
            );
            launchConfigurationsMatch(
            "name    | runInParallel | useIncrementalAnalysis | excludedClasses          | excludedMethods            | avoidCallsTo \n"
          + "FooTest | false         | true                  | org.foo.*IntTest, *DbTest | *toString*, doNotMutateMe* | org.slf4j, org.apache"
            );
        } finally {
            // reset default values
            IPreferenceStore preferenceStore = PitCoreActivator.getDefault().getPreferenceStore();
            preferenceStore.setValue(PitPreferences.TIMEOUT, PitConfiguration.DEFAULT_TIMEOUT);
            preferenceStore.setValue(PitPreferences.TIMEOUT_FACTOR, PitConfiguration.DEFAULT_TIMEOUT_FACTOR.toString());
            preferenceStore.setValue(PitPreferences.RUN_IN_PARALLEL, true);
            preferenceStore.setValue(PitPreferences.INCREMENTAL_ANALYSIS, false);
            preferenceStore.setValue(PitPreferences.EXCLUDED_CLASSES, PitConfiguration.DEFAULT_EXCLUDED_CLASSES);
            preferenceStore.setValue(PitPreferences.AVOID_CALLS_TO, PitConfiguration.DEFAULT_AVOID_CALLS_TO_LIST);
            preferenceStore.setValue(PitPreferences.EXCLUDED_METHODS, "");
        }
    }

//    @Test
//    public void launchConfigWithTargetClass() { // NOSONAR
//        createTestConfig();
//        PAGES.getRunMenu().setTargetClassForConfiguration(TEST_CONFIG_NAME, FOO_BAR_PACKAGE + '.' + FOO_CLASS);
//        // run test and confirm result is as expected
//        PAGES.getRunMenu().runPitWithConfiguration(TEST_CONFIG_NAME);
//        coverageReportGenerated(1, 80, 0, 3, 0);
//        mutationsAre(   "SURVIVED    | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    7 | negated conditional\n" +
//                        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | Replaced integer addition with subtraction\n" +
//                        "LINES_NOT_TESTED | " + TEST_PROJECT + " | foo.bar | foo.bar.Foo |    8 | replaced int return with 0 for foo/bar/Foo::f");
//    }

    private void createTestConfig() {
        PAGES.getRunMenu().createRunConfiguration(TEST_CONFIG_NAME,
                TEST_PROJECT,
                FOO_BAR_PACKAGE + '.' + FOO_TEST_CLASS);
    }
}
