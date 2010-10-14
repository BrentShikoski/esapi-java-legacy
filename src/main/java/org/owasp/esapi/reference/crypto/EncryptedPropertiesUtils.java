package org.owasp.esapi.reference.crypto;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.util.Properties;

import org.owasp.esapi.EncryptedProperties;

/**
 * Command line utilities for reading, writing and creating encrypted properties files.
 * <p>
 * Usage:<br/>
 * <code>
 *    java org.owasp.esapi.reference.crypto.EncryptedPropertiesUtils [--in file] [--out file] [--in-encrypted true|false]
 * </code>
 * <p>
 * Command line parameters:<br/>
 * <ul>
 * <li><b>--in</b> (Optional) Encrypted or plaintext file to read from. If no input file is specified, a new properties file will be created.</li>
 * <li><b>--out</b> (Optional) Encrypted file to output to. Default: Overwrite input file</li>
 * <li><b>--in-encrypted</b> (Optional) True if the input file is encrypted. Default: true</li>
 * <li><b>--verbose</b> (Optional) If true, output (potentially unencrypted) information to the terminal. Default: false</li>
 * </ul>
 *
 * @author August Detlefsen (augustd at codemagi dot com)
 *         <a href="http://www.codemagi.com">CodeMagi, Inc.</a>
 * @since October 8, 2010
 * @see org.owasp.esapi.EncryptedProperties
 */
public class EncryptedPropertiesUtils {

	/**
	 * Loads encrypted or plaintext properties file based on the location passed in args
	 * then prompts the user to input key-value pairs.  When the user enters a null or
	 * blank key, the values are stored to the properties file.
	 *
	 * @throws Exception Any exception thrown
	 */
	public static void main(String[] args) throws Exception {

		//command line options
		String inFile			= null;
		String outFile			= null;
		boolean inFileEncrypted = true;
		boolean verbose			= false;

		//parse command line params
		for (int i = 0; i < args.length; i = i + 2) {
			String paramType = args[i];

			if	("--in".equals(paramType) && args.length >= i+1) {
				inFile = args[i+1];

			} else if ("--out".equals(paramType) && args.length >= i+1) {
				outFile = args[i+1];

			} else if ("--in-encrypted".equals(paramType) && args.length >= i+1) {
				inFileEncrypted = Boolean.valueOf(args[i+1]);

			} else if ("--verbose".equals(paramType) && args.length >= i+1) {
				verbose = Boolean.valueOf(args[i+1]);
			}

		}

		if (outFile == null) outFile = inFile; //if no output file is specified we will overwrite the input file

		if (outFile == null) {
			//no input or output file specified. Can't continue.
			System.out.println("You must specify an input file or output file");
			System.exit(1);
		}

		FileInputStream in		= null;
		FileOutputStream out	= null;
		Properties props		= null; //The Properties object we will output

		if ( inFile != null) {

			File f = new File(inFile);
			if (!f.exists()) {
				System.out.println("Input properties file not found. Creating new.");
				props = new ReferenceEncryptedProperties();

			} else {
				String encrypted = inFileEncrypted ? "Encrypted" : "Plaintext";
				System.out.println(encrypted + " properties found in " + f.getAbsolutePath());

				Properties inProperties;
				if (inFileEncrypted) {
					inProperties = new ReferenceEncryptedProperties();
				} else {
					inProperties = new Properties();
				}

				in = new FileInputStream(f);
				inProperties.load(in);

				//Use the existing properties
				props = new ReferenceEncryptedProperties(inProperties);
			}
		} else {
			System.out.println("Input properties file not found. Creating new.");
			props = new ReferenceEncryptedProperties();
		}

		try {
    		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    		String key = null;
    		do {
    			System.out.print("Enter key: ");
    			key = br.readLine();

				if (props.containsKey(key)) {
					System.out.print("Key already exists. Replace? ");
	    			String confirm = br.readLine();
					if ( !("y".equals(confirm) || "yes".equals(confirm)) ) continue;
				}

				System.out.print("Enter value: ");
    			String value = br.readLine();

				if (key != null && key.length() > 0 && value != null && value.length() > 0) {
    				props.setProperty(key, value);
    			}

    		} while (key != null && key.length() > 0);

			out = new FileOutputStream( new File(outFile) );
			props.store(out, "Encrypted Properties File generated by org.owasp.esapi.reference.crypto.EncryptedPropertiesUtils");
			System.out.println("Encrypted Properties file output to " + outFile);

		} finally {
		    // FindBugs and PMD both complain about these next lines, that they may
		    // ignore thrown exceptions. Really!!! That's the whole point.
    		try { if ( in != null ) in.close(); } catch( Exception e ) {}
    		try { if ( out != null ) out.close(); } catch( Exception e ) {}
		}

		if (verbose) {
			for (Object oKey : props.keySet()) {
				String key = (String)oKey;
				String value = props.getProperty(key);
				System.out.println("   " + key + "=" + value);
			}
		}
		
	}

}
