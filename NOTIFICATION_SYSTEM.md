# Notification System Implementation

## ✅ Completed Features

### 1. **Time-Driven Notifications**

- **TimedTasks** (e.g., Morning Meditation 4:30-6:30 AM):
  - Reminder notification **5 minutes before** start time
  - Example: 4:25 AM notification for 4:30 AM meditation

- **Non-Timed Tasks** (ChecklistTask, VideoTask, MetricTask, JournalTask):
  - **Two daily reminders** at configurable times
  - **Default times**: 12:00 PM and 8:00 PM
  - Fully configurable in settings (coming next)

### 2. **Notification System Components**

#### Core Components Created

1. **`NotificationHelper`** - Manages notification channels and displays
2. **`TaskReminderReceiver`** - BroadcastReceiver for alarm callbacks
3. **`NotificationScheduler`** - Schedules/cancels all task reminders
4. **`UserPreferences`** - Data model for notification settings
5. **`PreferencesRepository`** - DataStore-based preference storage

#### Android Manifest Updates

- ✅ `POST_NOTIFICATIONS` permission (Android 13+)
- ✅ `SCHEDULE_EXACT_ALARM` permission for precise timing
- ✅ `USE_EXACT_ALARM` permission
- ✅ Registered `TaskReminderReceiver`

### 3. **Automatic Scheduling**

- **When**: Notifications are scheduled when user views a program day
- **Integration**: ProgramDetailViewModel automatically schedules on day selection
- **Smart scheduling**: Only schedules future notifications (skips past times)

### 4. **Notification Features**

- **High priority** notifications with vibration and lights
- **Tap to open** - Opens app to specific task
- **Auto-dismiss** - Notifications clear when tapped
- **Contextual messages**:
  - Timed tasks: "Starting at HH:MM"
  - Non-timed tasks: "Time to complete this task"

### 5. **Database & Preferences**

- Uses **Jetpack DataStore** for persistent preferences
- **Type-safe** preference storage
- **Reactive** - Preferences update automatically via Flow

## 📱 User Preferences Structure

```kotlin
data class UserPreferences(
    val nonTimedTaskReminderTime1: String = "12:00", // 12 PM
    val nonTimedTaskReminderTime2: String = "20:00", // 8 PM
    val notificationsEnabled: Boolean = true,
    val timedTaskReminderMinutesBefore: Int = 5
)
```

## 🔄 How It Works

### For TimedTask (Meditation 04:30-06:30)

1. Parse start time: 04:30
2. Subtract reminder minutes: 04:30 - 5min = 04:25
3. Schedule AlarmManager for 04:25
4. At 04:25, TaskReminderReceiver triggers
5. NotificationHelper shows: "Morning Meditation - Starting at 04:30"

### For Non-TimedTask (e.g., Video Task)

1. Check user preferences for reminder times (default: 12:00, 20:00)
2. Schedule TWO separate alarms:
   - Alarm 1: 12:00 PM
   - Alarm 2: 8:00 PM
3. At each time, show notification: "Watch Discourse - Time to complete this task"

## 🎯 Next Step: Settings Page

### What's Needed

Create a **Settings Screen** with:

- ✅ Toggle to enable/disable notifications
- ✅ Time picker for non-timed task reminder 1 (default: 12:00 PM)
- ✅ Time picker for non-timed task reminder 2 (default: 8:00 PM)
- ✅ Number input for timed task reminder (minutes before, default: 5)

### Navigation

- Add Settings menu item to main screen
- Navigate to SettingsScreen
- All changes save automatically to DataStore

Would you like me to implement the Settings Screen UI now?
