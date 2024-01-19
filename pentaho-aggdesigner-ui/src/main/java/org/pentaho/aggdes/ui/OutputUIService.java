/*
* This program is free software; you can redistribute it and/or modify it under the
* terms of the GNU General Public License, version 2 as published by the Free Software
* Foundation.
*
* You should have received a copy of the GNU General Public License along with this
* program; if not, you can obtain a copy at http://www.gnu.org/licenses/gpl-2.0.html
* or from the Free Software Foundation, Inc.,
* 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
*
* This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
* without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
* See the GNU General Public License for more details.
*
*
* Copyright 2006 - 2017 Hitachi Vantara.  All rights reserved.
*/

package org.pentaho.aggdes.ui;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.aggdes.output.Output;
import org.pentaho.aggdes.ui.ext.OutputUiExtension;

/**
 * OutputUIService is used to obtain an OutputUIGenerator for a particular Output object.
 * <p>
 * Developers can register new OutputUIGenerator objects at runtime on an ad-hoc basis with 
 * {@link #addOutputUIGenerator(OutputUIGenerator) addOutputUIGenerator(OutputUIGenerator)} 
 * or in bulk with
 * {@link #setOutputUIGenerators(List<OutputUIGenerator>) setOutputUIGenerators(List)}.
 * 
 * <p>The recommended usage of this application is to register new OutputUIGenerator objects via 
 * Spring injection in the plugins.xml file. (see below)
 * 
 * <p>Example pligins.xml entry to register a new OutputUIGenerator:
 * <p>
 * &lt;beans&gt;<br/>
 * ...<br/>
 * &lt;bean id="yourID" class="Your_OutputUIGenerator_Class_Name"/&gt;<br/>
 * ...<br/>
 * &lt;/beans&gt;<br/>
 * @author nbaker
 */
public class OutputUIService {

  private List<OutputUiExtension> extensions = new ArrayList<OutputUiExtension>();
  
  private static final Log logger = LogFactory.getLog(OutputUIService.class);

  /**
   * Set the list of registered OutputUiExtension objects. This is the methog used in the
   * plugins.xml file.
   * 
   * @param generators a list of OutputUiExtension objects
   */
  public void setOutputUiExtensions(List<OutputUiExtension> extensions) {
    for(OutputUiExtension e : extensions){
      this.extensions.add(e);
    }
  }

  /**
   * Add a new OutputUiExtension to the factory.
   * 
   * @param generatorClass OutputUiExtension object to register
   */
  public void addOutputUiExtension(OutputUiExtension extensionClass) {
    extensions.add(extensionClass);
  }

  /**
   * Used by the Aggregate Designer to find a UI generator for a particular Output type.
   * 
   * @param outputInstance Output object to find a UI extension for
   * @return UiExtension implementation
   */
  public OutputUiExtension getUiExtension(Output outputInstance) {
    
    /* look for accepting generator starting from the end of the collection
     * this allows us to register default handlers and let users essentially 
     * override those by adding new generators to the end of the list
     */
    for(OutputUiExtension ext: extensions){
      if(ext.accept(outputInstance)){
        logger.info("OutputUiExtension found for output type: "+outputInstance.getClass().getName()); 
        return ext;
      }
    }
    logger.warn("No OutputUiExtension found for output type: "+outputInstance.getClass().getName()); 
    return null;
  }
}
