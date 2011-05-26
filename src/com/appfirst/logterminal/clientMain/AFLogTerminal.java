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
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.appfirst.communication.*;
import com.appfirst.logterminal.commands.AFAuthKeyCommand;
import com.appfirst.logterminal.commands.AFHelpCommand;
import com.appfirst.logterminal.commands.AFLogDetailCommand;
import com.appfirst.logterminal.commands.AFLogListCommand;
import com.appfirst.logterminal.commands.AFLogSummaryCommand;
import com.appfirst.logterminal.commands.AFUrlCommand;
import com.appfirst.types.LogEntry;
import com.appfirst.types.Server;

/**
 * A small terminal to get view the logs.
 * 
 * @author Bin Liu
 * 
 */
public class AFLogTerminal {
	public static AFClient client = new AFClient();
	public static String logUrl = "/api/v1/logs";
	public static String baseUrl = "https://fedev/";
	public static String serverUrl = "/api/v1/servers/";
	public static HashMap<Integer, LogEntry> logIdMap = new HashMap<Integer, LogEntry>();
	public static HashMap<Integer, String> serverNameMap = new HashMap<Integer, String>();

	private static void reloadServers() {
		System.out.println("update server list...");
		serverNameMap.clear();
		List<Server> servers = client.getServerList(String.format("%s/%s",
				baseUrl, serverUrl));
		if (servers.size() == 0) {
			System.out.println("Error getting server list. \nCrashing...");
			System.exit(1);
		}
		for (int i = 0; i < servers.size(); i++) {
			serverNameMap.put(servers.get(i).getId(), servers.get(i)
					.getHostname());
		}
	}

	private static void loadConfig() {
		System.out
				.println("Welcome to AppFirst Log Viewer! \nReading config...");
		InputStream is = AFHelpCommand.class.getClassLoader()
				.getResourceAsStream("com/appfirst/logterminal/config.txt");
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		try {
			while (true) {
				String input = reader.readLine();
				if (input == null)
					break;
				System.out.println(input);
				if (input.startsWith("url")) {
					AFUrlCommand urlCommand = new AFUrlCommand();
					urlCommand.execute(input);
				} else if (input.startsWith("login")) {
					AFAuthKeyCommand keyCommand = new AFAuthKeyCommand();
					keyCommand.execute(input);
				}
			}
		} catch (IOException e) {
			System.out.println(e);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				System.out.println(e);
			}
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		loadConfig();
		System.out.print("Initializing...");
		reloadServers();
		System.out.println("Done.");
		AFLogListCommand myList = new AFLogListCommand();
		myList.execute("");
		System.out
				.println("----------------------------------------\nInput commands: ");
		int commandCount = 0;

		while (true) {
			try {
				BufferedReader br = new BufferedReader(new InputStreamReader(
						System.in));
				System.out.print("->");
				String input = br.readLine();
				if (input.equals("help")) {
					AFHelpCommand help = new AFHelpCommand();
					help.execute(input);
				} else if (input.equals("quit") || input.equals("q")) {
					System.out.println(String.format("%d commands executed!",
							commandCount));
					System.out.println("bye!");
					System.exit(0);
				} else if (input.equals("list") || input.equals("l")) {
					AFLogListCommand list = new AFLogListCommand();
					list.execute("");
				} else if (input.startsWith("summary") || input.startsWith("s ")) {
					AFLogSummaryCommand summary = new AFLogSummaryCommand();
					summary.execute(input);
				} else if (input.startsWith("detail") || input.startsWith("d ")) {
					AFLogDetailCommand detail = new AFLogDetailCommand();
					detail.execute(input);
				} else if (input.startsWith("url")) {
					AFUrlCommand urlCommand = new AFUrlCommand();
					urlCommand.execute(input);
				} else if (input.startsWith("login")) {
					AFAuthKeyCommand keyCommand = new AFAuthKeyCommand();
					keyCommand.execute(input);
				} else {
					System.out.println("Unknown command.");
				}
				commandCount++;
			} catch (IOException ioe) {
				System.out.println("IO error. !");
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Try to recover. ");
			}
		}
	}
}
