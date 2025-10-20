# Event-App CRC Cards
---

***Class*** Event

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Hold event metadata (name, description, time/place, posterUrl?, regOpen, regClose, capacity, status)<br>- Report registration state: `isRegistrationOpen(now)`, `isRegistrationClosed(now)`<br>- Provide organizer summaries (waiting/selected/cancelled/attendees counts)<br>- Validate updates (e.g., `regOpen < regClose`, `capacity ≥ 0`) | - Organizer<br>- WaitingListEntry<br>- Selection<br>- EventRepository<br>- PosterStorage |

**Comments / Refs:**  
- US 02.01.04, US 01.01.03, US 02.06.01–02.06.03  

---

***Class*** Profile

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent device-identified entrant (name, email, phone?, notificationsOptIn)<br>- Maintain participation history (registered/selected outcomes)<br>- Update and delete profile (right to leave the app) | - DeviceIdProvider<br>- ProfileRepository<br>- NotificationService |

**Comments / Refs:**  
- US 01.02.01–01.02.04  

---

***Class*** WaitingListEntry

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Record join/leave for a profile on an event (joinedAt, optional joinGeo)<br>- Prevent duplicate joins and expose presence/status<br>- Provide organizer counts and listing | - Event<br>- Profile<br>- WaitingListRepository<br>- GeoService |

**Comments / Refs:**  
- US 01.01.01–01.01.02, US 01.05.04  

---

***Class*** Selection

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent an invitation (selectedAt, expiresAt, status: PENDING / ACCEPTED / DECLINED / EXPIRED)<br>- Manage transitions: ACCEPTED → attendees, DECLINED/EXPIRED → backfill pool<br>- Expose invitation status for entrant and organizer views | - LotteryService<br>- SelectionRepository<br>- NotificationService<br>- Event |

**Comments / Refs:**  
- US 01.05.02, US 01.05.03, US 02.06.01–02.06.03  

---

***Class*** LotteryService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Draw N entrants without replacement after registration closes<br>- Run replacement draws on declines, cancellations, or expiry<br>- Persist results atomically and ensure idempotency | - Event<br>- WaitingListRepository<br>- SelectionRepository<br>- NotificationService |

**Comments / Refs:**  
- US 02.05.02, US 02.05.03, US 01.05.01  

---

***Class*** NotificationService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Send “win/lose” notifications and organizer bulk messages<br>- Manage device tokens, notification topics, and opt-in/opt-out settings<br>- Log each notification send for admin review | - Profile<br>- LotteryService<br>- AdminService<br>- NotificationLog |

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
| - Parse scanned QR/promotional codes and resolve them to eventId<br>- Trigger deep-link navigation to specific Event Details<br>- Validate QR code format and handle invalid or expired codes | - EventRepository<br>- Navigator |

**Comments / Refs:**  
- US 01.06.01, US 01.06.02, US 02.01.01  

---

***Class*** Organizer

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Create and edit events; set registration window and capacity; publish or unpublish events<br>- Upload or update event posters<br>- View waiting, selected, cancelled, and attendee lists<br>- Export CSV of participants; send bulk messages to entrants | - EventRepository<br>- PosterStorage<br>- WaitingListRepository<br>- SelectionRepository<br>- CsvExporter<br>- NotificationService |

**Comments / Refs:**  
- US 02.01.01, US 02.01.04, US 02.02.01–02.02.03, US 02.06.01–02.06.05  

---

***Class*** AdminService

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Browse or remove events, profiles, and images<br>- Remove or restrict organizers<br>- Review notification logs and system activity | - EventRepository<br>- ProfileRepository<br>- PosterStorage<br>- NotificationLog |

**Comments / Refs:**  
- US 03.01.01–03.07.01, US 03.08.01  

---

***Class*** PosterStorage

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Upload or replace poster images and return stable URLs<br>- List and browse poster images for organizers/admins<br>- Support deletion of images<br>- Enforce file type and size constraints | - Event<br>- Organizer<br>- AdminService |

**Comments / Refs:**  
- US 02.04.01–02.04.02, US 03.03.01  

---

***Class*** GeoService (optional)

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Capture coarse join location when organizer requires and user consents<br>- Provide aggregated map views to organizers | - WaitingListEntry<br>- Organizer |

**Comments / Refs:**  
- US 02.02.02, US 02.02.03  

---

***Class*** DeviceIdProvider

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Provide stable device-based identity for login-less profiles<br>- Detect first-run vs returning users<br>- Surface device integrity or validation checks | - ProfileRepository |

**Comments / Refs:**  
- US 01.07.01  

---

***Class*** EventRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - CRUD operations for events; publish/unpublish events<br>- Fetch event lists and event details<br>- Query by registration state or time; provide organizer dashboards/summaries<br>- Persist event data to Firebase (events, attendees, etc.) | - Event<br>- Organizer<br>- WaitingListRepository<br>- SelectionRepository |

**Comments / Refs:**  
- Firebase data storage for events  

---

***Class*** ProfileRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - CRUD profiles keyed by deviceId<br>- Manage notification tokens for delivery<br>- Retrieve or update participation history<br>- Delete profile data upon user request | - Profile<br>- DeviceIdProvider<br>- NotificationService |

**Comments / Refs:**  
- US 01.02.01–01.02.04  

---

***Class*** WaitingListRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Handle join/leave operations with conflict prevention (idempotent joins)<br>- Count and list waiting entrants<br>- Compute remaining pool for lottery draws<br>- Persist optional geo fields for join locations | - WaitingListEntry<br>- Event<br>- Profile |

**Comments / Refs:**  
- US 01.01.01–01.01.02, US 02.05.01  

---

***Class*** SelectionRepository

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Persist selection entries<br>- Update status on accept/decline/cancel/expiry<br>- Provide queries for "remaining pool" vs "already selected"<br>- Return finalized attendee list for export or check-in | - Selection<br>- LotteryService<br>- Event |

**Comments / Refs:**  
- US 01.05.02–01.05.03, US 02.06.01–02.06.03  

---

***Class*** CsvExporter

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Generate CSV files for attendee/selected lists<br>- Validate columns and delimiters<br>- Handle large lists efficiently<br>- Provide save/share functionality | - Organizer<br>- SelectionRepository<br>- EventRepository |

**Comments / Refs:**  
- US 02.06.05  

---

***Class*** Navigator

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Centralize navigation routes within the application (e.g., QR scan → Event Details screen)<br>- Support passing parameters safely between screens<br>- Provide fallback navigation to handle invalid states or missing data | - QRService<br>- EventRepository |

**Comments / Refs:**  
- US 01.06.01–01.06.02  


