# Task Resumption Feature

## ✅ Feature Completed

**"Resume Meditation Tasks"** has been implemented.

### 1. Core Functionality 🔄

Users can now start a timed task (e.g., Meditation), pause it, or leave the screen, and their progress will be saved. When they return, the timer resumes from where they left off.

### 2. Implementation Details 🛠️

#### **ViewModel (`TaskDetailViewModel`)**

- Now fetches `UserProgress` alongside the Task details.
- Added `saveProgress(minutes: Int)` function:
  - Updates `UserProgress` in the database.
  - Maintains `TaskStatus.PENDING` (does not complete the task).

#### **UI (`TaskDetailScreen`)**

- **Initialization**: Timer starts at `saved_minutes * 60` seconds.
- **Auto-Save Triggers**:
  1. **Pause Button**: Progress is saved immediately when pausing.
  2. **Screen Exit**: Progress is saved if the user navigates back (`DisposableEffect`).

### 3. User Experience 👤

- **Scenario**: User starts 2-hour meditation.
- **Action**: Stops at 45 mins to take a break. Exits app.
- **Return**: App shows "45 min" progress in dashboard (via Percentage feature).
- **Resume**: Opens task again. Timer explicitly shows `00:45:00`.
- **Result**: User continues to 2 hours.

## 🚀 Technical Notes

- **Precision**: Saved resolution is in **minutes** (database schema limitation), so seconds reset to `:00` on resume. This is acceptable for long-duration tasks like meditation.
- **Data Persistence**: Uses `updateTaskStatusUseCase` with partial updates.
