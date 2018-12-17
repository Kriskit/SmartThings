/**
 *  //Trend Setter - Switch Group Device
 *  Trend Setter - Contact Sensor Group Device
 *
 *  Copyright 2015 Chris Kitch
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 *  in compliance with the License. You may obtain a copy of the License at:
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
 *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
 *  for the specific language governing permissions and limitations under the License.
 * 
 *  2018-11-28 - Contact Sensor Group Device created by Alec McLure (Alecm) based on Chris Kitch's Power Meter Group Device
 *
 */
metadata {
	definition (name: "Contact Sensor Group Device", namespace: "kriskit-trendsetter", author: "Chris Kitch", vid: "generic-contact") {
	capability "Contact Sensor"
        capability "Sensor"
        capability "Refresh"
        
        attribute "openCount","number"
        attribute "closedCount","number"
        attribute "totalCount","number"
        attribute "openPercentage","number"
        attribute "someAll","string"
	}

	simulator {
		// TODO: define status and reply messages here
	}
	// AlecM - 2018-11-28 - Decided to stick to "official" States of open/closed for contact capability - (so no
	// "openish" or "closedish") in order to maintain compatibility with ActionTiles and SmartThings new app
	tiles(scale: 2) {
		multiAttributeTile(name:"contact", type: "generic", width: 6, height: 4) {
			tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
				attributeState "open", label: '${name}',icon: "st.contact.contact.open", backgroundColor: "#e86d13"
				attributeState "closed", label: '${name}', icon: "st.contact.contact.closed", backgroundColor: "#00a0dc"     
			}
    //Alec M - 2018-11-30 - someAll attribute provides status such as "All Closed, One Open, Some Open, Most Open, All Open"
            tileAttribute("device.someAll", key: "SECONDARY_CONTROL") {
				attributeState "someAll", label:'${currentValue}'
			}
	// AlecM - 2018-11-08 - Tiles below provide counts - how many open, how many closed, total in group		
        }
     	valueTile("openCount", "device.openCount", width: 2, height: 2) {
        	state "val", label:'Open\n${currentValue}',defaultState: true
    }
     	valueTile("closedCount", "device.closedCount", width: 2, height: 2) {
     		state "val", label:'Closed\n${currentValue}', defaultState: true
    }
    	valueTile("totalCount", "device.totalCount", width: 2, height: 2) {
    		state "val", label:'Total\n${currentValue}', defaultState: true
    }
   		valueTile("spacer", "spacer", decoration: "flat", inactiveLabel: false, width: 2, height: 2) {
   			state "default", label:''
	}
        standardTile("refresh", "refresh", height:2, width:2, inactiveLabel: false, decoration: "flat") {
        	state "default", action: "refresh.refresh", icon:"st.secondary.refresh"
    }
  	main("contact")
    details(["contact","openCount","closedCount","totalCount","spacer","refresh"])
	}
  } 

// parse events into attributes
def parse(String description) {
}
def groupSync(name, values) {
	try {
    	"sync${name.capitalize()}"(values)	
    } catch(ex) {
    	log.error "Error executing 'sync${name.capitalize()}' method: $ex"
    }
}
def refresh() {
	def contactValues = parent.getGroupCurrentValues("contact")
    log.debug "Getting current contact values"
    syncContact(contactValues)
}
//CONTACT SENSOR
def syncContact(values) {   
    log.debug "syncContact(): $values"
    def totalCount = values?.size
    def openCount = values?.count { it == "open" }
    def closedCount = totalCount - openCount
    def openPercentage = (int)Math.floor((openCount / values?.size()) * 100)
       
    log.debug "Total open: $openCount"
    log.debug "Total closed: $closedCount"
    log.debug "Total in group: $totalCount"
    log.debug "Percentage open: $openPercentage%"

    sendEvent(name:"openCount",value: openCount)
    sendEvent(name:"closedCount",value: closedCount)
    sendEvent(name:"totalCount",value: totalCount)
    sendEvent(name:"openPercentage",value: openPercentage)

    // AlecM 2018-11-30 Set value for "contact" - open or closed 
    if (openCount == 0) 
    	{sendEvent(name: "contact",value : 'closed', displayed:true)
        }
  	else if (openCount > 0) 
    	{sendEvent(name: "contact", value: 'open',displayed:true)
        }
    
    // AlecM 2018-11-30 Set other values for "someAll" secondary on tile
   
    if (openCount == 0) 
    	{sendEvent(name: "someAll", value: 'All Closed')
        }
    if ((openCount == 1)&&(openPercentage < 100))  //AlecM - if there's only one in group should go to All Open
    	{sendEvent(name: "someAll", value: 'One Open')
    	}
    else if ((openCount > 1) && (openPercentage < 75))
    	{sendEvent(name: "someAll", value: 'Some Open')
    	}
    else if ((openCount > 1) && openPercentage < 100)
    	{sendEvent(name: "someAll", value: 'Most Open')
    	}
    else if (openPercentage == 100)
    	{sendEvent(name: "someAll", value: 'All Open')
    	}
    }
