# Implementation Plan - Fix Unresolved Reference 'ScreenWithBottomNav'

The project is failing to build because `NavGraph.kt` references `ScreenWithBottomNav`, `memberNavItems`, and `vendorNavItems`, which are missing from the `com.dev.usbdigitalcommunityplatform.ui.home.common` package.

Upon analysis, `MainActivity.kt` has been updated to handle the bottom navigation globally using its own `Scaffold` and `ReusableBottomNav`. This makes the `ScreenWithBottomNav` wrapper in `NavGraph.kt` redundant and the cause of "double bottom bars" if it were present.

## Proposed Changes

### [navigation](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation)

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove `ScreenWithBottomNav` wrapper from `member_home`, `vendor_home`, and `MemberRoutes.PROFILE` composables.
- Remove the unresolved imports:
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.ScreenWithBottomNav`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.memberNavItems`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.vendorNavItems`

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify the build error is resolved.

### Manual Verification
- Deploy the app to a device/emulator.
- Verify that the bottom navigation bar is visible on Home and Profile screens for both Member and Vendor roles.
- Verify that there is only one bottom navigation bar visible (no double bars).
