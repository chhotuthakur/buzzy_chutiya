package com.video.buzzy.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.video.buzzy.workers.Draft;
import com.video.buzzy.workers.DraftDao;


@Database(entities = {Draft.class}, version = 3, exportSchema = false)
public abstract class ClientDatabase extends RoomDatabase {

    public abstract DraftDao drafts();
}
