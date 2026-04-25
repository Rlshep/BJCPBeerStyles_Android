# BJCP 2021 Beer Styles - Beer Style Compendium
An Android application to display the BJCP 2021 Beer Style Guidelines, 2025 Cider Style Guidelines, and the 2015 Mead Style Guidelines

All style guideline information is Copyright � 2025, BJCP, Inc. Beer Judge Certification Program.
Reproduced with permission.

Application authored by Richard Sheppard.
Thank you to Seth Kroger for providing the formatted XML.

## Build Information

1. Edit build.gradle within bjcp_beer_styles and add the following:
```
    signingConfigs {
       create("release") {
           // You need to specify either an absolute path or include the
           // keystore file in the same directory as the build.gradle file.
           storeFile = file("androidpersonal.keystore")
           storePassword = ""
           keyAlias = "AndroidPersonalKeystore"
           keyPassword = ""
           }
       }
```
2. Build the code: ./gradlew clean assembleRelease
3. Push the application to an emulator or connected device: adb install -d bjcp_beer_styles/build/outputs/apk/release/bjcp_beer_styles-release.apk
4. Execute device tests: ./gradlew cAT
   a. Device test only a single class: ./gradlew cAT -Pandroid.testInstrumentationRunnerArguments.class=io.github.rlshep.bjcp2015beerstyles.en.SearchTest


If changing db
-----------------------------------
Change BjcpConstants.DATABASE_VERSION

Change strings.xml about_version
update version.properties

./gradlew assembleRelease
./gradlew publishApkRelease