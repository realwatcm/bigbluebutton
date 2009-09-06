package org.bigbluebutton.app.video;

import java.util.ArrayList;

import org.red5.logging.Red5LoggerFactory;
import org.red5.server.adapter.MultiThreadedApplicationAdapter;
import org.red5.server.api.IBandwidthConfigure;
import org.red5.server.api.IConnection;
import org.red5.server.api.IScope;
import org.red5.server.api.stream.IBroadcastStream;
import org.red5.server.api.stream.IServerStream;
import org.red5.server.api.stream.IStreamCapableConnection;
import org.red5.server.api.stream.support.SimpleConnectionBWConfig;
import org.slf4j.Logger;

import xugglerutils.JoinedStream;
import xugglerutils.StreamDownsizer;

public class VideoApplication extends MultiThreadedApplicationAdapter { 
	private static Logger log = Red5LoggerFactory.getLogger(VideoApplication.class, "video");
	
	private IScope appScope;

	private IServerStream serverStream;
	private ArrayList<JoinedStream> joinedStreams;
	
    @Override
	public boolean appStart(IScope app) {
	    super.appStart(app);
		log.info("video appStart");  
		joinedStreams = new ArrayList<JoinedStream>();
		appScope = app;
		return true;
	}

    @Override
	public boolean appConnect(IConnection conn, Object[] params) {
		log.info("oflaDemo appConnect");
		// Trigger calling of "onBWDone", required for some FLV players
		measureBandwidth(conn);
		if (conn instanceof IStreamCapableConnection) {
			IStreamCapableConnection streamConn = (IStreamCapableConnection) conn;
			SimpleConnectionBWConfig bwConfig = new SimpleConnectionBWConfig();
			bwConfig.getChannelBandwidth()[IBandwidthConfigure.OVERALL_CHANNEL] =
				1024 * 1024;
			bwConfig.getChannelInitialBurst()[IBandwidthConfigure.OVERALL_CHANNEL] =
				128 * 1024;
			streamConn.setBandwidthConfigure(bwConfig);
		}
			 
		return super.appConnect(conn, params);
	}

    @Override
	public void appDisconnect(IConnection conn) {
		log.info("oflaDemo appDisconnect");
		if (appScope == conn.getScope() && serverStream != null) {
			serverStream.close();
		}
		super.appDisconnect(conn);
	}
    
    @Override
    public void streamPublishStart(IBroadcastStream stream){
    	super.streamPublishStart(stream);
    	
    	StreamDownsizer smallStream = new StreamDownsizer(stream);
    }
    
    @Override
    public boolean roomStart(IScope room){
    	boolean ret = super.roomStart(room);
    	
    	JoinedStream videoStream = new JoinedStream(room, room.getName(), 
    			VideoAppConstants.JOINED_WIDTH, VideoAppConstants.JOINED_HEIGHT, VideoAppConstants.JOINED_FRAME_RATE);
    	joinedStreams.add(videoStream);
    	Thread videoThread = new Thread(videoStream, room.getName());
    	videoThread.start();
    	
    	return ret;
    }
    
}
