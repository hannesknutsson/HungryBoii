package com.github.hannesknutsson.hungryboii.utilities.managers.implementations;

import com.github.hannesknutsson.hungryboii.structure.dataclasses.DiscordUser;
import com.github.hannesknutsson.hungryboii.structure.dataclasses.LunchSubscription;
import com.github.hannesknutsson.hungryboii.utilities.managers.abstractions.MappingManager;
import com.github.hannesknutsson.hungryboii.utilities.statichelpers.database.hibernate.EntityCoupler;
import com.github.hannesknutsson.hungryboii.utilities.workers.ScheduledMenu;
import net.dv8tion.jda.api.entities.User;
import org.hibernate.Session;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class SubscriptionManager extends MappingManager<Long, ScheduledFuture> {

    private static Logger LOG = LoggerFactory.getLogger(SubscriptionManager.class);

    private ScheduledExecutorService scheduler;

    private SubscriptionManager() {
        this.scheduler = Executors.newSingleThreadScheduledExecutor();

        try (Session temporarySession = EntityCoupler.getInstance().getSession()) {

            CriteriaBuilder criteriaBuilder = temporarySession.getCriteriaBuilder();
            CriteriaQuery<DiscordUser> discordUserCriteriaQuery = criteriaBuilder.createQuery(DiscordUser.class);
            Root<DiscordUser> discordUserRootEntry = discordUserCriteriaQuery.from(DiscordUser.class);
            CriteriaQuery<DiscordUser> allDiscordUsers = discordUserCriteriaQuery.select(discordUserRootEntry);
            TypedQuery<DiscordUser> allDiscordUsersQuery = temporarySession.createQuery(allDiscordUsers);
            List<DiscordUser> userList = allDiscordUsersQuery.getResultList();

            userList.stream().forEach(this::registerIfRelevant);
        }
    }

    public static void initialize() {
        getInstance();
    }

    public static SubscriptionManager getInstance() {
        if (instance == null) {
            instance = new SubscriptionManager();
        }
        return instance;
    };

    private static SubscriptionManager instance;

    @Override
    public String getManagerType() {
        return "Subscription";
    }

    public void registerIfRelevant(DiscordUser user) {
        if (user.getLunchSubscription() != null) {
            LunchSubscription subscription = user.getLunchSubscription();
            ScheduledMenu scheduledMenu = new ScheduledMenu(user.getId());

            DateTime timeToSendPlusOneDay = new DateTime(DateTimeZone.getDefault())
                    .withHourOfDay(subscription.getTime().hour)
                    .withMinuteOfHour(subscription.getTime().minute)
                    .withSecondOfMinute(0)
                    .withMillisOfSecond(0)
                    .plusDays(1);

            DateTime now = new DateTime(DateTimeZone.getDefault());

            long oneDayInMillis = 24 * 60 * 60 * 1000;
            long initialDelayInMillis = (timeToSendPlusOneDay.getMillis() - now.getMillis()) % oneDayInMillis;

            ScheduledFuture future = scheduler.scheduleAtFixedRate(scheduledMenu, initialDelayInMillis, oneDayInMillis, TimeUnit.MILLISECONDS);

            unRegister(user.getId());
            register(user.getId(), future);

            LOG.info("Registered daily subscription for {} at {}", user, user.getLunchSubscription());
        } else {
            //Not relevant to register
        }
    }

    @Override
    public void unRegister(Long toUnRegister) {
        ScheduledFuture toCancel = getRegisteredObjects().remove(toUnRegister);
        if (toCancel != null) {
            LOG.info("Unregistered daily subscription for {}", toUnRegister);
            toCancel.cancel(false);
        }
    }
}
