# Implementation Plan - Unify and Persist Bottom Navigation

The goal is to fix the issue where the bottom navigation bar is not showing on every screen and to use the custom `ReusableBottomNav` component consistently across the app.

## Proposed Changes

### 1. Centralize Bottom Navigation in `MainActivity`

Instead of wrapping individual screens in `NavGraph.kt`, we will move the `Scaffold` and `ReusableBottomNav` to `MainActivity.kt`. This ensures the bottom bar stays persistent during transitions and is easier to manage.

#### [MODIFY] [MainActivity.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/MainActivity.kt)
- Pass the `navController` to `AppNavGraph`.
- Observe the current route using `navController.currentBackStackEntryAsState()`.
- Conditionally show `ReusableBottomNav` based on the current route (e.g., hide on Splash, Login, etc.).
- Determine which set of `NavItems` (Member vs. Vendor) to show based on the current destination.

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)
- Update `AppNavGraph` to accept `NavController` as a parameter.
- Remove all usages of `ScreenWithBottomNav` to avoid nested Scaffolds and conflicting navigation bars.

### 2. Update and Clean Up Navigation Components

#### [MODIFY] [BottomNavComponents.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/common/BottomNavComponents.kt)
- Update `ScreenWithBottomNav` (if still needed for any reason, though `MainActivity` will handle most cases) to use `ReusableBottomNav`.
- Ensure `memberNavItems()` and `vendorNavItems()` are up-to-date with the latest routes.

#### [MODIFY] [VendorHomeScreen.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/ui/home/vendor/VendorHomeScreen.kt)
- Remove the hardcoded bottom navigation at the bottom of the screen to prevent duplication.

### 3. Logic for Bottom Bar Visibility and Items

We will define which routes belong to "Member" and "Vendor" workflows to show the correct navigation items.

- **Member Routes**: Home, Jobs, Legal, CA, Vendors, Community, Notices, Documents, Discover, Requests, Chat, Profile.
- **Vendor Routes**: Home, Listings, Requests, Chat, Profile.
- **Hidden Routes**: Splash, Language, Login, OTP, Profile Setup, Role Selection, Admin Home.

## Verification Plan

### Automated Tests
- Run `:app:compileDebugKotlin` to ensure no compilation errors.
- Render Compose Previews for `ReusableBottomNav` and `MainActivity` if possible.

### Manual Verification
- Deploy to an Android device/emulator.
- Verify that the bottom bar appears after login/on home screens.
- Verify that the bottom bar changes items when switching between Member and Vendor roles (if applicable).
- Verify that the bottom bar is NOT visible on the Splash or Login screens.
- Navigate through different sub-screens (Jobs, Legal) and ensure the bottom bar stays visible.
