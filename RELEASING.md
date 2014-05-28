1. Update `VERSION_NAME` in `gradle.properties`
2. Update version in `README.md`
3. `./gradlew clean build signArchives uploadArchives`
4. `git tag -a VERSION_NAME -m "Version VERSION_NAME" && git push && git push --tags`