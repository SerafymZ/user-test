package com.usertest.counter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SavedUsersCounterImpl implements SavedUsersCounter{

    private Logger logger = LoggerFactory.getLogger(getClass().getName());
    private int savedUsersCount = 0;

    @Override
    public void onUserSave() {
        logger.info("TEST_TAG!!! User saved successfully. Saved users number = " + ++savedUsersCount);
    }

    @Override
    public int getSavedUsersCount() {
        return savedUsersCount;
    }
}
