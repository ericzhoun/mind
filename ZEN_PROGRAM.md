# New Program Added: 7-Day Zen Meditation

## ✅ Feature Completed

Added a **"7-Day Zen Meditation"** program database seed.

### Program Details 🌸

- **Source**: Dharma Drum Mountain (法鼓山網路禪修)
- **Duration**: 7 Days
- **Structure**:
  - **Daily Dharma Talk** (Video): Simplifies Zen concepts like "Purity of Mind", "Wisdom of No-Self", and "Cause and Effect".
  - **Daily Zazen** (Meditation): A scheduled 30-minute meditation session (07:00 - 07:30) to practice the concepts.

### Video Tracklist 📹

1. **Day 1**: Purity of Mind, Purity of Land (心淨國土淨)
2. **Day 2**: Maintaining Mindfulness (如何保持正念)
3. **Day 3**: Chan Records (禪宗語錄)
4. **Day 4**: Wisdom of No-Self (無我的智慧)
5. **Day 5**: Peace of Mind at Work (安心安業)
6. **Day 6**: Right Intention (正確的禪修發心)
7. **Day 7**: Cause and Effect (因緣因果)

## 🚀 How to Test

1. **Clear Database**: Since this is a seeding change, you may need to clear app data or increment database version (if not using debug builds that clear on install) to trigger the `SeedDatabaseUseCase` again.
2. **Dashboard**: Verify "7-Day Zen Meditation" appears in the programs list.
3. **Day View**: Check Day 1 has both a Video task and a "Daily Zazen" task.
