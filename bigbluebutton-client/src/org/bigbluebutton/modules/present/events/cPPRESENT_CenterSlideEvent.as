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
package org.bigbluebutton.modules.present.events
{
    import flash.events.Event;
    
	/*****************************************************************************
	 ;  cPPRESENT_CenterSlideEvent
	 ;----------------------------------------------------------------------------
	 ; DESCRIPTION
	 ;   this class is used to send command to center slide in the presentation
	 ;	 window.
	 ;  
	 ; HISTORY
	 ; __date__ :        PTS:            Description
	 ; 2011.01.20
	 ******************************************************************************/
    public class cPPRESENT_CenterSlideEvent extends Event
    {
        public static const CENTER_SLIDE:String  = "CENTER_SLIDE";
        
		/*****************************************************************************
		 ;  cPPRESENT_CenterSlideEvent
		 ;----------------------------------------------------------------------------
		 ; DESCRIPTION
		 ;   this routine is the constructor of the class
		 ;   
		 ; RETURNS : N/A
		 ;
		 ; INTERFACE NOTES
		 ;   type:	event type
		 ;
		 ; IMPLEMENTATION
		 ;
		 ; HISTORY
		 ; __date__ :        PTS:            Description
		 ; 2011.01.20
		 ******************************************************************************/
        public function cPPRESENT_CenterSlideEvent(type:String)
        {
            super(type, true, false);
        }/** END FUNCTION 'cPPRESENT_CenterSlideEvent' **/
    }
	/** END CLASS 'cPPRESENT_CenterSlideEvent' **/
}