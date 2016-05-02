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

package com.recalot.addons.controller.cronjobs;


import com.recalot.addons.controller.cronjobs.templates.CronJobsTemplate;
import com.recalot.addons.model.cronjobs.interfaces.CronJob;
import com.recalot.addons.model.cronjobs.CronJobsAccess;
import com.recalot.addons.model.cronjobs.interfaces.CronJobInformation;
import com.recalot.common.GenericServiceListener;
import com.recalot.common.Helper;
import com.recalot.common.communication.TemplateResult;
import com.recalot.common.exceptions.BaseException;
import com.recalot.common.interfaces.controller.Controller;
import com.recalot.common.interfaces.controller.RequestAction;
import org.osgi.framework.BundleContext;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * @author matthaeus.schmedding
 */
public class CronJobsController implements Controller, Closeable {

    private final BundleContext context;
    private final GenericServiceListener<CronJobsTemplate> templates;
    private final GenericServiceListener<CronJobsAccess> access;


    public CronJobsController(BundleContext context) {
        this.context = context;

        this.templates = new GenericServiceListener(context, CronJobsTemplate.class.getName());
        this.access = new GenericServiceListener(context, CronJobsAccess.class.getName());

        this.context.addServiceListener(templates);
    }

    @Override
    public TemplateResult process(RequestAction action, String templateKey, Map<String, String> param) throws BaseException {

        CronJobsTemplate template = templates.getInstance(templateKey);
        TemplateResult result = null;

        try {
            switch ((CronJobsRequestAction) action) {
                case GetCronJobs: {
                    result = getCronJobs(template, param);
                    break;
                }
                case GetCronJob: {
                    result = getCronJob(template, param);
                    break;
                }
                case GetCronJobBuilder: {
                    result = getCronJobBuilder(template, param);
                    break;
                }
                case DeleteCronJob: {
                    result = deleteCronJob(template, param);
                    break;
                }
                case CreateCronJob: {
                    result = createCronJob(template, param);
                    break;
                }
            }
        } catch (BaseException ex) {
            result = template.transform(ex);
        }

        return result;
    }

    private TemplateResult createCronJob(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();

        return template.transform(cra.createCronJob(param.get(Helper.Keys.ID), param));
    }

    private TemplateResult deleteCronJob(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();

        return template.transform(cra.deleteCronJob(param.get(Helper.Keys.ID)));
    }

    private TemplateResult getCronJob(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();

        return template.transform(cra.getCronJob(param.get(Helper.Keys.ID)));
    }

    private TemplateResult getCronJobBuilder(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();

        return template.transform(cra.getCronJobBuilder(param.get(Helper.Keys.ID)));
    }

    private TemplateResult getCronJobs(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();
        List<CronJobInformation> jobs = cra.getCronJobs();

        if (param.containsKey(Helper.Keys.State)) {

            CronJob.CronJobState state = CronJob.CronJobState.valueOf(param.get(Helper.Keys.State));
            List<CronJobInformation> temp = new ArrayList<>();
            for (CronJobInformation info : jobs) {
                if (info.getState() == state) {
                    temp.add(info);
                }
            }

            return template.transform(temp);
        }

        return template.transform(jobs);
    }

    @Override
    public void close() throws IOException {

        if (templates != null) {
            this.context.removeServiceListener(templates);
        }
    }

    public static enum CronJobsRequestAction implements RequestAction {
        GetCronJobs(0),
        GetCronJob(1),
        DeleteCronJob(2),
        CreateCronJob(3),
        GetCronJobBuilder(4);

        private final int value;

        public int getValue() {
            return this.value;
        }

        private CronJobsRequestAction(int value) {
            this.value = value;
        }
    }
}
