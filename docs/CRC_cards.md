| **Class** | Event |
|-----------|--------|
| **Responsibilities** | **Collaborators**  |
| - Hold event metadata (name, description, time/place, posterUrl?, regOpen, regClose, capacity, status)<br>- Report registration state: `isRegistrationOpen(now)`, `isRegistrationClosed(now)`<br>- Provide organizer summaries (waiting/selected/cancelled/attendees counts)<br>- Validate updates (e.g., `regOpen < regClose`, `capacity ≥ 0`) | - Organizer<br>- WaitingListEntry<br>- Selection<br>- EventRepository<br>- PosterStorage |
| **Comments / Refs** | - Refs: US 02.01.04, US 01.01.03, US 02.06.01–02.06.03<br>- (additional team comments here) |
