begin_unit|revision:0.9.5;language:Java;cregit-version:0.0.1
begin_comment
comment|/**  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements. See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership. The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License. You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied. See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|management
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|JMException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|MBeanServer
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|management
operator|.
name|ObjectName
import|;
end_import

begin_comment
comment|/**   *  InstrumentationManager interface for the instrumentations query, register   *  and unregister  */
end_comment

begin_interface
specifier|public
interface|interface
name|InstrumentationManager
block|{
comment|/**      * Register a component with management infrastructure. Component will supply registration name.      * @param component      * @return name used to register the component      * @throws JMException      */
name|ObjectName
name|register
parameter_list|(
name|ManagedComponent
name|component
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Register a component with management infrastructure. Component will supply registration name.      * @param component      * @param forceRegistration if set to true, then component will be registered despite existing component.      * @return name used to register the component      * @throws JMException      */
name|ObjectName
name|register
parameter_list|(
name|ManagedComponent
name|component
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      * @param obj      * @param name      * @throws JMException      */
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Registers object with management infrastructure with a specific name. Object must be annotated or       * implement standard MBean interface.      * @param obj      * @param name      * @param forceRegistration if set to true, then component will be registered despite existing component.      * @throws JMException      */
name|void
name|register
parameter_list|(
name|Object
name|obj
parameter_list|,
name|ObjectName
name|name
parameter_list|,
name|boolean
name|forceRegistration
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Unregisters component with management infrastructure      * @param component      * @throws JMException      */
name|void
name|unregister
parameter_list|(
name|ManagedComponent
name|component
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Unregisters component based upon registered name      * @param name      * @throws JMException      */
name|void
name|unregister
parameter_list|(
name|ObjectName
name|name
parameter_list|)
throws|throws
name|JMException
function_decl|;
comment|/**      * Cleans up and shutsdown management infrastructure.      */
name|void
name|shutdown
parameter_list|()
function_decl|;
comment|/**      * Get the MBeanServer which hosts managed components      * NOTE: if the configuration is not set the JMXEnabled to be true, this method      * will return null      * @return the MBeanServer       */
name|MBeanServer
name|getMBeanServer
parameter_list|()
function_decl|;
name|String
name|getPersistentBusId
parameter_list|()
function_decl|;
name|void
name|setPersistentBusId
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

