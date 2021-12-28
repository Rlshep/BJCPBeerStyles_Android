# BJCP2015BeerStyles
An Android application to display BJCP 2015 Style Guidelines

All style guideline information is Copyright � 2015, BJCP, Inc. Beer Judge Certification Program.
Reproduced with permission.

Application authored by Richard Sheppard.
Thank you to Seth Kroger for providing the formatted XML.

## Build Information

If changing db
-----------------------------------
Change BjcpConstants.DATABASE_VERSION

Change strings.xml about_version
update version.properties

./gradlew assembleRelease
./gradlew publishApkRelease