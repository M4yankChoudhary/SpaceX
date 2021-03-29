package me.mayankchoudhary.spacex.Room;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = Crew.class, version = 1, exportSchema = false)
public abstract class CrewDatabase extends RoomDatabase {

    public abstract DAO dao();

    private static CrewDatabase INSTANCE;

    static CrewDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (Crew.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CrewDatabase.class, "Crew")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

}
