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

import com.appfirst.logterminal.clientMain.AFLogTerminal;
import com.appfirst.types.LogEntry;

/**
 * @author Bin Liu
 * 
 */
public class AFLogListCommand extends AFCommandBase {
	private List<LogEntry> list = null;

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.appfirst.logterminal.commands.AFCommandBase#execute(java.lang.String)
	 */
	@Override
	public void execute(String arg) {
		// TODO Auto-generated method stub
		System.out.println("Querying...");
		list = AFLogTerminal.client.getLogList(String.format("%s/%s", AFLogTerminal.baseUrl,
				AFLogTerminal.logUrl));
		print();
	}

	private void print() {
		System.out
				.println(String.format(
						"---------------%d logs in total.---------------", list
								.size()));
		for (int cnt = 0; cnt < list.size(); cnt++) {
			System.out.println(String.format("-Log %d", list.get(cnt).getId()));
			System.out.println(String.format("     hostname: %s",
					AFLogTerminal.serverNameMap.get(list.get(cnt)
							.getServer_id())));
			System.out.println(String.format("     source: %s", list.get(cnt)
					.getSource()));
			System.out.println(String.format("     type: %s", list.get(cnt)
					.getType()));
			System.out.println(String.format("     filter: %s", list.get(cnt)
					.getFilter()));
			System.out.println(String.format("     warining: %s", list.get(cnt)
					.getWarning()));
			System.out.println(String.format("     filter: %s", list.get(cnt)
					.getCritical()));
			System.out.println(String.format("     critical: %s", list.get(cnt)
					.getLimit().toString()));
		}

		System.out
				.println("-----------use log id for detail query------------------");
	}

}
