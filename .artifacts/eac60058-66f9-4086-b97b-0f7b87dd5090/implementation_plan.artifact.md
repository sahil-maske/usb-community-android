# Remove Redundant Bottom Navigation Bar

The application currently displays two bottom navigation bars on certain screens (like Home and Profile). One is a floating rounded bar (`ReusableBottomNav` in `MainActivity.kt`), and the other is a standard Material 3 bar (`ScreenWithBottomNav`). Based on the provided images, the user wants to remove the Material 3 bar (Figure 2).

## User Review Required

> [!IMPORTANT]
> I will be removing the `ScreenWithBottomNav` wrapper from `NavGraph.kt`. This will eliminate the double bottom navigation bar issue, leaving only the floating `ReusableBottomNav` defined in `MainActivity.kt`.

## Proposed Changes

### Navigation

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)

- Remove `ScreenWithBottomNav` wrapper from `member_home`, `vendor_home`, and `MemberRoutes.PROFILE`.
- Remove unused imports:
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.ScreenWithBottomNav`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.memberNavItems`
    - `com.dev.usbdigitalcommunityplatform.ui.home.common.vendorNavItems`

### UI Components

#### [MODIFY] [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)

- Remove the `ScreenWithBottomNav` composable as it is no longer used and was causing the double bottom navigation bar.
- Keep `NavItem`, `memberNavItems()`, and `vendorNavItems()` if they are needed elsewhere (e.g., in `MainActivity.kt`).
    - *Note:* `MainActivity.kt` currently uses these to define the items in its own bottom bar.

## Verification Plan

### Manual Verification
- Deploy the app.
- Navigate to the Home screen (as Member or Vendor).
- Verify that only one bottom navigation bar (the floating rounded one) is visible.
- Navigate to the Profile screen and verify the same.
