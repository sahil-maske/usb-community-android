# Fix "No parameter with name 'navController' found" error

The user is encountering a compilation error in `MainActivity.kt` where `AppNavGraph` is called with a named parameter `navController`, but the compiler claims no such parameter exists.

## Research Findings
- There are two `AppNavGraph` definitions in the project:
    1. `D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt`: `fun AppNavGraph(navController: NavHostController)`
    2. `D:/USBDigitalCommunityPlatform/usb-community-android/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt`: `fun AppNavGraph()`
- The error is reported in the root `app` module's `MainActivity.kt`.
- The root `app` module's `NavGraph.kt` *does* have the parameter, so the error is unexpected if this is the version being used.
- However, if the compiler is somehow picking up the version from `usb-community-android` (which is in a separate project folder inside the root), it would cause this error.
- Alternatively, there might be a mismatch in the `NavController` vs `NavHostController` types if the compiler is being overly strict or confused, though the error message specifically mentions the parameter *name*.

## Proposed Changes

### 1. Verify and Fix `NavGraph.kt` in the root `app` module
I will ensure that `AppNavGraph` in `D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt` correctly defines the `navController` parameter and that all types are imported.

### 2. Update `MainActivity.kt`
I will verify the call in `MainActivity.kt` and ensure the imports are correct. I may also try using a positional argument if the named argument continues to cause issues in the user's environment.

### 3. Resolve Conflict with `usb-community-android` (if necessary)
If the nested project is causing classpath conflicts, I will investigate how it's being included and suggest moving it or excluding it from the root build.

## Verification Plan
- Run `./gradlew :app:compileDebugKotlin` to ensure the error is resolved.
- Perform a Gradle Sync to ensure the IDE is up to date.
