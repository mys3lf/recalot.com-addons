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
import com.recalot.addons.model.cronjobs.CronJob;
import com.recalot.addons.model.cronjobs.CronJobsAccess;
import com.recalot.common.GenericControllerListener;
import com.recalot.common.GenericServiceListener;
import com.recalot.common.Helper;
import com.recalot.common.builder.DataSplitterBuilder;
import com.recalot.common.builder.MetricBuilder;
import com.recalot.common.builder.RecommenderBuilder;
import com.recalot.common.communication.TemplateResult;
import com.recalot.common.context.ContextProvider;
import com.recalot.common.exceptions.AlreadyExistsException;
import com.recalot.common.exceptions.BaseException;
import com.recalot.common.impl.experiment.Experiment;
import com.recalot.common.interfaces.controller.RecommenderController;
import com.recalot.common.interfaces.controller.RequestAction;
import com.recalot.common.interfaces.model.data.DataAccess;
import com.recalot.common.interfaces.model.data.DataSource;
import com.recalot.common.interfaces.model.experiment.DataSplitter;
import com.recalot.common.interfaces.model.experiment.ExperimentAccess;
import com.recalot.common.interfaces.model.experiment.Metric;
import com.recalot.common.interfaces.model.experiment.OnlineExperiment;
import com.recalot.common.interfaces.model.rec.Recommender;
import com.recalot.common.interfaces.template.ExperimentTemplate;
import org.osgi.framework.BundleContext;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author matthaeus.schmedding
 */
public class CronJobsController implements Closeable {

    private final BundleContext context;
    private final GenericServiceListener<CronJobsTemplate> templates;
    private final GenericServiceListener<CronJobsAccess> access;


    public CronJobsController(BundleContext context) {
        this.context = context;

        this.templates = new GenericServiceListener(context, CronJobsTemplate.class.getName());
        this.access = new GenericServiceListener(context, CronJobsAccess.class.getName());

        this.context.addServiceListener(templates);
    }

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
        return null;
    }

    private TemplateResult deleteCronJob(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        return null;
    }

    private TemplateResult getCronJob(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        return null;
    }

    private TemplateResult getCronJobs(CronJobsTemplate template, Map<String, String> param) throws BaseException {
        CronJobsAccess cra = access.getFirstInstance();
        List<CronJob> jobs = cra.getCronJobs();
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
        CreateCronJob(3);

        private final int value;

        public int getValue() {
            return this.value;
        }

        private CronJobsRequestAction(int value) {
            this.value = value;
        }
    }
}
