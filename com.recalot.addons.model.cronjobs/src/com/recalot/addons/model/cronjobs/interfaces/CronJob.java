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
package com.recalot.addons.model.cronjobs.interfaces;

/**
 * @author Matthäus Schmedding (info@recalot.com)
 */
public abstract class CronJob implements CronJobInformation {

    private String id;
    private String info;
    private String description;
    private CronJobState state;

    public CronJob(String id, String info, String description) {
        this.id = id;
        this.info = info;
        this.description = description;
    }

    public CronJobState getState() {
        return state;
    }

    public String getId() {
        return id;
    }

    public String getKey() {
        return getId();
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public void setState(CronJobState state) {
        this.state = state;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String getDescription() {
        return description;
    }
}
