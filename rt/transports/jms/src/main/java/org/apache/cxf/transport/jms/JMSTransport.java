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
name|transport
operator|.
name|jms
package|;
end_package

begin_import
import|import
name|javax
operator|.
name|jms
operator|.
name|Destination
import|;
end_import

begin_comment
comment|/**  * Common accessors between the conduit and destination which are needed for common code.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|JMSTransport
block|{
name|AddressType
name|getJMSAddress
parameter_list|()
function_decl|;
name|SessionPoolType
name|getSessionPool
parameter_list|()
function_decl|;
comment|/**      * Callback from the JMSProviderHub indicating the ClientTransport has      * been sucessfully connected.      *      * @param targetDestination the target destination      * @param sessionFactory used to get access to a pooled JMS resources      */
name|void
name|connected
parameter_list|(
name|Destination
name|target
parameter_list|,
name|Destination
name|reply
parameter_list|,
name|JMSSessionFactory
name|factory
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

