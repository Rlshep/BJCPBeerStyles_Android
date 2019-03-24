package io.github.rlshep.bjcp2015beerstyles.helpers;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.LocaleList;
import android.util.DisplayMetrics;

import org.junit.Test;

import java.util.Locale;

import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.DEFAULT_COUNTRY;
import static io.github.rlshep.bjcp2015beerstyles.constants.BjcpConstants.DEFAULT_LANGUAGE;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LocaleHelperTest extends LocaleHelper {

    @Test
    public void getLanguageDefault_en() {
        Context c = mock(Context.class);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(c)));
    }

    @Test
    public void getLanguage_en_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(Locale.ENGLISH);
        when(locales.size()).thenReturn(1);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_en_multiple_error () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = null;

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_es_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(new Locale("es"));
        when(locales.get(1)).thenReturn(Locale.ENGLISH);
        when(locales.size()).thenReturn(2);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(!DEFAULT_LANGUAGE.equals(getLanguage(context)));
        assert("es".equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_de_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(Locale.GERMAN);
        when(locales.size()).thenReturn(1);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_de_then_es_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(Locale.GERMAN);
        when(locales.get(1)).thenReturn(new Locale("es"));
        when(locales.size()).thenReturn(2);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert("es".equals(getLanguage(context)));
    }


    @Test
    public void getLanguage_en_single () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 23);
        Locale.setDefault(Locale.ENGLISH);
        Context context = mock(Context.class);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_de_single () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 23);
        Locale.setDefault(new Locale("de"));
        Context context = mock(Context.class);

        assert(DEFAULT_LANGUAGE.equals(getLanguage(context)));
    }

    @Test
    public void getLanguage_es_single () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 23);
        Locale.setDefault(new Locale("es"));
        Context context = mock(Context.class);

        assert("es".equals(getLanguage(context)));
    }

    @Test
    public void getCountry_US_single () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 23);
        Locale.setDefault(Locale.US);
        Context context = mock(Context.class);

        assert(DEFAULT_COUNTRY.equals(getCountry(context)));
    }

    @Test
    public void getCountry_de_single () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 23);
        Locale.setDefault(Locale.GERMANY);
        Context context = mock(Context.class);

        assert("DE".equals(getCountry(context)));
    }

    @Test
    public void getCountry_US_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(Locale.US);
        when(locales.size()).thenReturn(1);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(DEFAULT_COUNTRY.equals(getCountry(context)));
    }

    @Test
    public void getCountry_US_multiple_error () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(null);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert(DEFAULT_COUNTRY.equals(getCountry(context)));
    }

    @Test
    public void getCountry_DE_multiple () throws Exception {
        setFinalStatic(Build.VERSION.class.getField("SDK_INT"), 25);

        LocaleList locales = mock(LocaleList.class);
        when(locales.get(0)).thenReturn(Locale.GERMANY);
        when(locales.size()).thenReturn(1);

        Configuration config = mock(Configuration.class);
        when(config.getLocales()).thenReturn(locales);

        Resources resources = mock(MockResources.class);
        when(resources.getConfiguration()).thenReturn(config);

        Context context = mock(Context.class);
        when(context.getResources()).thenReturn(resources);
        when(context.getResources().getConfiguration().getLocales()).thenReturn(locales);

        assert("DE".equals(getCountry(context)));
    }

    private class MockResources extends Resources {
        /**
         * Create a new Resources object on top of an existing set of assets in an
         * AssetManager.
         *
         * @param assets  Previously created AssetManager.
         * @param metrics Current display metrics to consider when
         *                selecting/computing resource values.
         * @param config  Desired device configuration to consider when
         * @deprecated Resources should not be constructed by apps.
         * See {@link Context#createConfigurationContext(Configuration)}.
         */
        public MockResources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
            super(assets, metrics, config);
        }
    }
}
