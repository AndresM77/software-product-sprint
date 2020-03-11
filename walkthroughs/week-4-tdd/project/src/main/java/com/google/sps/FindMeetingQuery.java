// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.Collection;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Arrays;

public final class FindMeetingQuery {
  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
      //Return the times that a meeting can happen today
      //MeetingRequest holds all the information regarding atendees, and length of meeting
      //Event has a set of attendees and a specific timerange the atendees are meeting 
      //TimeRange is a specific set of time a meeting can start and end
    Collection<TimeRange> slots = new HashSet<TimeRange>();
    if (request.getDuration() < TimeRange.WHOLE_DAY.duration()) {
        slots.add(TimeRange.WHOLE_DAY); //Initializing base case
    }

    for (Event event : events) {
        for (String attendee : event.getAttendees()) {
            if (request.getAttendees().contains(attendee)) {
                Collection<TimeRange> temp = new HashSet<TimeRange>(slots);

                for (TimeRange slot : temp) {
                    if (slot.overlaps(event.getWhen())) {
                        //Split slot into slots that dont have overlaps with events
                        TimeRange range1 = slot.fromStartEnd(slot.start(), event.getWhen().start(), false);
                        TimeRange range2 = slot.fromStartEnd(event.getWhen().end(), slot.end(), false);
                        if (slot.contains(event.getWhen())) { 
                            //inside slot
                            if (range1.duration() >= request.getDuration()) {
                                slots.add(range1);
                            }
                            if (range2.duration() >= request.getDuration()) {
                                slots.add(range2);
                            }
                        } else if (event.getWhen().contains(slot)) { 
                            //inside event
                        } else if (slot.contains(event.getWhen().start())) { 
                            //slot first 
                            if (range1.duration() >= request.getDuration()) {
                                slots.add(range1);
                            }
                        } else if (event.getWhen().contains(slot.start())) { 
                            //event first 
                            if (range2.duration() >= request.getDuration()) {
                                slots.add(range2);
                            }
                        }
                        slots.remove(slot);
                    }
                }
            }
        }
    }

    List<TimeRange> output = new ArrayList<TimeRange>(slots);
    Collections.sort(output, TimeRange.ORDER_BY_START);

    return output;
  }
}
