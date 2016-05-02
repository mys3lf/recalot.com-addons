// Copyright (C) 2016 Matthäus Schmedding
//
// This file is part of recalot.com.
//
// recalot.com is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// recalot.com is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with recalot.com. If not, see <http://www.gnu.org/licenses/>.

package com.recalot.addons.model.cronjobs;

import com.recalot.addons.model.cronjobs.jobs.TestCronJob;
import com.recalot.common.builder.DataSourceBuilder;
import com.recalot.common.builder.Initiator;
import com.recalot.common.configuration.Configuration;
import com.recalot.common.configuration.ConfigurationItem;
import com.recalot.common.configuration.Configurations;
import com.recalot.common.exceptions.BaseException;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Matthaeus.schmedding
 */
public class Activator implements BundleActivator, Initiator {

    private CronJobsAccess access;
    private ArrayList<CronJobBuilder> cronJobBuilder;

    /**
     * Implements BundleActivator.start(). Prints
     * a message and adds itself to the bundle context as a service
     * listener.
     *
     * @param context the framework context for the bundle.
     */
    public void start(BundleContext context) {
        this.access = new CronJobsAccess(context);
        context.registerService(CronJobsAccess.class.getName(), access, null);

        cronJobBuilder = new ArrayList<>();

        HashMap<String, String> cronJobs = new HashMap<>();
        cronJobs.put("test", TestCronJob.class.getName());

        for(String key : cronJobs.keySet()) {
            try {
                CronJobBuilder builder = new CronJobBuilder(this, cronJobs.get(key), key, "");
                builder.appendConfiguration(getConfigurationItems(cronJobs.get(key)));
                cronJobBuilder.add(builder);
            } catch (BaseException e) {
                e.printStackTrace();
            }
        }

        for (CronJobBuilder c : cronJobBuilder) {
            context.registerService(CronJobBuilder.class.getName(), c, null);
        }
    }

    /**
     * Implements BundleActivator.stop(). Prints
     * a message and removes itself from the bundle context as a
     * service listener.
     *
     * @param context the framework context for the bundle.
     */
    public void stop(BundleContext context) throws Exception {
        if(access != null) access.close();

        if (cronJobBuilder != null) {
            for (CronJobBuilder c : cronJobBuilder) {
                c.close();
            }
            cronJobBuilder = null;
        }
    }


    @Override
    public Object createInstance(String className) {
        try {
            Class c = Class.forName(className);
            return c.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static ArrayList<ConfigurationItem> getConfigurationItems(String className) {
        Map<String, ConfigurationItem> items = new HashMap<>();

        try {
            Class recommender = Class.forName(className);

            while (recommender != null) {


                if (recommender.isAnnotationPresent(Configuration.class)) {
                    com.recalot.common.configuration.Configuration config = (Configuration) recommender.getAnnotation(Configuration.class);

                    if (config != null && !items.containsKey(config.key())) {
                        items.put(config.key(), new ConfigurationItem(config.key(), config.type(), config.value(), config.requirement(), config.description(), new ArrayList<>(Arrays.asList(config.options()))));
                    }
                }

                if (recommender.isAnnotationPresent(Configurations.class)) {

                    com.recalot.common.configuration.Configuration annotations[] = ((Configurations) recommender.getAnnotation(Configurations.class)).value();

                    for (com.recalot.common.configuration.Configuration t : annotations) {
                        if (!items.containsKey(t.key())) {
                            items.put(t.key(), new ConfigurationItem(t.key(), t.type(), t.value(), t.requirement(), t.description(), new ArrayList<>(Arrays.asList(t.options()))));
                        }
                    }
                }

                recommender = recommender.getSuperclass();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoClassDefFoundError e) {
            e.printStackTrace();
        }

        return new ArrayList<>(items.values());
    }
}