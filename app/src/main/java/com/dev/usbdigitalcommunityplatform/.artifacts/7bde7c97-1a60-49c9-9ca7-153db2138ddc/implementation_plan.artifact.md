# Implementation Plan - Fix Missing Vendor Routes in Navigation Graph

The application is crashing with an `IllegalArgumentException` because the navigation destination `vendor_profile_edit` is not defined in the `NavHost`. This route is used in the `VendorHomeScreen` and `MainActivity` but was missing from the `AppNavGraph`.

## User Review Required

> [!IMPORTANT]
> I will be adding placeholder "Coming Soon" screens for all vendor routes that do not have an implementation yet. This includes Listings, Requests, Chat, and Profile.

## Proposed Changes

### Navigation

#### [MODIFY] [NavGraph.kt](file:///D:/USBDigitalCommunityPlatform/app/src/main/java/com/dev/usbdigitalcommunityplatform/navigation/NavGraph.kt)

- Update `vendor_home` route to use `VendorRoutes.HOME`.
- Add new `composable` destinations for:
    - `VendorRoutes.LISTINGS`
    - `VendorRoutes.REQUESTS`
    - `VendorRoutes.CHAT`
    - `VendorRoutes.PROFILE`
- All new routes will initially display the `ComingSoonScreen`.

## Verification Plan

### Manual Verification
- Deploy the app and navigate to the Vendor Home screen.
- Click on "My Listings", "Inquiries", or "My Profile" (Avatar).
- Verify that the app no longer crashes and displays the "Coming Soon" screen for these features.
