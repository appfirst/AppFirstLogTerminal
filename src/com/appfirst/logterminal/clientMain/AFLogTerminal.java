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
package com.appfirst.logterminal.clientMain;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import com.appfirst.communication.*;
import com.appfirst.logterminal.commands.AFHelpCommand;
import com.appfirst.logterminal.commands.AFLogDetailCommand;
import com.appfirst.logterminal.commands.AFLogListCommand;
import com.appfirst.logterminal.commands.AFLogSummaryCommand;
import com.appfirst.types.Server;

/**
 * A small terminal to get view the logs. 
 * @author Bin Liu
 * 
 */
public class AFLogTerminal {
	private AFClient client = new AFClient();
	public static String baseUrl = "https://fedev/api/v1/logs";
	public static HashMap<Integer, String> serverNameMap = new HashMap<Integer, String>();
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AFLogTerminal terminal = new AFLogTerminal();
		AFClient client = terminal.getClient();
		client.setmAuthString("api_key".getBytes());
		System.out.println("Initializing.");
		List<Server> servers = client.getServerList("https://fedev/api/v1/servers/");
		for (int i = 0; i < servers.size(); i++) {
			serverNameMap.put(servers.get(i).getId(), servers.get(i).getHostname());
		}
		System.out.println("Done.");
		int commandCount = 0;
		while(true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
			try {
				String input = br.readLine();
				if (input.equals("help")) {
					AFHelpCommand help = new AFHelpCommand();
					help.execute(input);
				} else if (input.equals("quit")) {
					System.out.println(String.format("%d commands executed!", commandCount));
					System.out.println("bye!");
					System.exit(0);
				} else if (input.equals("list")) {
					AFLogListCommand list = new AFLogListCommand(client);
					list.execute("");
				} else if (input.startsWith("summary")) {
					AFLogSummaryCommand summary = new AFLogSummaryCommand(client);
					summary.execute(input);
				} else if (input.startsWith("detail")) {
					AFLogDetailCommand detail = new AFLogDetailCommand(client);
					detail.execute(input);
				}
				commandCount ++;
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			} catch (Exception e) {
				System.exit(1);
			}
		}
	}

	/**
	 * @param client
	 *            the client to set
	 */
	public void setClient(AFClient client) {
		this.client = client;
	}

	/**
	 * @return the client
	 */
	public AFClient getClient() {
		return client;
	}
}
