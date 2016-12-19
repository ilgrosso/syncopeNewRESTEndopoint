package org.apache.syncope.common.rest.api.service;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

/**
 * REST operations for reminders.
 */
@Path("users/reminder")
public interface ReminderService extends JAXRSService {

    /**
     * Sends an e-mail with his / her username to the user identified by the provided e-mail address.
     *
     * @param email e-mal address identifying a given user
     */
    @GET
    void remindUsername(@NotNull @QueryParam("email") String email);
}
