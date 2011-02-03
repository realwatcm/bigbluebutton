/**
* BigBlueButton open source conferencing system - http://www.bigbluebutton.org/
*
* Copyright (c) 2010 BigBlueButton Inc. and by respective authors (see below).
*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU Lesser General Public License as published by the Free Software
* Foundation; either version 2.1 of the License, or (at your option) any later
* version.
*
* BigBlueButton is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
* PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License along
* with BigBlueButton; if not, see <http://www.gnu.org/licenses/>.
* 
*/

package org.bigbluebutton.conference.service.chat;

import java.util.ArrayList;
import java.util.List;

import org.bigbluebutton.conference.service.chat.IChatRoomListener;
import org.bigbluebutton.conference.service.chat.UserMessageRecorder;
import org.red5.server.api.so.ISharedObject;
import org.slf4j.Logger;
import org.red5.logging.Red5LoggerFactory;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

/*****************************************************************************
;  ChatMessageRecorder
;----------------------------------------------------------------------------
; DESCRIPTION
;   this class is used to record the chat message
;  
; HISTORY
; __date__ :        PTS:            Description
; 12-27-2010
******************************************************************************/
public class ChatMessageRecorder implements IChatRoomListener {

private static Logger log = Red5LoggerFactory.getLogger( ChatMessageRecorder.class, "bigbluebutton" );

    private boolean record;
    private ISharedObject so;
    
    private File curHistoryFile = null ;
    private String curFileName = "PublicChatMessageRecorded-" ;
    private String curDir ;
    private UserMessageRecorder objUser ;
    private String dPath = "/tmp/" ;

    
    String name = "CHATRECORDER";

    /*****************************************************************************
    ;  ChatMessageRecorder
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is the constructor of ChatMessageRecorder Class
    ; RETURNS : N/A
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   so      :   ShareObject of the chat
    ;   dir    :   Directory to store the record message file
    ; 
    ; IMPLEMENTATION
    ;  
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    public ChatMessageRecorder(ISharedObject so, String dir) {
        log.debug("ChatMessageRecorder Constructor...");
        // check whether the input parameter is null or not
        if ( (null == so) || (null == dir) ){
            log.debug("The SO parameter is null");
        }
        
        
        boolean success = false ;
        
        this.so = so; 
        this.curDir = this.dPath + dir ;
        
        success = (new File(curDir)).mkdir() ;
        
        log.debug("Initialize ObjUser");
        objUser = new UserMessageRecorder() ;
        log.debug("objUser {}",objUser);
        
        if ( true == success ){
            log.debug("Directory {} was created",curDir);
        }else{
            log.debug("Directory {} was not created",curDir);
        }
        
    }/** END FUNCTION 'ChatMessageRecorder' **/
        
    /*****************************************************************************
    ;  getName
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   This routine is implemented from IChatRoomListener.
    ; RETURNS : name
    ;
    ; INTERFACE NOTES
    ;   N/A
    ; 
    ; IMPLEMENTATION
    ;  get the name of Application
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 27-12-2010
    ******************************************************************************/
    @Override
    public String getName() {
        log.debug("ChatMessageRecorder getName {}" , name);
        return name;
    }/** END FUNCTION 'getName' **/
    
    /*****************************************************************************
    ;  newChatMessage
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is implemented from IChatRoomListener. It is used to save 
    ;   chat message to file when record status is true
    ; RETURNS : N/A
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   message :   new message
    ; 
    ; IMPLEMENTATION
    ;  save chat to file when new message
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    @Override
    public void newChatMessage(String message) {
        log.debug("ChatMessageRecorder newChatMessage {}" , message);
        
        //check whether the input parameter is null or not
        if ( null == message ){
            log.debug("ChatMessageRecorder newChatMessage null");
            return ;
        }
        
        addMessageToRecordUser(message) ;
    }/** END FUNCTION 'newChatMessage' **/
    
    /*****************************************************************************
    ;  addMessageToRecordUser
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is implemented from IChatRoomListener. It is used to set up
    ;   recording status
    ; RETURNS : N/A
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   message  :   new chat message
    ; 
    ; IMPLEMENTATION
    ;  add new message to user 
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    private void addMessageToRecordUser(String message){
        
        if ( null == message ){
            return ;
        }
        
        ArrayList<UserMessageRecorder> userList = objUser.getUserList() ;
        int i=0 ;
        for(i=0; i<userList.size(); i++){
            if ( true == userList.get(i).record ){
                addChatHistory(message,userList.get(i).curFile) ;
            }
        }
        
    }
    
    /*****************************************************************************
    ;  isFileExist
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is implemented from IChatRoomListener. It is used to set up
    ;   recording status
    ; RETURNS : true/false
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   fileName : file name to save chat message
    ; 
    ; IMPLEMENTATION
    ;  check whether the file exist
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    private boolean isFileExist(String fileName){
        if ( null == fileName ){
            return false ;
        }   
        
        File f = new File(this.curDir,fileName) ;
        if ( false == f.exists() ){
            return false ;
        }else{
            return true ;
        }
    }
    /*****************************************************************************
    ;  setRecordStatus
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is implemented from IChatRoomListener. It is used to set up
    ;   recording status
    ; RETURNS : N/A
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   status  :   true/false
    ;   userid  :   id of user
    ;   username:   name of user
    ; 
    ; IMPLEMENTATION
    ;  set the recording status
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    @Override
    public void setRecordStatus(String userid, String username, boolean status){
        if ( null == userid || null == username ){
            log.debug("Null parameter");
            return ;
        }        
                
        if ( null != objUser ){
            log.debug("Object Null");
        }
        
        objUser.addUserToList(userid,username,status) ;
        
        objUser.setRecordStatusToUser(userid,status) ;
        
        
        boolean success = false ;
                
        log.debug("setRecordStatus Setting {}",status);
        
        // create file when moderator click record button
        if ( true == status ){
            success = createHistoryFile(userid) ;
            if ( false == success ){
                return ;
            }
        }
        
    }/** END FUNCTION 'setRecordStatus' **/
    
    /*****************************************************************************
    ;  createUniqueFile
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ;   this routine is used to create a unique file name
    ;
    ; RETURNS : true/false
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   userid : id of user
    ; 
    ; IMPLEMENTATION
    ;  initialize new file in gDir
    ;  if file exists, initialize another new file name
    ;
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010
    ******************************************************************************/
    private boolean createUniqueFile(String userid){
    
        String fileName = objUser.getCurrentFileFromUser(userid) ;
        if ( null == fileName ) {
            log.debug ("Failed to get file name");
            return false ;
        }
        
        boolean fUpdate= false ;
        fUpdate = objUser.updateFileName(userid,curFileName + fileName);
        if ( false == fUpdate ){
            return false ;
        }
        
        curHistoryFile = new File( curDir, objUser.getCurrentFileFromUser(userid) );
        if ( null == curHistoryFile ){
            log.debug ("ERROR INITIALIZE gHistoryFile");
            return false ;
        }       
        try{
            curHistoryFile.createNewFile();
        }catch(IOException e){
            log.debug ("ERROR: {}",e.getMessage());
        }
        
        log.debug("Current Used File {}", curHistoryFile);
        return true ;
        
    }/** END FUNCTION 'createUniqueFile' **/
    
    /*****************************************************************************
    ;  createHistoryFile
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ; this routine is used to create a text file
    ;
    ; RETURNS : true/false
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   userid  :   id of user
    ; 
    ; IMPLEMENTATION
    ;  create a unique file name
    ;  
    ;
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010 
    ******************************************************************************/
    private boolean createHistoryFile(String userid){
        boolean success = false ;
        success = createUniqueFile(userid);        
        return success ;
    }/** END FUNCTION 'createHistoryFile' **/
    
    /*****************************************************************************
    ;  addChatHistory
    ;----------------------------------------------------------------------------
    ; DESCRIPTION
    ; this routine is used to write the message to text file
    ;
    ; RETURNS : N/A
    ;
    ; INTERFACE NOTES
    ;   INPUT
    ;   message : String
    ;   fileName:   name of file
    ; 
    ; IMPLEMENTATION
    ;  write message to fileName 
    ;
    ; HISTORY
    ; __date__ :        PTS:            Description
    ; 12-27-2010 
    ******************************************************************************/
    public void addChatHistory(String message,String fileName){
        
        if ( null == message ){
            log.debug("error input parameter");
            return  ;
        }
                
        File file = new File(curDir,fileName);
        if ( null == file ){
            return ;
        }
        
        String name = objUser.getUserName(message) ;
        String msg  = objUser.getMessage(message)  ;
        String time = objUser.getTime(message)     ;
        
        try{
            BufferedWriter out = new BufferedWriter(new FileWriter(file, true));
            if ( null == out ){
                return ;
            }
            out.write("[" + name + "]" + " : " + time + " : " + msg + "\n" ) ;
            out.close();
        }catch(IOException e){
            log.debug("error {}",e.getMessage());
        }
    }/** END FUNCTION 'addChatHistory' **/

}/** END CLASS 'ChatMessageRecorder' **/
