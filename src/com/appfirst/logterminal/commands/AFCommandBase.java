/*
 * Copyright 2009-2011 AppFirst, Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.appfirst.logterminal.commands;

import java.util.Calendar;

/**
 * @author Bin Liu
 *
 */
public abstract class AFCommandBase {
	protected String commandName = "AFCommandBase";
	
	public abstract void execute(String arg);
	
	protected Long getTimeArg(String arg, String prefix) {
		Long ret = 0L;
		String startString = arg.replace(prefix, "").trim();
		if (startString.contains(":")) {
			ret = createDate(startString.split(":"));
		} else {
			ret = Long.parseLong(startString);
		}
		return ret;
	}

	protected Long createDate(String[] timePart) {
		Calendar calendar = Calendar.getInstance();
		try {
			Integer hour = 0;
			Integer minute = 0;
			Integer year = Integer.parseInt(timePart[0]);
			Integer month = Integer.parseInt(timePart[1]);
			Integer day = Integer.parseInt(timePart[2]);
			if (timePart.length > 3) {
				hour = Integer.parseInt(timePart[3]);
				if (timePart.length > 4) {
					minute = Integer.parseInt(timePart[4]);
				}
			}
			calendar.set(year, month, day, hour, minute);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return calendar.getTimeInMillis();
	}
}