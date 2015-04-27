import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class Return extends ApplicationFrame {
	
	private static final int COMPANY = 0;
	private static final int DATE = 1;
	private static final int PRICE = 2;
	private static final int SINGAL = 5;
	
	private String inputFile;
	private static final long serialVersionUID = 1L;
	public Return(String applicationTitle, String chartTitle, String inputFile) {
		super(applicationTitle);
		LinkedHashMap<String, ArrayList<Trade>> BSData = CSVParser.SummaryParse(inputFile);	
		for (Entry<String, ArrayList<Trade>> entry : BSData.entrySet()) {
		    String key = entry.getKey();
		    ArrayList<Trade> value = entry.getValue();
		    for (Iterator iterator = value.iterator(); iterator.hasNext();) {
				Trade trade = (Trade) iterator.next();
				System.out.println(key+trade.date+trade.price+trade.signal);
			}
		}
		/////////Arthur: Code up here is iteration of the summary data set. 
		
		
		
		JFreeChart lineChart = ChartFactory.createLineChart(chartTitle,
				"Years", "Price", createDataset(), PlotOrientation.VERTICAL,
				true, true, false);

		ChartPanel chartPanel = new ChartPanel(lineChart);
		chartPanel.setPreferredSize(new java.awt.Dimension(560, 367));
		setContentPane(chartPanel);
	}

	private DefaultCategoryDataset createDataset( )
	   {
	      DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	      BufferedReader br = null;
		   String line = "";
		   String cvsSplitBy = ",";

		   try {
				br = new BufferedReader(new FileReader(inputFile));
				while ((line = br.readLine()) != null ) {
					// use comma as separator
					String[] trade = line.split(cvsSplitBy);
					//
					if(!trade[0].equals("#RIC") && !trade[8].isEmpty()){
						//Adds data
						//Value, category, x-value
						dataset.addValue(Double.parseDouble(trade[PRICE]) , trade[COMPANY] , trade[DATE] );    
						//System.out.println(trade[8]);
					}
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NumberFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
	      return dataset;
	   }

}
