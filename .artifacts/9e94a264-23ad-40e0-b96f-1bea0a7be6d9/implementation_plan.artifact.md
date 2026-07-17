# Implementation Plan - Fix Unresolved Reference 'memberNavItems'

The project is failing to build due to an `Unresolved reference 'memberNavItems'` error in `NavGraph.kt`. This function, along with `vendorNavItems` and `ScreenWithBottomNav`, is referenced in `NavGraph.kt` but not defined or imported correctly.

Further analysis reveals that `MainActivity.kt` has been updated to handle bottom navigation globally using a `Scaffold` and `ReusableBottomNav`. Therefore, rendering these navigation components inside individual screens in `NavGraph.kt` is not only causing build errors but would also lead to "double bottom navigation" UI issues.

## Proposed Changes

### Navigation

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove unused and unresolved imports:
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.ScreenWithBottomNav`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.memberNavItems`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.vendorNavItems`
    - `com.dev.usbdigitalcommunityplatform.ui.home.bottembar.ReusableBottomNav` (no longer used in this file)
- Remove the `ReusableBottomNav` wrapper from the `member_home` composable.
- Remove the `ReusableBottomNav` wrapper from the `vendor_home` composable.
- Remove the `ScreenWithBottomNav` wrapper from the `MemberRoutes.PROFILE` composable.
- Update the indentation and structure of these composables to directly call the screen content.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify that the unresolved reference error is resolved and the project compiles.

### Manual Verification
- Deploy the app and verify that the bottom navigation appears correctly on the Home and Profile screens (handled by `MainActivity`) without duplication.
