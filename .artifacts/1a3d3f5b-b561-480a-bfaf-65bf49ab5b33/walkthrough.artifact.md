# Walkthrough - Fix Compilation Error in ReusableBottomNav

I have fixed the compilation error `Cannot access 'val RowColumnParentData?.weight: Float': it is internal in file` by removing an incorrect import in `Reusablebottomnav.kt`.

## Changes

### [Reusablebottomnav.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/bottembar/Reusablebottomnav.kt)

Removed the incorrect import `androidx.compose.foundation.layout.weight`.

In Jetpack Compose, the `weight` modifier is an extension function provided by `RowScope` and `ColumnScope`. It should not be imported as a top-level member of `androidx.compose.foundation.layout`. When used inside a `Row` or `Column` block, it is automatically available via the scope.

```diff
-import androidx.compose.foundation.layout.weight
```

## Verification Results

### Automated Tests
- Ran `:app:compileDebugKotlin` and the build finished successfully.

```
$ ./gradlew :app:compileDebugKotlin
...
BUILD SUCCESSFUL in 2s
```
