package org.apache.syncope.core.logic;

import java.lang.reflect.Method;
import java.util.List;
import org.apache.syncope.common.lib.AbstractBaseBean;
import org.apache.syncope.common.lib.types.AnyTypeKind;
import org.apache.syncope.common.lib.types.AuditElements;
import org.apache.syncope.common.lib.types.StandardEntitlement;
import org.apache.syncope.core.persistence.api.dao.AnySearchDAO;
import org.apache.syncope.core.persistence.api.dao.NotFoundException;
import org.apache.syncope.core.persistence.api.dao.search.AttributeCond;
import org.apache.syncope.core.persistence.api.dao.search.SearchCond;
import org.apache.syncope.core.persistence.api.entity.user.User;
import org.apache.syncope.core.provisioning.api.data.UserDataBinder;
import org.apache.syncope.core.provisioning.api.notification.NotificationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class ReminderLogic extends AbstractTransactionalLogic<AbstractBaseBean> {

    @Autowired
    private AnySearchDAO searchDAO;

    @Autowired
    private UserDataBinder binder;

    @Autowired
    private NotificationManager notificationManager;

    @PreAuthorize("isAnonymous() or hasRole('" + StandardEntitlement.ANONYMOUS + "')")
    public void remindUsername(final String email) {
        AttributeCond emailCond = new AttributeCond(AttributeCond.Type.EQ);
        emailCond.setSchema("email");
        emailCond.setExpression(email);

        List<User> matching = searchDAO.search(SearchCond.getLeafCond(emailCond), AnyTypeKind.USER);
        if (matching.isEmpty()) {
            throw new NotFoundException("User with e-mail address " + email);
        }
        if (matching.size() > 1) {
            LOG.error("Multiple users with e-mail address {}: {}", email, matching);
            throw new IllegalArgumentException("Multiple users with e-mail address " + email);
        }

        User user = matching.get(0);

        notificationManager.createTasks(
                AuditElements.EventCategoryType.CUSTOM,
                null,
                null,
                "remindUsername",
                AuditElements.Result.SUCCESS,
                binder.getUserTO(user, false),
                null);
    }

    @Override
    protected AbstractBaseBean resolveReference(final Method method, final Object... os)
            throws UnresolvedReferenceException {

        throw new UnresolvedReferenceException();
    }

}
