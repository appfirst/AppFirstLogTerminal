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

import java.util.List;

import com.appfirst.communication.AFClient;
import com.appfirst.communication.Helper;
import com.appfirst.datatypes.LogSummaryData;
import com.appfirst.logterminal.clientMain.AFLogTerminal;

/**
 * @author Bin Liu
 * 
 */
public class AFLogSummaryCommand extends AFCommandBase {
	private AFClient client;
	private List<LogSummaryData> list;

	public AFLogSummaryCommand(AFClient afClient) {
		this.client = afClient;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.appfirst.logterminal.commands.AFCommandBase#execute(java.lang.String)
	 */
	@Override
	public void execute(String arg) {
		Long start = 0L;
		Long end = 0L;
		int num = 1;
		int id = 0;
		// TODO Auto-generated method stub
		try {

			String[] args = arg.split(" ");
			id = Integer.parseInt(args[1]);
			for (int cnt = 2; cnt < args.length; cnt++) {
				if (args[cnt].startsWith("start=")) {
					start = getTimeArg(args[cnt], "start=");
				} else if (args[cnt].startsWith("end=")) {
					end = getTimeArg(args[cnt], "end=");
				} else if (args[cnt].startsWith("num=")) {
					num = Integer
							.parseInt(args[cnt].replace("num=", "").trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (id <= 0) {
			System.out.println(String.format("Invalid id %d", id));
			return;
		}

		String url = String.format("%s/%d/data/", AFLogTerminal.baseUrl, id);
		if (num > 1) {
			url = String.format("%s?num=%d", url, num);
		}
		System.out.println("Querying...");
		list = client.getLogSummaryList(url);
		System.out.println("Done");
		System.out.println(String.format("Log summary for log id: %d ", id));
		print();
	}

	protected void print() {
		System.out.println(String.format(
				"-------------Log summary count: %d------------", list.size()));
		for (int cnt = 0; cnt < list.size(); cnt++) {
			System.out.println(String.format("%s", Helper
					.formatLongTime(list.get(cnt).getTime() * 1000)));
			System.out.println(String.format("    Info: %s", list.get(cnt)
					.getNum_info().toString()));
			System.out.println(String.format("    Warning:  %s", list.get(cnt)
					.getNum_warning().toString()));
			System.out.println(String.format("    Critical: %s", list.get(cnt)
					.getNum_critical().toString()));
		}
		System.out
				.println("-------------use detail command to see detail----------");
	}
}
