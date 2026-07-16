# Fix Unresolved Reference 'ScreenWithBottomNav'

The project is failing to build because `NavGraph.kt` references `ScreenWithBottomNav`, which is missing or incomplete in `BottomNavComponents.kt`.

Upon analysis, `MainActivity.kt` has been updated to handle the bottom navigation globally using a single top-level `Scaffold`. Wrapping individual screens in `NavGraph.kt` with another `ScreenWithBottomNav` wrapper is redundant and would lead to a "double bottom nav" issue.

## Proposed Changes

### [Component: Navigation]

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Remove `ScreenWithBottomNav` wrapper from `member_home`, `vendor_home`, and `MemberRoutes.PROFILE` destinations.
- Remove the unresolved import `com.dev.usbdigitalcommunityplatform.ui.home.common.ScreenWithBottomNav`.
- Remove unused imports `memberNavItems` and `vendorNavItems` (as `MainActivity` now defines its own simplified list of items).

### [Component: UI Common]

#### [MODIFY] [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)
- Clean up the file by removing the incomplete `ScreenWithBottomNav` KDoc and any unused item list functions if they are strictly redundant.
- Keep `NavItem` as it is used by `MainActivity.kt`.

## Verification Plan

### Automated Tests
- Run `./gradlew :app:compileDebugKotlin` to verify the build error is resolved.

### Manual Verification
- Deploy the app to a device/emulator and verify that the bottom navigation bar appears correctly on Home and Profile screens, and only one bar is visible.
