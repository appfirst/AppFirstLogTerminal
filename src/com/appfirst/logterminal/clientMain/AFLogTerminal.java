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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

import com.appfirst.communication.*;
import com.appfirst.logterminal.commands.AFAuthKeyCommand;
import com.appfirst.logterminal.commands.AFHelpCommand;
import com.appfirst.logterminal.commands.AFLogDetailCommand;
import com.appfirst.logterminal.commands.AFLogListCommand;
import com.appfirst.logterminal.commands.AFLogSummaryCommand;
import com.appfirst.logterminal.commands.AFUrlCommand;
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
	public static HashMap<Integer, String> serverNameMap = new HashMap<Integer, String>();

	private static void reloadServers() {
		serverNameMap.clear();
		List<Server> servers = client
				.getServerList("https://fedev/api/v1/servers/");
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
					AFAuthKeyCommand keyCommand = new AFAuthKeyCommand(client);
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
		AFHelpCommand startHelp = new AFHelpCommand();
		startHelp.execute("");
		System.out.println("----------------------------------------\nInput commands: ");
		int commandCount = 0;
		while (true) {
			BufferedReader br = new BufferedReader(new InputStreamReader(
					System.in));
			try {
				System.out.print("->");
				String input = br.readLine();
				if (input.equals("help")) {
					AFHelpCommand help = new AFHelpCommand();
					help.execute(input);
				} else if (input.equals("quit")) {
					System.out.println(String.format("%d commands executed!",
							commandCount));
					System.out.println("bye!");
					System.exit(0);
				} else if (input.equals("list")) {
					AFLogListCommand list = new AFLogListCommand(client);
					list.execute("");
				} else if (input.startsWith("summary")) {
					AFLogSummaryCommand summary = new AFLogSummaryCommand(
							client);
					summary.execute(input);
				} else if (input.startsWith("detail")) {
					AFLogDetailCommand detail = new AFLogDetailCommand(client);
					detail.execute(input);
				} else if (input.startsWith("url")) {
					AFUrlCommand urlCommand = new AFUrlCommand();
					urlCommand.execute(input);
				} else if (input.startsWith("login")) {
					AFAuthKeyCommand keyCommand = new AFAuthKeyCommand(client);
					keyCommand.execute(input);
				} else {
					System.out.println("Unknown command.");
				}
				commandCount++;
			} catch (IOException ioe) {
				System.out.println("IO error trying to read your name!");
				System.exit(1);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Try to recover. ");
			}
		}
	}
}
