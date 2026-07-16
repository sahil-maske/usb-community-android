# Implementation Plan - Fix Navigation Crash for Vendor Profile

The application is crashing because the `vendor_profile_edit` route is not defined in the `NavGraph`. This route is used when a user in the "Vendor" role attempts to navigate to their profile (e.g., via the bottom navigation bar).

## Proposed Changes

### [Navigation]

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)

- Add the following vendor-specific routes to the `NavHost` in `AppNavGraph`:
    - `VendorRoutes.LISTINGS` (Coming Soon)
    - `VendorRoutes.REQUESTS` (Coming Soon)
    - `VendorRoutes.CHAT` (Coming Soon)
    - `VendorRoutes.PROFILE` (Coming Soon)
- Ensure that `VendorRoutes.HOME` is used for the `vendor_home` route for consistency.

## Verification Plan

### Manual Verification
- Deploy the app to a device/emulator.
- Log in as a user with the "Vendor" role (or select Vendor role if prompted).
- Click on the "Profile" tab in the bottom navigation bar.
- Verify that the app no longer crashes and displays the "Coming Soon" screen for the Profile.
- Verify that other quick actions on the Vendor Home screen (My Listings, Inquiries) also navigate to the "Coming Soon" screen without crashing.
