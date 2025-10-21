# LuckySpot CRC Cards
---

## Core Domain Classes

***Class*** Event

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Hold event metadata (name, description, time/place, posterUrl?, regOpen, regClose, capacity, waitingListLimit?, status)<br>- Report registration state: `isRegistrationOpen(now)`, `isRegistrationClosed(now)`<br>- Provide organizer summaries (waiting/selected/cancelled/attendees counts)<br>- Validate updates (e.g., `regOpen < regClose`, `capacity ≥ 0`)<br>- Track waiting list limit (optional) and enforce capacity<br>- Store generated QR code URL | - Organizer<br>- WaitingListEntry<br>- Selection<br>- EventRepository<br>- PosterStorage<br>- QRCodeGenerator<br>- WaitingListRepository |

**Comments / Refs:**  
- US 02.01.04, US 01.01.03, US 02.06.01–02.06.03, US 02.03.01  

---

***Class*** Profile

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent device-identified entrant (name, email, phone?, notificationsOptIn)<br>- Maintain participation history (registered/selected outcomes)<br>- Update and delete profile (right to leave the app)<br>- Store Firebase Cloud Messaging token<br>- Track notification opt-in preferences (lottery results, event updates, admin messages) | - DeviceIdProvider<br>- ProfileRepository<br>- NotificationService<br>- PermissionManager |

**Comments / Refs:**  
- US 01.02.01–01.02.04, US 01.04.03  

---

***Class*** WaitingListEntry

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Record join/leave for a profile on an event (joinedAt, optional joinGeo)<br>- Prevent duplicate joins and expose presence/status<br>- Provide organizer counts and listing<br>- Enforce waiting list capacity limits | - Event<br>- Profile<br>- WaitingListRepository<br>- GeoService |

**Comments / Refs:**  
- US 01.01.01–01.01.02, US 01.05.04, US 02.03.01  

---

***Class*** Selection

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent an invitation (selectedAt, expiresAt?, status: PENDING / ACCEPTED / DECLINED / EXPIRED / CANCELLED)<br>- Manage transitions: ACCEPTED → attendees, DECLINED/EXPIRED → backfill pool<br>- Expose invitation status for entrant and organizer views<br>- Track cancellation reason and timestamp | - LotteryService<br>- SelectionRepository<br>- NotificationService<br>- Event<br>- CancellationManager |

**Comments / Refs:**  
- US 01.05.02, US 01.05.03, US 02.06.01–02.06.04  

---

## Service Classes

***Class*** LotteryService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Draw N entrants without replacement after registration closes<br>- Run replacement draws on declines, cancellations, or expiry<br>- Persist results atomically and ensure idempotency<br>- Ensure cryptographically secure random selection<br>- Log lottery draw for audit trail<br>- Handle edge cases (more winners than entrants, no eligible entrants)<br>- Prevent duplicate lottery draws | - Event<br>- WaitingListRepository<br>- SelectionRepository<br>- NotificationService<br>- AnalyticsService |

**Comments / Refs:**  
- US 02.05.02, US 02.05.03, US 01.05.01  

---

***Class*** NotificationService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Send "win/lose" notifications and organizer bulk messages<br>- Manage device tokens, notification topics, and opt-in/opt-out settings<br>- Log each notification send for admin review<br>- Request notification permissions (Android 13+)<br>- Handle notification delivery failures | - Profile<br>- LotteryService<br>- AdminService<br>- NotificationLog<br>- PermissionManager |

**Comments / Refs:**  
- US 01.04.01–01.04.03, US 02.05.01, US 02.07.01–02.07.03  

---

***Class*** NotificationLog

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Store metadata of sent notifications (audience, type, eventId, timestamp, sentBy)<br>- Provide query and filter capabilities for admin audit<br>- Link logged notifications to related event and initiator | - NotificationService<br>- AdminService |

**Comments / Refs:**  
- US 03.08.01  

---

***Class*** QRService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Parse scanned QR/promotional codes and resolve them to eventId<br>- Trigger deep-link navigation to specific Event Details<br>- Validate QR code format and handle invalid or expired codes<br>- Request camera permissions | - EventRepository<br>- Navigator<br>- PermissionManager<br>- DeepLinkHandler |

**Comments / Refs:**  
- US 01.06.01, US 01.06.02  

---

***Class*** QRCodeGenerator

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Generate unique QR codes for events upon creation<br>- Encode eventId or deep link URL into QR format<br>- Provide downloadable/shareable QR code images<br>- Validate QR code generation success | - Event<br>- Organizer<br>- EventRepository |

**Comments / Refs:**  
- US 02.01.01 (generate promotional QR code)  

---

***Class*** AnalyticsService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Track platform-wide metrics (total events, users, lottery draws)<br>- Calculate event-level statistics (cancellation rate, acceptance rate, avg waiting list size)<br>- Flag events with high cancellation rates (>30%)<br>- Generate reports for admin dashboard<br>- Calculate lottery success rates and trends | - Event<br>- Selection<br>- AdminService<br>- EventRepository<br>- SelectionRepository |

**Comments / Refs:**  
- US 03.12.01 (flag high cancellation events)<br>- US 03.13.01 (export platform usage reports)  

---

***Class*** SearchService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Filter events by keywords (name, description)<br>- Filter events by date range, price range, category<br>- Filter events by status (open, closed, completed)<br>- Sort events (by date, name, capacity, entrant count)<br>- Return filtered/sorted event lists<br>- Handle empty search results | - EventRepository<br>- Event |

**Comments / Refs:**  
- US 01.01.04 (filter events by interests and availability)  

---

***Class*** ValidationService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Validate email format (regex pattern)<br>- Validate phone number format (optional field)<br>- Validate event dates (regOpen < regClose, future dates, minimum duration)<br>- Validate capacity (positive integer)<br>- Validate waiting list limit (if set, must be positive)<br>- Provide user-friendly validation error messages | - Profile<br>- Event<br>- Organizer<br>- ProfileRepository<br>- EventRepository |

**Comments / Refs:**  
- US 01.02.01 (profile validation)<br>- US 02.01.04 (registration period validation)  

---

***Class*** CancellationManager

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Handle organizer cancellations of selected/confirmed entrants<br>- Record cancellation reason and timestamp<br>- Optionally trigger replacement draw<br>- Update entrant status to CANCELLED<br>- Send cancellation notifications to affected entrants<br>- Maintain cancelled entrants list | - Selection<br>- LotteryService<br>- NotificationService<br>- Organizer<br>- SelectionRepository |

**Comments / Refs:**  
- US 02.06.04 (cancel entrants who did not sign up)<br>- US 02.06.02 (see cancelled entrants list)  

---

***Class*** GeoService (optional)

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Capture coarse join location when organizer requires and user consents<br>- Provide aggregated location data to organizers<br>- Handle location permission requests<br>- Validate location data quality | - WaitingListEntry<br>- Organizer<br>- MapService<br>- PermissionManager |

**Comments / Refs:**  
- US 02.02.02, US 02.02.03  

---

***Class*** MapService (optional)

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Integrate with mapping API (Google Maps)<br>- Display entrant join locations on interactive map<br>- Show heatmap visualization of entrant density<br>- Handle map permissions and loading states<br>- Provide map clustering for dense areas | - GeoService<br>- Organizer<br>- WaitingListEntry |

**Comments / Refs:**  
- US 02.02.02 (see map where entrants joined)  

---

***Class*** PermissionManager

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Request and check camera permission (QR scanning)<br>- Request and check location permission (geolocation)<br>- Request and check notification permission (Android 13+)<br>- Handle permission denial gracefully with explanations<br>- Provide permission rationale to users<br>- Check if permissions are granted before operations | - QRService<br>- GeoService<br>- NotificationService<br>- Profile |

**Comments / Refs:**  
- US 01.06.01 (camera for QR)<br>- US 02.02.03 (geolocation requirement)<br>- Android runtime permissions  

---

---

## User Role Classes

***Class*** Organizer

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Create and edit events; set registration window and capacity; publish or unpublish events<br>- Upload or update event posters<br>- View waiting, selected, cancelled, and attendee lists<br>- Export CSV of participants; send bulk messages to entrants<br>- Generate and download QR codes<br>- View event analytics (acceptance rate, cancellation rate)<br>- Set waiting list capacity limits<br>- Conduct lottery draws and draw replacements<br>- Cancel non-responsive entrants | - EventRepository<br>- PosterStorage<br>- WaitingListRepository<br>- SelectionRepository<br>- CsvExporter<br>- NotificationService<br>- QRCodeGenerator<br>- AnalyticsService<br>- LotteryService<br>- CancellationManager<br>- ValidationService |

**Comments / Refs:**  
- US 02.01.01, US 02.01.04, US 02.02.01–02.02.03, US 02.03.01, US 02.05.01–02.05.03, US 02.06.01–02.06.05  

---

***Class*** AdminService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Browse or remove events, profiles, and images<br>- Remove or restrict organizers who violate policy<br>- Review notification logs and system activity<br>- View platform statistics and analytics<br>- Flag problematic events (high cancellation rates)<br>- Export platform usage reports<br>- Audit geolocation usage for privacy compliance | - EventRepository<br>- ProfileRepository<br>- PosterStorage<br>- NotificationLog<br>- AnalyticsService<br>- GeoService |

**Comments / Refs:**  
- US 03.01.01–03.07.01, US 03.08.01, US 03.12.01, US 03.13.01  

---

## Storage & Repository Classes

***Class*** PosterStorage

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Upload or replace poster images and return stable URLs<br>- List and browse poster images for organizers/admins<br>- Support deletion of images<br>- Enforce file type and size constraints<br>- Compress images before upload<br>- Handle upload failures and retries | - Event<br>- Organizer<br>- AdminService<br>- ImageValidator |

**Comments / Refs:**  
- US 02.04.01–02.04.02, US 03.03.01, US 03.06.01  

---

***Class*** ImageValidator

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Validate image file types (JPG, PNG only)<br>- Enforce maximum file size (5MB)<br>- Compress images if needed before upload<br>- Reject invalid or corrupted images<br>- Provide validation error messages | - PosterStorage<br>- Organizer |

**Comments / Refs:**  
- US 02.04.01, US 03.03.01 (image upload validation)  

---

***Class*** DeviceIdProvider

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Provide stable device-based identity for login-less profiles<br>- Detect first-run vs returning users<br>- Surface device integrity or validation checks<br>- Handle device ID persistence across app reinstalls | - ProfileRepository<br>- Profile |

**Comments / Refs:**  
- US 01.07.01 (device-based identification)  

---

***Class*** EventRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - CRUD operations for events; publish/unpublish events<br>- Fetch event lists and event details<br>- Query by registration state or time; provide organizer dashboards/summaries<br>- Persist event data to Firebase (events, attendees, etc.)<br>- Handle real-time event updates<br>- Support filtering and sorting | - Event<br>- Organizer<br>- WaitingListRepository<br>- SelectionRepository<br>- SearchService<br>- AnalyticsService |

**Comments / Refs:**  
- Firebase data storage for events  

---

***Class*** ProfileRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - CRUD profiles keyed by deviceId<br>- Manage notification tokens for delivery<br>- Retrieve or update participation history<br>- Delete profile data upon user request (GDPR compliance)<br>- Handle profile data validation | - Profile<br>- DeviceIdProvider<br>- NotificationService<br>- ValidationService |

**Comments / Refs:**  
- US 01.02.01–01.02.04  

---

***Class*** WaitingListRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Handle join/leave operations with conflict prevention (idempotent joins)<br>- Count and list waiting entrants<br>- Compute remaining pool for lottery draws<br>- Persist optional geo fields for join locations<br>- Enforce waiting list capacity limits<br>- Prevent duplicate entries | - WaitingListEntry<br>- Event<br>- Profile<br>- LotteryService |

**Comments / Refs:**  
- US 01.01.01–01.01.02, US 02.03.01, US 02.05.01  

---

***Class*** SelectionRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Persist selection entries<br>- Update status on accept/decline/cancel/expiry<br>- Provide queries for "remaining pool" vs "already selected"<br>- Return finalized attendee list for export or check-in<br>- Track selection history for analytics<br>- Handle concurrent status updates atomically | - Selection<br>- LotteryService<br>- Event<br>- CancellationManager<br>- AnalyticsService |

**Comments / Refs:**  
- US 01.05.02–01.05.03, US 02.06.01–02.06.03  

---

## Utility Classes

***Class*** CsvExporter

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Generate CSV files for attendee/selected/cancelled lists<br>- Validate columns and delimiters<br>- Handle large lists efficiently (pagination)<br>- Provide save/share functionality<br>- Format data appropriately (escape commas, quotes) | - Organizer<br>- SelectionRepository<br>- EventRepository<br>- AdminService |

**Comments / Refs:**  
- US 02.06.05 (export final list in CSV)  

---

***Class*** Navigator

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Centralize navigation routes within the application (e.g., QR scan → Event Details screen)<br>- Support passing parameters safely between screens<br>- Provide fallback navigation to handle invalid states or missing data<br>- Handle deep link navigation<br>- Maintain navigation history/back stack | - QRService<br>- EventRepository<br>- DeepLinkHandler |

**Comments / Refs:**  
- US 01.06.01–01.06.02  

---

***Class*** ErrorHandler

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Catch and log application errors<br>- Display user-friendly error messages<br>- Handle network errors (offline mode)<br>- Handle Firebase errors (permission denied, timeout)<br>- Provide retry mechanisms for transient failures<br>- Report crashes to Firebase Crashlytics | - All repositories<br>- All services |

**Comments / Refs:**  
- General error handling and user experience  

---


## Class Organization Summary

### Critical Path (Must Implement for Halfway Checkpoint):
1. Event, Profile, WaitingListEntry, Selection
2. EventRepository, ProfileRepository, WaitingListRepository, SelectionRepository
3. LotteryService, NotificationService
4. QRService, QRCodeGenerator
5. Organizer, Navigator
6. ValidationService, PermissionManager

### Secondary Priority (Complete System):
7. AdminService, AnalyticsService
8. PosterStorage, ImageValidator
9. CancellationManager, SearchService
10. NotificationLog, CsvExporter
11. DeviceIdProvider, ErrorHandler

### Optional:
12. GeoService, MapService

---

*Last Updated: Project Part 2 - Initial Design Phase*
