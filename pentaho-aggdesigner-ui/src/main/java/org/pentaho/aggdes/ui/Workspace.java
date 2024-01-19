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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.pentaho.aggdes.model.Schema;
import org.pentaho.aggdes.ui.util.Messages;
import org.pentaho.ui.xul.XulEventSourceAdapter;

public class Workspace extends XulEventSourceAdapter{

  private static final Log logger = LogFactory.getLog(Workspace.class);
    
  private static final String CONFIG_DIR = System.getProperty("user.home") + File.separator + ".aggdesigner";
  private static final String CONFIG_FILE = CONFIG_DIR + File.separator + "aggdesigner.properties";
    
  private Properties workspaceProperties;
  
  private File workspaceLocation;
  
  private Schema schema;
  
  private boolean workspaceUpToDate = true;
  
  private boolean applicationUnlocked = false;

  private boolean saveEnabled = false;
  
  public Workspace() {
    loadWorkspaceProperties();
  }

  // properties management
  
  
  /**
   * load properties
   */
  private void loadWorkspaceProperties() {
    workspaceProperties = new Properties();
    try {
      File f = new File(CONFIG_FILE);
      if (f.exists()) {
        FileInputStream in = new FileInputStream(f);
        workspaceProperties.load(in);
        in.close();
      } else {
        logger.debug(CONFIG_FILE + " does not exist.");
      }
    } catch (Exception e) {
      logger.error(Messages.getString("Workspace.ERROR_0001_LOAD_PROPS_EXCEPTION"), e);
    }
  }
  
  /**
   * returns the value of a workspace property
   * 
   * @param key
   *          key to lookup
   * @return the value
   */
  public String getWorkspaceProperty(String key) {
    return workspaceProperties.getProperty(key);
  }

  /**
   * set a workspace property. Note that this does not save the property, a call
   * to storeWorkspaceProperties is required.
   * 
   * @param key
   *          property key
   * @param value
   *          property value
   */
  public void setWorkspaceProperty(String key, String value) {
    workspaceProperties.setProperty(key, value);
  }
  
  /**
   * returns the current workspace file location
   * 
   * @return workspace file location
   */
  public File getWorkspaceLocation() {
    return workspaceLocation;
  }
  
  /**
   * set the flag to maintain whether the saved state of the 
   * workspace is in sync with the serialized model 
   * 
   * @param workspaceUpToDate boolean value
   */
  public void setWorkspaceUpToDate(boolean workspaceUpToDate) {
    boolean oldVal = this.workspaceUpToDate;
    this.workspaceUpToDate = workspaceUpToDate;
    this.firePropertyChange("workspaceUpToDate", oldVal, workspaceUpToDate);
    setSaveEnabled(applicationUnlocked && !workspaceUpToDate);
  }

  /**
   * flag to maintain whether the saved state of the 
   * workspace is in sync with the serialized model 
   * 
   * @return true if workspace is up to date.
   */
  public boolean getWorkspaceUpToDate() {
    return workspaceUpToDate;
  }
  
  /**
   * sets the current workspace file location
   * 
   * @param workspaceLocation location of serialized workspace file
   */
  public void setWorkspaceLocation(File workspaceLocation) {
    this.workspaceLocation = workspaceLocation;
  }
  
  /**
   * sets the current schema object
   * 
   * @param schema overall schema object
   */
  public void setSchema(Schema schema) {
    this.schema = schema;
  }
  
  /**
   * returns the current schema object
   * 
   * @return overall schema object
   */
  public Schema getSchema() {
    return schema;
  }
  
  /**
   * save workspace properties
   */
  public void storeWorkspaceProperties() {
    // save properties to file
    File dir = new File(CONFIG_DIR);
    try {
      if (dir.exists()) {
        if (!dir.isDirectory()) {
          logger.error(Messages.getString("Workspace.ERROR_0002_CONFIG_NOT_DIR", CONFIG_DIR));
          return;
        }
      } else {
        dir.mkdirs();
      }
    } catch (Exception ex) {
      logger.error(Messages.getString("Workspace.ERROR_0003_STORE_PROPS_EXCEPTION"), ex);
      return;
    }

    OutputStream out = null;
    try {
      out = (OutputStream) new FileOutputStream(new File(CONFIG_FILE));
      workspaceProperties.store(out, "aggdesigner configuration");
    } catch (Exception e) {
      logger.error(Messages.getString("Workspace.ERROR_0002_STORE_PROPS_EXCEPTION"), e);
    } finally {
      try {
        out.close();
      } catch (IOException eIO) {
        logger.error(Messages.getString("Workspace.ERROR_0002_STORE_PROPS_EXCEPTION"), eIO);
      }
    }
  }

  public boolean isApplicationUnlocked() {
  
    return applicationUnlocked;
  }

  public void setApplicationUnlocked(boolean applicationUnlocked) {
  
    boolean prevVal = this.applicationUnlocked;
    this.applicationUnlocked = applicationUnlocked;
    this.firePropertyChange("applicationUnlocked", prevVal, applicationUnlocked);
    setSaveEnabled(applicationUnlocked && !workspaceUpToDate);
  }

  public boolean isSaveEnabled(){
    return saveEnabled;
  }
  
  public void setSaveEnabled(boolean enabled){
    boolean oldVal = saveEnabled;
    saveEnabled = enabled;
    this.firePropertyChange("saveEnabled", oldVal, saveEnabled);
  }
  
}
