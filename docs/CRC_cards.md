### Event  
**Responsibilities**  
- Hold event metadata (name, description, time/place, posterUrl?, regOpen, regClose, capacity, status)  
- Report registration state: isRegistrationOpen(now), isRegistrationClosed(now)  
- Provide organizer summaries (waiting/selected/cancelled/attendees counts)  
- Validate updates (e.g., regOpen < regClose, capacity ≥ 0)  

**Collaborators**  
- Organizer  
- WaitingListEntry  
- Selection  
- EventRepository  
- PosterStorage  

**Comments**  
- Refs: US 02.01.04, US 01.01.03, US 02.06.01–02.06.03  
- (additional team comments here)  
---
