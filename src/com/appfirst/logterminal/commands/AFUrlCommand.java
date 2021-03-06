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

import com.appfirst.logterminal.clientMain.AFLogTerminal;

/**
 * @author Bin Liu
 *
 */
public class AFUrlCommand extends AFCommandBase {

	/* (non-Javadoc)
	 * @see com.appfirst.logterminal.commands.AFCommandBase#execute(java.lang.String)
	 */
	@Override
	public void execute(String arg) {
		// TODO Auto-generated method stub
		String[] args = arg.split(" ");
		if (args.length < 2) {
			System.out.println("url can't be null");
			return;
		}
		
		AFLogTerminal.baseUrl = args[1];
		System.out.println(String.format("Url switched to %s", AFLogTerminal.baseUrl));
	}
}
