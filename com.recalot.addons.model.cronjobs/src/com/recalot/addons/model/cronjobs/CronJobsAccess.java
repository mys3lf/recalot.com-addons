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

import com.recalot.addons.model.cronjobs.interfaces.CronJob;
import com.recalot.addons.model.cronjobs.interfaces.CronJobInformation;
import com.recalot.common.GenericServiceListener;
import com.recalot.common.communication.Message;
import com.recalot.common.exceptions.BaseException;
import org.osgi.framework.BundleContext;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Matthäus Schmedding (info@recalot.com)
 */
public class CronJobsAccess implements com.recalot.common.communication.Service {
    private final GenericServiceListener<CronJobBuilder> cronJobBuilder;
    private HashMap<String, CronJob> cronJobs;
    private BundleContext context;

    public CronJobsAccess(BundleContext context) {
        this.context = context;
        this.cronJobBuilder = new GenericServiceListener<>(context, CronJobBuilder.class.getName());
    }

    public List<CronJobInformation> getCronJobs() throws BaseException {
        List<CronJobInformation> list = new ArrayList<>();
        list.addAll(cronJobs.values());
        list.addAll(cronJobBuilder.getAll());

        return list;
    }

    @Override
    public String getKey() {
        return "cron-jobs";
    }

    @Override
    public String getDescription() {
        return "Delivers cron jobs builder for different use cases and initialized cron jobs.";
    }

    @Override
    public void close() throws IOException {

    }

    public CronJobInformation getCronJob(String id) {
        return null;
    }

    public Message deleteCronJob(String id) {
        return null;
    }

    public CronJobInformation createCronJob(String id, Map<String, String> param) {
        return null;
    }
}
