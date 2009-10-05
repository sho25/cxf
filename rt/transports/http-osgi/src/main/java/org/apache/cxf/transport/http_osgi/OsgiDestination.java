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
name|http_osgi
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Level
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|logging
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|Bus
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|common
operator|.
name|logging
operator|.
name|LogUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|message
operator|.
name|MessageImpl
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|service
operator|.
name|model
operator|.
name|EndpointInfo
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|MessageObserver
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|cxf
operator|.
name|transport
operator|.
name|http
operator|.
name|AbstractHTTPDestination
import|;
end_import

begin_class
specifier|public
class|class
name|OsgiDestination
extends|extends
name|AbstractHTTPDestination
block|{
specifier|static
specifier|final
name|Logger
name|LOG
init|=
name|LogUtils
operator|.
name|getL7dLogger
argument_list|(
name|OsgiDestination
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1L
decl_stmt|;
specifier|final
name|OsgiDestinationRegistryIntf
name|factory
decl_stmt|;
specifier|final
name|String
name|path
decl_stmt|;
comment|/**      * Constructor, allowing substitution of configuration.      *      * @param b the associated Bus      * @param ci the associated conduit initiator      * @param ei the endpoint info of the destination      * @param fact the transport factory      * @param p the path      * @throws IOException      */
specifier|public
name|OsgiDestination
parameter_list|(
name|Bus
name|b
parameter_list|,
name|EndpointInfo
name|ei
parameter_list|,
name|OsgiDestinationRegistryIntf
name|fact
parameter_list|,
name|String
name|p
parameter_list|)
throws|throws
name|IOException
block|{
comment|// would add the default port to the address
name|super
argument_list|(
name|b
argument_list|,
name|ei
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|factory
operator|=
name|fact
expr_stmt|;
name|path
operator|=
name|p
expr_stmt|;
block|}
specifier|protected
name|Logger
name|getLogger
parameter_list|()
block|{
return|return
name|LOG
return|;
block|}
specifier|protected
name|Bus
name|getBus
parameter_list|()
block|{
return|return
name|bus
return|;
block|}
specifier|protected
name|void
name|doMessage
parameter_list|(
name|MessageImpl
name|inMessage
parameter_list|)
throws|throws
name|IOException
block|{
try|try
block|{
name|setHeaders
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
name|inMessage
operator|.
name|setDestination
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|incomingObserver
operator|.
name|onMessage
argument_list|(
name|inMessage
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
if|if
condition|(
name|LOG
operator|.
name|isLoggable
argument_list|(
name|Level
operator|.
name|FINE
argument_list|)
condition|)
block|{
name|LOG
operator|.
name|fine
argument_list|(
literal|"Finished servicing http request on thread: "
operator|+
name|Thread
operator|.
name|currentThread
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
name|factory
operator|.
name|removeDestination
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|super
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
specifier|public
name|MessageObserver
name|getMessageObserver
parameter_list|()
block|{
return|return
name|this
operator|.
name|incomingObserver
return|;
block|}
block|}
end_class

end_unit

