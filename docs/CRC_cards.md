***Class*** Event

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Hold event metadata (name, description, time/place, posterUrl?, regOpen, regClose, capacity, status)<br>- Report registration state: `isRegistrationOpen(now)`, `isRegistrationClosed(now)`<br>- Provide organizer summaries (waiting/selected/cancelled/attendees counts)<br>- Validate updates (e.g., `regOpen < regClose`, `capacity ≥ 0`) | - Organizer<br>- WaitingListEntry<br>- Selection<br>- EventRepository<br>- PosterStorage |

**Comments / Refs:**  
- US 02.01.04, US 01.01.03, US 02.06.01–02.06.03  
- (additional team comments here)

***Class*** Profile

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent device-identified entrant (name, email, phone?, notificationsOptIn)<br>- Maintain participation history (registered/selected outcomes)<br>- Update and delete profile (right to leave the app) | - DeviceIdProvider<br>- ProfileRepository<br>- NotificationService |

**Comments / Refs:**  
- US 01.02.01–01.02.04  
- (additional team comments here)

***Class*** WaitingListEntry

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Record join/leave for a profile on an event (joinedAt, optional joinGeo)<br>- Prevent duplicate joins and expose presence/status<br>- Provide organizer counts and listing | - Event<br>- Profile<br>- WaitingListRepository<br>- GeoService |

**Comments / Refs:**  
- US 01.01.01–01.01.02, US 01.05.04  
- (additional team comments here)

***Class*** Selection

| **Responsibilities** | **Collaborators** |
|----------------------|-------------------|
| - Represent an invitation (selectedAt, expiresAt, status: PENDING / ACCEPTED / DECLINED / EXPIRED)<br>- Manage transitions: ACCEPTED → attendees, DECLINED/EXPIRED → backfill pool<br>- Expose invitation status for entrant and organizer views | - LotteryService<br>- SelectionRepository<br>- NotificationService<br>- Event |

**Comments / Refs:**  
- US 01.05.02, US 01.05.03, US 02.06.01–02.06.03  
- (additional team comments here)
