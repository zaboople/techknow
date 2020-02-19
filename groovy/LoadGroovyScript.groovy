		// This gets the current file. This is helpful if you want to do relative-path traversal
		// to get to another file:
		File thisFile = new File(getClass().getProtectionDomain().getCodeSource().getLocation().toURI())


		// This is actually loading a groovy script into the runtime:
		File file=new File(args[0]);
		File dir=file.getParentFile()
		def gse = new groovy.util.GroovyScriptEngine([dir.toURI().toURL()] as URL[])
		gse.loadScriptByName(file.getName()).newInstance()


		// Oh this is even better, use current script to get file that shares a "groovy"
		// in its path
		if (scriptName.startsWith("groovy/") || scriptName.contains("/groovy/"))
			scriptName=scriptName.substring(
				scriptName.indexOf("groovy/")+"groovy/".length()
			)
		File thisFile = new File(currClass.getProtectionDomain().getCodeSource().getLocation().toURI())
		if (!thisFile.exists()) throw new RuntimeException("Couldn't get file name of current file")
		while (!thisFile.getName().equals("groovy"))
			thisFile=thisFile.getParentFile();
		def gse = new groovy.util.GroovyScriptEngine([thisFile.toURI().toURL()] as URL[])
		gse.loadScriptByName(scriptName)
