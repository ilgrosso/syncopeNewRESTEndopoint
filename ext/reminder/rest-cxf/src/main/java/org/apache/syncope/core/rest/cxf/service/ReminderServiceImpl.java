package org.apache.syncope.core.rest.cxf.service;

import org.apache.syncope.common.rest.api.service.ReminderService;
import org.apache.syncope.core.logic.ReminderLogic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReminderServiceImpl extends AbstractServiceImpl implements ReminderService {

    @Autowired
    private ReminderLogic logic;

    @Override
    public void remindUsername(final String email) {
        logic.remindUsername(email);
    }

}
