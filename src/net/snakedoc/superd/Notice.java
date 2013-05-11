/*******************************************************************************
 *  Copyright 2013 Kevin Sipula                                                *
 *                                                                             *
 *  Licensed under the Apache License, Version 2.0 (the "License");            *
 *  you may not use this file except in compliance with the License.           *
 *  You may obtain a copy of the License at                                    *
 *                                                                             *
 *      http://www.apache.org/licenses/LICENSE-2.0                             *
 *                                                                             *
 *  Unless required by applicable law or agreed to in writing, software        *
 *  distributed under the License is distributed on an "AS IS" BASIS,          *
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.   *
 *  See the License for the specific language governing permissions and        *
 *  limitations under the License.                                             *
 *******************************************************************************/

package net.snakedoc.superd;


public class Notice {

    private String superD_ascii = "                                                                                \n" + 
                                  "                                                     ___                        \n" +
                                  "                          ___ _   _ _ __   ___ _ __ /   \\                      \n" +
                                  "                         / __| | | | '_ \\ / _ \\ '_// /\\ /                    \n" +
                                  "                         \\__ \\ |_| | |_) |  __/ | / /_//                      \n" +
                                  "                         |___/\\__,_| .__/ \\___|_|/___,'                       \n" +
                                  "                                   |_|                                          \n" + 
                                  "                                                                                \n" + 
                                  "                                                                                \n";
    

    public String get_superD_ascii() {
        
        return this.superD_ascii;
        
    }
    

    public void set_superD_ascii(String superD_ascii) {
        
        this.superD_ascii = superD_ascii;
        
    }
    
}
