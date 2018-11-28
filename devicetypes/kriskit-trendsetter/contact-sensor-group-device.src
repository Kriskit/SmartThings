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
 */
metadata {
	definition (name: "Contact Sensor Group Device", namespace: "kriskit-trendsetterADM", author: "Chris Kitch", vid: "generic-contact") {
		capability "Contact Sensor"
        capability "Sensor"
        capability "Refresh"
        
        attribute "percentOpen", "number"
        //attribute "openClosedtotal", "string"
        attribute "openCount","number"
        attribute "closedCount","number"
        attribute "totalCount","number"
	}

	simulator {
		// TODO: define status and reply messages here
	}
	tiles(scale: 2) {
		multiAttributeTile(name:"contact", type: "generic", width: 6, height: 4) {
			tileAttribute ("device.contact", key: "PRIMARY_CONTROL") {
				attributeState "open", label: 'SOME ${name}',icon: "st.contact.contact.open", backgroundColor: "#e86d13"
				attributeState "closed", label: 'ALL ${name}', icon: "st.contact.contact.closed", backgroundColor: "#00a0dc" 
			}
        
			//tileAttribute ("device.percentOpen", key: "SECONDARY_CONTROL") {
			//	attributeState "default", label: '${currentValue} %'
			//}
			//tileAttribute ("device.openClosedtotal", key: "SECONDARY_CONTROL") {
			//attributeState "default", label: '${currentValue}'}
            
        }
     valueTile("openCount", "device.openCount", width: 2, height: 2) {
        state "val", label:'${currentValue} open', defaultState: true
    }
      valueTile("closedCount", "device.closedCount", width: 2, height: 2) {
        state "val", label:'${currentValue}  closed', defaultState: true
    }
         valueTile("totalCount", "device.totalCount", width: 2, height: 2) {
        state "val", label:'${currentValue}  total', defaultState: true
    }
     
        
        standardTile("refresh", "refresh", height:2, width:6, inactiveLabel: false, decoration: "flat") {
        	state "default", action: "refresh.refresh", icon:"st.secondary.refresh"
        }
        
        main("contact")
        details(["contact","openCount","closedCount","totalCount","refresh"])
	}
    }

	
            
		//	tileAttribute ("device.openPercentage", key: "SECONDARY_CONTROL") {
			//	attributeState "oPenPercentage", label:'${currentValue}% Open'
             //   attributeState "100", label:'All Open'
            //    attributeState "0", label:'All Closed'
			//}

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
    def percentOpen = (int)Math.floor((openCount / values?.size()) * 100)
       
    log.debug "Total open: $openCount"
    log.debug "Total closed: $closedCount"
    log.debug "Percentage open: $percentOpen%"
    
    
    //sendEvent(name:"percentOpen",value: percentOpen)
    sendEvent(name:"openCount",value: openCount)
    sendEvent(name:"closedCount",value: closedCount)
    sendEvent(name:"totalCount",value: totalCount)
    //sendEvent (name: "openClosedtotal", value: "Open: ${openCount} /Closed: ${closedCount}/ Total: ${totalCount}")
    if (openCount == 0) 
    	{sendEvent(name: "contact",value : 'closed', displayed:true)
        }
        else if (openCount > 0) 
    {sendEvent(name: "contact", value: 'open',displayed:true)
    }
    }
    
  
