import java.awt.BorderLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

public class ComputeListsenr {
	ActionListener listener;
	static String inpuFilePath;
	static String fileName;
	//private ArrayList<JTabbedPane> company_tabs = new ArrayList<>();
	
	
	public ComputeListsenr(final Arguments_form arguments_form_module1, final JTabbedPane jtp_companies, String module) {
		listener = new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Toolkit.getDefaultToolkit().beep();

				// MAKE THIS VARIABLE
				// Build string to run the jar
				String filepath = module;
				Process proc;
				// Add input file parameter
				String execCommand = "java -jar " + filepath;
				if(module == "Awesome-MSM-1.8.1.jar"){
					// Add fileName of parameters file
					execCommand = execCommand + " " + inpuFilePath
							+ " parameters.txt";
					// Create parameters.txt file
					int window = 3;
					double threshold = 0.001;
					// Add window, default value is 3 if empty
					window = arguments_form_module1.getWindow();
					// Add threshold, default value is 0.001 if empty
					threshold = arguments_form_module1.getThreshold();

					File fileTemp = new File("parameters.txt");
					if (fileTemp.exists()) {
						fileTemp.delete();
					}
					try (PrintWriter out = new PrintWriter(new BufferedWriter(
							new FileWriter("parameters.txt", true)))) {
						out.println("window = " + window);
						out.println("threshold = " + threshold);
						out.println("output = summary.csv");
					} catch (IOException e1) {
					}
				// System.out.println(execCommand);
				// Run module
				}
				if(module =="aurora.jar"){
					execCommand = execCommand + " " + inpuFilePath
							+ " aurora_params.txt";
				}
				try {

					// System.out.println("FSDFDSFds");
					proc = Runtime.getRuntime().exec(execCommand);
					proc.waitFor();
					InputStream in = proc.getInputStream();
					InputStream err = proc.getErrorStream();

					byte b[] = new byte[in.available()];
					in.read(b, 0, b.length);
					// System.out.println(new String(b));

					byte c[] = new byte[err.available()];
					err.read(c, 0, c.length);
					// System.out.println(new String(c));
					// System.out.println("DONE");
					// create graph
					System.out.println(module);
					LinkedHashMap<String, ArrayList<TradeRec>> dataHashMap = new LinkedHashMap<String, ArrayList<TradeRec>>();
					LinkedHashMap<String, ArrayList<Trade>> trades = new LinkedHashMap<String, ArrayList<Trade>>(); 
					if(module == "Awesome-MSM-1.8.1.jar"){
						dataHashMap = CSVParser.CSVParse(inpuFilePath, module);
						trades = CSVParser.SummaryParse("summary.csv", module);
					}
					else
						//(module == "aurora.jar")
						{
						dataHashMap = CSVParser.CSVParse(inpuFilePath, module);
						trades = CSVParser.SummaryParse("output.csv", module);
					}
					/*
					for(String company: dataHashMap.keySet()){
						 JTabbedPane company_tab = new JTabbedPane();
						 company_tabs.add(company_tab);
						 //jtp_companies.add(company + " from " + fileName,company_tab);
					}*/
					
					
					for(String company: dataHashMap.keySet()){
						JTabbedPane company_tab = new JTabbedPane();
						jtp_companies.add(company + " from " + fileName,company_tab);
					// price graph
						Price price = new Price("Price - ", "Price over Time",
							dataHashMap.get(company),trades.get(company));
						company_tab.add("Price", price.get_chartPanel());
					// return graph
						Return return_ = new Return("Return - company", 
		 					 "Return over Time", 
		 					 trades.get(company), 
		 					 company);
						company_tab.add("Return", return_.get_chartPanel());
					// profit graph
						Profit profit = new Profit(
							      "Profit - " + company ,
							      "Profit over Time",
							      trades.get(company),
							      company);
						company_tab.add("Profit", profit.get_chartPanel());
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}

			}
		};
	}

	public ActionListener returnListener() {
		return listener;
	}

	public void setFileName(String fileN) {
		this.fileName = fileN;
	}

	public void setFilePath(String fileP) {
		this.inpuFilePath = fileP;
	}

}
