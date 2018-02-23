package src.edu.asu.ser516.projecttwo.team04.ui;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYDataItem;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import src.edu.asu.ser516.projecttwo.team04.constants.ColorConstants;
import src.edu.asu.ser516.projecttwo.team04.listeners.ClientListener;
import src.edu.asu.ser516.projecttwo.team04.model.client.ClientChannel;
import src.edu.asu.ser516.projecttwo.team04.model.client.ClientModel;
import src.edu.asu.ser516.projecttwo.team04.model.client.ClientValueTuple;
import src.edu.asu.ser516.projecttwo.team04.util.Log;


/**
 * ClientGraphView, class that plots the graph when client frame is running.
 * @author  Sai Saran Kandimalla. (skandim2@asu.edu) 
 * @since   FEB 2018
 * @version 1.
 */
public class ClientGraphView extends JPanel  {
	
	
	JFreeChart plotGraph;
	private int channelCount;
	private ArrayList<XYDataset> datasetList = new ArrayList<XYDataset>();;
	ArrayList<XYSeries> seriesList = new ArrayList<XYSeries>();
	JPanel graphPanel = new JPanel(new BorderLayout());
	ChartPanel panelChart = new ChartPanel(plotGraph, false);
	
	
	/**
	 * default constructor for class.
	 */
	public ClientGraphView() {
		
		ClientModel.get().addListener(new ClientListener() {
            @Override
            public void changedValues() {
                
                ClientGraphView.this.updateValues();
            }

            @Override
            public void changedChannelCount() {
                
                ClientGraphView.this.updateSeries();
            }

            @Override
            public void started() {
                
                for(int i = 0; i < datasetList.size(); i++) {
                    datasetList.remove(i);
                }
            }

            @Override
            public void shutdown() {}
        });

	    createGraph();
	}

	public void createGraph() {
		
		
        graphPanel.setBackground(ColorConstants.BACKGROUND_PINK);
        graphPanel.setBorder(BorderFactory.createLineBorder(Color.black));
 
		
		updateSeries();
		
		this.plotGraph = ChartFactory.createXYLineChart("Display", "Number", "Value", 
			(XYDataset) datasetList,PlotOrientation.VERTICAL, true, true, false);
		
		
		graphPanel.add(panelChart, BorderLayout.CENTER);
		this.add(graphPanel, BorderLayout.CENTER);

	}

	private void updateSeries() {
		// TODO Auto-generated method stub
		channelCount = ClientModel.get().getChannels().size(); 
		List<ClientChannel> channelList = ClientModel.get().getChannels();
		if(channelCount > datasetList.size())
	    {
	    	
	    	for (int i=datasetList.size();i<channelCount;i++)
	    	{
	    		ClientChannel channel = channelList.get(i);
                XYSeries series = new XYSeries("Channel " + channel.id);
                for(ClientValueTuple tuple : channel.getValues())
                {
                	series.add(tuple.tick, tuple.value);
                }
                	datasetList.add((XYDataset) series);
	    	}
	    }
		else if 
		(channelCount > datasetList.size()) 
		{
		     for(int i = channelCount; i<datasetList.size();i++)
		     {
		    	 datasetList.remove(i);
		     }
		}
	
	}
	

	private void updateValues() {
        List<ClientChannel> channels = ClientModel.get().getChannels();
        for(int i = 0; i < datasetList.size(); i++) {
            if(channels.size() == datasetList.size()) {
                XYSeries series = (XYSeries) datasetList.get(i);
                ClientValueTuple tuple = channels.get(i).getLast();
                if(tuple != null)
                    series.add(tuple.tick, tuple.value);
            } else {
                Log.w("Channel and series size differ (" + channels.size() + " : " 
		      + datasetList.size() + ")", ClientGraphView.class);
            }
        }
    }



	}

