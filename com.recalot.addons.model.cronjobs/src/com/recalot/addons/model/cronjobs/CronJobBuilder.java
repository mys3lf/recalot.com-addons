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
import com.recalot.common.Helper;
import com.recalot.common.builder.Initiator;
import com.recalot.common.builder.InstanceBuilder;
import com.recalot.common.configuration.ConfigurationItem;
import com.recalot.common.exceptions.BaseException;

/**
 * Is used for the initialization of CronJobs
 * @author Matthäus Schmedding (info@recalot.com)
 */
public class CronJobBuilder extends InstanceBuilder<CronJob> implements CronJobInformation {

    /**
     * Initialize the CronJobBuilder and the default configuration
     *
     * @param initiator the instance that initialize the default instance of the datasource
     * @param className the class name of the data source instance
     * @param key the key of the cron job builder
     * @param description the description data source builder
     * @throws com.recalot.common.exceptions.BaseException
     */
    public CronJobBuilder(Initiator initiator, String className, String key, String description) throws BaseException {
        super(initiator, className, key, description);

        setConfiguration(new ConfigurationItem(Helper.Keys.SourceId, ConfigurationItem.ConfigurationItemType.String, key, ConfigurationItem.ConfigurationItemRequirementType.Required, ""));
        setConfiguration(new ConfigurationItem(Helper.Keys.DataBuilderId, ConfigurationItem.ConfigurationItemType.String, key, ConfigurationItem.ConfigurationItemRequirementType.Hidden));
    }

    /**
     *
     * @return the current state (always "AVAILABLE")
     */
    @Override
    public CronJobInformation.CronJobState getState() {
        return CronJobInformation.CronJobState.AVAILABLE;
    }

    /**
     *
     * @return the id of the data source builder
     */
    @Override
    public String getId() {
        return "data-builder-" + getKey();
    }

    @Override
    public String getInfo() {
        return "";
    }
}
